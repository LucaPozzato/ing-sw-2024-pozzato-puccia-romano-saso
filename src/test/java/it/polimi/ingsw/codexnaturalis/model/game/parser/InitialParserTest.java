package it.polimi.ingsw.codexnaturalis.model.game.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;

public class InitialParserTest {
    @Test
    void testParse() {
        List<InitialCard> initialDeck = new InitialParser().parse();
        assertTrue(initialDeck.size() == 6);
        int i = 1;
        for (InitialCard card : initialDeck) {
            assertTrue(card.getIdCard().equals("I0" + i));
            assertTrue(card.getFrontCorners() != null);
            for (String corner : card.getFrontCorners()) {
                assertTrue(
                        corner != null && corner != "" && !corner.contains("\"") && Resource.valueOf(corner) != null);
            }
            assertTrue(card.getFrontCenterResources() != null);
            for (String centre : card.getFrontCenterResources()) {
                assertTrue(
                        centre != null && centre != "" && !centre.contains("\"") && Resource.valueOf(centre) != null);
            }
            assertTrue(card.getBackCorners() != null);
            for (String corner : card.getBackCorners()) {
                assertTrue(
                        corner != null && corner != "" && !corner.contains("\"") && Resource.valueOf(corner) != null);
            }
            assertTrue(card instanceof InitialCard);
            i++;
        }
    }
}
