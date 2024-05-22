package it.polimi.ingsw.codexnaturalis.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.codexnaturalis.controller.EndGameState;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public class GameTest {
    @Test
    void ChairPlusResCheck() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard RedTest1 = parser.parse().get(0); // R01
        ResourceCard RedTest2 = parser.parse().get(1); // R02
        ResourceCard BluTest1 = parser.parse().get(22); // R23
        ResourceCard BluTest2 = parser.parse().get(26); // R27
        ResourceCard BluTest3 = parser.parse().get(21); // R22
        ResourceCard GreenTest1 = parser.parse().get(18); // R19

        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(8)); // OR1

        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); // OP5
        commonObjective.add(objPars.parse().get(5)); // OP6

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, RedTest1, "BL", true);
            structure.placeCard(initialCardTest, BluTest1, "BR", true);
            structure.placeCard(BluTest1, BluTest2, "BR", true);
            structure.placeCard(BluTest2, BluTest3, "BL", true);
            structure.placeCard(BluTest3, GreenTest1, "BL", true);
            structure.placeCard(GreenTest1, RedTest2, "TL", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(5, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void StairCheck() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard GreenTest1 = parser.parse().get(12); //R11
        ResourceCard GreenTest2 = parser.parse().get(11); // R02
        ResourceCard GreenTest3 = parser.parse().get(10); // R19
        ResourceCard RedTest1 = parser.parse().get(0); // R01

        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(8)); // OR1

        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(1)); // OP2
        commonObjective.add(objPars.parse().get(2)); // OP3

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, GreenTest1, "BR", true);
            structure.placeCard(GreenTest1, GreenTest2, "BR", true);
            structure.placeCard(GreenTest2, GreenTest3, "BR", true);
            structure.placeCard(GreenTest3, RedTest1, "BL", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(2, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void MultipleStairCheckFail() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard GreenTest1 = parser.parse().get(12); // R13
        ResourceCard GreenTest2 = parser.parse().get(15); //R16
        ResourceCard GreenTest3 = parser.parse().get(16); // R17
        ResourceCard GreenTest4 = parser.parse().get(18); // R19
        ResourceCard GreenTest5 = parser.parse().get(10); //R11


        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(8)); // OR1

        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(1)); // OP2
        commonObjective.add(objPars.parse().get(2)); // OP3

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, GreenTest1, "BR", true);
            structure.placeCard(GreenTest1, GreenTest2, "BR", true);
            structure.placeCard(GreenTest2, GreenTest3, "BR", true);
            structure.placeCard(GreenTest3, GreenTest4, "BR", true);
            structure.placeCard(GreenTest4, GreenTest5, "BR", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(2, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void MixedStairCheckSuccess() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard RedTest1 = parser.parse().get(0); // R01
        ResourceCard RedTest2 = parser.parse().get(1); // R02
        ResourceCard BluTest1 = parser.parse().get(22); // R23
        ResourceCard BluTest2 = parser.parse().get(26); // R27
        ResourceCard BluTest3 = parser.parse().get(21); // R22
        ResourceCard GreenTest1 = parser.parse().get(18); // R19
        ResourceCard GreenTest2 = parser.parse().get(16); // R17
        ResourceCard GreenTest3 = parser.parse().get(17); // R18

        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(8)); // OR1

        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); // OP5
        commonObjective.add(objPars.parse().get(1)); // OP2

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, RedTest1, "BL", true);
            structure.placeCard(initialCardTest, BluTest1, "BR", true);
            structure.placeCard(BluTest1, BluTest2, "BR", true);
            structure.placeCard(BluTest2, BluTest3, "BL", true);
            structure.placeCard(BluTest3, GreenTest1, "BL", true);
            structure.placeCard(GreenTest1, RedTest2, "TL", true);
            structure.placeCard(GreenTest1, GreenTest2, "BR", true);
            structure.placeCard(GreenTest2, GreenTest3, "BR", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(7, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void MixedStairCheckFailure() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard RedTest1 = parser.parse().get(0); // R01
        ResourceCard RedTest2 = parser.parse().get(1); // R02
        ResourceCard BluTest1 = parser.parse().get(22); // R23
        ResourceCard BluTest2 = parser.parse().get(26); // R27
        ResourceCard BluTest3 = parser.parse().get(21); // R22
        ResourceCard GreenTest1 = parser.parse().get(18); // R19
        ResourceCard RedTest3 = parser.parse().get(2); // R03
        ResourceCard GreenTest2 = parser.parse().get(17); // R18
        ResourceCard PurpleTest1 = parser.parse().get(31); // R32


        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(1)); // OR1

        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); // OP5
        commonObjective.add(objPars.parse().get(5)); // OP6

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, RedTest1, "BL", true);
            structure.placeCard(initialCardTest, BluTest1, "BR", true);
            structure.placeCard(BluTest1, BluTest2, "BR", true);
            structure.placeCard(BluTest2, BluTest3, "BL", true);
            structure.placeCard(BluTest3, GreenTest1, "BL", true);
            structure.placeCard(GreenTest1, RedTest2, "TL", true);
            structure.placeCard(GreenTest1, RedTest3, "BR", true);
            structure.placeCard(RedTest3, GreenTest2, "BL", true);
            structure.placeCard(GreenTest2, PurpleTest1, "BL", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(3, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void ResourceCheck() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard PurpleTest1 = parser.parse().get(33); // R34
        ResourceCard PurpleTest2 = parser.parse().get(30); // R31


        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(11)); // OR
        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); // OP5
        commonObjective.add(objPars.parse().get(5)); // OP6

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, PurpleTest1, "BR", true);
            structure.placeCard(PurpleTest1, PurpleTest2, "BR", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(2, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void ObjCheck() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard RedTest1 = parser.parse().get(6); // R07
        ResourceCard GreenTest1 = parser.parse().get(16); // R17


        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(13)); // OR
        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); // OP5
        commonObjective.add(objPars.parse().get(5)); // OP6

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, RedTest1, "BR", true);
            structure.placeCard(RedTest1, GreenTest1, "BR", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(2, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void FoldedHandsCheck() {
        Game game = new Game(1, null, null);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().getFirst(); // IC1
        ResourceCard RedTest1 = parser.parse().get(6); // R07
        ResourceCard GreenTest1 = parser.parse().get(16); // R17
        ResourceCard GreenTest2 = parser.parse().get(15); // R16
        ResourceCard GreenTest3 = parser.parse().get(14); // R15


        // Setto l'obiettivo segreto
        Hand hand = new Hand();
        game.addPlayer(player);
        game.setPlayerHand(player, hand);
        hand = game.getHandByPlayer(player);
        hand.setSecretObjective(objPars.parse().get(12)); // OR
        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); // OP5
        commonObjective.add(objPars.parse().get(5)); // OP6

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);
        board.getActualScores().put(player, 0);

        // Piazzo le carte sulla board
        try {
            structure.placeCard(null, initialCardTest, null, true);
            structure.placeCard(initialCardTest, RedTest1, "BR", true);
            structure.placeCard(RedTest1, GreenTest1, "BR", true);
            structure.placeCard(GreenTest1, GreenTest2, "BR", true);
            structure.placeCard(GreenTest1, GreenTest3, "BL", true);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            structure.printReducedMatrix(structure.getCardMatrix(), structure.getRadius(structure.getCoordinateToCard()));
            EndGameState endState = new EndGameState(game, new RmiServer(), new SocketServer());
            assertEquals(3, game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // [x] chair + res pattern check
    // [x] stair pattern check
    // [x] consecutive multistair pattern which fails due to share of a card among
    // [x] mixed chair and stair pattern which succeeds
    // [x] consecutive multistair pattern which fails due to share of a card among
    // [x] resource requirement check
    // [x] object requirement check
    // [x] foldedhandWiseman requirement check


    // [] Test the declare winner in strange situations

}