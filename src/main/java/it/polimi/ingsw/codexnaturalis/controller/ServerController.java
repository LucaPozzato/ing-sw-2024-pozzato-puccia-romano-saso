package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;

public class ServerController {
    // model
    private final Game game;

    public ServerController() {
        this.game = new Game(0);
    }

    public void drawCard() throws IllegalCommandException {
        game.getState().initialized(null, null, 0);
    }

    public void joinGame() throws IllegalCommandException {
        game.getState().joinGame(null, null);
    }

    public void placeCard() throws IllegalCommandException {
        game.getState().placedCard(null, null, null, null);
    }

    public void endMatch() throws IllegalCommandException {
        game.getState().matchEnded();
    }

    // inizializza game -> già tutto inizializzato
    // espone metodi degli stati
    // drawCard -> game.state.drawCard
}
