package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedGame {
    @Test
    void Initialize(){

        InitState initState;
        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            SocketServer socketServer = new SocketServer();
            game = new Game(0, rmiServer, socketServer);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        game.getState().initialized("0", "nick", "pw", Color.RED, 2);

        //The game has to be created with the right id
        assertEquals(0, game.getGameId());
        //There are only two allowed player
        assertEquals(2, game.getNumPlayers());
        //The list of players has to have length 2
        assertEquals(2, game.getPlayers().size());
        //The nickname associated with the first player has to be nick
        assertEquals("nick", game.getPlayers().getFirst().getNickname());
        //The color associated with the first player has to be red
        assertEquals(Color.RED, game.getPlayers().getFirst().getColor());
        //The first player has to be the current player
        assertEquals("nick", game.getCurrentPlayer().getNickname());
        //The score associated to the only player in game has to be zero
        assertEquals(0, game.getBoard().getActualPoints(game.getCurrentPlayer()));
        //Common objectives have to be assigned since they involve all players
        assertEquals(2, game.getBoard().getCommonObjectives().size());
        //Two secret objectives have to be assigned to each player even if the second one is not in the lobby yet.
        //The player will choose between those in a second moment, so they are actually placed in choosebetween auxiliary structure.
        assertEquals(2, game.getHandByPlayer(game.getPlayers().getFirst()).getChooseBetweenObj().size());
        assertEquals(2, game.getHandByPlayer(game.getPlayers().get(1)).getChooseBetweenObj().size());
        //The four uncovered card (2 foreach deck have to be placed)
        assertEquals(4, game.getBoard().getUncoveredCards().size());
        //The other player is not in the lobby yet
        assertEquals("", game.getPlayers().get(1).getNickname());
        //One initial card has to be randomly assigned to each player
        assertNotNull(game.getHandByPlayer(game.getPlayers().getFirst()).getInitCard());
        assertNotNull(game.getHandByPlayer(game.getPlayers().get(1)).getInitCard());
        //The hands have to be dealt for both players
        assertEquals(3, game.getHandByPlayer(game.getPlayers().getFirst()).getCardsHand().size());
        assertEquals(3, game.getHandByPlayer(game.getPlayers().get(1)).getCardsHand().size());

        //Let's verify we moved from InitState -> WaitPlayerState
        assertInstanceOf(WaitPlayerState.class, game.getState());

        //Now we are waiting for another player to join the lobby.
        //Once he'll join the game will start

        //Let's suppose another player wants to join but his nickname is empty
        game.getState().joinGame("1", "", "pw", Color.BLUE);
        assertEquals("Nickname can't be empty", game.getEventTracker().pop());

        //Let's suppose another player wants to join but his nickname is already taken
        game.getState().joinGame("1", "nick", "pw", Color.BLUE);
        assertEquals("Nickname already taken", game.getEventTracker().pop());

        //Let's suppose another player wants to join but his color is already taken
        game.getState().joinGame("1", "luca", "pw", Color.RED);
        assertEquals("Color already taken", game.getEventTracker().pop());

        //Let's suppose finally another player join, let's check it has the correct parameters
        game.getState().joinGame("1", "luca", "pw", Color.BLUE);
        //The nickname associated with the new player has to be luca
        assertEquals("luca", game.getPlayers().get(1).getNickname());
        //The color associated with the new player has to be blue
        assertEquals(Color.BLUE, game.getPlayers().get(1).getColor());
        //The first player has to be the current player still
        assertNotEquals("luca", game.getCurrentPlayer().getNickname());
        //The score associated to the new player has to be zero
        assertEquals(0, game.getBoard().getActualPoints(game.getPlayers().get(1)));

        //Now everything is set. Let's verify we moved from WaitPlayerState -> ChooseSetUpState
        assertInstanceOf(ChooseSetUpState.class, game.getState());






    }
}
