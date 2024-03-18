public class WaitPlayerState extends State {
    public WaitPlayerState(Game game) {
        super(game);
    }

    @Override
    public void initialized() throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        if (game.isFull()) {
            throw new IllegalCommandException("Game already full");
        }
        super.game.createNewPlayers(nickname);
    }

    private void createNewPlayers(String nickname, Color color) {
        super.game.getPlayers().get(super.game.getPlayers().size() - 1).setNickname(nickname);
        super.game.getPlayers().get(super.game.getPlayers().size() - 1).setColor(color);
        if (isFull()) {
            super.game.setState(new WaitPlayerState(super.game));
        } else {
            super.game.setState(new PlacedCardState(super.game));
        }
    }

    private boolean isFull() {
        if (super.game.getPlayers().size().equals(super.game.getNumPlayers())) {
            return true;
        }
        return false;
    }

    @Override
    public void placedCard() throws IllegalCommandException {
        throw new IllegalCommandException("Can't place card yet");
    }

    @Override
    public void drawnCard() throws IllegalCommandException {
        throw new IllegalCommandException("Can't draw card yet");
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        throw new IllegalCommandException("Game not started yet");
    }
}