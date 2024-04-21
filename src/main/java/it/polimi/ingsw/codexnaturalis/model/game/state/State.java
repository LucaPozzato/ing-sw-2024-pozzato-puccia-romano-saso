package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public abstract void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException;

    public abstract void joinGame(String nickname, Color color) throws IllegalCommandException;

    public abstract void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard)
            throws IllegalCommandException;

    // Commands:
    // [ ] choose initial card side
    // [ ] choose secret objective
    // [ ] map player-action to set the next state

    public abstract void placedCard(Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException;

    public abstract void drawnCard(String type, String id) throws IllegalCommandException;

    public abstract void matchEnded() throws IllegalCommandException;
}