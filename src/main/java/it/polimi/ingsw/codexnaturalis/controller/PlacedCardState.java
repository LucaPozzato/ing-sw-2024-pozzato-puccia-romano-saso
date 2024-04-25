package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class PlacedCardState extends ControllerState {
    // TODO: remove regionMatches and use equals -> code more readable
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
    public void chooseSetUp(Player nickName, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Game already set up");
    }

    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {

        if (!player.equals(game.getCurrentPlayer()))
            throw new IllegalCommandException("Not your turn");

        Structure structure = super.game.getStructureByPlayer(super.game.getCurrentPlayer());

        removeFromHand(placeThis);
        structure.placeCard(father, placeThis, position, frontUp);

        // Compute the points which are direct consequence of card's placement:

        // Points assigned because the placed card is a gold card or a resource card
        // with bonus points (these are actual points,
        // immediately assigned to the player who placed the card and which determines a
        // movement of his pawn on the board)
        int pointFromCard = structure.getPointsFromPlayableCard(placeThis, frontUp);
        updateActualPoints(pointFromCard);

        if (game.getBoard().getActualPoints(player) >= 20)
            game.setLastTurn();

        super.game.setState(new DrawnCardState(super.game));
        // Points resulting from resources objective are computed at the end of the game
        // (END GAME STATE) since they could be covered anytime during the match
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        throw new IllegalCommandException("Card already drawn in last turn");
    }

    private void removeFromHand(Card placeThis) throws IllegalCommandException {
        // Card bottomCard;
        Hand hand = super.game.getHandByPlayer(super.game.getCurrentPlayer());
        // iterate over list of cards in the hand of the player to find the card with
        // the same id and then add it to the structure and remove it from the hand
        for (Card card : hand.getCardsHand()) {
            if (card.equals(placeThis)) {
                // bottomCard = structure.getCard(idBottomCard);
                // structure.insertCard(bottomCard, card, position);
                hand.removeCard(card);
                break;
            }
        }
    }

    private void updateActualPoints(int points) throws IllegalCommandException {
        super.game.getBoard().updateActualScore(super.game.getCurrentPlayer(), points);
    }
}