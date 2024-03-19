import java.util.List;

public abstract class Game {

    private int gameId;
    private State gameState;
    private List<Player> players;
    private List<Hand> playerHand;
    private List<Structure> playerStructure;
    private Deck deck;
    private Board board;
    private int numPlayers;
    private int numParticipants;
    private Player currentPlayer;
    private Player nextPlayer;
    private List<View> observerList;

    public void game(int gameId) {
        this.gameId = gameId;
        //?
    }

    public void notifyAll() {
        //...
    }

    public void addObserver (View view){
        //...
    }

    public void removeObserver(View view){
        //...
    }

    public void addPlayer(){
        //...
    }

    public Player getCurrentPlater(){
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }
    public State getState() {
        return gameState;
    }

    public Deck getDeck() {
        return deck;
    }

    public Board getBoard() {
        return board;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Hand getHandByPlayer(Player player){
        //...
    }

    public Structure getStructureByPlayer(Player player){
        //...
    }

    public void setState (State state){
        this.gameState = state;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayerHand(Player player, Hand hand){
        //...
    }

    public void setPlayerStructure(Player player, Structure structure){
        //...
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }


}
