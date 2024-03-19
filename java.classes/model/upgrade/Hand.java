import java.util.List;

import javax.smartcardio.Card;

public class Hand {

    private List<Card> chooseBetweenObj;
    private Card secretObjective;
    private List<Card> cardsHand;

    //costruttore
    public void hand() {
        //
    }

    public void setSecretObjective(Card secretObjective) {
        this.secretObjective = secretObjective;
    }

    //getter
    public Card getSecretObjective() {
        return secretObjective;
    }

    public List<Card> getCardsHand() {
        return cardsHand;
    }

    public void addCard(Card card){}
    public void removeCard(Card card){}

}