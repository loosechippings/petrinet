package loosechippings.petrinet;

import domain.Instruction;
import domain.StatusUpdate;
import domain.Trade;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PetrinetTest {

   Petrinet petrinet;
   Place<Trade> receivedTrade;
   Place<Instruction> settlementInstructed;
   Place<Instruction> instructionOpen;
   Place<StatusUpdate> receivedStatus;

   @Before
   public void init() {
      receivedTrade = new Place<>("Received Trade");
      settlementInstructed = new Place<>("Settlement Instructed");
      instructionOpen = new Place<>("Instruction Open");
      receivedStatus = new Place<>("Received Status");
      Place<Instruction> instructionClosed = new Place<>("Instruction Closed");
      Place<Instruction> instructionWithStatusApplied = new Place<>("Instruction with status");

      Transition<Instruction> instructSettlement = new Transition<>("Instruct settlement", this::instructTrade);
      Transition<Instruction> t1 = new Transition<>("t1", this::foo);
      Transition<Instruction> evaluateStatus = new Transition<>("Apply status", this::foo);
      Transition<Instruction> checkIfStatusOpen = new Transition<>("Check if status open", this::foo);
      Transition<Instruction> checkIfStatusClosed = new Transition<>("Check if status closed", this::foo);

      petrinet = new Petrinet.Builder()
            .withArc(receivedTrade, instructSettlement)
            .withArc(instructSettlement, settlementInstructed)
            .withArc(settlementInstructed, t1)
            .withArc(t1, instructionOpen)
            .withArc(instructionOpen, evaluateStatus)
            .withArc(receivedStatus, evaluateStatus)
            .withArc(evaluateStatus, instructionWithStatusApplied)
            .withArc(instructionWithStatusApplied, checkIfStatusOpen)
            .withArc(instructionWithStatusApplied, checkIfStatusClosed)
            .withArc(checkIfStatusOpen, instructionOpen)
            .withArc(checkIfStatusClosed, instructionClosed)
            .build();

   }

   public Instruction instructTrade(Map<Class, Object> tokens) {
      System.out.println("creating Instruction from Trade");
      Trade t = Trade.class.cast(tokens.get(Trade.class));
      Instruction i = new Instruction();
      i.setTradeReference(t.getReference());
      return new Instruction();
   }

   public Instruction foo(Map<Class, Object> tokens) {
      System.out.println("triggered foo");
      return Instruction.class.cast(tokens.get(Instruction.class));
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
      Assert.assertThat(placesWithTokens, CoreMatchers.hasItem(instructionOpen));
   }

   @Test
   public void addNewTradeAndStatus() {
      receivedTrade.addToken(new Trade());
      receivedStatus.addToken(new StatusUpdate());
      petrinet.fireUntilNoneCanFire();
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      Assert.assertThat(placesWithTokens, CoreMatchers.hasItem(instructionOpen));
   }
}
