package loosechippings.petrinet.example.domain;

public class Instruction {

   private String tradeReference;
   private Status status;
   private String instructionReference;

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

   public String getInstructionReference() {
      return instructionReference;
   }

   public void setInstructionReference(String instructionReference) {
      this.instructionReference = instructionReference;
   }

   public enum Status {
      SENT,
      MATCHED,
      SETTLED
   }
}
