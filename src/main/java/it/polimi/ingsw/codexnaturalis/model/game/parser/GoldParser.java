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
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;

public class GoldParser {

    public GoldParser() {
    }

    public void Parse() throws Exception {
        Deck deck = new Deck();
        GoldCard drawn;
        File input = new File(
                "src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/goldDeck.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
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
                listCorner.add(corner.toString());
            }
            // Inserire ultima chiave
            JsonArray arrayOfReq = cardsObject.get("resources required").getAsJsonArray();
            List<String> listReq = new ArrayList<>();
            for (JsonElement req : arrayOfReq) {
                JsonObject reqObj = req.getAsJsonObject();
                int amount = reqObj.get("amount").getAsInt();
                String type = reqObj.get("type").getAsString();
                if (amount != 0) {
                    for (int i = 0; i < amount; i++) {
                        listReq.add(type);
                    }
                }
            }

            // Creazione dell'oggetto goldCard
            GoldCard goldCard = new GoldCard(idCard, symbol, pointsType, listCorner, listReq, points);
            deck.addGoldCard(goldCard);
        }
        drawn = deck.drawGoldCard();
        while (!deck.emptyGold()) {
            drawn.print();
            drawn = deck.drawGoldCard();
        }
        drawn.print();
    }
}
