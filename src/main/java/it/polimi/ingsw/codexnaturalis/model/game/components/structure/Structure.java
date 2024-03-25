package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.HashMap;

import it.polimi.ingsw.codexnaturalis.model.enumerations.*;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;

public class Structure {

    private StructureNode root;
    private HashMap<Resource, Integer> currentResources;
    private HashMap<Objects, Integer> currentObjects;

    public Structure(Card card) {
        root = new StructureNode(card, null); // passare un hashmap vuota dei fathers al costruttore
    }

//     public void insertCard(List<HashMap<StructureNode, Boolean>> fathers, Card
//     card,
//     HashMap<StructureNode, Positions> positions) {
//
//     StructureNode newNode = new StructureNode(card, fathers);
//
//     for (StructureNode father : fathers) {
//         if (fathers(father)) { // non ricordo a cosa serve il boolean comuqnue consideriamolo
//             switch (positions(father)) {
//                 case "TOP_RIGHT":
//                    father.setTopRightChild(newNode);
//                    break;
//                 case "TOP_LEFT":
//                     father.setTopLeftChild(newNode);
//                    break;
//                 case "BOTTOM_RIGHT":
//                     father.setBottomRightChild(newNode);
//                     break;
//                 case "BOTTOM_LEFT":
//                    father.setBottomLeftChild(newNode);
//                   break;
//                 default:
//                   // throw?
//                   break;
//                 }
//             }
//         }
//     }

    // FIXME: all the code that is commented does not compile

    public void insertCard(Card idBottomCard, Card id, String position) {

    }

    public StructureNode getRoot() {
        return root;
    }

    public HashMap<Objects, Integer> getCurrentObjects() {
        return currentObjects;
    }

    public HashMap<Resource, Integer> getCurrentResources() {
        return currentResources;
    }

    public Card getCard(String idCard) {
        return null;
        // decidere algoritmo di ricerca
        // probabilmente necessario tenere traccia dei nodi visitati
    }

    public void updateResources(Resource resource, int quantity) {
        currentResources.put(resource, currentResources.get(resource) + quantity);
    }

    public void updateObjects(Objects object, int quantity) {
        currentObjects.put(object, currentObjects.get(object) + quantity);
    }

}