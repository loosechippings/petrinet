package loosechippings.petrinet.example;

import loosechippings.petrinet.Place;
import loosechippings.petrinet.example.domain.Instruction;
import loosechippings.petrinet.example.domain.StatusUpdate;
import loosechippings.petrinet.example.domain.Trade;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class TradePetrinetTest {

   TradePetrinet tradePetrinet;

   @Before
   public void init() {
      tradePetrinet = new TradePetrinet();
   }

   @Test
   public void testPetrinet() {
      tradePetrinet.generateDot("target/petrinet.dot");
   }

   @Test
   public void addNewTrade() {
      tradePetrinet.receiveTrade(new Trade());
      tradePetrinet.fire();
      List<Place> placesWithTokens = tradePetrinet.getMarkedPlaces();
      assertThat(placesWithTokens, contains(tradePetrinet.instructionOpen));
   }

   @Test
   public void addNewTradeAndSettledStatus() {
      tradePetrinet.receiveTrade(new Trade());
      StatusUpdate s = new StatusUpdate();
      s.setStatus(StatusUpdate.Status.SETTLED);
      tradePetrinet.receiveStatus(s);
      tradePetrinet.fire();
      List<Place> placesWithTokens = tradePetrinet.getMarkedPlaces();
      assertThat(placesWithTokens, contains(tradePetrinet.instructionClosed));
   }

   @Test
   public void addNewTradeAndNotSettledStatus() {
      tradePetrinet.receiveTrade(new Trade());
      StatusUpdate s = new StatusUpdate();
      s.setStatus(StatusUpdate.Status.MATCHED);
      tradePetrinet.receiveStatus(s);
      tradePetrinet.fire();
      List<Place> placesWithTokens = tradePetrinet.getMarkedPlaces();
      assertThat(placesWithTokens, contains(tradePetrinet.instructionOpen));
   }

   @Test
   public void whenMoreThanOneInstructionStatusIsAppliedToCorrectOne() {
      Trade t1 = new Trade();
      t1.setReference("ref1");
      t1.setVersion(1);
      Trade t2 = new Trade();
      t2.setReference(t1.getReference());
      t2.setVersion(t1.getVersion()+1);
      tradePetrinet.receiveTrade(t1);
      tradePetrinet.receiveTrade(t2);
      tradePetrinet.fire();
      Place<Instruction> instructionOpen = tradePetrinet.instructionOpen;
      assertThat(tradePetrinet.getMarkedPlaces(), contains(instructionOpen));
      assertEquals(2, instructionOpen.getTokenCount());
      assertNotSame(instructionOpen.getTokens().get(0).getInstructionReference(), instructionOpen.getTokens().get(1).getInstructionReference());
   }
}
