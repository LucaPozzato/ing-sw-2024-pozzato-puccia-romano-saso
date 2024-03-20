package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.*;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class InitState extends State {

    // Costruttore
    public InitState(Game game) {
        super(game);
    }

    // Metodi privati
    private void createDeck() {
        super.game.setDeck(new Deck());
        // Forall cards in json file
        // super.game.getDeck.push(card);
    }

    private void createFirstPlayer(String nick, Color color, int numPlayers) {
        // TODO togliere dal costruttore di Players il nickname e il colore e istituire
        // dei setter specifici
        // delete getDeck method from deck class
        for (int i = 0; i < numPlayers; i++) {
            super.game.addPlayer(new Player());
        }
        super.game.getPlayers().get(0).setNickname(nick);
        super.game.getPlayers().get(0).setColor(color);
        super.game.setCurrentPlayer(super.game.getPlayers().get(0));
        super.game.setNumPlayers(numPlayers);
    }

    private void dealHands(int numPlayers) {
        // Costruisce una nuova mano per ogni giocatore
        // Riempie la nuova mano con una lista di carte pescata dai mazzi

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

    private void dealSecretObjective() {

    }

    private void dealCommonObjective() {

    }

    // Metodi pubblici
    @Override
    public void initialized(String nick, Color color, int numPlayers) {
        super.game.setState(new PlacedCardState(super.game));
        createDeck();
        createFirstPlayer(nick, color, numPlayers);
        dealHands(numPlayers);
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
    public void drawnCard(String type, int id) throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }
}
