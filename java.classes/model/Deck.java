import java.util.List;

public class Deck {

    private List<GoldCard> goldDeck;
    private List<AngledCard> resourceDeck;

    public Deck deck() {
        this.goldDeck = new List<GoldCard>();
        this.resourceDeck = new List<AngledCard>();
    }

    public List<GoldCard> goldDeck() {
        return this.goldDeck;
    }

    public List<AngledCard> resourceDeck() {
        return this.resourceDeck;
    }
}
}