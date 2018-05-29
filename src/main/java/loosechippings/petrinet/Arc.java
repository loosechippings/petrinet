package loosechippings.petrinet;

public class Arc {

   private Place place;
   private Transition transition;

   public Arc(Place place, Transition transition) {
      this.place = place;
      this.transition = transition;
      transition.addIncoming(this);
   }

   public Arc(Transition transition, Place place) {
      this.place = place;
      this.transition = transition;
      transition.addOutgoing(this);
   }

   public Place getPlace() {
      return place;
   }

   public Transition getTransition() {
      return transition;
   }
}
