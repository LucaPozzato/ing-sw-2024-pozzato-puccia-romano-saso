public class EndGameState {
    public EndGameState(Game game) {
        this.game = game;
    }

    public void initialized() throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    public void joinGame() throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");

    }

    public void placedCard() throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    public void drawnCard() throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    public void matchEnded() throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }
}
