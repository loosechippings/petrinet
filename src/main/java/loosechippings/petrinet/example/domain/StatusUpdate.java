package loosechippings.petrinet.example.domain;

public class StatusUpdate {

   private Status status;

   public Status getStatus() {
      return status;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public enum Status {
      MATCHED,
      SETTLED
   }
}
