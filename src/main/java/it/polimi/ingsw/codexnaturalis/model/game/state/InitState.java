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

public class InitState extends State {
    private ResourceParser resPar;
    private GoldParser goldPar;
    private InitialParser initPar;
    private ObjectiveParser objPar;

    public InitState(Game game) {
        super(game);
    }

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

    private void createFirstPlayer(String nick, Color color, int numPlayers) {
        // TODO togliere dal costruttore di Players il nickname e il colore e istituire
        // dei setter specifici

        super.game.getPlayers().add(new Player(nick, color));

        // Gives information about the match to Game class
        // [ ] Decide who plays first
        super.game.setCurrentPlayer(super.game.getPlayers().get(0));
        super.game.setNumPlayers(numPlayers);
    }

    private void dealHands(int numPlayers) throws IllegalCommandException {
        // Builds a new hand for each player
        // Fills each hand with the right number of cards drawn from the right deck

        for (int i = 0; i < numPlayers; i++) {
            super.game.setPlayerHand(super.game.getPlayers().get(i), new Hand());
            // The hand is made of 2 resource card and a gold one randomly selected from the
            // deck
            for (int j = 0; j < 2; j++) {
                super.game.getHandByPlayer(super.game.getPlayers().get(i))
                        .addCard(super.game.getDeck().drawResourceCard());
            }
            super.game.getHandByPlayer(super.game.getPlayers().get(i))
                    .addCard(super.game.getDeck().drawGoldCard());
        }
    }

    private void dealInitialCard() {
        initPar = new InitialParser();
        Collections.shuffle(initPar.parse());
        for (Player player : game.getPlayers()) {
            game.getHandByPlayer(player).setInitCard(initPar.parse().removeFirst());
        }
    }

    // Nick: il deal common objective deve aggiungere due carte a caso all'attributo
    // common objective della board e non alla mano del giocatore
    private void dealCommonObjective() {
        objPar = new ObjectiveParser();
        for (int i = 0; i < 2; i++) {
            game.getBoard().getCommonObjectives().add(objPar.parse().removeFirst());
        }
    }

    private void dealSecretObjective() {
        for (Player player : game.getPlayers()) {
            for (int i = 0; i < 2; i++) {
                game.getHandByPlayer(player).getChooseBetweenObj().add(objPar.parse().removeFirst());
            }
        }
    }

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

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @Override
    public void chooseSetUp(Player nickName, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Game not set up yet");
    }

    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException("Can't place card yet");
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException("Can't draw card yet");
    }
}
