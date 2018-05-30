package loosechippings.petrinet;

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
   private final Set<Transition> transitions;
   private final List<Arc> arcs;

   private Petrinet(Set places, Set transitions, List arcs) {
      this.places = places;
      this.transitions = transitions;
      this.arcs = arcs;
   }

   public void addToken(Place p) {
      if (places.contains(p) && !p.hasInputs()) {
         p.addToken();
      }
      else {
         throw new IllegalStateException("Can only add tokens to input places.");
      }
   }

   public List<Place> getPlacesWithTokens() {
      return places.stream().filter(it -> it.hasAtLeastTokens(1)).collect(Collectors.toList());
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
               printWriter.printf("\"%s\" -> \"%s\"\n", incomming.getPlace().getName(), transition.getName());
            });
            transition.getOutgoing().forEach(outgoing -> {
               printWriter.printf("\"%s\" -> \"%s\"\n", transition.getName(), outgoing.getPlace().getName());
            });
         });
         printWriter.println("}");
         fileWriter.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void fire() {
      transitions.stream().filter(it -> it.canFire()).forEach(it -> it.fire());
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
