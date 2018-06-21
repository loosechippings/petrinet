package loosechippings.petrinet.example;

import loosechippings.petrinet.*;
import loosechippings.petrinet.example.domain.Instruction;
import loosechippings.petrinet.example.domain.StatusUpdate;
import loosechippings.petrinet.example.domain.Trade;

import java.util.List;

public class TradePetrinet {

   private Petrinet petrinet;
   Place<Trade> receivedTrade;
   Place<Instruction> settlementInstructed;
   Place<Instruction> instructionOpen;
   Place<StatusUpdate> receivedStatus;
   Place<Instruction> instructionClosed;

   private TokenDescriptor<Trade> tradeTokenDescriptor = new TokenDescriptor<>(Trade.class, "trade");
   private TokenDescriptor<Instruction> instructionTokenDescriptor = new TokenDescriptor<>(Instruction.class, "instruction");
   private TokenDescriptor<StatusUpdate> statusUpdateTokenDescriptor = new TokenDescriptor<>(StatusUpdate.class, "statusUpdate");

   public TradePetrinet() {
      receivedTrade = new Place<>("Received Trade", tradeTokenDescriptor);
      settlementInstructed = new Place<>("Settlement Instructed", instructionTokenDescriptor);
      instructionOpen = new Place<>("Instruction Open", instructionTokenDescriptor);
      receivedStatus = new Place<>("Received Status", statusUpdateTokenDescriptor);
      instructionClosed = new Place<>("Instruction Closed", instructionTokenDescriptor);
      Place<Instruction> instructionWithStatusApplied = new Place<>("Instruction with status", instructionTokenDescriptor);

      Transition<Instruction> instructSettlement = new Transition<>("Instruct settlement", this::instructTrade);
      Transition<Instruction> t1 = new Transition<>("t1", this::foo);
      Transition<Instruction> applyStatus = new Transition<>("Apply status", this::applyStatus);
      Transition<Instruction> statusIsOpen = new Transition<>("Status is open", this::checkIfStatusOpen);
      Transition<Instruction> statusIsClosed = new Transition<>("Status is closed", this::checkIfStatusClosed);

      Arc<Instruction> statusOpenArc = new Arc<>(instructionWithStatusApplied, statusIsOpen, this::statusIsOpen);
      Arc<Instruction> statusClosedArc = new Arc<>(instructionWithStatusApplied, statusIsClosed, this::statusIsClosed);

      petrinet = new Petrinet.Builder()
            .withArc(receivedTrade, instructSettlement)
            .withArc(instructSettlement, settlementInstructed)
            .withArc(settlementInstructed, t1)
            .withArc(t1, instructionOpen)
            .withArc(instructionOpen, applyStatus)
            .withArc(receivedStatus, applyStatus)
            .withArc(applyStatus, instructionWithStatusApplied)
            .withArc(statusOpenArc)
            .withArc(statusClosedArc)
            .withArc(statusIsOpen, instructionOpen)
            .withArc(statusIsClosed, instructionClosed)
            .build();
   }

   public Boolean statusIsOpen(Instruction i) {
      return !i.getStatus().equals(Instruction.Status.SETTLED);
   }

   public Boolean statusIsClosed(Instruction i) {
      return !statusIsOpen(i);
   }

   public Instruction instructTrade(Context context) {
      System.out.println("creating Instruction from Trade");
      Trade t = Trade.class.cast(context.getToken(tradeTokenDescriptor));
      Instruction i = new Instruction();
      i.setTradeReference(t.getReference());
      i.setStatus(Instruction.Status.SENT);
      return i;
   }

   public Instruction foo(Context context) {
      System.out.println("triggered foo");
      return context.getToken(instructionTokenDescriptor);
   }

   public Instruction applyStatus(Context context) {
      System.out.println("triggered applyStatus");
      Instruction i = context.getToken(instructionTokenDescriptor);
      StatusUpdate s = context.getToken(statusUpdateTokenDescriptor);
      switch (s.getStatus()) {
         case MATCHED:i.setStatus(Instruction.Status.MATCHED);
            break;
         case SETTLED:i.setStatus(Instruction.Status.SETTLED);
            break;
         default:
      }
      return i;
   }

   public Instruction checkIfStatusOpen(Context context) {
      System.out.println("triggered checkIfStatusOpen");
      return context.getToken(instructionTokenDescriptor);
   }

   public Instruction checkIfStatusClosed(Context context) {
      System.out.println("triggered checkIfStatusClosed");
      return context.getToken(instructionTokenDescriptor);
   }

   public void generateDot(String filepath) {
      petrinet.generateDot(filepath);
   }

   public void receiveTrade(Trade trade) {
      receivedTrade.addToken(trade);
   }

   public void receiveStatus(StatusUpdate statusUpdate) {
      receivedStatus.addToken(statusUpdate);
   }

   public void fire() {
      petrinet.fireUntilNoneCanFire();
   }

   public List<Place> getMarkedPlaces() {
      return petrinet.getMarkedPlaces();
   }
}
