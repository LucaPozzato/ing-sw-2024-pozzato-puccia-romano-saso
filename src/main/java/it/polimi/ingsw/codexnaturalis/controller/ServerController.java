package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.state.State;

public class ServerController {
    // model
    private final Game game;
    private final State state ;


    public ServerController(State state) {
        this.state = state;
        this.game = new Game(0);
    }


    // inizializza game -> già tutto inizializzato
    // espone metodi degli stati
    // drawCard -> game.state.drawCard


    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        this.state.joinGame(nickname,color);
    }

}
