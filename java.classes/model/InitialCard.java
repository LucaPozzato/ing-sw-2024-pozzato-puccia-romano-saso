import java.util.List;

public class InitialCard extends Card {

    //modifiche da rivedere/inserire su uml
    private Resource[4] frontCardCorners;
    private Resource[3] backCardResources;
    private boolean[4] backCardCorners;

    public void InitialCard(int idCard, String cardFrontImage, String cardBackImage, Resource[4] frontCardCorners, Resource[3] backCardResources, boolean[4] backCardCorners){
        super( idCard, cardFrontImage, cardBackImage);
        this.frontCardCorners = frontCardCorners;
        this.frontCardResources = frontCardResources;
        this.backCardCorners = backCardCorners;
    }

    public Resource[] getFrontCardCorners(){
        return frontCardCorners;
    }
    public Resource[] getBackCardResources(){
        return backCardResources;
    }
    public boolean[] getBackCardCorners(){
        return backCardCorners;
    }

    public void setFrontCardCorners (Resource[4] frontCardCorners){
        this.frontCardCorners = frontCardCorners;
    }
    public void setBackCardResources (Resource[3] backCardResources){
        this.backCardResources = backCardResources;
    }
    public void setBackCardCorners (boolean[4] backCardCorners){
        this.backCardCorners = backCardCorners;
    }

}