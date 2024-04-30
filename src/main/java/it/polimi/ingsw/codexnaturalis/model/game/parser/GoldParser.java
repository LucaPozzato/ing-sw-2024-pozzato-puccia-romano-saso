package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;

public class GoldParser {
    private Stack<GoldCard> goldDeck;

    public GoldParser() {
        goldDeck = new Stack<>();
    }

    /**
     * When this method is called goldDeck is generated extracting the information needed from the JSON file.
     * Note that it extracts :
     * #) the card's id at "id"
     * #) the information about the type of symbol linked to the card at "symbol"<br>
     * #) the points made when the objective is satisfied at "points" <br>
     * #) the objects (feather, scroll, ink) which appear in the corners at "points type" <br>
     * #) an array of 4 strings which represent the occupation of every corner of the card starting from the TL and going clockwise<br>
     * #) a map which represent the resource that the player needs to have on his structure in order to place the goldCard at "resources required" <br>
     * @return a unique stack of resource card
     */
    public Stack<GoldCard> parse() {
        File input = new File("src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/goldDeck.json");
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
            String pointsType = cardsObject.get("points type").getAsString();
            JsonArray corners = cardsObject.get("corners").getAsJsonArray();
            List<String> listCorner = new ArrayList<>();
            for (JsonElement corner : corners) {
                listCorner.add(corner.getAsString());
            }
            JsonArray arrayOfReq = cardsObject.get("resources required").getAsJsonArray();
            Map<String, Integer> listReq = new HashMap<>();
            for (JsonElement req : arrayOfReq) {
                JsonObject reqObj = req.getAsJsonObject();
                int amount = reqObj.get("amount").getAsInt();
                String type = reqObj.get("type").getAsString();
                listReq.put(type, amount);
            }

            // Creazione dell'oggetto goldCard
            GoldCard goldCard = new GoldCard(idCard, symbol, points, pointsType, listCorner, listReq);
            goldDeck.push(goldCard);
        }
        return goldDeck;
    }
}