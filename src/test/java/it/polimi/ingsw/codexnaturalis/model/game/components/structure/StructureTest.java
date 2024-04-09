package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Stack;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;

public class StructureTest {
    @Test
    void testGetPointsFromCard() {
        Structure structure = new Structure();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        Stack<GoldCard> goldDeck = new GoldParser().parse();

        Card testCard;
        InitialCard initialCardTest = new InitialParser().parse().get(0);
        ObjectiveCard objectiveCardTest = new ObjectiveParser().parse().get(0);

        // checks for null card
        try {
            assertEquals(0, structure.getPointsFromCard(null, true));
        } catch (Exception e) {
            assertEquals("Card cannot be null", e.getMessage());
        }

        // try {
        // assertEquals(0, structure.getPointsFromCard(initialCardTest, true));
        // } catch (Exception e) {
        // assertEquals("Passed neither gold nor resource card", e.getMessage());
        // }

        // try {
        // assertEquals(0, structure.getPointsFromCard(objectiveCardTest, true));
        // } catch (Exception e) {
        // assertEquals("Passed neither gold nor resource card", e.getMessage());
        // }

        testCard = resourceDeck.get(0);

        // checks for null frontUp
        try {
            assertEquals(structure.getPointsFromCard(testCard, null), 0);
        } catch (Exception e) {
            assertEquals("FrontUp cannot be null", e.getMessage());
        }

        // checks for point in resource card, placed on both sides
        try {
            assertEquals("R01", testCard.getIdCard());
            assertEquals(0, structure.getPointsFromCard(testCard, true));
            assertEquals(0, structure.getPointsFromCard(testCard, false));

            testCard = resourceDeck.get(7);

            assertEquals("R08", testCard.getIdCard());
            assertEquals(1, structure.getPointsFromCard(testCard, true));
            assertEquals(0, structure.getPointsFromCard(testCard, false));
        } catch (Exception e) {
        }

        // checks for gold card with feather point type
        try {
            testCard = goldDeck.get(0);
            assertEquals(0, structure.getPointsFromCard(testCard, false));

            structure.placeCard(initialCardTest, null, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(4), "TR", true);
            structure.placeCard(initialCardTest, resourceDeck.get(20), "BL", false);
            structure.placeCard(initialCardTest, testCard, "BR", true);

            assertEquals(2, structure.getPointsFromCard(testCard, true));
        } catch (Exception e) {
        }

        // check gold card with ink point type
        try {
            testCard = goldDeck.get(1);
            assertEquals(0, structure.getPointsFromCard(testCard, false));

            structure.placeCard(initialCardTest, null, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "TR", true);
            structure.placeCard(initialCardTest, resourceDeck.get(2), "BL", false);
            structure.placeCard(initialCardTest, testCard, "BR", true);

            assertEquals(1, structure.getPointsFromCard(testCard, true));
        } catch (Exception e) {
        }

        // checks gold card with scroll point type
        try {
            testCard = goldDeck.get(2);
            assertEquals(0, structure.getPointsFromCard(testCard, false));

            structure.placeCard(initialCardTest, null, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(5), "TR", true);
            structure.placeCard(initialCardTest, resourceDeck.get(15), "BL", false);
            structure.placeCard(initialCardTest, testCard, "BR", true);

            assertEquals(3, structure.getPointsFromCard(testCard, true));
        } catch (Exception e) {
        }

        // checks gold card corner point type with 1 corner
        try {
            testCard = goldDeck.get(5);
            assertEquals(0, structure.getPointsFromCard(testCard, false));

            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "TR", false);
            structure.placeCard(initialCardTest, resourceDeck.get(2), "BL", false);
            structure.placeCard(initialCardTest, testCard, "BR", true);

            assertEquals(2, structure.getPointsFromCard(testCard, true));
        } catch (Exception e) {
        }

