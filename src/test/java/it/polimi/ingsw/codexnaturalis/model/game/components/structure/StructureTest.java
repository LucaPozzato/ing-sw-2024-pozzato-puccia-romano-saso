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

        Structure structure = new Structure();
        Structure secondStructure = new Structure();

        InitialCard initialCardTest = new InitialParser().parse().get(0);
        ResourceCard firstResCardTest = new ResourceParser().parse().get(0);
        ResourceCard secondResCardTest = new ResourceParser().parse().get(1);
        GoldCard firstGoldCardTest = new GoldParser().parse().get(1);
        ResourceCard thirdResCardTest = new ResourceParser().parse().get(2);
        ResourceCard forthResCardTest = new ResourceParser().parse().get(5);


        String compareWith;

        // test inital card (front)

        try {
            structure.placeCard(null, initialCardTest, null, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // test inital card (back)

        try {
            secondStructure.placeCard(null, initialCardTest, null, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, secondStructure.getVisibleObjects());

        // place the first resourcecard on structure (front), then the second (front) and test

        try {
            structure.placeCard(initialCardTest, firstResCardTest, "BL", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            structure.placeCard(firstResCardTest, secondResCardTest, "BL", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // place the first resourcecard on secondStructure(back), then the second (back) and test

        try {
            secondStructure.placeCard(initialCardTest, firstResCardTest, "BL", false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            secondStructure.placeCard(firstResCardTest, secondResCardTest, "BL", false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, secondStructure.getVisibleObjects());

        // place the first goldcard on Structure(front) and test

        try {
            structure.placeCard(initialCardTest, firstGoldCardTest, "BR", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        compareWith = "FEATHER: 0\nINK: 1\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        //more objects available
        try {
            structure.placeCard(firstGoldCardTest, forthResCardTest, "BR", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 2\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        //coverage case
        try {
            structure.placeCard(firstGoldCardTest, thirdResCardTest, "TR", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        compareWith = "FEATHER: 0\nINK: 1\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // place the first goldcard on Structure(back) and test
        try {
            secondStructure.placeCard(secondResCardTest, firstGoldCardTest, "BR", false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, secondStructure.getVisibleObjects());

        // [X] Initial card front when no object available
        // [X] Initial card back when no object available
        // [X] Various resource cards front when no object available
        // [X] Various resource cards back when no object available
        // [X] Various resource cards front + goldcard front when ONE object available
        // [X] Various resource cards front + goldcard front when TWO objects available
        // [X] Various resource cards front + goldcard front when ONE object available is covered by a non-object card
        // [X] Various resource cards back + goldcard back when no object available


    }

    @Test
    void testGetVisibleResources() {

        Structure structure = new Structure();
        Structure secondStructure = new Structure();

        InitialCard initialCardTest = new InitialParser().parse().get(0);
        ResourceCard firstResCardTest = new ResourceParser().parse().get(0);
        ResourceCard secondResCardTest = new ResourceParser().parse().get(1);
        GoldCard firstGoldCardTest = new GoldParser().parse().get(1);

        String compareWith;

        // test inital card (front)

        try {
            structure.placeCard(null, initialCardTest, null, true);
        } catch (Exception e) {
            System.err.println("catch 1");
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 2\nSHROOM: 0";
        assertEquals(compareWith, structure.getVisibleResources());

        // test inital card (back)

        try {
            secondStructure.placeCard(null, initialCardTest, null, false);
        } catch (Exception e) {
            System.err.println("catch 2");
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 1\nSHROOM: 1";
        assertEquals(compareWith, secondStructure.getVisibleResources());

        // place the first resourcecard on structure (front), then the second (front) and test

        try {
            structure.placeCard(initialCardTest, firstResCardTest, "BL", true);
        } catch (Exception e) {
            System.err.println("catch 3");
        }

        try {
            structure.placeCard(firstResCardTest, secondResCardTest, "BL", true);
        } catch (Exception e) {
            System.err.println("catch 4");
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 1\nSHROOM: 3";
        assertEquals(compareWith, structure.getVisibleResources());

        // place the first resourcecard on secondStructure(back), then the second (back) and test

        try {
            secondStructure.placeCard(initialCardTest, firstResCardTest, "BL", false);
        } catch (Exception e) {
            System.err.println("catch 5");
        }

        try {
            secondStructure.placeCard(firstResCardTest, secondResCardTest, "BL", false);
        } catch (Exception e) {
            System.err.println("catch 6");
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 0\nSHROOM: 3";
        assertEquals(compareWith, secondStructure.getVisibleResources());

        // place the first goldcard on Structure(front) and test

        try {
            structure.placeCard(secondResCardTest, firstGoldCardTest, "BR", true);
        } catch (Exception e) {
            System.err.println("catch 7");
        }


        compareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 1\nSHROOM: 3";
        assertEquals(compareWith, structure.getVisibleResources());

        // place the first goldcard on Structure(back) and test
        try {
            secondStructure.placeCard(secondResCardTest, firstGoldCardTest, "BR", false);
        } catch (Exception e) {
            System.err.println("catch 8");
        }
        compareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 0\nSHROOM: 4";
        assertEquals(compareWith, secondStructure.getVisibleResources());

        // [X] Initial card front
        // [X] Initial card back
        // [X] Various resource cards front
        // [X] Various resource cards back
        // [X] Various resource cards front + goldcard front
        // [X] Various resource cards back + goldcard back
    }

    @Test
    void testPlaceCard() {

    }
}
