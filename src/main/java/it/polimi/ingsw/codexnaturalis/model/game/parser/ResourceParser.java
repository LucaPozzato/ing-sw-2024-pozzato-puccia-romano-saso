package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;

public class ResourceParser {
    private Stack<ResourceCard> ResourceDeck;

    public ResourceParser() {
        ResourceDeck = new Stack<>();
    }

    public Stack<ResourceCard> parse() {
        File input = new File("src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/resourceDeck.json");
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
            String symbol = cardsObject.get("symbol").getAsString();
            int points = cardsObject.get("points").getAsInt();
            JsonArray corners = cardsObject.get("corners").getAsJsonArray();
            List<String> array = new ArrayList<>();
            for (JsonElement corner : corners) {
                array.add(corner.getAsString());
            }
            ResourceCard resCard = new ResourceCard(idCard, symbol, points, array);
            ResourceDeck.push(resCard);
        }
        return ResourceDeck;
    }
}