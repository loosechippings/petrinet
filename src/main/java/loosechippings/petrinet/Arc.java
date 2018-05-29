package loosechippings.petrinet;

import static loosechippings.petrinet.Arc.Direction.PLACE_TO_TRANSITION;
import static loosechippings.petrinet.Arc.Direction.TRANSITION_TO_PLACE;

public class Arc {

   private Place place;
   private Transition transition;
   private Direction direction;

   public Arc(Place place, Transition transition) {
      this.place = place;
      this.transition = transition;
      this.direction = PLACE_TO_TRANSITION;
      transition.addIncoming(this);
   }

   public Arc(Transition transition, Place place) {
      this.place = place;
      this.transition = transition;
      this.direction = TRANSITION_TO_PLACE;
      transition.addOutgoing(this);
   }

   public Place getPlace() {
      return place;
   }

   public Transition getTransition() {
      return transition;
   }

   public boolean canFire(int weight) {
      return direction.canFire(place, weight);
   }

   enum Direction {
      PLACE_TO_TRANSITION {
         public boolean canFire(Place p, int weight) {
            return p.hasAtLeastTokens(weight);
         }
      },

      TRANSITION_TO_PLACE {
         public boolean canFire(Place p, int weight) {
            return p.hasCapacityOf(weight);
         }
      };

      public abstract boolean canFire(Place p, int weight);
   }
}
