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
   private Function<Map<Class, Object>, T> func;

   public Transition(String name, Function<Map<Class, Object>, T> func) {
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
         Map<Class, Object> tokens = new HashMap<>();
         incoming.forEach(it -> {
            Object i = it.getPlace().removeToken();
            tokens.put(i.getClass(), i);
         });
         Object o = func.apply(tokens);
         outgoing.forEach(it -> it.getPlace().addToken(o));
      }
   }
}
