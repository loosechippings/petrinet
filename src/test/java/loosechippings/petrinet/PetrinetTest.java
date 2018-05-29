package loosechippings.petrinet;

import org.junit.Test;

public class PetrinetTest {

   @Test
   public void testPetrinet() {
      Place receivedTrade = new Place("Received Trade");
      Place settlementInstructed = new Place("Settlement Instructed");
      Place instructionOpen = new Place("Instruction Open");
      Place receivedStatus = new Place("Received Status");
      Place instructionClosed = new Place("Instruction Closed");

      Transition instructSettlement = new Transition("Instruct settlement");
      Transition t1 = new Transition("t1");
      Transition evaluateStatus = new Transition("Evaluate status");

      Petrinet petrinet = new Petrinet.Builder()
            .withArc(receivedTrade, instructSettlement)
            .withArc(instructSettlement, settlementInstructed)
            .withArc(settlementInstructed, t1)
            .withArc(t1, instructionOpen)
            .withArc(instructionOpen, evaluateStatus)
            .withArc(receivedStatus, evaluateStatus)
            .withArc(evaluateStatus, instructionOpen)
            .withArc(evaluateStatus, instructionClosed)
            .build();

      petrinet.generateDot();
   }
}
