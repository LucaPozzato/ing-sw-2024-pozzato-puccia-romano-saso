package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import javafx.util.Pair;

public class StructureTest {
    @Test
    void testGetPointsFromPlayableCard() {
        Structure structure = new Structure();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        Stack<GoldCard> goldDeck = new GoldParser().parse();

        Card testCard;
        InitialCard initialCardTest = new InitialParser().parse().get(2);
        ObjectiveCard objectiveCardTest = new ObjectiveParser().parse().get(0);

        // checks for null card
        try {
            assertEquals(0, structure.getPointsFromPlayableCard(null, true));
        } catch (Exception e) {
            assertEquals("Card cannot be null", e.getMessage());
        }

        try {
            assertEquals(0, structure.getPointsFromPlayableCard(initialCardTest, true));
        } catch (Exception e) {
            assertEquals("Passed neither gold nor resource card", e.getMessage());
        }

        try {
            assertEquals(0, structure.getPointsFromPlayableCard(objectiveCardTest, true));
        } catch (Exception e) {
            assertEquals("Passed neither gold nor resource card", e.getMessage());
        }

        testCard = resourceDeck.get(0);

        // checks for null frontUp
        try {
            assertEquals(structure.getPointsFromPlayableCard(testCard, null), 0);
        } catch (Exception e) {
            assertEquals("FrontUp cannot be null", e.getMessage());
        }

        // checks for point in resource card, placed on both sides
        try {
            assertEquals("R01", testCard.getIdCard());
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, true));
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            testCard = resourceDeck.get(7);

            assertEquals("R08", testCard.getIdCard());
            assertEquals(1, structure.getPointsFromPlayableCard(testCard, true));
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // checks for gold card with feather point type
        try {
            structure = new Structure();
            testCard = goldDeck.get(0);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(4), "TR", true);
            structure.placeCard(initialCardTest, resourceDeck.get(20), "BL", false);
            structure.placeCard(initialCardTest, testCard, "BR", true);

