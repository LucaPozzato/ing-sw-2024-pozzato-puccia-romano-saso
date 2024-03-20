package it.polimi.ingsw.codexnaturalis.model.game.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.*;
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
    public void placedCard(String idBottomCard, String idCard, int points, String position)
            throws IllegalCommandException {
        throw new IllegalCommandException("Card already placed");
    }

    @Override
    public void drawnCard(String type, int id) throws IllegalCommandException {
        updateDeck(type, id);
        nextTurn();
        super.game.setState(new PlacedCardState(super.game));
    }

    @Override
    public void matchEnded() {
        endMatch();
        super.game.setState(new EndGameState(super.game));
    }

    private void updateDeck(String type, int id) {
        List<Card> tempCardList = new ArrayList<>();
        Card tempCard = null;
        // if chosen deck is gold then draw from gold deck
        if (type.equals("GOLD")) {
            tempCard = super.game.getDeck().drawGoldCard();

        }
        // if chosen deck is resource then draw from resource deck
        else if (type.equals("RESOURCE")) {
            tempCard = super.game.getDeck().drawResourceCard();
        }
        // if chosen card is from uncovered cards then draw from uncovered cards
        else if (type.equals("UNCOVERED")) {
            tempCardList = super.game.getBoard().getUncoveredCards();
            // check what card in the uncovered cards has the same id as the chosen card
            for (Card card : tempCardList) {
                if (card.getIdCard().equals(id)) {
                    // remove the chosen card from the uncovered cards
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
                    break;
                }
            }
        }
        // add card in the hand of the player
        super.game.getHandByPlayer(super.game.getCurrentPlayer()).addCard(tempCard);
    }

    private void nextTurn() {
        super.game.setCurrentPlayer(super.game.getNextPlayer());
        List<Player> players = super.game.getPlayers();
        super.game.setNextPlayer(players.get((players.indexOf(super.game.getCurrentPlayer()) + 1) % players.size()));
    }

    private void endMatch() {
        System.out.println("Match ended");
    }
}