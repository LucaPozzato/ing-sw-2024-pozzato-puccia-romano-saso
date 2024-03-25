package it.polimi.ingsw.codexnaturalis.model.game.parser;

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
        assert deck.size() == 40;
        int i = 1;
        for (GoldCard card : deck) {
            assert card.getIdCard().equals("G" + i);
            assert card.getSymbol() != null && card.getSymbol() != "" && !card.getSymbol().contains("\"")
                    && Resource.valueOf(card.getSymbol()) != null;
            assert card.getPoints() >= 0;
            assert card.getPointsType() != null && card.getPointsType() != "" && !card.getPointsType().contains("\"")
                    && GoldCardPointType.valueOf(card.getPointsType()) != null;
            assert card.getCorners() != null;
            for (String corner : card.getCorners()) {
                assert corner != null && corner != "" && !corner.contains("\"") && CornerType.valueOf(corner) != null;
            }
            assert card.getRequirements() != null;
            for (String requirement : card.getRequirements()) {
                assert requirement != null && requirement != "" && !requirement.contains("\"")
                        && Resource.valueOf(requirement) != null;
            }
            assert card instanceof GoldCard;
            i++;
        }
    }
}
