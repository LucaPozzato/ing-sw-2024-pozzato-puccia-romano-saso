package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

public class PlacedCardState extends State {
    public PlacedCardState(Game game) {
        super(game);
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
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
    public void drawnCard(String type, String id) throws IllegalCommandException {
        throw new IllegalCommandException("Card already drawn in last turn");
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        throw new IllegalCommandException("Match has not ended");
    }

    private void addCard(String idBottomCard, String idCard, String position) {
        Card bottomCard;
        Structure structure = super.game.getStructureByPlayer(super.game.getCurrentPlayer());
        Hand hand = super.game.getHandByPlayer(super.game.getCurrentPlayer());
        // iterate over list of cards in the hand of the player to find the card with
        // the same id and then add it to the structure and remove it from the hand
        for (Card card : hand.getCardsHand()) {
            if (card.getIdCard() == idCard) {
                // bottomCard = structure.getCard(idBottomCard);
                // structure.insertCard(bottomCard, card, position);
                hand.removeCard(card);
                break;
            }
        }
    }

    private void updatePoints(int points) {
        super.game.getBoard().updateScore(super.game.getCurrentPlayer(), points);
    }
}