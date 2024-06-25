package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DisconnectionInEveryStateTest {

    @Test
    public void disconnectInit(){

        //This test's aim is to examine disconnections. In particular in every feasible game's state a disconnection is performed and the followings are checked:
        // -> According to the nature of the state and the number of players still connected the game is shut down, waits for reconnections or keeps on going skipping disconnected player's turns
        // -> Since that socketServer is OFF during the test when try to send an event to a null server an exception is caught in order not to shut the game.

        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a player gets disconnected when the game is noi even set up then the game is over
        game.getState().disconnect("0");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Game was shut down due to clients' disconnections", game.getEventTracker().pop());
        assertInstanceOf(ForcedEndState.class, game.getState());
    }

    @Test
    public void disconnectWait(){
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a player gets disconnected when is the only one in the lobby then the games is over
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        game.getState().disconnect("0");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Game was shut down due to clients' disconnections", game.getEventTracker().pop());
        assertInstanceOf(ForcedEndState.class, game.getState());
    }


    @Test
    public void disconnectChoose(){
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a player gets disconnected when the game isn't started yet then the games is over
        game.getState().initialized("0", "nick", "pw", Color.RED, 3);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        game.getState().joinGame("2", "edo", "pw", Color.GREEN);

        game.getState().disconnect("1");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Game was shut down due to clients' disconnections", game.getEventTracker().pop());
        assertInstanceOf(ForcedEndState.class, game.getState());
    }

    @Test
    public void disconnectPlaceLeavingOne(){
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a player gets disconnected when choosing the secret objective then the games is over
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);

        Player nick = game.getPlayers().getFirst();

        game.getState().chooseSetUp("0", nick, true, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        game.getState().disconnect("1");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Game was shut down due to clients' disconnections", game.getEventTracker().pop());
        assertInstanceOf(ForcedEndState.class, game.getState());
    }

    @Test
    public void disconnectPlaceLeavingMore() {
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a player which is not the current one gets disconnected leaving a single opponent then the games is over if he doesn't reconnect in 30 seconds
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);

        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().get(1);

        game.getState().chooseSetUp("0", nick, true, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("1", luca, true, (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());

        game.getState().disconnect("1");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Game was shut down due to clients' disconnections", game.getEventTracker().pop());
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("You are the only player left, waiting for others to rejoin the game...", game.getEventTracker().pop());

        assertInstanceOf(ForcedEndState.class, game.getState());
    }


    @Test
    public void disconnectPlaceAndReconnected() {
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a generic player gets disconnected leaving a more than one opponent then the games is goes on without hom
        game.getState().initialized("0", "nick", "pw", Color.RED, 3);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        game.getState().joinGame("2", "edo", "pw", Color.GREEN);

        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().get(1);
        Player edo = game.getPlayers().get(2);
        Card initForN = game.getHandByPlayer(nick).getInitCard();
        Card placeN = game.getHandByPlayer(nick).getCardsHand().getFirst();

        game.getState().chooseSetUp("0", nick, true, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("1", luca, true, (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("2", edo, true, (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());

        game.getState().disconnect("0");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("nick has disconnected", game.getEventTracker().pop());
        assertFalse(game.getConnected().get(nick));

        //The disconnected player tries to reconnect but with a wrong nickname
        game.getState().rejoinGame("0", "nickk", "pw");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("no player with this nickname in the game 0", game.getEventTracker().pop());

        //The disconnected player tries to reconnect but with a wrong password
        game.getState().rejoinGame("0", "nick", "pwxxx");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("wrong password", game.getEventTracker().pop());

        //The disconnected player finally manage to reconnect
        game.getState().rejoinGame("0", "nick", "pw");
        assertEquals("No server found", game.getEventTracker().pop());
        assertTrue(game.getConnected().get(nick));

        //An already connected player can't rejoin the game
        game.getState().rejoinGame("0", "nick", "pw");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("the player is already connected", game.getEventTracker().pop());
    }
    @Test
    public void DisconnectedDraw() {
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When a generic player gets disconnected leaving a more than one opponent then the games is goes on without hom
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        assertEquals("No server found", game.getEventTracker().pop());
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        assertEquals("No server found", game.getEventTracker().pop());


        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().get(1);
        Card initForN = game.getHandByPlayer(nick).getInitCard();
        Card placeN = game.getHandByPlayer(nick).getCardsHand().getFirst();

        game.getState().chooseSetUp("0", nick, true, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        assertEquals("No server found", game.getEventTracker().pop());
        game.getState().chooseSetUp("1", luca, true, (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());
        assertEquals("No server found", game.getEventTracker().pop());



        game.getState().placedCard("0", nick, initForN, placeN, "TL",false);
        assertEquals("No server found", game.getEventTracker().pop());
        game.getState().disconnect("0");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Game was shut down due to clients' disconnections", game.getEventTracker().pop());
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("You are the only player left, waiting for others to rejoin the game...", game.getEventTracker().pop());
    }

    @Test
    public void DisconnectedDrawAndReconnected() {
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When the current player gets disconnected leaving a more than one opponent then the games is goes on without him
        game.getState().initialized("0", "nick", "pw", Color.RED, 3);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        game.getState().joinGame("2", "edo", "pw", Color.GREEN);

        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().get(1);
        Player edo = game.getPlayers().get(2);
        Card initForN = game.getHandByPlayer(nick).getInitCard();
        Card initForL = game.getHandByPlayer(luca).getInitCard();
        Card placeN = game.getHandByPlayer(nick).getCardsHand().getFirst();
        Card placeL = game.getHandByPlayer(luca).getCardsHand().getFirst();

        game.getState().chooseSetUp("0", nick, true, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("1", luca, true, (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("2", edo, true, (ObjectiveCard) game.getHandByPlayer(edo).getChooseBetweenObj().getFirst());


        int mapSizeBeforePlacing = game.getStructureByPlayer(nick).getCoordinateToCard().size();

        List<Pair<Card, Boolean>> structureBeforePlacing = new ArrayList<>(game.getStructureByPlayer(nick).getPlacedCards());

        game.getState().placedCard("0", nick, initForN, placeN, "TL",false);
        game.getState().disconnect("0");

        int mapSizeAfterDisconnection = game.getStructureByPlayer(nick).getCoordinateToCard().size();
        List<Pair<Card, Boolean>> structureAfterDisconnection = game.getStructureByPlayer(nick).getPlacedCards();

        game.getEventTracker().pop();
        game.getEventTracker().pop();


        assertEquals("restoring the structure and hand pre disconnection", game.getEventTracker().pop());
        //If a player gets disconnected after placing a card the structure must be reverted and the placed card must go back in his hand
        assertEquals(mapSizeBeforePlacing, mapSizeAfterDisconnection);

        //System.out.println("BEFORE" + structureBeforePlacing + structureBeforePlacing.size());
        //System.out.println("AFTER" + structureAfterDisconnection + structureAfterDisconnection.size());

        assertEquals(structureAfterDisconnection.size(), structureBeforePlacing.size());

        //Luca is now the current player
        assertEquals(game.getCurrentPlayer(), luca);
        //Luca places a card
        game.getState().placedCard("1", luca, initForL, placeL, "TL", false);
        //Now we are in DrawCard state when...
        assertInstanceOf(DrawnCardState.class, game.getState());

        //A player tries to rejoin even if he's still online
        game.getState().rejoinGame("1", "luca", "pw");
        game.getEventTracker().pop();
        assertEquals("the player is already connected", game.getEventTracker().pop());
        //The disconnected player tries to join using a wrong nickname
        game.getState().rejoinGame("o", "nickk", "pw");
        game.getEventTracker().pop();
        assertEquals("no player with this nickname in the game 0", game.getEventTracker().pop());
        //The disconnected player tries to join using a wrong nickname
        game.getState().rejoinGame("o", "nick", "pws");
        game.getEventTracker().pop();
        assertEquals("wrong password", game.getEventTracker().pop());
        //The disconnected player finally manage to insert the right parameters
        assertFalse(game.getConnected().get(nick));
        game.getState().rejoinGame("o", "nick", "pw");
        assertTrue(game.getConnected().get(nick));

        //Another player gets disconnected
        game.getState().disconnect("2");
        assertEquals("edo has disconnected", game.getEventTracker().pop());
    }

    @Test
    public void DisconnectForcedEnd(){
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        game.getState().disconnect("0");
        //Now that we are in ForcedEndState we perform the disconnection
        game.getState().disconnect("1");
        assertInstanceOf(ForcedEndState.class, game.getState());
    }

    
}
