package it.polimi.ingsw.codexnaturalis.model.game.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.enumerations.ObjectiveCardType;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;

public class ObjectiveParserTest {
    @Test
    void testParse() {
        List<ObjectiveCard> objDeck = new ObjectiveParser().parse();
        assertTrue(objDeck.size() == 16);
        int i = 1;
        for (ObjectiveCard card : objDeck) {
            if (i <= 8)
                assertTrue(card.getIdCard().equals("OP" + i));
            else
                assertTrue(card.getIdCard().equals("OR" + (i - 8)));
            assertTrue(card.getPoints() == 2 || card.getPoints() == 3);
            assertTrue(card.getShape() != null && card.getShape() != "" && !card.getShape().contains("\"")
                    && ObjectiveCardType.valueOf(card.getShape()) != null);
            assertTrue(card.getMustHave() != null && card.getMustHave() != "" && !card.getMustHave().contains("\"")
                    && (card.getMustHave().contains("SHROOM") || card.getMustHave().contains("INSECT")
                            || card.getMustHave().contains("VEGETABLE") || card.getMustHave().contains("ANIMAL")
                            || card.getMustHave().contains("FOLDEDHANDS") || card.getMustHave().contains("INK")
                            || card.getMustHave().contains("SCROLL") || card.getMustHave().contains("FEATHER")));
            assertTrue(card instanceof ObjectiveCard);
            i++;
        }
    }
}
