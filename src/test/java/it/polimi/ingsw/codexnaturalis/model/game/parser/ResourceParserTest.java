package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.util.Stack;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.enumerations.CornerType;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;

public class ResourceParserTest {
    @Test
    void testParse() {
        Stack<ResourceCard> deck = new ResourceParser().parse();
        assert deck.size() == 40;
        int i = 1;
        for (ResourceCard card : deck) {
            assert card.getIdCard().equals("R" + i);
            assert card.getSymbol() != null && card.getSymbol() != "" && !card.getSymbol().contains("\"")
                    && Resource.valueOf(card.getSymbol()) != null;
            assert card.getPoints() >= 0;
            assert card.getCorners() != null;
            for (String corner : card.getCorners()) {
                assert corner != null && corner != "" && !corner.contains("\"") & CornerType.valueOf(corner) != null;
            }
            assert card instanceof ResourceCard;
            i++;
        }
    }
}
