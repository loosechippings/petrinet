package loosechippings.petrinet;

import java.util.ArrayList;
import java.util.List;

public class Transition {
   private final String name;
   private List<Place> incoming;
   private List<Place> outgoing;

   public Transition(String name) {
      this.name = name;
      incoming = new ArrayList<>();
      outgoing = new ArrayList<>();
   }

   public void addIncoming(Place place) {
      incoming.add(place);
   }

   public void addOutgoing(Place place) {
      outgoing.add(place);
   }

   public List<Place> getIncoming() {
      return incoming;
   }

   public List<Place> getOutgoing() {
      return outgoing;
   }

   public String getName() {
      return name;
   }
}
