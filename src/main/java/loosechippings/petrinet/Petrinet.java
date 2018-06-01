package loosechippings.petrinet;

import domain.Trade;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Petrinet {

   private final Set<Place> places;
   private final Set<Transition<?>> transitions;
   private final List<Arc> arcs;

   private Petrinet(Set places, Set transitions, List arcs) {
      this.places = places;
      this.transitions = transitions;
      this.arcs = arcs;
   }

   public List<Place> getPlacesWithTokens() {
      return places.stream()
            .filter(it -> it.hasAtLeastTokens(1))
            .collect(Collectors.toList());
   }

   public void generateDot() {
      FileWriter fileWriter = null;
      try {
         fileWriter = new FileWriter("target/petrinet.dot");
         PrintWriter printWriter = new PrintWriter(fileWriter);
         printWriter.println("digraph G {");
         printWriter.printf("subgraph place {\ngraph [shape=circle,color=gray]\nnode [shape=circle,fixedsize=true,width=2]\n");
         places.forEach(it -> printWriter.printf("\"%s\"\n", it.getName()));
         printWriter.printf("}\n");
         printWriter.printf("subgraph transitions {\nnode [shape=rect,height=0.2,width=2]\n");
         transitions.forEach(it -> printWriter.printf("\"%s\" [shape=box]\n", it.getName()));
         printWriter.printf("}\n");
         transitions.forEach(transition -> {
            transition.getIncoming().forEach(arc -> {
               printWriter.printf("\"%s\" -> \"%s\"\n", arc.getPlace().getName() , transition.getName());
            });
            transition.getOutgoing().forEach(arc -> {
               printWriter.printf("\"%s\" -> \"%s\"\n", transition.getName(), arc.getPlace().getName());
            });
         });
         printWriter.println("}");
         fileWriter.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void fireOnce() {
      transitions.stream().filter(it -> it.canFire()).forEach(it -> it.fire());
   }

   // todo: this should get the set of transitions that can fire and then fire them
   // otherwise we risk firing dependant transitions in the same round of execution
   public void fireUntilNoneCanFire() {
      boolean fired;
      do {
         fired = false;
         for (Transition transition : transitions) {
            if (transition.canFire()) {
               transition.fire();
               fired = true;
            }
         }
      } while(fired);
   }

   public static class Builder {

      private Set<Place> places;
      private Set<Transition> transitions;
      private List<Arc> arcs;

      public Builder() {
         this.places = new HashSet<>();
         this.transitions = new HashSet<>();
         this.arcs = new ArrayList<>();
      }

      public Builder withArc(Place p, Transition t) {
         places.add(p);
         transitions.add(t);
         arcs.add(new Arc(p, t));
         return this;
      }

      public Builder withArc(Transition t, Place p) {
         places.add(p);
         transitions.add(t);
         arcs.add(new Arc(t, p));
         return this;
      }

      public Petrinet build() {
         return new Petrinet(places, transitions, arcs);
      }
   }
}
