import java.util.HashMap;

public abstract class Card {

    private int idCard;
    private String cardFrontImage;
    private String cardBackImage;


    public void card(int idCard, String cardFrontImage, String cardBackImage) {
        this.idCard = idCard;
        this.cardBackImage = cardBackImage;
        this.cardFrontImage = cardFrontImage;
    }

    public int getIdCard(){
        return idCard;
    }

    public String getCardFrontImage(){
        return cardFrontImage;
    }

    public String getCardBackImage(){
        return cardBackImage;
    }

}
