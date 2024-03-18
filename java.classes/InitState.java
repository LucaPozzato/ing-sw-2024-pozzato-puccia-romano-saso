import java.util.List;
import model.*;

public class InitState extends State {

    // Costruttore
    public InitState(Game game) {
        super(game);
    }

    // Metodi privati
    private void createDeck() {
        super.game.setDeck(new Deck());
        // Forall cards in json file
        super.game.getDeck.push(card);
    }

    private void createFirstPlayer(String nick, Color color, int numPlayers) {
        // TODO togliere dal costruttore di Players il nickname e il colore e istituire
        // dei setter specifici
        for (int i = 0; i < numPlayers; i++) {
            super.game.getPlayers().addPlayers(new Player());
        }
        super.game.getPlayers().get(0).setNickname(nick);
        super.game.getPlayers().get(0).setColor(color);
        setCurrentPlayer(super.game.getPlayers().get(0));
        setNumPlayers(numPlayers);
    }

    private void dealHands(int numPlayers) {
        // Costruisce una nuova mano per ogni giocatore
        // Riempie la nuova mano con una lista di carte pescata dai mazzi

        for (int i = 0; i < numPlayers; i++) {
            super.game.setPlayerHand(super.game.getPlayers().get(i), new Hand());
            for (int j = 0; j < 2; j++) {
                super.game.getHandByPlayer(super.game.getPlayers().get(i)).getCardsHand()
                        .add(super.game.getDeck().getResourceDeck().pull());
            }
            super.game.getHandByPlayer(super.game.getPlayers().get(i)).getCardsHand()
                    .add(super.game.getDeck().getGoldDeck().pull());
        }
    }

    private void dealSecretObjective() {

    }

    private void dealCommonObjective() {

    }

    // Metodi pubblici
    @override
    public void initialized() {
        super.game.setState(new PlacedCardState(super.game));
        createDeck();
        createFirstPlayer(nick, color, numPlayers);
        dealHands(numPlayers);
        dealSecretObjective();
        dealCommonObjective();

    }

    @override
    public void joinGame() throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @override
    public void placedCard() throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @override
    public void drawnCard() throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }

    @override
    public void matchEnded() throws IllegalCommandException {
        super.game.setState(new InitState(super.game));
        throw new IllegalCommandException();
    }
}
