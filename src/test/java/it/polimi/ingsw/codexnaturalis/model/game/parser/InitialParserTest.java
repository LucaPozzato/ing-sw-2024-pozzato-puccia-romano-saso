package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;

public class InitialParserTest {
    @Test
    void testParse() {
        List<InitialCard> initialDeck = new InitialParser().parse();
        assert initialDeck.size() == 6;
        int i = 1;
        for (InitialCard card : initialDeck) {
            assert card.getIdCard().equals("I" + i);
            assert card.getFrontCornerRes() != null;
            for (String corner : card.getFrontCornerRes()) {
                assert corner != null && corner != "" && !corner.contains("\"") && Resource.valueOf(corner) != null;
            }
            assert card.getFrontCentreRes() != null;
            for (String centre : card.getFrontCentreRes()) {
                assert centre != null && centre != "" && !centre.contains("\"") && Resource.valueOf(centre) != null;
            }
            assert card.getBackCornerRes() != null;
            for (String corner : card.getBackCornerRes()) {
                assert corner != null && corner != "" && !corner.contains("\"") && Resource.valueOf(corner) != null;
            }
            assert card instanceof InitialCard;
            i++;
        }
    }
}
