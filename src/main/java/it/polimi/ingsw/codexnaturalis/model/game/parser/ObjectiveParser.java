package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;

public class ObjectiveParser {
    private List<ObjectiveCard> collection;

    public ObjectiveParser() {
        collection = new ArrayList<>();
    }

    /**
     * When this method is called the objectivePatternDeck and the
     * objectiveResourceDeck are generated extracting the information needed from
     * the JSON file.
     * Note that it extracts :
     * #) the card's id at "id"
     * #) the points made when the objective is satisfied at "points" <br>
     * #) the information about the type of pattern (chai/stair) or about the type
     * of resource (idol/wiseman) at "shape"<br>
     * #) the symbol of the list of symbols which compose the pattern in case of
     * patternObj or the symbol to look for in case of resourceObj at "mustHave"<br>
     * #) 0 if it's a stair pattern or if the card colored differently in the chair
     * pattern is to link to the upper card of the chair, 2 if it's to link to the
     * lower one at "divideBy_seatColor"<br>
     * #) the list of int which represent the whole cells of a 3x3 matrix in case of
     * strair pattern,
     * the list of int which represent the whole cells of a 2x4 matrix in case of
     * chair pattern,
     * a null value in case of resourceObj at wholeCells""<br>
     *
     * @return a unique list of objective card, firstly the patternObjective and
     * then the resureObjective
     */
    public List<ObjectiveCard> parse() {
        InputStreamReader input = new InputStreamReader(
                getClass().getResourceAsStream("/it/polimi/ingsw/codexnaturalis/JSON/objectivePatternDeck.json"));
        JsonElement fileElement = null;
        try {
            fileElement = JsonParser.parseReader(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JsonArray fileArray = fileElement.getAsJsonArray();
        for (JsonElement cards : fileArray) {
            JsonObject cardsObject = cards.getAsJsonObject();
            String idCard = cardsObject.get("id").getAsString();
            int points = cardsObject.get("points").getAsInt();
            String shape = cardsObject.get("shape").getAsString();
            String mustHave = cardsObject.get("mustHave").getAsString();
            Integer divideBy_seatColor = cardsObject.get("divideBy_seatColor").getAsInt();
            String numString = cardsObject.get("wholeCells").getAsString();
            String[] splittedString = numString.split(",");
            int[] wholeCells = new int[splittedString.length];
            for (int i = 0; i < splittedString.length; i++) {
                wholeCells[i] = Integer.parseInt(splittedString[i]);
            }

            ObjectiveCard objCard = new ObjectiveCard(idCard, points, shape, mustHave, divideBy_seatColor, wholeCells);
            collection.add(objCard);
        }

        InputStreamReader input2 = new InputStreamReader(
                getClass().getResourceAsStream("/it/polimi/ingsw/codexnaturalis/JSON/objectiveResourceDeck.json"));
        JsonElement fileElement2 = null;
        try {
            fileElement2 = JsonParser.parseReader(input2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JsonArray fileArray2 = fileElement2.getAsJsonArray();
        for (JsonElement cards : fileArray2) {
            JsonObject cardsObject = cards.getAsJsonObject();
            String idCard = cardsObject.get("id").getAsString();
            int points = cardsObject.get("points").getAsInt();
            String shape = cardsObject.get("shape").getAsString();
            String mustHave = cardsObject.get("mustHave").getAsString();
            Integer divideBy_seatColor = cardsObject.get("divideBy_seatColor").getAsInt();
            int[] wholeCells = null;

            ObjectiveCard objCard = new ObjectiveCard(idCard, points, shape, mustHave, divideBy_seatColor, wholeCells);
            collection.add(objCard);
        }

        return collection;
    }
}
