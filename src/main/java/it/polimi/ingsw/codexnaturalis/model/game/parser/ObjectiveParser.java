package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public List<ObjectiveCard> parse() {
        File input = new File("src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/objectivePatternDeck.json");
        JsonElement fileElement = null;
        try {
            fileElement = JsonParser.parseReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonArray fileArray = fileElement.getAsJsonArray();
        for (JsonElement cards : fileArray) {
            JsonObject cardsObject = cards.getAsJsonObject();
            String idCard = cardsObject.get("id").getAsString();
            int points = cardsObject.get("points").getAsInt();
            String shape = cardsObject.get("shape").getAsString();
            String mustHave = cardsObject.get("mustHave").getAsString();
            Integer divideBy = cardsObject.get("divideBy").getAsInt();
            String numStringCheck = cardsObject.get("whole3x3").getAsString();
            Integer[] whole3x3 = new Integer[numStringCheck.length()];

            if (numStringCheck.equals("null")) {
                whole3x3 = null;
            } else {
                String[] numString = numStringCheck.split(",");
                for (int i = 0; i < 3; i++) {
                    whole3x3[i] = Integer.parseInt(numString[i]);
                }
            }

            ObjectiveCard objCard = new ObjectiveCard(idCard, points, shape, mustHave, divideBy, whole3x3);
            collection.add(objCard);
        }

        File input2 = new File(
                "src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/objectiveResourceDeck.json");
        JsonElement fileElement2 = null;
        try {
            fileElement2 = JsonParser.parseReader(new FileReader(input2));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonArray fileArray2 = fileElement2.getAsJsonArray();
        for (JsonElement cards : fileArray2) {
            JsonObject cardsObject = cards.getAsJsonObject();
            String idCard = cardsObject.get("id").getAsString();
            int points = cardsObject.get("points").getAsInt();
            String shape = cardsObject.get("shape").getAsString();
            String mustHave = cardsObject.get("mustHave").getAsString();
            Integer divideBy = cardsObject.get("divideBy").getAsInt();
            String numStringCheck = cardsObject.get("whole3x3").getAsString();
            Integer[] whole3x3 = new Integer[numStringCheck.length()];

            if (numStringCheck.equals("null")) {
                whole3x3 = null;
            } else {
                String[] numString = numStringCheck.split(",");
                for (int i = 0; i < 3; i++) {
                    whole3x3[i] = Integer.parseInt(numString[i]);
                }
            }

            ObjectiveCard objCard = new ObjectiveCard(idCard, points, shape, mustHave, divideBy, whole3x3);
            collection.add(objCard);
        }

        return collection;
    }
}
