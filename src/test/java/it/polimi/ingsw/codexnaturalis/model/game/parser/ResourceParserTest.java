package it.polimi.ingsw.codexnaturalis.model.game.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Stack;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.enumerations.CornerType;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;

public class ResourceParserTest {
    @Test
    void testParse() {
        Stack<ResourceCard> deck = new ResourceParser().parse();
        assertTrue(deck.size() == 40);
        int i = 1;
        for (ResourceCard card : deck) {
            assertTrue(card.getIdCard().equals("R" + i));
            assertTrue(card.getSymbol() != null && card.getSymbol() != "" && !card.getSymbol().contains("\"")
                    && Resource.valueOf(card.getSymbol()) != null);
            assertTrue(card.getPoints() >= 0);
            assertTrue(card.getCorners() != null);
            for (String corner : card.getCorners()) {
                assertTrue(
                        corner != null && corner != "" && !corner.contains("\"") & CornerType.valueOf(corner) != null);
            }
            assertTrue(card instanceof ResourceCard);
            i++;
        }
    }
}
