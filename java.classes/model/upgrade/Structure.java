import java.util.List;

public class Structure {

    private StructureNode root;
    private HashMap <Resource, Integer> currentResources;
    private HashMap <Objects, Integer> currentObjects;

    public void structure(){
        //?
    }

    public StructureNode getRoot(){
        return root;
    }

    public HashMap<Objects, Integer> getCurrentObjects() {
        return currentObjects;
    }

    public HashMap<Resource, Integer> getCurrentResources() {
        return currentResources;
    }

    public Card getCard(id: String) {
        //?
    }

    public void insertCard(father: Card, card: Card, position: String)(){
        //?
    }

    public void updateObjects(object: Objects, quantity: int){
        //?
    }

}