package it.polimi.ingsw.codexnaturalis.model.game.state;

import java.util.EmptyStackException;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class DrawnCardState extends State {
    public DrawnCardState(Game game) {
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
        throw new IllegalCommandException("Card already placed");
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        if (!player.equals(super.game.getCurrentPlayer())) {
            throw new IllegalCommandException("Not your turn");
        }

        updateDeck(card, fromDeck);
        nextTurn();
        super.game.setState(new PlacedCardState(super.game));
    }

    private void updateDeck(Card card, String fromDeck) throws IllegalCommandException {
        Card tempCard = null;

        if (super.game.getDeck().getGoldDeck().isEmpty() && super.game.getDeck().getResourceDeck().isEmpty()) {
            super.game.setLastTurn();
        }

        // if chosen deck is gold then draw from gold deck
        if (fromDeck.equals("GOLD")) {
            try {
                tempCard = super.game.getDeck().drawGoldCard();
            } catch (EmptyStackException e) {
                throw new IllegalCommandException("No more gold cards in the deck");
            }
        }
        // if chosen deck is resource then draw from resource deck
        else if (fromDeck.equals("RESOURCE")) {
            try {
                tempCard = super.game.getDeck().drawResourceCard();
            } catch (EmptyStackException e) {
                throw new IllegalCommandException("No more resource cards in the deck");
            }
        }
        // if chosen card is from uncovered cards then draw from uncovered cards
        else {
            // check what card in the uncovered cards has the same id as the chosen card
            super.game.getBoard().removeUncoveredCard(card);

            // draw a new card from the same deck as the chosen card and add it to the
            // uncovered cards
            if (card instanceof GoldCard) {
                tempCard = super.game.getDeck().drawGoldCard();
                super.game.getBoard().addUncoveredCard(tempCard);
            } else if (card instanceof ResourceCard) {
                tempCard = super.game.getDeck().drawResourceCard();
                super.game.getBoard().addUncoveredCard(tempCard);
            }
        }
        // add card in the hand of the player
        super.game.getHandByPlayer(super.game.getCurrentPlayer()).addCard(tempCard);

    }

    private void nextTurn() {
        super.game.setCurrentPlayer(super.game.getNextPlayer());
        if (super.game.isLastTurn()) {
            if (super.game.getTurnCounter() >= 0)
                // [ ] test the >= 0
                super.game.removeTurn();
            else
                super.game.setState(new EndGameState(super.game));
        }
        List<Player> players = super.game.getPlayers();
        super.game.setNextPlayer(players.get((players.indexOf(super.game.getCurrentPlayer()) + 1) % players.size()));
    }
}