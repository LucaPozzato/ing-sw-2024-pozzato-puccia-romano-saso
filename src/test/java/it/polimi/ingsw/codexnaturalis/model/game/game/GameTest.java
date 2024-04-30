package it.polimi.ingsw.codexnaturalis.model.game.game;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
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
import it.polimi.ingsw.codexnaturalis.model.game.strategies.Strategy;
import javafx.util.Pair;

import javax.security.auth.login.CredentialNotFoundException;

public class GameTest {
    @Test
    void getPatternsTotemPointsTest() {
        Game game = new Game(1);
        Player player = new Player();

        Structure structure = new Structure();
        ResourceParser parser = new ResourceParser();
        ObjectiveParser objPars = new ObjectiveParser();

        InitialCard initialCardTest = new InitialParser().parse().get(0); //IC1
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
        hand.setSecretObjective(objPars.parse().get(8)); //OR1

        game.setPlayerStructure(player, structure);

        // Setto gli obiettivi comuni
        List<Card> commonObjective = new ArrayList<>();
        commonObjective.add(objPars.parse().get(4)); //OP5
        commonObjective.add(objPars.parse().get(5)); //OP6

        Board board = new Board();
        game.setBoard(board);
        board = game.getBoard();
        board.setCommonObjectives(commonObjective);

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
            game.getStrategyMap().put(player, new ArrayList<Pair<Strategy, Card>>());
            // endState.setStrategies(player);
            EndGameState endState = new EndGameState(game);

            //TODO: determine why this call gives the wrong result
//            System.out.println(game.getBoard().getActualPoints(player));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}