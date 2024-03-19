import java.util.List;

public class GoldCard extends Card {

    private Resource cardType;
    private Resource[4] cardCorners;
    private Resource[5] resourceRequired; //uml segna 4
    private int cardPoints;
    private GoldCardPointType pointType;

    public void GoldCard(int idCard, String cardFrontImage, String cardBackImage, Resource cardType, Resource[4] cardCorners, Resource[5] resourceRequired, int cardPoints, GoldCardPointType pointType){
        super( idCard, cardFrontImage, cardBackImage);
        this.cardType = cardType;
        this.cardCorners = cardCorners;
        this.resourceRequired = resourceRequired;
        this.cardPoints = cardPoints;
        this.pointType = pointType;

    }

    public Resource getCardType() {
        return cardType;
    }

    public Resource[] getCardCorners(){
        return cardCorners;
    }

    public Resource[] getResourceRequired(){
        return resourceRequired;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public GoldCardPointType getPointType() {
        return pointType;
    }

    public void setCardType(Resource cardType){
        this.cardType = cardType;
    }

    public void setCardCorners(Resource[4] cardCorners) {
        this.cardCorners = cardCorners;
    }

    public void setResourceRequired (Resource[5] resourceRequired) {
        this.resourceRequired = resourceRequired;
    }

    public void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
    }

    public void setPointType(GoldCardPointType pointType) {
        this.pointType = pointType;
    }

}