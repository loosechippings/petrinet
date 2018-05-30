package loosechippings.petrinet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Transition {
   private final String name;
   private List<Arc> incoming;
   private List<Arc> outgoing;
   private Function<String, String> func;

   public Transition(String name, Function<String, String> func) {
      this.name = name;
      this.func = func;
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

   public boolean canFire() {
      return incoming.stream().allMatch(it -> it.canFire(1));
   }

   public void fire() {
      if (canFire()) {
         func.apply(name);
         incoming.forEach(it -> it.getPlace().removeToken());
         outgoing.forEach(it -> it.getPlace().addToken());
      }
   }
}
