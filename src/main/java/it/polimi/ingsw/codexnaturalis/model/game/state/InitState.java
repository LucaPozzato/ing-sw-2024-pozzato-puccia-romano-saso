package it.polimi.ingsw.codexnaturalis.model.game.state;

import java.util.Collections;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

/**
 * Initial game state. It sets up the environment when the first player joins the game and provides general information. Specifically it creates the decks and deals player's hands.
 */
public class InitState extends State {
    private ResourceParser resPar;
    private GoldParser goldPar;
    private InitialParser initPar;
    private ObjectiveParser objPar;

    /**
     * InitState's constructor
     * @param game it's used to link the state with the game it has to model
     */
    public InitState(Game game) {
        super(game);
    }

    /**
     * Parses the JSON file containing the cards in order to create the gold and the resource decks which are then shuffled.
     */
    private void createDecks() {
        // Creates instances of the needed parser
        resPar = new ResourceParser();
        goldPar = new GoldParser();
        // Creates an instance of deck and assigns it to the Game
        super.game.setDeck(new Deck(goldPar.parse(), resPar.parse()));
        // Shuffles the decks
        super.game.getDeck().shuffleGoldDeck();
        super.game.getDeck().shuffleResourceDeck();
    }

    /**
     * Allows to set first player's parameter and to let the game recognize the current player
     * @param nick first player's nickname
     * @param color first player's chosen color
     * @param numPlayers first player's chosen number of competitors
     */
    private void createFirstPlayer(String nick, Color color, int numPlayers) {
        // TODO togliere dal costruttore di Players il nickname e il colore e istituire dei setter specifici

        super.game.getPlayers().add(new Player(nick, color));
        // [ ] Decide who plays first
        super.game.setCurrentPlayer(super.game.getPlayers().get(0));
        super.game.setNumPlayers(numPlayers);
    }

    /**
     * Builds a new hand for each player and fills each hand with the right number of cards drawn from the right deck. The hand is made of 2 resource card and a gold one randomly selected from the deck.
     * @param numPlayers it's needed to know how many hands deal
     * @throws IllegalCommandException it propagates above the exceptions coming from either from the draw method or the add (??)
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
        }
    }

    /**
     * Parses and deals the initial card automatically taking it from the initial card shuffled deck and assigning it to each player
     */
    private void dealInitialCard() {
        initPar = new InitialParser();
        Collections.shuffle(initPar.parse());
        for (Player player : game.getPlayers()) {
            game.getHandByPlayer(player).setInitCard(initPar.parse().removeFirst());
        }
    }

    /**
     * Randomly chooses two cards parsed from the objective card's JSON file and assigns them to the commonObjective game's attribute
     */
    // TODO: Verify that the collection returned from the parse() method is already shuffled
    private void dealCommonObjective() {
        objPar = new ObjectiveParser();
        for (int i = 0; i < 2; i++) {
            game.getBoard().getCommonObjectives().add(objPar.parse().removeFirst());
        }
    }

    /**
     * Randomly chooses two cards parsed from the objective card's JSON file in order for the player to choose one between them as secret objective
     */
    // TODO: Verify that the collection returned from the parse() method is already shuffled

    private void dealSecretObjective() {
        for (Player player : game.getPlayers()) {
            for (int i = 0; i < 2; i++) {
                game.getHandByPlayer(player).getChooseBetweenObj().add(objPar.parse().removeFirst());
            }
        }
    }

    /**
     * Performs one after the other the sequence of actions needed for the game's set-up phase
     * @param nick needed to identify the player
     * @param color needed for GUI's purposes
     * @param numPlayers needed for dealing hands and cards issues
     * @throws IllegalCommandException propagates above the possibly rising exceptions
     */

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        createDecks();
        createFirstPlayer(nick, color, numPlayers);
        dealHands(numPlayers);
        dealInitialCard();
        dealCommonObjective();
        dealSecretObjective();
        super.game.setState(new WaitPlayerState(super.game));
    }

    /**
     * Methods inherited from State. When it's called in InitState an exception is thrown.
     * @param nickname the state deputed to manage this method's invocation needs this parameter
     * @param color the state deputed to manage this method's invocation needs this parameter
     * @throws IllegalCommandException the exception thrown when this method is called in this state
     */
    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    /**
     * Methods inherited from State. When it's called in InitState an exception is thrown.
     * @param nickName the state deputed to manage this method's invocation needs this parameter
     * @param side the state deputed to manage this method's invocation needs this parameter
     * @param objCard the state deputed to manage this method's invocation needs this parameter
     * @throws IllegalCommandException the exception thrown when this method is called in this state
     */
    @Override
    public void chooseSetUp(Player nickName, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Game not set up yet");
    }

    /**
     * Methods inherited from State. When it's called in InitState an exception is thrown.
     * @param player the state deputed to manage this method's invocation needs this parameter
     * @param father the state deputed to manage this method's invocation needs this parameter
     * @param placeThis the state deputed to manage this method's invocation needs this parameter
     * @param position the state deputed to manage this method's invocation needs this parameter
     * @param frontUp the state deputed to manage this method's invocation needs this parameter
     * @throws IllegalCommandException the exception thrown when this method is called in this state
     */
    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException("Can't place card yet");
    }

    /**
     * Methods inherited from State. When it's called in InitState an exception is thrown.
     * @param player the state deputed to manage this method's invocation needs this parameter
     * @param card the state deputed to manage this method's invocation needs this parameter
     * @param fromDeck the state deputed to manage this method's invocation needs this parameter
     * @throws IllegalCommandException the exception thrown when this method is called in this state
     */

    /**
     * Methods inherited from State. When it's called in InitState an exception is thrown.
     * @param player the state deputed to manage this method's invocation needs this parameter
     * @param card the state deputed to manage this method's invocation needs this parameter
     * @param fromDeck the state deputed to manage this method's invocation needs this parameter
     * @throws IllegalCommandException the exception thrown when this method is called in this state
     */
    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException("Can't draw card yet");
    }
}
