public abstract class Card {

    private int id;
    private int cardPoints;
    // private int codeFront;
    // private int codeBack;
    private COLORE colour;
    // informazioni per caratterizzare la carta da decicdre nel gson e tenere
    // coerenti

    public Card card(int id, int points, COLORE colour) {
        this.id = id;
        this.cardPoints = points;
        this.colour = colour;
    }

    public int getIdCard() {
        return id;
    }

    // public getCodeFront() {
    // return codeFront;
    // }

    // public getCodeBack() {
    // return codeBack;
    // }

    public int getCardPoints() {
        return cardPoints;
    }

}
