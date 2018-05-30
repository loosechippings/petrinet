package loosechippings.petrinet;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PetrinetTest {

   Petrinet petrinet;
   Place receivedTrade;
   Place settlementInstructed;
   Place instructionOpen;
   Place receivedStatus;

   @Before
   public void init() {
      receivedTrade = new Place("Received Trade");
      settlementInstructed = new Place("Settlement Instructed");
      instructionOpen = new Place("Instruction Open");
      receivedStatus = new Place("Received Status");
      Place instructionClosed = new Place("Instruction Closed");
      Place instructionWithStatusApplied = new Place("Instruction with status");

      Transition instructSettlement = new Transition("Instruct settlement", this::foo);
      Transition t1 = new Transition("t1", this::foo);
      Transition evaluateStatus = new Transition("Apply status", this::foo);
      Transition checkIfStatusOpen = new Transition("Check if status open", this::foo);
      Transition checkIfStatusClosed = new Transition("Check if status closed", this::foo);

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

   public String foo(String name) {
      System.out.println("triggered: " + name);
      return null;
   }

   @Test
   public void testPetrinet() {
      petrinet.generateDot();
   }

   @Test
   public void addNewTrade() {
      petrinet.addToken(receivedTrade);
      petrinet.fireUntilNoneCanFire();
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      Assert.assertThat(placesWithTokens, CoreMatchers.hasItem(instructionOpen));
   }

   @Test
   public void addNewTradeAndStatus() {
      petrinet.addToken(receivedTrade);
      petrinet.addToken(receivedStatus);
      petrinet.fireUntilNoneCanFire();
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      Assert.assertThat(placesWithTokens, CoreMatchers.hasItem(instructionOpen));
   }
}
