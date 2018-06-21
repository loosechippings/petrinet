package loosechippings.petrinet.example;

import loosechippings.petrinet.Place;
import loosechippings.petrinet.example.domain.StatusUpdate;
import loosechippings.petrinet.example.domain.Trade;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class PetrinetTest {

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
      assertThat(placesWithTokens, hasItem(tradePetrinet.instructionOpen));
   }

   @Test
   public void addNewTradeAndSettledStatus() {
      tradePetrinet.receiveTrade(new Trade());
      StatusUpdate s = new StatusUpdate();
      s.setStatus(StatusUpdate.Status.SETTLED);
      tradePetrinet.receiveStatus(s);
      tradePetrinet.fire();
      List<Place> placesWithTokens = tradePetrinet.getMarkedPlaces();
      assertThat(placesWithTokens, hasItem(tradePetrinet.instructionClosed));
   }

   @Test
   public void addNewTradeAndNotSettledStatus() {
      tradePetrinet.receiveTrade(new Trade());
      StatusUpdate s = new StatusUpdate();
      s.setStatus(StatusUpdate.Status.MATCHED);
      tradePetrinet.receiveStatus(s);
      tradePetrinet.fire();
      List<Place> placesWithTokens = tradePetrinet.getMarkedPlaces();
      assertThat(placesWithTokens, hasItem(tradePetrinet.instructionOpen));
   }
}
