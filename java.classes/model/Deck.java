import java.util.List;

public class Deck {

    private Stack<Card> goldDeck;
    private Stack<Card> resourceDeck;

    public Deck() {
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
        goldDeck.push();
    }

    public void addResourceCard(ResourceCard card){
        resourceDeck.push();
    }

    public GoldCard drawGoldCard(){
        return goldDeck.pop();
    }
    public ResourceCard drawResourceCard(){
        return resourceDeck.pop();
    }
    public void shuffleGoldDeck() {
        Collections.shuffle(goldDeck);
    }

    public void shuffleResourceDeck() {
        Collections.shuffle(resourceDeck);
    }

}