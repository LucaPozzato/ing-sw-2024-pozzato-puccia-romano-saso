package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.InputStreamReader;
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

    /**
     * When this method is called resourceDeck is generated extracting the
     * information needed from the JSON file.
     * Note that it extracts :
     * #) the card's id at "id"
     * #) the information about the type of symbol linked to the card at
     * "symbol"<br>
     * #) the points made when the objective is satisfied at "points" <br>
     * #) an array of 4 strings which represent the occupation of every corner of
     * the card starting from the TL and going clockwise
     * 
     * @return a unique stack of resource card
     */
    public Stack<ResourceCard> parse() {
        InputStreamReader input = new InputStreamReader(
                getClass().getResourceAsStream("/it/polimi/ingsw/codexnaturalis/JSON/resourceDeck.json"));
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