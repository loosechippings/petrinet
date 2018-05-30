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

      Transition instructSettlement = new Transition("Instruct settlement", this::foo);
      Transition t1 = new Transition("t1", this::foo);
      Transition evaluateStatus = new Transition("Evaluate status", this::foo);

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
      List<Place> placesWithTokens = petrinet.getPlacesWithTokens();
      petrinet.fire();
      Assert.assertThat(placesWithTokens, CoreMatchers.hasItem(receivedTrade));
   }
}
