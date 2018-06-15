package loosechippings.petrinet;

import java.util.HashMap;
import java.util.Map;

public class Context {

   private Map<String, Object> context;

   public Context() {
      context = new HashMap<>();
   }

   public <T> void addToken(TokenDescriptor<T> descriptor, T token) {
      context.put(descriptor.getName(), token);
   }

   public <T> T getToken(TokenDescriptor<T> descriptor) {
      return descriptor.getClazz().cast(context.get(descriptor.getName()));
   }
}
