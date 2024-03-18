import model.Card;

public class PlacedCardState {
    public PlacedCardState(Game game) {
        super(game);
    }

    @Override
    public void initialized() throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame() throws IllegalCommandException {
        throw new IllegalCommandException("Game already joined");
    }

    @Override
    public void placedCard(String idBottomCard, String idCard, int points, String position)
            throws IllegalCommandException {
        addCard(idBottomCard, idCard, position);
        updatePoints(points);
        super.game.setState(new DrawnCardState(super.game));
    }

    @Override
    public void drawnCard() throws IllegalCommandException {
        throw new IllegalCommandException("Card already drawn in last turn");
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        throw new IllegalCommandException("Match has not ended");
    }

    private void addCard(String idBottomCard, String idCard, String position) {
        Card bottomCard = new Card();
        Structure structure = super.game.getStructureByPlayer(super.game.getCurrentPlayer());
        Hand hand = super.game.getHandByCurrentPlayer(super.game.getCurrentPlayer());
        // iterate over list of cards in the hand of the player to find the card with
        // the same id and then add it to the structure and remove it from the hand
        for (Card card : hand.getCardsHand()) {
            if (card.getId().equals(idCard)) {
                bottomCard = structure.getCard(idBottomCard);
                structure.addCard(bottomCard, card, position);
                hand.removeCard(card);
                break;
            }
        }
    }

    private void updatePoints(int points) {
        super.game.getBoard().updateScore(super.game.getCurrentPlayer(), points);
    }
}