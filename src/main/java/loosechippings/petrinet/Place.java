package loosechippings.petrinet;

import java.util.ArrayList;
import java.util.List;

public class Place<T> {

   private final String name;
   private List<T> tokens;
   private int weight = 1;
   private int maxCapacity = 1;
   private List<Arc> outboundArc;
   private List<Arc> inboundArc;
   private TokenDescriptor<T> descriptor;

   public Place(String name, TokenDescriptor<T> descriptor) {
      inboundArc = new ArrayList<>();
      outboundArc = new ArrayList<>();
      tokens = new ArrayList<>();
      this.descriptor = descriptor;
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public TokenDescriptor<T> getDescriptor() {
      return descriptor;
   }

   public boolean hasInputs() {
      return inboundArc.size() > 0;
   }

   public boolean hasOutputs() {
      return outboundArc.size() > 0;
   }

   public boolean hasCapacityOf(int capacity) {
      return maxCapacity - tokens.size() > capacity;
   }

   public boolean numberOfTokensGreaterThan(int tokens) {
      return this.tokens.size() >= tokens;
   }

   public void addToken(T token) {
      if (hasInputs()) {
         throw new IllegalStateException("Can only add tokens to input places.");
      }
      tokens.add(token);
   }

   public T peekToken() {
      return tokens.get(0);
   }

   public T removeToken() {
      return tokens.remove(0);
   }

   @Override
   public String toString() {
      return String.format("place: %s, token count: %d", name, tokens.size());
   }
}
