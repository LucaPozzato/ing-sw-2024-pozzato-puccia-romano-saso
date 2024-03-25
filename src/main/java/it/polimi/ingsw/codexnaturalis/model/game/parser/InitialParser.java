package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;

public class InitialParser {

    public InitialParser() {
    }

    public List<InitialCard> Parse() {
        List<InitialCard> collection = new ArrayList<>();

        File input = new File("src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/initialDeck.json");
        JsonElement fileElement = null;
        try {
            fileElement = JsonParser.parseReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonArray fileArray = fileElement.getAsJsonArray();
        for (JsonElement cards : fileArray) {
            JsonObject cardsObject = cards.getAsJsonObject();

            String id = cardsObject.get("id").getAsString();

            List<String> frontCornRes = new ArrayList<>();
            JsonArray array = cardsObject.get("front corners").getAsJsonArray();
            for (JsonElement corner : array) {
                frontCornRes.add(corner.toString());
            }

            List<String> frontCentRes = new ArrayList<>();
            JsonArray array2 = cardsObject.get("front centre").getAsJsonArray();
            for (JsonElement centre : array2) {
                frontCentRes.add(centre.toString());
            }

            List<String> backCornRes = new ArrayList<>();
            JsonArray array3 = cardsObject.get("back corners").getAsJsonArray();
            for (JsonElement centre : array3) {
                backCornRes.add(centre.toString());
            }

            InitialCard initCard = new InitialCard(id, frontCornRes, frontCentRes, backCornRes);
            collection.add(initCard);
            Collections.shuffle(collection);
        }
        // Stampa
        // System.out.println(collection);
        return collection;
    }
}