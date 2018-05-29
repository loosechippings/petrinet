package loosechippings.petrinet;

public class Arc {
   public Arc(Place place, Transition transition) {
      transition.addIncoming(place);
   }

   public Arc(Transition transition, Place place) {
      transition.addOutgoing(place);
   }
}
