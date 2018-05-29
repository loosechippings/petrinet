package loosechippings.petrinet;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PetrinetTest {

   Petrinet petrinet;
   Place receivedTrade;

   @Before
   public void init() {
      receivedTrade = new Place("Received Trade");
      Place settlementInstructed = new Place("Settlement Instructed");
      Place instructionOpen = new Place("Instruction Open");
      Place receivedStatus = new Place("Received Status");
      Place instructionClosed = new Place("Instruction Closed");

      Transition instructSettlement = new Transition("Instruct settlement");
      Transition t1 = new Transition("t1");
      Transition evaluateStatus = new Transition("Evaluate status");

      petrinet = new Petrinet.Builder()
            .withArc(receivedTrade, instructSettlement)
            .withArc(instructSettlement, settlementInstructed)
            .withArc(settlementInstructed, t1)
            .withArc(t1, instructionOpen)
            .withArc(instructionOpen, evaluateStatus)
            .withArc(receivedStatus, evaluateStatus)
            .withArc(evaluateStatus, instructionOpen)
            .withArc(evaluateStatus, instructionClosed)
            .build();

   }

   @Test
   public void testPetrinet() {
      petrinet.generateDot();
   }

   @Test
   public void addNewTrade() {
      petrinet.addToken(receivedTrade);
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      Assert.assertThat(placesWithTokens, CoreMatchers.hasItem(receivedTrade));
   }
}
