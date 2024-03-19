import java.util.List;

public class ObjectiveCard extends Card {

    private ObjectiveCardType objectiveType;

    public void ObjectiveCard( int idCard, String cardFrontImage, String cardBackImage, ObjectiveCardType objectiveType){
        super( idCard, cardFrontImage, cardBackImage);
        this.objectiveType = objectiveType;
    }

    public ObjectiveCardType getObjectiveType() {
        return objectiveType;
    }

    public void setObjectiveType(ObjectiveCardType objectiveType) {
        this.objectiveType = objectiveType;
    }

}