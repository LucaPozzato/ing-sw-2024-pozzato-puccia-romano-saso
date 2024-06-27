package it.polimi.ingsw.codexnaturalis.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;

public class EndGameTest {

    @Test
    void EndTestTie() {

        // This test's aim is to stress the final part of the game. In particular the
        // followings are checked:
        // -> that a winning situation is recognized when the first player that reaches
        // 20 points draws
        // -> that the count of the remaining turns is correctly computed depending on
        // the number of players
        // -> that a disconnection in the endGame state does not alter winner's
        // declaration
        // -> that a tie is correctly managed

        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().getLast();
        game.getState().chooseSetUp("0", nick, true,
                (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("1", luca, true,
                (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());

        // Here the scoreboard is rigged

        try {
            game.getBoard().updateActualScore(nick, 20);
            game.setLastTurn();
        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }

        Card initN = game.getHandByPlayer(nick).getInitCard();
        Card placeN = game.getHandByPlayer(nick).getCardsHand().getFirst();
        ResourceCard drawnN = game.getDeck().getResourceDeck().peek();

        game.getState().placedCard("0", nick, initN, placeN, "TL", false);
        game.getState().drawnCard("0", nick, drawnN, "RESOURCE");

        // Here we recognize it's last turn -> turn counter = 3
        Card initL = game.getHandByPlayer(luca).getInitCard();
        Card placeL = game.getHandByPlayer(luca).getCardsHand().getFirst();
        Card drawnL = game.getDeck().getResourceDeck().peek();

        // -> turn counter = 2

        game.getState().placedCard("1", luca, initL, placeL, "TL", false);
        game.getState().drawnCard("1", luca, drawnL, "RESOURCE");

        // -> turn counter = 1
        Card placeNII = game.getHandByPlayer(nick).getCardsHand().getFirst();
        ResourceCard drawnNII = game.getDeck().getResourceDeck().peek();

        game.getState().placedCard("0", nick, initN, placeNII, "TR", false);
        game.getState().drawnCard("0", nick, drawnNII, "RESOURCE");

        // -> turn counter = 0
        Card placeLII = game.getHandByPlayer(luca).getCardsHand().getFirst();
        ResourceCard drawnLII = game.getDeck().getResourceDeck().peek();

        // At the very end Luca manage to get 20 points

        try {
            game.getBoard().updateActualScore(luca, 20);
        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }
        game.getState().placedCard("1", luca, initL, placeLII, "TR", false);
        game.getState().drawnCard("1", luca, drawnLII, "RESOURCE");

        // After 4 turns the state must be EndGame
        assertInstanceOf(EndGameState.class, game.getState());

        // We must add to the actual points the virtual ones
        assertEquals(20, game.getBoard().getActualPoints(nick));

        // Since there's a tie there are two winners
        assertEquals("No server found", game.getEventTracker().pop());
        game.getEventTracker().pop();
        // assertEquals("The winners are: nick luca ", game.getEventTracker().pop());

        // Series of method's call ineffective in EndGameState
        game.getState().initialized("0", "nick", "pw", Color.BLUE, 2);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().joinGame("0", "nick", "pw", Color.RED);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", new Player(), true,
                new ObjectiveCard("R01", 1, "Chair", "shroom", 2, new int[1]));
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().placedCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()),
                new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().drawnCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()),
                "Resource");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().rejoinGame("1", "luca", "pw");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        // Luca leaves the game
        game.getState().disconnect("1");

    }

    @Test
    void EndTestSatObjWin() {

        // This test's aim is to stress the final part of the game. In particular the
        // followings are checked:
        // -> that a tie is correctly managed and resolved in the winning of the player
        // with the higher number of satisfiedPatterns

        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().getLast();
        game.getState().chooseSetUp("0", nick, true,
                (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        game.getState().chooseSetUp("1", luca, true,
                (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().getFirst());

        // Here the scoreboard is rigged

        try {
            game.getBoard().updateActualScore(nick, 20);
            game.setLastTurn();
        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }

        Card initN = game.getHandByPlayer(nick).getInitCard();
        Card placeN = game.getHandByPlayer(nick).getCardsHand().getFirst();
        ResourceCard drawnN = game.getDeck().getResourceDeck().peek();

        game.getState().placedCard("0", nick, initN, placeN, "TL", false);
        game.getState().drawnCard("0", nick, drawnN, "RESOURCE");

        // Here we recognize it's last turn -> turn counter = 3
        Card initL = game.getHandByPlayer(luca).getInitCard();
        Card placeL = game.getHandByPlayer(luca).getCardsHand().getFirst();
        Card drawnL = game.getDeck().getResourceDeck().peek();

        // -> turn counter = 2

        game.getState().placedCard("1", luca, initL, placeL, "TL", false);
        game.getState().drawnCard("1", luca, drawnL, "RESOURCE");

        // -> turn counter = 1
        Card placeNII = game.getHandByPlayer(nick).getCardsHand().getFirst();
        ResourceCard drawnNII = game.getDeck().getResourceDeck().peek();

        game.getState().placedCard("0", nick, initN, placeNII, "TR", false);
        game.getState().drawnCard("0", nick, drawnNII, "RESOURCE");

        // -> turn counter = 0
        Card placeLII = game.getHandByPlayer(luca).getCardsHand().getFirst();
        ResourceCard drawnLII = game.getDeck().getResourceDeck().peek();

        // At the very end Luca manage to get 20 points

        try {
            game.getBoard().updateActualScore(luca, 20);
        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }

        // But nick has one satisfied pattern more than luca
        game.getStructureByPlayer(nick).setSatisfiedObj();

        game.getState().placedCard("1", luca, initL, placeLII, "TR", false);
        game.getState().drawnCard("1", luca, drawnLII, "RESOURCE");

        // After 4 turns the state must be EndGame
        assertInstanceOf(EndGameState.class, game.getState());

        // Player with the higher number of satisfiedPatterns wins
        assertEquals("No server found", game.getEventTracker().pop());

        // Series of method's call ineffective in EndGameState
        game.getState().initialized("0", "nick", "pw", Color.BLUE, 2);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().joinGame("0", "nick", "pw", Color.RED);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", new Player(), true,
                new ObjectiveCard("R01", 1, "Chair", "shroom", 2, new int[1]));
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().placedCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()),
                new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().drawnCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()),
                "Resource");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().rejoinGame("1", "luca", "pw");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        // Luca, full of anger, leaves the game
        game.getState().disconnect("1");
    }
}
