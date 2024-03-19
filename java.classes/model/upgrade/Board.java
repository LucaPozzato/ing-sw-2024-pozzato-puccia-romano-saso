import java.util.HashMap;

public class Board {

    private HashMap<Color, Integer> scores;
    private ObjectiveCard commonObjective;
    private List<Card> uncoveredCards;


    public void board() {
        this.scores = new HashMap<COLORE, Integer>();
        //scores.put(YELLOW, 0); ? per numPlayers
        //...
    }

    public void setCommonObjective(ObjectiveCard commonObjective){
        this.commonObjective = commonObjective;
    }

    public HashMap<Color, Integer> getScores() {
        return scores;
    }

    public ObjectiveCard getCommonObjective() {
        return commonObjective;
    }

    public List<Card> getUncoveredCards() {
        return uncoveredCards;
    }

    public void updateScore(Color player, int newPoints) {

        scores.put(player, scores.get(player) + newPoints);
    }

    public void addUncoveredCard(Card card){
        //...
    }

    public void removeUncoveredCard(Card card){
        //...
    }

    public boolean isLastTurn(){
        //...
    }


}