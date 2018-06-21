package loosechippings.petrinet.example.domain;

public class Instruction {

   private String tradeReference;
   private Status status;

   public String getTradeReference() {
      return tradeReference;
   }

   public void setTradeReference(String tradeReference) {
      this.tradeReference = tradeReference;
   }

   public Status getStatus() {
      return status;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public enum Status {
      SENT,
      MATCHED,
      SETTLED
   }
}
