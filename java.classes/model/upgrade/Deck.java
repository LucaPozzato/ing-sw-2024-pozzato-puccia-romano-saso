import java.util.List;

public class Deck {

    private Stack<Card> goldDeck;
    private Stack<Card> resourceDeck;

    public void deck() {
        this.goldDeck = new Stack<Card>();
        this.resourceDeck = new Stack<Card>();
    }

    public Stack<Card> getGoldDeck(){
        return goldDeck;
    }

    public Stack<Card> getResourceDeck(){
        return resourceDeck;
    }

    public void addGoldCard(GoldCard card){
        //...
    }

    public void addResourceCard(ResourceCard card){
        //...
    }

    public GoldCard drawGoldCard(){
        //...
    }
    public ResourceCard drawResourceCard(){
        //...
    }
    public void shuffleGoldDeck() {
        //...
    }

    public void shuffleResourceDeck() {
        //...
    }


}