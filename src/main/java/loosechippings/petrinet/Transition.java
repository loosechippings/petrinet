package loosechippings.petrinet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Transition<T> {
   private final String name;
   private List<Arc> incoming;
   private List<Arc> outgoing;
   private Function<Context, T> func;

   public Transition(String name, Function<Context, T> func) {
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
         Context context = new Context();
         for (Arc arc : incoming) {
            context.addToken(arc.getPlace().getDescriptor(), arc.getPlace().removeToken());
         }
         Object o = func.apply(context);
         outgoing.forEach(it -> it.getPlace().addToken(o));
      }
   }
}
