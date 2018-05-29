package loosechippings.petrinet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Petrinet {

   private final Set<Place> places;
   private final Set<Transition> transitions;
   private final List<Arc> arcs;

   private Petrinet(Set places, Set transitions, List arcs) {
      this.places = places;
      this.transitions = transitions;
      this.arcs = arcs;
   }

   public void generateDot() {
      FileWriter fileWriter = null;
      try {
         fileWriter = new FileWriter("target/petrinet.dot");
         PrintWriter printWriter = new PrintWriter(fileWriter);
         printWriter.println("digraph G {");
         places.forEach(it -> printWriter.printf("\"%s\" [shape=circle]\n", it.getName()));
         transitions.forEach(it -> printWriter.printf("\"%s\" [shape=box]\n", it.getName()));
         transitions.forEach(transition -> {
            transition.getIncoming().forEach(incomming -> {
               printWriter.printf("\"%s\" -> \"%s\"\n", incomming.getName(), transition.getName());
            });
            transition.getOutgoing().forEach(outgoing -> {
               printWriter.printf("\"%s\" -> \"%s\"\n", transition.getName(), outgoing.getName());
            });
         });
         printWriter.println("}");
         fileWriter.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
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
