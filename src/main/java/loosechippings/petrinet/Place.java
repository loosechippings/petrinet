package loosechippings.petrinet;

import java.util.ArrayList;
import java.util.List;

public class Place {

   private final String name;
   private int tokens = 0;
   private int weight = 1;
   private int maxCapacity = 1;
   private List<Arc> outboundArc;
   private List<Arc> inboundArc;

   public Place(String name) {
      inboundArc = new ArrayList<>();
      outboundArc = new ArrayList<>();
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void addOutboundArc(Arc a) {
      outboundArc.add(a);
   }

   public void addInboundArc(Arc a) {
      inboundArc.add(a);
   }

   public boolean hasInputs() {
      return inboundArc.size() > 0;
   }

   public boolean hasOutputs() {
      return outboundArc.size() > 0;
   }

   public boolean hasCapacityOf(int capacity) {
      return maxCapacity - tokens > capacity;
   }

   public boolean hasAtLeastTokens(int tokens) {
      return this.tokens >= tokens;
   }

   public void addToken() {
      tokens++;
   }
}
