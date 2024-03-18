public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public abstract void initialized() throws IllegalCommandException;

    public abstract void joinGame() throws IllegalCommandException;

    public abstract void placedCard() throws IllegalCommandException;

    public abstract void drawnCard() throws IllegalCommandException;

    public abstract void matchEnded() throws IllegalCommandException;
}