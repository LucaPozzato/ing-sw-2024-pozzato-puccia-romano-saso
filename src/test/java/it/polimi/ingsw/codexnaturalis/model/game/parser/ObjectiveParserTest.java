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
                    && (card.getMustHave().equals("RED") || card.getMustHave().equals("PURPLE")
                            || card.getMustHave().equals("GREEN") || card.getMustHave().equals("BLUE")
                            || card.getMustHave().equals("FOLDEDHANDS") || card.getMustHave().equals("ANIMALS")
                            || card.getMustHave().equals("SHROOM") || card.getMustHave().equals("INSECTS")
                            || card.getMustHave().equals("VEGETABLES") || card.getMustHave().equals("INK")
                            || card.getMustHave().equals("SCROLL") || card.getMustHave().equals("FEATHER")));
            assertTrue(card instanceof ObjectiveCard);
            i++;
        }
    }
}
