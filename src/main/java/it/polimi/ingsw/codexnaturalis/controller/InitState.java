package it.polimi.ingsw.codexnaturalis.controller;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.ForcedEndEvent;
import it.polimi.ingsw.codexnaturalis.network.events.InLobbyEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

/**
 * Initial game state. It sets up the environment when the first player joins
 * the game and provides general information. Specifically it creates the decks
 * and deals player's hands.
 */
public class InitState extends ControllerState {
    private ResourceParser resPar;
    private GoldParser goldPar;
    private InitialParser initPar;
    private ObjectiveParser objPar;
    private List<ObjectiveCard> objCards;
    private List<InitialCard> initCards;

    /**
     * InitState's constructor
     *
     * @param game it's used to link the state with the game it has to model
     */
    public InitState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
        this.goldPar = new GoldParser();
        this.resPar = new ResourceParser();
        this.initPar = new InitialParser();
        this.initCards = initPar.parse();
        Collections.shuffle(initCards);
        // For demo purposes
        // Collections.shuffle(initCards, new Random(110110110));
        this.objPar = new ObjectiveParser();
        this.objCards = objPar.parse();
        Collections.shuffle(objCards);
        // For demo purposes
        // Collections.shuffle(objCards, new Random(110110110));
    }

    /**
     * Parses the JSON file containing the cards in order to create the gold and the
     * resource decks which are then shuffled.
     */
    private void createDecks() {
        // Creates an instance of deck and assigns it to the Game
        super.game.setDeck(new Deck(this.goldPar.parse(), this.resPar.parse()));
        // Shuffles the decks
        super.game.getDeck().shuffleGoldDeck();
        super.game.getDeck().shuffleResourceDeck();
    }

    /**
     * Allows to set first player's parameter and to let the game recognize the
     * current player
     *
     * @param nick       first player's nickname
     * @param color      first player's chosen color
     * @param numPlayers first player's chosen number of competitors
     */
    private void createFirstPlayer(String nick, String password, Color color, int numPlayers, String clientId)
            throws IllegalCommandException {

        Player player = new Player(nick, password);
        player.setColor(color);

        super.game.getPlayers().add(player);
        super.game.getConnected().put(player, true);
        super.game.getFromPlayerToId().put(player, clientId);
        // [ ] Decide who plays first
        super.game.setCurrentPlayer(super.game.getPlayers().getFirst());
        super.game.setNumPlayers(numPlayers); // throws illegal command exc
        super.game.addParticipant();
        // FIXME: clean this up
        super.game.getBoard().updateActualScore(player, 0); // throws illegal command exc
        // Uncomment to start the match with an increased score for player
        //super.game.getBoard().updateActualScore(player, 19);
    }

    /**
     * Builds a new hand for each player and fills each hand with the right number
     * of cards drawn from the right deck. The hand is made of 2 resource card and a
     * gold one randomly selected from the deck.
     *
     * @param numPlayers it's needed to know how many hands deal
     */
    private void dealHands(int numPlayers) throws IllegalCommandException {

        for (int i = 0; i < numPlayers; i++) {
            super.game.setPlayerHand(super.game.getPlayers().get(i), new Hand());
            for (int j = 0; j < 2; j++) {
                super.game.getHandByPlayer(super.game.getPlayers().get(i))
                        .addCard(super.game.getDeck().drawResourceCard());
            }
            super.game.getHandByPlayer(super.game.getPlayers().get(i))
                    .addCard(super.game.getDeck().drawGoldCard());
            super.game.setPlayerStructure(super.game.getPlayers().get(i), new Structure());
        }
    }

    /**
     * Parses and deals the initial card automatically taking it from the initial
     * card shuffled deck and assigning it to each player
     */
    private void dealInitialCard() {
        for (Player player : game.getPlayers()) {
            game.getHandByPlayer(player).setInitCard(initCards.removeFirst());
        }
    }

    /**
     * Randomly chooses two cards parsed from the objective card's JSON file and
     * assigns them to the commonObjective game's attribute
     */
    private void dealCommonObjective() {
        for (int i = 0; i < 2; i++) {
            game.getBoard().getCommonObjectives().add(objCards.removeFirst());
        }
    }

    /**
     * Randomly chooses two cards parsed from the objective card's JSON file in
     * order for the player to choose one between them as secret objective
     */

    private void dealSecretObjective() {
        for (Player player : game.getPlayers()) {
            for (int i = 0; i < 2; i++) {
                game.getHandByPlayer(player).getChooseBetweenObj().add(objCards.removeFirst());
            }
        }
    }

    /**
     * Performs one after the other the sequence of actions needed for the game's
     * set-up phase
     *
     * @param nick       needed to identify the player
     * @param color      needed for GUI's purposes
     * @param numPlayers needed for dealing hands and cards issues
     */
    @Override
    public void initialized(String clientId, String nick, String password, Color color, int numPlayers) {
        Event event = null;

        try {
            // If the creator inserted an invalid number of player the game ends
            if (numPlayers < 2 || numPlayers > 4)
                throw new IllegalCommandException("Number of players has to be between 2 and 4!");

            createDecks();

            game.setBoard(new Board());
            game.getBoard().addUncoveredCard(game.getDeck().drawResourceCard());
            game.getBoard().addUncoveredCard(game.getDeck().drawResourceCard());
            game.getBoard().addUncoveredCard(game.getDeck().drawGoldCard());
            game.getBoard().addUncoveredCard(game.getDeck().drawGoldCard());

            createFirstPlayer(nick, password, color, numPlayers, clientId);
            dealHands(numPlayers);
            dealInitialCard();

            dealCommonObjective();
            dealSecretObjective();

            event = new InLobbyEvent(clientId, super.game.getGameId(), "Wait", nick);

            super.game.setState(new WaitPlayerState(super.game, super.rmiServer, super.socketServer));

        } catch (IllegalCommandException e) {
            super.game.pushEvent(e.getMessage());
            event = new ErrorEvent(clientId, game.getGameId(), e.getMessage());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the player has already joined the game,
     * preventing redundant join attempts.
     *
     * @param clientId The client ID attempting to join.
     * @param nickname The nickname of the player attempting to join.
     * @param password The password provided by the player.
     * @param color    The color chosen by the player.
     */
    @Override
    public void joinGame(String clientId, String nickname, String password, Color color) {
        String error = "Game not created yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the game setup has already been
     * completed,
     * preventing redundant setup attempts.
     *
     * @param clientId The client ID attempting to set up the game.
     * @param nickname The player initiating the setup.
     * @param side     The side chosen by the player.
     * @param objCard  The objective card chosen by the player.
     */
    @Override
    public void chooseSetUp(String clientId, Player nickname, Boolean side, ObjectiveCard objCard) {
        String error = "Game not set up yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the player cannot place a card at the
     * current moment.
     *
     * @param clientId  The client ID attempting to place the card.
     * @param player    The player attempting to place the card.
     * @param father    The card where the placement is initiated.
     * @param placeThis The card being placed.
     * @param position  The position where the card is placed.
     * @param frontUp   The orientation of the card.
     */
    @Override
    public void placedCard(String clientId, Player player, Card father, Card placeThis, String position,
                           Boolean frontUp) {
        String error = "Can't place card yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }

    }

    /**
     * Sends an error event indicating that the player cannot draw a card at the
     * current moment.
     *
     * @param clientId The client ID attempting to place the card.
     * @param player   The player attempting to place the card.
     * @param card     The card to draw
     * @param fromDeck The deck from where to draw
     */
    @Override
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        String error = "Can't draw card yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Handles disconnection of a client from the game shutting it down and sending
     * a ForcedEndEvent to the other players
     *
     * @param clientId The client identifier.
     */
    @Override
    public void disconnect(String clientId) {
        String error = "Game was shut down due to clients' disconnections";
        super.game.pushEvent(error);
        Event event = new ForcedEndEvent(game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
        super.game.setState(new ForcedEndState(super.game, super.rmiServer, super.socketServer));
    }

    /**
     * Handles a player's attempt to rejoin the game.
     *
     * @param clientId The client identifier.
     * @param nickname The nickname of the client.
     * @param password The password of the client.
     */
    @Override
    public void rejoinGame(String clientId, String nickname, String password) {
        String error = "Can't rejoin game now";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

}
