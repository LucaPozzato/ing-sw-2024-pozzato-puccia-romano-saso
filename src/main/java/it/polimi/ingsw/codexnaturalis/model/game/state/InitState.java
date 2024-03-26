package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.*;
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

        // Creates the same number of player as specified by the first one
        for (int i = 0; i < numPlayers; i++) {
            super.game.addPlayer(new Player());
        }
        // Allows the first player to insert his parameters
        super.game.getPlayers().get(0).setNickname(nick);
        super.game.getPlayers().get(0).setColor(color);
        // Gives information about the match to Game class
        super.game.setCurrentPlayer(super.game.getPlayers().get(0));
        super.game.setNumPlayers(numPlayers);
    }

    private void dealHands(int numPlayers) {
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
        for (Player player : game.getPlayers()) {
            game.getHandByPlayer(player).setInitCard(initPar.parse().removeFirst());
        }
    }

    private void dealSecretObjective() {
        objPar = new ObjectiveParser();
        for (Player player : game.getPlayers()) {
            game.getHandByPlayer(player).setSecretObjective(objPar.parse().removeFirst());
        }
    }

    private void dealCommonObjective() {
        for (Player player : game.getPlayers()) {
            for (int i = 0; i < 2; i++) {
                game.getHandByPlayer(player).getChooseBetweenObj().add(objPar.parse().removeFirst());
            }
        }
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) {
        super.game.setState(new PlacedCardState(super.game));
        createDecks();
        createFirstPlayer(nick, color, numPlayers);
        dealHands(numPlayers);
        dealInitialCard();
        dealSecretObjective();
        dealCommonObjective();
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @Override
    public void placedCard(String idBottomCard, String idCard, int points, String position)
            throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @Override
    public void drawnCard(String type, String id) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }
}
