package it.polimi.ingsw.codexnaturalis.model.game.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;

//Nel metodo della classe InitState "dealSecretObjective" occorre chiamare il parser che ritorna la collection.
//Della lista (che è già randomica) dobbiamo assegnare due carte alla hand di ogni player facendole contemporaneamente scomparire da collection
//Bisogna implementare un metodo setter di chooseBetweenObject (attributo di hand)
//SE VOGLIAMO LAVORARE SU COLLECTION OCCORRE CHE PARSE() TORNI UNA LISTA DI OBJECTIVE CARDS

//Nel metodo della classe InitState "dealCommonObjective" occorre conservare ciò che rimane della collecion dopo che il player ha scelto la sua carta obiettivo segreto.
//Considerare di chiamare il metodo in uno stato successivo all'iniziale?
//In ogni caso andranno selezionate randomicamente due carte tra quelle che collection contiene e posizionate sulla board come common Objective
//Va cambiato il tipo di commonObjective nell'UML

public class ObjectiveParser {
    public ObjectiveParser() {
    }

    public List<ObjectiveCard> Parse() {
        List<ObjectiveCard> collection = new ArrayList<>();

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

            ObjectiveCard objCard = new ObjectiveCard(idCard, points, shape, mustHave);
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

            ObjectiveCard objCard = new ObjectiveCard(idCard, points, shape, mustHave);
            collection.add(objCard);
        }
        Collections.shuffle(collection);
        // System.out.println(collection);
        return collection;
    }
}
