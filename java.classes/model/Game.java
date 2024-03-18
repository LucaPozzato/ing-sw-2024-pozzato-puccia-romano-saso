import java.util.List;

public abstract class Game {

    private int gameId;
    private Board board;
    private List<Player> players;
    private int numPlayers;
    // private List<View> observerList;
    private COLORE currentPlayer;
    private COLORE nextPlayer;
    private List<Integer> commonObjectives;
    private List<List<Integer>> placedCards;

    public Game game(int id, int numPlayers, String firstPlayer) {
        this.gameId = id;
        this.numPlayers = numPlayers;
        // create della Board
        // addPlayer con il primo player (firstPlayer)
        // setcommonobjective()
    }

    // public void notifyAll() {
    //
    // }

    public addObserver () {}

    public removeObserver() {}

    public drawResourceCards() {}

    public draGoldCard () {}

    public draSecretObjective() {}

    public getPoints() {}

    public getFuturePoints () {}

    public insertCard() {}

    public checkPattern () {}

    public calculateCurrentResources() {}

    public calculatePoints() {}
}
