package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;

public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public abstract void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException;

    public abstract void joinGame(String nickname, Color color) throws IllegalCommandException;

    public abstract void placedCard(String idBottomCard, String idCard, int points, String position)
            throws IllegalCommandException;

    public abstract void drawnCard(String type, String id) throws IllegalCommandException;

    public abstract void matchEnded() throws IllegalCommandException;
}