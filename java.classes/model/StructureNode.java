import java.util.List;

public class StructureNode {

    private Card card;
    private StructureNode topLeftChild;
    private StructureNode topRightChild;
    private StructureNode bottomLeftChild;
    private StructureNode bottomRightChild;
    //cornercase + father da rivedere
    private List <HashMap<StructureNode, boolean>> fathers;


    //constructor
    public StructureNode() {
        //...
    }

    //getter
    public Card getCard() {
        return card;
    }

    public List<HashMap<StructureNode, boolean>> getFathers() {
        return fathers;
    }

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

    //setter
    public void setTopLeftChild(StructureNode child){
        //istanza new?
        topLeftChild = child;
    }
    public void setTopRightChild(StructureNode child){
        topRightChild = child;
    }
    public void setBottomRightChild(StructureNode child){
        bottomRightChild = child;
    }
    public void setBottomLeftChild(StructureNode child){
        bottomLeftChild = child;
    }


}