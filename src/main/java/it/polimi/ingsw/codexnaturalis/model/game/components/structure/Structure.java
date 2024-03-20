package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.HashMap;
import java.util.Objects;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;

public class Structure {

    private StructureNode root;
    private HashMap<Resource, Integer> currentResources;
    private HashMap<Objects, Integer> currentObjects;

    public Structure() {
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
        Card card = null;
        return card;
        // TODO: implement this method + return card
    }

    public void insertCard(Card father, Card card, String position) {
        // ?
    }

    public void updateResources(Resource resource, int quantity) {
        currentResources.put(resource, currentResources.get(resource) + quantity);
    }

    public void updateObjects(Objects object, int quantity) {
        currentObjects.put(object, currentObjects.get(object) + quantity);
    }

}
