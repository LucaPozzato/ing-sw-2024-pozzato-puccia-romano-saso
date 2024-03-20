package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;

public class StructureNode {

    private Card card;
    private StructureNode topLeftChild;
    private StructureNode topRightChild;
    private StructureNode bottomLeftChild;
    private StructureNode bottomRightChild;

    // cornercase + father da rivedere
    private List<HashMap<StructureNode, Boolean>> fathers;

    // constructor
    public StructureNode() {
    }

    // getter
    public StructureNode getTopLeftChild() {
        return topLeftChild;
    }

    public StructureNode getTopRightChild() {
        return topRightChild;
    }

    public StructureNode getBottomLeftChild() {
        return bottomLeftChild;
    }

    public StructureNode getBottomRightChild() {
        return bottomRightChild;
    }

    // setter
    public void setTopLeftChild(StructureNode child) {
        topLeftChild = child;
    }

    public void setTopRightChild(StructureNode child) {
        topRightChild = child;
    }

    public void setBottomRightChild(StructureNode child) {
        bottomRightChild = child;
    }

    public void ssetBottomLeftChild(StructureNode child) {
        bottomLeftChild = child;
    }

}