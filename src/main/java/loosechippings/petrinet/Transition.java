package loosechippings.petrinet;

import java.util.ArrayList;
import java.util.List;

public class Transition {
   private final String name;
   private List<Arc> incoming;
   private List<Arc> outgoing;

   public Transition(String name) {
      this.name = name;
      incoming = new ArrayList<>();
      outgoing = new ArrayList<>();
   }

   public void addIncoming(Arc arc) {
      incoming.add(arc);
   }

   public void addOutgoing(Arc arc) {
      outgoing.add(arc);
   }

   public List<Arc> getIncoming() {
      return incoming;
   }

   public List<Arc> getOutgoing() {
      return outgoing;
   }

   public String getName() {
      return name;
   }
}
