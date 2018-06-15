package loosechippings.petrinet;

import domain.Instruction;
import domain.StatusUpdate;
import domain.Trade;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class PetrinetTest {

   Petrinet petrinet;
   Place<Trade> receivedTrade;
   Place<Instruction> settlementInstructed;
   Place<Instruction> instructionOpen;
   Place<StatusUpdate> receivedStatus;
   Place<Instruction> instructionClosed;

   @Before
   public void init() {
      receivedTrade = new Place<>("Received Trade", new TokenDescriptor<>(Trade.class, "trade"));
      settlementInstructed = new Place<>("Settlement Instructed", new TokenDescriptor<>(Instruction.class, "instruction"));
      instructionOpen = new Place<>("Instruction Open", new TokenDescriptor<>(Instruction.class, "instruction"));
      receivedStatus = new Place<>("Received Status", new TokenDescriptor<>(StatusUpdate.class, "statusUpdate"));
      instructionClosed = new Place<>("Instruction Closed", new TokenDescriptor<>(Instruction.class, "instruction"));
      Place<Instruction> instructionWithStatusApplied = new Place<>("Instruction with status", new TokenDescriptor<>(Instruction.class, "instruction"));

      Transition<Instruction> instructSettlement = new Transition<>("Instruct settlement", this::instructTrade);
      Transition<Instruction> t1 = new Transition<>("t1", this::foo);
      Transition<Instruction> applyStatus = new Transition<>("Apply status", this::applyStatus);
      Transition<Instruction> checkIfStatusOpen = new Transition<>("Check if status open", this::checkIfStatusOpen);
      Transition<Instruction> checkIfStatusClosed = new Transition<>("Check if status closed", this::checkIfStatusClosed);

      Arc<Instruction> statusOpenArc = new Arc<>(instructionWithStatusApplied, checkIfStatusOpen, this::statusIsOpen);
      Arc<Instruction> statusClosedArc = new Arc<>(instructionWithStatusApplied, checkIfStatusClosed, this::statusIsClosed);

      petrinet = new Petrinet.Builder()
            .withArc(receivedTrade, instructSettlement)
            .withArc(instructSettlement, settlementInstructed)
            .withArc(settlementInstructed, t1)
            .withArc(t1, instructionOpen)
            .withArc(instructionOpen, applyStatus)
            .withArc(receivedStatus, applyStatus)
            .withArc(applyStatus, instructionWithStatusApplied)
            .withArc(statusOpenArc)
            .withArc(statusClosedArc)
            .withArc(checkIfStatusOpen, instructionOpen)
            .withArc(checkIfStatusClosed, instructionClosed)
            .build();

   }

   public Boolean statusIsOpen(Instruction i) {
      return !i.getStatus().equals(Instruction.Status.SETTLED);
   }

   public Boolean statusIsClosed(Instruction i) {
      return !statusIsOpen(i);
   }

   public Instruction instructTrade(Context context) {
      System.out.println("creating Instruction from Trade");
      Trade t = Trade.class.cast(context.getToken(new TokenDescriptor<>(Trade.class, "trade")));
      Instruction i = new Instruction();
      i.setTradeReference(t.getReference());
      i.setStatus(Instruction.Status.SENT);
      return i;
   }

   public Instruction foo(Context context) {
      System.out.println("triggered foo");
      return context.getToken(new TokenDescriptor<>(Instruction.class, "instruction"));
   }

   public Instruction applyStatus(Context context) {
      System.out.println("triggered applyStatus");
      Instruction i = context.getToken(new TokenDescriptor<>(Instruction.class, "instruction"));
      StatusUpdate s = context.getToken(new TokenDescriptor<>(StatusUpdate.class, "statusUpdate"));
      switch (s.getStatus()) {
         case MATCHED:i.setStatus(Instruction.Status.MATCHED);
            break;
         case SETTLED:i.setStatus(Instruction.Status.SETTLED);
            break;
         default:
      }
      return i;
   }

   public Instruction checkIfStatusOpen(Context context) {
      System.out.println("triggered checkIfStatusOpen");
      return context.getToken(new TokenDescriptor<>(Instruction.class, "instruction"));
   }

   public Instruction checkIfStatusClosed(Context context) {
      System.out.println("triggered checkIfStatusClosed");
      return context.getToken(new TokenDescriptor<>(Instruction.class, "instruction"));
   }

   @Test
   public void testPetrinet() {
      petrinet.generateDot();
   }

   @Test
   public void addNewTrade() {
      receivedTrade.addToken(new Trade());
      petrinet.fireUntilNoneCanFire();
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      assertThat(placesWithTokens, hasItem(instructionOpen));
   }

   @Test
   public void addNewTradeAndStatus() {
      receivedTrade.addToken(new Trade());
      StatusUpdate s = new StatusUpdate();
      s.setStatus(StatusUpdate.Status.SETTLED);
      receivedStatus.addToken(s);
      petrinet.fireUntilNoneCanFire();
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      assertThat(placesWithTokens, hasItem(instructionClosed));
   }
}
