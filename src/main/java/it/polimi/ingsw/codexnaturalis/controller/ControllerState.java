package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public abstract class ControllerState {

    protected Game game;
    protected RmiServer rmiServer;
    protected SocketServer socketServer;

    public ControllerState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        this.game = game;
        this.rmiServer = rmiServer;
        this.socketServer = socketServer;
    }

    public abstract void initialized(String clientId, String nick, Color color, int numPlayers);

    public abstract void joinGame(String clientId, String nickname, Color color);

    public abstract void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard);

    public abstract void placedCard(String clientId, Player player, Card father, Card placeThis, String position, Boolean frontUp);

    public abstract void drawnCard(String clientId, Player player, Card card, String fromDeck);

    // public abstract void text(String message, Player sender, Player receiver/*,
    // long timeStamp*/) throws IllegalCommandException;
}