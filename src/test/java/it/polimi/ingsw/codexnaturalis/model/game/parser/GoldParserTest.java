package it.polimi.ingsw.codexnaturalis.model.game.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Stack;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.enumerations.CornerType;
import it.polimi.ingsw.codexnaturalis.model.enumerations.GoldCardPointType;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;

public class GoldParserTest {
    @Test
    void testParse() {
        Stack<GoldCard> deck = new GoldParser().parse();
        assertTrue(deck.size() == 40);
        int i = 1;
        for (GoldCard card : deck) {
            assertTrue(card.getIdCard().equals("G" + i));
            assertTrue(card.getSymbol() != null && card.getSymbol() != "" && !card.getSymbol().contains("\"")
                    && Resource.valueOf(card.getSymbol()) != null);
            assertTrue(card.getPoints() >= 0);
            assertTrue(
                    card.getPointsType() != null && card.getPointsType() != "" && !card.getPointsType().contains("\"")
                            && GoldCardPointType.valueOf(card.getPointsType()) != null);
            assertTrue(card.getFrontCorners() != null);
            for (String corner : card.getFrontCorners()) {
                assertTrue(
                        corner != null && corner != "" && !corner.contains("\"") && CornerType.valueOf(corner) != null);
            }
            assertTrue(card.getRequirements() != null);
            for (String requirement : card.getRequirements().keySet()) {
                assertTrue(requirement != null && requirement != "" && !requirement.contains("\"")
                        && Resource.valueOf(requirement) != null && card.getRequirements().get(requirement) >= 0);
            }
            assertTrue(card instanceof GoldCard);
            i++;
        }
    }
}
