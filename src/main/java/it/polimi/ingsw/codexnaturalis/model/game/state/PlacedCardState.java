package it.polimi.ingsw.codexnaturalis.model.game.state;

import java.util.ArrayList;
import java.util.List;

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
    public void placedCard(Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        Structure structure = super.game.getStructureByPlayer(super.game.getCurrentPlayer());

        removeFromHand(placeThis);
        structure.placeCard(father, placeThis, position, frontUp);

        // TODO: update points due to GoldCard or ResourceCard

        int newPoints = structure.countPattern(getSuitablePatt(placeThis, gatherPatterns()), placeThis);
        updateTotalPoints(newPoints);
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

    private void removeFromHand(Card placeThis) {
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

    private List<Card> gatherPatterns() {
        // Declare a list that collects the secret objective associated with the
        // interested player and the common objectives
        List<Card> totPatterns = new ArrayList<>();
        String comparison = new String("OP");

        // gather the secret objective of the interested player in totObj if it is a
        // pattern card
        Card secretObjCard = super.game.getHandByPlayer(super.game.getCurrentPlayer()).getSecretObjective();
        String idSecretObj = secretObjCard.getIdCard();
        if (idSecretObj.regionMatches(0, comparison, 0, 2)) {
            totPatterns.add(secretObjCard);
        }

        // gathers common objectives in totObj if they are pattern cards
        Card commonObj0 = super.game.getBoard().getCommonObjective().get(0);
        String idCommonObj0 = commonObj0.getIdCard();
        Card commonObj1 = super.game.getBoard().getCommonObjective().get(1);
        String idCommonObj1 = commonObj1.getIdCard();
        if (idCommonObj0.regionMatches(0, comparison, 0, 2)) {
            totPatterns.add(commonObj0);
        }
        if (idCommonObj1.regionMatches(0, comparison, 0, 2)) {
            totPatterns.add(commonObj1);
        }

        // totPatterns will contain the list of pattern objective cards available on the
        // board and in the specific player's hand
        return totPatterns;
    }

    // Return the sublist of gatherPatterns that suits the placed card
    // The goal is to minimize the set of patterns I'm going to check for in
    // Structure
    private List<Card> getSuitablePatt(Card placeThis, List<Card> patternList) throws IllegalCommandException {
        switch (placeThis.getSymbol()) {
            case "SHROOM":
                for (Card card : patternList) {
                    if (!card.getMustHave().equals("RED")) {
                        patternList.remove(card);
                    }
                }
                return patternList;
            case "VEGETABLE":
                for (Card card : patternList) {
                    if (!card.getMustHave().equals("GREEN")) {
                        patternList.remove(card);
                    }
                }
                return patternList;
            case "ANIMAL":
                for (Card card : patternList) {
                    if (!card.getMustHave().equals("BLUE")) {
                        patternList.remove(card);
                    }
                }
                return patternList;
            case "INSECT":
                for (Card card : patternList) {
                    if (!card.getMustHave().equals("PURPLE")) {
                        patternList.remove(card);
                    }
                }
                return patternList;

            default:
                throw new IllegalCommandException();
        }
    }

    private void updatePoints(int points) {
        super.game.getBoard().updateScore(super.game.getCurrentPlayer(), points);
    }

    private void updateTotalPoints(int points) {
        super.game.getBoard().updateTotalScore(super.game.getCurrentPlayer(), points);
    }
}