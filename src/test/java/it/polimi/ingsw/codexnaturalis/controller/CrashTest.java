package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CrashTest {

    @Test
    void CrashTester() {


        // This test's aim is to examine early terminations. In particular in InitGameState a disconnection is performed and the followings are checked:
        // -> That the state moves to ForcedEndState
        // -> That the game is shut and any other action is feasible

        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            game = new Game(0, rmiServer, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //The state has to be InitState at the beginning
        assertInstanceOf(InitState.class, game.getState());

        //If the creator insert an invalid number of player then the state moves to ForcedEndState
        game.getState().initialized("0", "nick", "pw", Color.BLUE, 66);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Number of players has to be between 2 and 4!", game.getEventTracker().pop());

        //If the creator gets disconnected before the game is set up then the state moves to ForcedEnd state
        game.getState().disconnect("0");

        //Series of command that don't bring me anywhere if called in this state
        game.getState().joinGame("0", "nick", "pw", Color.RED);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", new Player(), true, new ObjectiveCard("R01", 1, "Chair", "shroom", 2, new int[1]));
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().placedCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().drawnCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "Resource");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().rejoinGame("1", "luca", "pw");
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

        game.getState().initialized("0", "nick", "pw", Color.BLUE, 2);
        assertEquals("No server found", game.getEventTracker().pop());
        assertEquals("Match Ended", game.getEventTracker().pop());

    }
}