            assertEquals(2, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // check gold card with ink point type
        try {
            structure = new Structure();
            testCard = goldDeck.get(1);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "TR", false);
            structure.placeCard(initialCardTest, resourceDeck.get(10), "BL", false);
            structure.placeCard(initialCardTest, testCard, "BR", true);

            assertEquals(1, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // checks gold card with scroll point type
        try {
            structure = new Structure();
            testCard = goldDeck.get(2);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(6), "TL", true);
            structure.placeCard(initialCardTest, resourceDeck.get(16), "TR", true);
            structure.placeCard(initialCardTest, resourceDeck.get(25), "BL", true);
            structure.placeCard(resourceDeck.get(25), testCard, "BR", true);

            assertEquals(3, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // checks gold card corner point type with 1 corner
        try {
            structure = new Structure();
            testCard = goldDeck.get(4);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "TR", false);
            structure.placeCard(initialCardTest, testCard, "BL", true);

            assertEquals(2, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // check gold card corner point type with 2 corners
        try {
            structure = new Structure();
            testCard = goldDeck.get(4);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "BL", false);
            structure.placeCard(resourceDeck.get(0), testCard, "BL", true);

            assertEquals(4, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // check gold card corner point type with 3 corners
        try {
            structure = new Structure();
            testCard = goldDeck.get(4);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "BL", false);
            structure.placeCard(resourceDeck.get(0), resourceDeck.get(2), "TL", false);
            structure.placeCard(resourceDeck.get(2), resourceDeck.get(3), "BL", false);
            structure.placeCard(resourceDeck.get(0), testCard, "BL", true);

            assertEquals(6, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // check gold card corner point type with 4 corners
        try {
            structure = new Structure();
            testCard = goldDeck.get(4);
            assertEquals(0, structure.getPointsFromPlayableCard(testCard, false));

            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, resourceDeck.get(0), "TL", false);
            structure.placeCard(initialCardTest, resourceDeck.get(1), "BL", false);
            structure.placeCard(resourceDeck.get(0), resourceDeck.get(2), "TL", false);
            structure.placeCard(resourceDeck.get(2), resourceDeck.get(3), "BL", false);
            structure.placeCard(resourceDeck.get(1), resourceDeck.get(4), "BL", false);
            structure.placeCard(resourceDeck.get(4), resourceDeck.get(5), "TL", false);

            structure.placeCard(resourceDeck.get(0), testCard, "BL", true);

            assertEquals(8, structure.getPointsFromPlayableCard(testCard, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // [x] check for null card
        // [x] check for null frontUp
        // [x] check for point in resource card, placed on both sides
        // [x] check for gold card with feather point type
        // [x] check gold card with ink point type
        // [x] check gold card with scroll point type
        // [x] check gold card corner point type with 1 corner
        // [x] check gold card corner point type with 2 corners
        // [x] check gold card corner point type with 3 corners
        // [x] check gold card corner point type with 4 corners
    }

    // @Test
    // void testGetPointsFromPattern() {

    // Structure structure = new Structure();
    // ResourceParser parser = new ResourceParser();
    // ObjectiveParser objPars = new ObjectiveParser();

    // InitialCard initialCardTest = new InitialParser().parse().get(0);
    // ResourceCard RedTest1 = parser.parse().get(0); // R01
    // ResourceCard RedTest2 = parser.parse().get(1); // R02
    // ResourceCard BluTest1 = parser.parse().get(22); // R23
    // ResourceCard BluTest2 = parser.parse().get(26); // R27
    // ResourceCard BluTest3 = parser.parse().get(21); // R22
    // ResourceCard GreenTest1 = parser.parse().get(18); // R19

    // List<Card> patternList = new ArrayList<Card>();
    // patternList.add(objPars.parse().get(4));

    // try {
    // structure.placeCard(null, initialCardTest, null, true);
    // structure.placeCard(initialCardTest, RedTest1, "BL", true);
    // structure.placeCard(initialCardTest, BluTest1, "BR", true);
    // structure.placeCard(BluTest1, BluTest2, "BR", true);
    // structure.placeCard(BluTest2, BluTest3, "BL", true);
    // structure.placeCard(BluTest3, GreenTest1, "BL", true);
    // structure.placeCard(GreenTest1, RedTest2, "TL", true);

    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // int result = 0;
    // try {
    // result = structure.getPointsFromPatterns(patternList, RedTest2);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // assertEquals(3, result);

    // //////////////////////////////////

    // ResourceCard GreenTest2 = parser.parse().get(16); // R17
    // ResourceCard GreenTest3 = parser.parse().get(15); // R16
    // patternList.removeLast();
    // patternList.add(objPars.parse().get(1));

    // try {
    // structure.placeCard(GreenTest1, GreenTest2, "BR", true);
    // structure.placeCard(GreenTest2, GreenTest3, "BR", true);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // result = 0;
    // try {
    // result = structure.getPointsFromPatterns(patternList, GreenTest3)
    // + structure.getPointsFromPatterns(patternList, RedTest2);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(0, result);

    // ////////////////////////////////// <- mixed chair and stair pattern which
    // fails
    // ////////////////////////////////// due to share of a card among

    // ResourceCard GreenTest4 = parser.parse().get(12); // R13

    // try {
    // structure.placeCard(GreenTest3, GreenTest4, "BR", true);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // result = 0;
    // try {
    // result = structure.getPointsFromPatterns(patternList, RedTest2)
    // + structure.getPointsFromPatterns(patternList, GreenTest3);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // assertEquals(2, result);

    // //////////////////////////////////

    // Structure structure_II = new Structure();
    // ResourceParser parser_II = new ResourceParser();
    // InitialParser InitialParser_II = new InitialParser();
    // ObjectiveParser objPars_II = new ObjectiveParser();

    // InitialCard initialCardTest_II = InitialParser_II.parse().get(0);
    // ResourceCard RedTest1_II = parser_II.parse().get(0); // R01
    // ResourceCard RedTest2_II = parser_II.parse().get(1); // R02
    // ResourceCard BluTest1_II = parser_II.parse().get(22); // R23
    // ResourceCard BluTest2_II = parser_II.parse().get(26); // R27
    // ResourceCard BluTest3_II = parser_II.parse().get(21); // R22
    // ResourceCard GreenTest1_II = parser_II.parse().get(18); // R19
    // ResourceCard GreenTest2_II = parser_II.parse().get(16); // R17
    // ResourceCard GreenTest3_II = parser_II.parse().get(15); // R16
    // ResourceCard GreenTest4_II = parser_II.parse().get(12); // R13
    // ResourceCard PurpleTest1_II = parser_II.parse().get(32); // R33

    // List<Card> patternList_II = new ArrayList<Card>();
    // patternList_II.add(objPars_II.parse().get(1));
    // patternList_II.add(objPars_II.parse().get(4));

    // try {
    // structure_II.placeCard(null, initialCardTest_II, null, true);
    // structure_II.placeCard(initialCardTest_II, RedTest1_II, "BL", true);
    // structure_II.placeCard(initialCardTest_II, BluTest1_II, "BR", true);
    // structure_II.placeCard(BluTest1_II, BluTest2_II, "BR", true);
    // structure_II.placeCard(BluTest2_II, BluTest3_II, "BL", true);
    // structure_II.placeCard(BluTest3_II, GreenTest1_II, "BL", true);
    // structure_II.placeCard(GreenTest1_II, RedTest2_II, "TL", true);
    // structure_II.placeCard(GreenTest1_II, PurpleTest1_II, "BR", true);
    // structure_II.placeCard(PurpleTest1_II, GreenTest2_II, "BR", true);
    // structure_II.placeCard(GreenTest2_II, GreenTest3_II, "BR", true);
    // structure_II.placeCard(GreenTest3_II, GreenTest4_II, "BR", true);

    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // result = 0;
    // try {
    // result = structure_II.getPointsFromPatterns(patternList_II, GreenTest4_II)
    // + structure_II.getPointsFromPatterns(patternList_II, RedTest2_II);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // assertEquals(5, result);

    // //////////////////////////////////

    // ResourceCard GreenTest5_II = parser_II.parse().get(17); // R18
    // try {
    // structure_II.placeCard(GreenTest4_II, GreenTest5_II, "BR", true);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // try {
    // result = structure_II.getPointsFromPatterns(patternList, GreenTest5_II);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // assertEquals(0, result);

    // //////////////////////////////////

    // ObjectiveCard shroomIdol = objPars_II.parse().get(8);
    // ObjectiveCard vegIdol = objPars_II.parse().get(9);
    // ObjectiveCard wolfIdol = objPars_II.parse().get(10);
    // ObjectiveCard mosquitoIdol = objPars_II.parse().get(11);

    // int pointsmade = 0;
    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(shroomIdol);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(4, pointsmade);

    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(vegIdol);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(2, pointsmade);

    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(wolfIdol);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(2, pointsmade);

    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(mosquitoIdol);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(2, pointsmade);

    // //////////////////////////////////
    // ObjectiveCard foldedhandsWiseman = objPars_II.parse().get(12);
    // ObjectiveCard scrollWiseman = objPars_II.parse().get(13);
    // ObjectiveCard inkWiseman = objPars_II.parse().get(14);
    // ObjectiveCard featherWiseman = objPars_II.parse().get(15);

    // ResourceCard GreenTest6_II = parser_II.parse().get(14); // R15
    // ResourceCard PurpleTest2_II = parser_II.parse().get(36); // R37

    // try {
    // structure_II.placeCard(GreenTest5_II, GreenTest6_II, "BL", true);
    // structure_II.placeCard(GreenTest6_II, PurpleTest2_II, "BR", true);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // pointsmade = 0;
    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(foldedhandsWiseman);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(3, pointsmade);

    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(scrollWiseman);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(0, pointsmade);

    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(inkWiseman);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(0, pointsmade);

    // try {
    // pointsmade = structure_II.getPointsFromObjResCard(featherWiseman);
    // } catch (IllegalCommandException e) {
    // System.out.println(e.getMessage());
    // }
    // assertEquals(2, pointsmade);

    // // [x] chair pattern check
    // // [x] mixed chair and stair pattern which fails due to share of a card among
    // // two patterns
    // // [x] stair pattern check
    // // [x] mixed chair and stair pattern which succeeds
    // // [x] multistair pattern
    // // [x] resource requirement check
    // // [x] object requirement check
    // // [x] foldedhandWiseman requirement check
    // }

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
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // test inital card (back)

        try {
            secondStructure.placeCard(null, initialCardTest, null, false);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, secondStructure.getVisibleObjects());

        // place the first resourcecard on structure (front), then the second (front)
        // and test

        try {
            structure.placeCard(initialCardTest, firstResCardTest, "BL", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            structure.placeCard(firstResCardTest, secondResCardTest, "BL", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // place the first resourcecard on secondStructure(back), then the second (back)
        // and test

        try {
            secondStructure.placeCard(initialCardTest, firstResCardTest, "BL", false);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            secondStructure.placeCard(firstResCardTest, secondResCardTest, "BL", false);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, secondStructure.getVisibleObjects());

        // place the first goldcard on Structure(front) and test

        try {
            structure.placeCard(initialCardTest, firstGoldCardTest, "BR", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 1\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // more objects available
        try {
            structure.placeCard(firstGoldCardTest, forthResCardTest, "BR", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 2\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // coverage case
        try {
            structure.placeCard(firstGoldCardTest, thirdResCardTest, "TR", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "FEATHER: 0\nINK: 1\nSCROLL: 0";
        assertEquals(compareWith, structure.getVisibleObjects());

        // place the first goldcard on Structure(back) and test
        try {
            secondStructure.placeCard(secondResCardTest, firstGoldCardTest, "BR", false);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        compareWith = "FEATHER: 0\nINK: 0\nSCROLL: 0";
        assertEquals(compareWith, secondStructure.getVisibleObjects());

        // [x] Initial card front when no object available
        // [x] Initial card back when no object available
        // [x] Various resource cards front when no object available
        // [x] Various resource cards back when no object available
        // [x] Various resource cards front + goldcard front when ONE object available
        // [x] Various resource cards front + goldcard front when TWO objects available
        // [x] Various resource cards front + goldcard front when ONE object available
        // is covered by a non-object card
        // [x] Various resource cards back + goldcard back when no object available

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
            fail(e.getMessage());
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 2\nSHROOM: 0";
        assertEquals(compareWith, structure.getVisibleResources());

        // test inital card (back)

        try {
            secondStructure.placeCard(null, initialCardTest, null, false);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 1\nSHROOM: 1";
        assertEquals(compareWith, secondStructure.getVisibleResources());

        // place the first resourcecard on structure (front), then the second (front)
        // and test

        try {
            structure.placeCard(initialCardTest, firstResCardTest, "BL", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            structure.placeCard(firstResCardTest, secondResCardTest, "BL", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 1\nSHROOM: 3";
        assertEquals(compareWith, structure.getVisibleResources());

        // place the first resourcecard on secondStructure(back), then the second (back)
        // and test

        try {
            secondStructure.placeCard(initialCardTest, firstResCardTest, "BL", false);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            secondStructure.placeCard(firstResCardTest, secondResCardTest, "BL", false);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 0\nSHROOM: 3";
        assertEquals(compareWith, secondStructure.getVisibleResources());

        // place the first goldcard on Structure(front) and test

        try {
            structure.placeCard(secondResCardTest, firstGoldCardTest, "BR", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        compareWith = "VEGETABLE: 1\nANIMAL: 0\nINSECT: 1\nSHROOM: 3";
        assertEquals(compareWith, structure.getVisibleResources());

        // place the first goldcard on Structure(back) and test
        try {
            secondStructure.placeCard(secondResCardTest, firstGoldCardTest, "BR", false);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        compareWith = "VEGETABLE: 1\nANIMAL: 1\nINSECT: 0\nSHROOM: 4";
        assertEquals(compareWith, secondStructure.getVisibleResources());

        // [x] Initial card front
        // [x] Initial card back
        // [x] Various resource cards front
        // [x] Various resource cards back
        // [x] Various resource cards front + goldcard front
        // [x] Various resource cards back + goldcard back
    }

    @Test
    void testPlaceCard() {
        Structure structure = null;
        InitialCard initialCard = new InitialParser().parse().get(5);
        Stack<GoldCard> goldDeck = new GoldParser().parse();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        List<Card> placedCards = new ArrayList<>();
        Card testCard;

        // test that that gold card is placed correctly when meeting requirements
        testCard = goldDeck.get(0);
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, resourceDeck.get(0), "TR", true);
            structure.placeCard(resourceDeck.get(0), testCard, "TL", true);
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertTrue(placedCards.contains(testCard));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies that when gold card does not have to meet requirements
        // when placed with !frontUp
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, testCard, "TL", false);
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertTrue(placedCards.contains(testCard));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies that gold card is not placed when not meeting requirements
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, testCard, "TL", true);
        } catch (Exception e) {
            assertEquals("Gold card requirements not met", e.getMessage());
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertFalse(placedCards.contains(testCard));
        }

        // test that verifies that card cannot be placed on TR null
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, testCard, "BL", true);
        } catch (Exception e) {
            assertEquals("Card cannot be placed on null corner", e.getMessage());
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertFalse(placedCards.contains(testCard));
        }

        // test that verifies that bottom card cannot be null
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(null, testCard, "TR", true);
        } catch (Exception e) {
            assertEquals("Bottom card cannot be null", e.getMessage());
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertFalse(placedCards.contains(testCard));
        }

        // test that verifies that card cannot be placed if spot is already taken
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, resourceDeck.get(0), "TR", true);
            structure.placeCard(initialCard, testCard, "TR", true);
        } catch (Exception e) {
            assertEquals("Another card is already placed in that position", e.getMessage());
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertFalse(placedCards.contains(testCard));
        }

        // test that verifies that card cannot be placed twice
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, resourceDeck.get(0), "TR", true);
            structure.placeCard(initialCard, resourceDeck.get(0), "TL", true);
        } catch (Exception e) {
            assertEquals("Card is already placed", e.getMessage());
            int i = 0;
            for (Pair<Card, Boolean> card : structure.getPlacedCards()) {
                if (card.getKey().equals(resourceDeck.get(0)))
                    i++;
            }
            assertEquals(1, i);
        }

        // test that verifies that bottom card is present
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(resourceDeck.get(1), resourceDeck.get(0), "TR", true);
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertTrue(placedCards.contains(testCard));
        } catch (Exception e) {
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertEquals("Bottom card is not placed", e.getMessage());
            assertFalse(placedCards.contains(resourceDeck.get(0)));
            assertFalse(placedCards.contains(resourceDeck.get(1)));
        }

        // test that verifies that a card cannot be placed indirectly on null
        try {
            structure = new Structure();
            structure.placeCard(null, initialCard, null, true);
            structure.placeCard(initialCard, resourceDeck.get(2), "TL", true);
            structure.placeCard(initialCard, resourceDeck.get(0), "TR", true);
            structure.placeCard(resourceDeck.get(0), testCard, "TR", false);
        } catch (Exception e) {
            assertEquals("Bottom card is not placed", e.getMessage());
            placedCards = structure.getPlacedCards().stream().map(Pair::getKey).collect(Collectors.toList());
            assertFalse(placedCards.contains(testCard));
        }

        // [x] Gold card meets requirements
        // [x] Gold card does not meet requirements
        // [x] Any card on top null
        // [x] Any card when father is null
        // [x] Any card when place is already taken
        // [x] Any card that is already twice
        // [x] Any card when father is not placed
        // [x] Any card placed not placed indirectly on null
    }

    @Test
    void testgetRadiusAndPrintReducedMatrix() throws IllegalCommandException {

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().get(0);
        ResourceCard RedTest1 = parser.parse().get(0); // R01
        ResourceCard RedTest2 = parser.parse().get(1); // R02
        ResourceCard BluTest1 = parser.parse().get(22); // R23
        ResourceCard BluTest2 = parser.parse().get(26); // R27
        ResourceCard BluTest3 = parser.parse().get(21); // R22
        ResourceCard GreenTest1 = parser.parse().get(18); // R19

        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, RedTest1, "BL", true);
            structure.placeCard(initialCardTest, BluTest1, "BR", true);
            structure.placeCard(BluTest1, BluTest2, "BR", true);
            structure.placeCard(BluTest2, BluTest3, "BL", true);
            structure.placeCard(BluTest3, GreenTest1, "BL", true);
            structure.placeCard(GreenTest1, RedTest2, "TL", true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        structure.printReducedMatrix(structure.getCardMatrix(), structure.getRadius(structure.getCoordinateToCard()));
        assertEquals(4, structure.getRadius(structure.getCoordinateToCard()));
    }
}
