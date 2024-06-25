package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedGameTest {
    @Test
    void Initialize() {

        // This test's aim is to simulate a complete game in the starting phase and in midgame.
        // For this purpose the controller method's tested here are: InitState, WaitPlayerState, ChooseSetUpState, PlacedCardState and DrawCardState
        // In particular the followings are checked:
        // -> that a match starts correctly
        // -> that all the illegal commands called in every state are correctly managed
        // -> that the chat is working fine
        // -> that transitions between states in the controller FSA happens correctly

        Game game;

        try {
            RmiServer rmiServer = new RmiServer();
            SocketServer socketServer = new SocketServer();
            game = new Game(0, rmiServer, socketServer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //The state has to be InitState at the beginning
        assertInstanceOf(InitState.class, game.getState());

        //Series of command that don't bring me anywhere if called in this state
        game.getState().joinGame("0", "nick", "pw", Color.RED);
        assertEquals("Game not created yet", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", new Player(), true, new ObjectiveCard("R01", 1, "Chair", "shroom", 2, new int[1]));
        assertEquals("Game not set up yet", game.getEventTracker().pop());

        game.getState().placedCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("Can't place card yet", game.getEventTracker().pop());

        game.getState().drawnCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "Resource");
        assertEquals("Can't draw card yet", game.getEventTracker().pop());

        game.getState().rejoinGame("1", "luca", "pw");
        assertEquals("Can't rejoin game now", game.getEventTracker().pop());

        //Finally the proper method for InitState is called
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


        //Series of command that don't bring me anywhere if called in this state
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        assertEquals("Game already initialized", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", new Player(), true, new ObjectiveCard("R01", 1, "Chair", "shroom", 2, new int[1]));
        assertEquals("Game not set up yet", game.getEventTracker().pop());

        game.getState().placedCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("Can't place card yet", game.getEventTracker().pop());

        game.getState().drawnCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "Resource");
        assertEquals("Can't draw card yet", game.getEventTracker().pop());

        game.getState().rejoinGame("1", "luca", "pw");
        assertEquals("Can't rejoin game now", game.getEventTracker().pop());


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
        // Let's verify we moved from WaitPlayerState -> ChooseSetUpState
        assertInstanceOf(ChooseSetUpState.class, game.getState());

        //Now everything is set. The game is starting...
        Player nick = game.getPlayers().getFirst();
        Player luca = game.getPlayers().getLast();

        //Series of command that don't bring me anywhere if called in this state
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        assertEquals("Game already initialized", game.getEventTracker().pop());

        game.getState().joinGame("0", "luca", "pwd", Color.BLUE);
        assertEquals("Game already joined", game.getEventTracker().pop());

        game.getState().placedCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("Can't place card yet", game.getEventTracker().pop());

        game.getState().drawnCard("0", new Player(), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "Resource");
        assertEquals("Can't draw card yet", game.getEventTracker().pop());

        game.getState().rejoinGame("1", "luca", "pw");
        assertEquals("Can't rejoin game now", game.getEventTracker().pop());


        //Finally the proper method for ChooseSetUpState is called


        //The first player choose his secret objective
        game.getState().chooseSetUp("0", nick, true, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        //The secret objective chosen has to be in player's hand
        assertEquals(game.getHandByPlayer(nick).getSecretObjective(), game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());

        //The game state has to be chooseSetUp still
        assertInstanceOf(ChooseSetUpState.class, game.getState());

        //The first player tries to change his previous choice but he's blocked
        game.getState().chooseSetUp("0", nick, false, (ObjectiveCard) game.getHandByPlayer(nick).getChooseBetweenObj().getFirst());
        assertEquals("Player already made his choice", game.getEventTracker().pop());

        //The second player choose his secret objective
        game.getState().chooseSetUp("1", luca, true, (ObjectiveCard) game.getHandByPlayer(luca).getChooseBetweenObj().get(1));
        //The secret objective chosen has to be in player's hand
        assertEquals(game.getHandByPlayer(luca).getSecretObjective(), game.getHandByPlayer(luca).getChooseBetweenObj().get(1));

        //In both player structure has to appear only the initial card in the centre of the matrix
        assertEquals(1, game.getStructureByPlayer(nick).getCoordinateToCard().size());
        assertEquals(game.getHandByPlayer(nick).getInitCard(), game.getStructureByPlayer(nick).getCoordinateToCard().get(4040).getFirst());
        assertEquals(1, game.getStructureByPlayer(luca).getCoordinateToCard().size());
        assertEquals(game.getHandByPlayer(luca).getInitCard(), game.getStructureByPlayer(luca).getCoordinateToCard().get(4040).getFirst());

        //The game state has to be changed in PlacedCardState
        assertInstanceOf(PlacedCardState.class, game.getState());


        //Series of command that don't bring me anywhere if called in this state
        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        assertEquals("Game already initialized", game.getEventTracker().pop());

        game.getState().joinGame("0", "luca", "pwd", Color.BLUE);
        assertEquals("Game already joined", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", nick, true, new ObjectiveCard("R01", 1, "CHAIR", "SHROOM", 1, new int[1]));
        assertEquals("Game already set up", game.getEventTracker().pop());

        game.getState().drawnCard("0", nick, new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "Resource");
        assertEquals("Error while drawing", game.getEventTracker().pop());

        //We have to select a proper card to place on the initial one for both player among the ones they have in hand
        System.out.println(game.getHandByPlayer(nick).getCardsHand());
        System.out.println(game.getHandByPlayer(luca).getCardsHand());


        InitialCard initForNick = game.getHandByPlayer(nick).getInitCard();
        InitialCard initForLuca = game.getHandByPlayer(luca).getInitCard();
        Card placeThisN = game.getHandByPlayer(nick).getCardsHand().getFirst();
        Card placeThisL = game.getHandByPlayer(luca).getCardsHand().getFirst();

        //Luca tries to place in a turn which is not his. He gets blocked
        game.getState().placedCard("1", luca, initForLuca, placeThisL, "TL", false);
        assertEquals("Not your turn", game.getEventTracker().pop());


        //Finally the proper method for this state is called by Nick
        game.getState().placedCard("0", nick, initForNick, placeThisN, "TL", false);
        //The coordinate to card map has to be increased
        assertEquals(2, game.getStructureByPlayer(nick).getCoordinateToCard().size());
        //The new card has to be correctly placed
        assertEquals(placeThisN, game.getStructureByPlayer(nick).getCoordinateToCard().get(3941).getFirst());
        //The turn is not ended. It will end when the player draw
        assertEquals(nick.getNickname(), game.getCurrentPlayer().getNickname());
        //The placed card is not in player's hand after placing
        assertEquals(2, game.getHandByPlayer(nick).getCardsHand().size());
        //The state has to be changed in DrawnState
        assertInstanceOf(DrawnCardState.class, game.getState());

        //Luca tries to place his card while Nick has to draw
        game.getState().placedCard("1", luca, initForLuca, placeThisL, "TL", false);
        assertEquals("Error while placing card", game.getEventTracker().pop());

        //Let's suppose that Nick makes a series of illegal actions in this state

        game.getState().initialized("0", "nick", "pw", Color.RED, 2);
        assertEquals("Game already initialized", game.getEventTracker().pop());

        game.getState().joinGame("0", "luca", "pwd", Color.BLUE);
        assertEquals("Game already joined", game.getEventTracker().pop());

        game.getState().chooseSetUp("0", nick, true, new ObjectiveCard("R01", 1, "CHAIR", "SHROOM", 1, new int[1]));
        assertEquals("Game already set up", game.getEventTracker().pop());

        game.getState().placedCard("0", nick, new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), new ResourceCard("R01", "SHROOM", 1, new ArrayList<>()), "TL", true);
        assertEquals("Error while placing card", game.getEventTracker().pop());

        //Finally he makes the right move

        ResourceCard drawnCard = game.getDeck().getResourceDeck().pop();
        game.getDeck().getResourceDeck().push(drawnCard);

        System.out.println("Gold deck size: " + game.getDeck().getGoldDeck().size());
        System.out.println("Res deck size: " + game.getDeck().getResourceDeck().size());

        //The card had to be in the Resource Deck
        assertTrue(game.
                getDeck().
                getResourceDeck().
                stream().
                anyMatch(card -> card.getIdCard().equals(drawnCard.getIdCard())));

        //Luca tries to draw his card while Nick has to draw
        game.getState().drawnCard("1", luca, game.getDeck().getGoldDeck().peek(), "GOLD");
        assertEquals("Not your turn", game.getEventTracker().pop());

        //After this illegal action the size of the gold deck has to be the same as before
        assertEquals(36 , game.getDeck().getGoldDeck().size());

        //Nick draws from gold resource deck
        game.getState().drawnCard("0", nick, drawnCard, "RESOURCE");
        //The drawn card has to be in Nick's hand
        assertTrue(game.
                getHandByPlayer(nick).
                getCardsHand().
                stream().
                anyMatch(card -> card.getIdCard().equals(drawnCard.getIdCard())));

        //The card has to be removed from the deck it belonged
        assertFalse(game.
                getDeck().
                getResourceDeck().
                stream().
                anyMatch(card -> card.getIdCard().equals(drawnCard.getIdCard())));

        //The deck size has to be reduced
        assertEquals(33, game.getDeck().getResourceDeck().size());

        //FIXME: I don't understand what happens if I don't specify the deck amd the card is not among the uncovered ones

        //It's Luca place turn
        assertInstanceOf(PlacedCardState.class, game.getState());
        assertEquals("luca", game.getCurrentPlayer().getNickname());

        //He places the right card
        game.getState().placedCard("1", luca, initForLuca, placeThisL, "TL", false);
        //The coordinate to card map has to be increased
        assertEquals(2, game.getStructureByPlayer(luca).getCoordinateToCard().size());
        //The new card has to be correctly placed
        assertEquals(placeThisL, game.getStructureByPlayer(luca).getCoordinateToCard().get(3941).getFirst());
        //The turn is not ended. It will end when the player draw
        assertEquals(luca.getNickname(), game.getCurrentPlayer().getNickname());
        //The placed card is not in player's hand after placing
        assertEquals(2, game.getHandByPlayer(luca).getCardsHand().size());
        //The state has to be changed in DrawnState
        assertInstanceOf(DrawnCardState.class, game.getState());

        Card selectedOne = game.getBoard().getUncoveredCards().getFirst();
        //The selected card has to be among the uncovered ones before drawing
        assertTrue(game.
                getBoard().
                getUncoveredCards().
                stream().
                anyMatch(card -> card.getIdCard().equals(selectedOne.getIdCard())));

        //He draws a card from the uncovered ones
        game.getState().drawnCard("1", luca, selectedOne ,"");

        //The selected card has to be removed from the uncovered ones
        assertFalse(game.
                getBoard().
                getUncoveredCards().
                stream().
                anyMatch(card -> card.getIdCard().equals(selectedOne.getIdCard())));

        //The card has to be replaced immediately so the size of the uncovered card list has to be unchanged
        assertEquals(4, game.getBoard().getUncoveredCards().size());

        game.getState().placedCard("0", nick, placeThisN, game.getHandByPlayer(nick).getCardsHand().getFirst(), "TL", false);

        //The game keeps on going until the end...

        //A player tries to send a message
        assertEquals(0, game.getChat().getChatMessages().size());
        game.getState().updateChat("0", "hey there", nick, luca);
        assertEquals(1, game.getChat().getChatMessages().size());
        assertEquals("hey there", game.getChat().getChatMessages().getFirst().getMessage());

        //A message sent by a non-existing player gets blocked
        game.getState().updateChat("1", "who are you?", nick, new Player());
        assertEquals("receiver not found", game.getEventTracker().pop());

    }
}
