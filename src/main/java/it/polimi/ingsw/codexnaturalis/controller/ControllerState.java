package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public abstract class ControllerState {
    protected Game game;

    public ControllerState(Game game) {
        this.game = game;
    }

    public abstract void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException;

    public abstract void joinGame(String nickname, Color color) throws IllegalCommandException;

    public abstract void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard)
            throws IllegalCommandException;

    public abstract void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException;

    public abstract void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException;
}