import java.util.List;

public class ResourceCard extends Card {

    private Resource cardType;
    private Resource[4] cardCorners;
    private int cardPoints;


    public void resourceCard ( int idCard, String cardFrontImage, String cardBackImage, Resource cardType, Resource[4] cardCorners, int cardPoints) {
        super( idCard, cardFrontImage, cardBackImage); //check super
        this.cardType = cardType;
        this.cardCorners = cardCorners;
        this.cardPoints = cardPoints;
    }

    public Resource getCardType(){
        return cardType;
    }

    public Resource[] getCardCorners(){
        return cardCorners;
    }

    public int getCardPoints() {
        return cardPoints;
    }

}