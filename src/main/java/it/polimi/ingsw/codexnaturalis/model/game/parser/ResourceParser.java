package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;

public class ResourceParser {

    public ResourceParser() {
    }

    public void Parse() throws Exception {
        Deck deck = new Deck();
        ResourceCard drawn;
        File input = new File(
                "src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/resourceDeck.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonArray fileArray = fileElement.getAsJsonArray();
        for (JsonElement cards : fileArray) {
            JsonObject cardsObject = cards.getAsJsonObject();
            String idCard = cardsObject.get("id").getAsString();
            String symbol = cardsObject.get("symbol").getAsString();
            int points = cardsObject.get("points").getAsInt();
            JsonArray corners = cardsObject.get("corners").getAsJsonArray();
            List<String> array = new ArrayList<>();
            for (JsonElement corner : corners) {
                array.add(corner.toString());
            }
            ResourceCard resCard = new ResourceCard(idCard, symbol, array, points);
            deck.addResourceCard(resCard);
        }
        drawn = deck.drawResourceCard();
        while (!deck.emptyRes()) {
            drawn.print();
            drawn = deck.drawResourceCard();
        }
    }
}