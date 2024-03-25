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
            assertTrue(card.getIdCard().equals("I" + i));
            assertTrue(card.getFrontCornerRes() != null);
            for (String corner : card.getFrontCornerRes()) {
                assertTrue(
                        corner != null && corner != "" && !corner.contains("\"") && Resource.valueOf(corner) != null);
            }
            assertTrue(card.getFrontCentreRes() != null);
            for (String centre : card.getFrontCentreRes()) {
                assertTrue(
                        centre != null && centre != "" && !centre.contains("\"") && Resource.valueOf(centre) != null);
            }
            assertTrue(card.getBackCornerRes() != null);
            for (String corner : card.getBackCornerRes()) {
                assertTrue(
                        corner != null && corner != "" && !corner.contains("\"") && Resource.valueOf(corner) != null);
            }
            assertTrue(card instanceof InitialCard);
            i++;
        }
    }
}
