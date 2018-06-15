package loosechippings.petrinet;

public class TokenDescriptor<T> {

   private final String name;
   private final Class<T> clazz;

   public TokenDescriptor(Class<T> clazz, String name) {
      this.clazz = clazz;
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public Class<T> getClazz() {
      return clazz;
   }
}
