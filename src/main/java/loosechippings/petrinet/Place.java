package loosechippings.petrinet;

public class Place {

   private final String name;
   private int tokens;
   private int weight = 1;

   public Place(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }
}
