//package it.polimi.ingsw.codexnaturalis.network.events;
//
//import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
//import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
//
//import java.io.Serial;
//
//public class EndTurnEvent extends Event {
//    @Serial
//    private static final long serialVersionUID = 582703916472093L;
//    private String state;
//    private Integer turnCounter;
//    private boolean lastTurn;
//
//    public EndTurnEvent(String state, Integer turnCounter, boolean lastTurn) {
//        this.lastTurn = lastTurn;
//        this.turnCounter = turnCounter;
//        this.state = state;
//    }
//
//    @Override
//    public void doJob(MiniModel miniModel) throws IllegalCommandException {
//        miniModel.setLastTurn(lastTurn);
//        miniModel.setTurnCounter(turnCounter);
//        miniModel.setState(state);
//    }
//}