import java.util.List;

public class AngledCard extends Card {

    private List<List<Integer>> cardCorners;
    private List<List<SIMBOLO>> cardSymbols;

    @Override
    public AngledCard card(int id, int points, COLORE colour) {
        super(id, points, colour);
        // this.cardCorners json?
        // this.cardSymbols
    }

    // public List<List<Integer>> getCardCorners(){}
    // public List<List<SIMBOLO>> getCardSymbols(){}

}