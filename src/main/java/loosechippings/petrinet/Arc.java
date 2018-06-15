package loosechippings.petrinet;

import java.util.function.Predicate;

import static loosechippings.petrinet.Arc.Direction.PLACE_TO_TRANSITION;
import static loosechippings.petrinet.Arc.Direction.TRANSITION_TO_PLACE;

public class Arc<T> {

   private final Predicate predicate;
   private Place<T> place;
   private Transition transition;
   private Direction direction;

   public Arc(Place place, Transition transition) {
      this(place, transition, it -> true);
   }

   public Arc(Place place, Transition transition, Predicate<T> predicate) {
      this.place = place;
      this.transition = transition;
      this.direction = PLACE_TO_TRANSITION;
      this.predicate = predicate;
      transition.addIncoming(this);
   }

   public Arc(Transition transition, Place place) {
      this.place = place;
      this.transition = transition;
      this.direction = TRANSITION_TO_PLACE;
      this.predicate = null;
      transition.addOutgoing(this);
   }

   public Place<T> getPlace() {
      return place;
   }

   public Transition getTransition() {
      return transition;
   }

   public boolean canFire(int weight) {
      return direction.canFire(place, weight, predicate);
   }

   enum Direction {
      PLACE_TO_TRANSITION {
         public boolean canFire(Place p, int weight, Predicate predicate) {
            return p.numberOfTokensGreaterThan(weight) && predicate.test(p.peekToken());
         }
      },

      TRANSITION_TO_PLACE {
         public boolean canFire(Place p, int weight, Predicate predicate) {
            return p.hasCapacityOf(weight);
         }
      };

      public abstract boolean canFire(Place p, int weight, Predicate predicate);
   }
}
