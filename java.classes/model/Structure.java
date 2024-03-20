import java.util.List;

public class Structure {

    private StructureNode root;
    private HashMap <Resource, Integer> currentResources;
    private HashMap <Objects, Integer> currentObjects;

    public Structure(Card card){
        root = new StructureNode(card, null ); //passare un hashmap vuota dei fathers al costruttore
        root.card = card;
        root.fathers = fathers;
    }

    public void insertCard(List <HashMap<StructureNode, boolean>> fathers, Card card, HashMap<StructureNode, Positions> positions){

        StructureNode newNode = new StructureNode(card, fathers);

    for ( StructureNode father : fathers ) {
        if (fathers(father)) { //non ricordo a cosa serve il boolean comuqnue consideriamolo
            switch ( positions(father) ) {
                case "TOP_RIGHT":
                   father.topRightChild = newNode;
                   break;
                case "TOP_LEFT":
                   father.topLeftChild = newNode;
                   break;
                case "BOTTOM_RIGHT":
                   father.bottomRightChild = newNode;
                   break;
                case "BOTTOM_LEFT":
                   father.bottomLeftChild = newNode;
                   break;
                default:
                   //throw?
                   break;
            }
        }
    }

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

    public Card getCard(String idCard) {
        //decidere algoritmo di ricerca
        //probabilmente necessario tenere traccia dei nodi visitati
    }

    public void updateResources (Resource resource, int quantity){
        currentResources.put(resource, currentResources.get(resource) + quantity );
    }

    public void updateObjects(Objects object, int quantity){
        currentObjects.put(object, currentObjects.get(object) + quantity );
    }

}