        // check gold card corner point type with 2 corners
        try {
            testCard = goldDeck.get(5);
            assertEquals(0, structure.getPointsFromCard(testCard, false));

            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "TR", false);
            structure.placeCard(initialCardTest, resourceDeck.get(2), "BL", false);
            structure.placeCard(resourceDeck.get(0), testCard, "BL", true);

            assertEquals(1, structure.getPointsFromCard(testCard, true));
            // FIXME: test does not fail
        } catch (Exception e) {
        }

        // [x] Gold card 1 corner
        // [ ] Gold card 2 corners
        // [ ] Gold card 3 corners
        // [ ] Gold card 4 corners
    }

    @Test
    void testGetPointsFromPatterns() {

    }

    @Test
    void testGetVisibleObjects() {
    }

    @Test
    void testGetVisibleResources() {

        Structure structure = new Structure();
        Structure secondStructure = new Structure();

        InitialCard initialCardTest = new InitialParser().parse().get(0);
        ResourceCard firstResCardTest = new ResourceParser().parse().get(0);
        ResourceCard secondResCardTest = new ResourceParser().parse().get(1);
        GoldCard firstGoldCardTest = new GoldParser().parse().get(0);

        ////////////////////////////// test inital card (front)

        try {
            structure.placeCard(null, initialCardTest, null, true);
        } catch (Exception e) {
            System.err.println("first catch");
        }

        String firstCompareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 2\nSHROOM: 0";
        assertEquals(firstCompareWith, structure.getVisibleResources());

        ///////////////////////////// test inital card (back)

        try {
            secondStructure.placeCard(null, initialCardTest, null, false);
        } catch (Exception e) {
            System.err.println("second catch");
        }

        String thenCompareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 1\nSHROOM: 1";
        assertEquals(thenCompareWith, secondStructure.getVisibleResources());

        ///////////////////////////// place the first resourcecard on structure (front),
        ///////////////////////////// then the second (front) and test

        try {
            structure.placeCard(initialCardTest, firstResCardTest, "BL", true);
        } catch (Exception e) {
            System.err.println("Third catch");
        }

        try {
            structure.placeCard(firstResCardTest, secondResCardTest, "BL", true);
        } catch (Exception e) {
            System.err.println("Forth catch");
        }

        String secondCompareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 1\nSHROOM: 3";
        assertEquals(secondCompareWith, structure.getVisibleResources());

        ///////////////////////////// place the first resourcecard on
        ///////////////////////////// secondStructure(back), then the second (back) and
        ///////////////////////////// test

        ResourceCard backFirstCardTest = new ResourceParser().parse().get(0);
        try {
            secondStructure.placeCard(initialCardTest, backFirstCardTest, "BL", false);
        } catch (Exception e) {
            System.err.println("2");
        }

        ResourceCard backSecondCardTest = new ResourceParser().parse().get(1);
        try {
            structure.placeCard(firstCardTest, backSecondCardTest, "BL", false);
        } catch (Exception e) {
            System.err.println("3");
        }

        secondCompareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 3\nSHROOM: 1";
        assertEquals(secondCompareWith, structure.getVisibleResources());

        ///////////////////////////// place the first goldcard on
        ///////////////////////////// Structure(front) and test

        GoldCard firstGoldTest = new GoldParser().parse().get(0);

        try {
            structure.placeCard(secondCard, firstCardTest, "BL", true);
        } catch (Exception e) {
            System.err.println("2");
        }

        ResourceCard secondCardTest = new ResourceParser().parse().get(1);
        try {
            structure.placeCard(firstCardTest, secondCardTest, "BL", true);
        } catch (Exception e) {
            System.err.println("3");
        }

        String secondCompareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 1\nSHROOM: 3";
        assertEquals(secondCompareWith, structure.getVisibleResources());
    }

    // [X] Initial card front
    // [X] Initial card back
    // [X] Various resource cards front
    // [X] Various resource cards back
    // [] Various resource cards front + goldcard front
    // [] Various resource cards back + goldcard back

    @Test
    void testPlaceCard() {

    }
}
