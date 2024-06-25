package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.enumerations.ObjectiveCardType;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectiveParserTest {
    @Test
    void testParse() {
        ObjectiveParser parser = new ObjectiveParser();
        List<ObjectiveCard> objDeck = parser.parse();
        Card cards = objDeck.getFirst();
        System.out.println(cards);
        assertTrue(objDeck.size() == 16);
        assertThrows(IllegalCommandException.class, cards::getPointsType);
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
