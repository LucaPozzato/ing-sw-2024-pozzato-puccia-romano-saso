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
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;


public class ResourceParser {

    public ResourceParser(){
    }
    public Stack<ResourceCard> Parse()  {
        Stack<ResourceCard> Resourcedeck = new Stack<>();
        ResourceCard drawn;
        File input = new File("/Users/niccolo/Desktop/Università/Ingegneria_SW/ing-sw-2024-pozzato-puccia-romano-saso/src/main/resources/it/polimi/ingsw/codexnaturalis/JSON/resourceDeck.json");
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
                array.add(corner.toString());
            }
            ResourceCard resCard = new ResourceCard(idCard, symbol, points, array);
            Resourcedeck.push(resCard);
        }
        //Stampa le carte parsate
        //drawn = Resourcedeck.drawResourceCard();
        //while(!deck.emptyRes()){
        //    drawn.print();
        //    drawn = Resourcedeck.drawResourceCard();
        //}
        return Resourcedeck;
    }
}