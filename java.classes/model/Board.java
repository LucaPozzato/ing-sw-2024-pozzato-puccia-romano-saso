import java.util.HashMap;

public class Board {

    private HashMap<COLORE, Integer> scores;

    public Board board() {
        this.scores = new HashMap<COLORE, Integer>();
        scores.put(YELLOW, 0);
        scores.put(GREEN, 0);
        scores.put(BLUE, 0);
        scores.put(RED, 0);
        // mmm andrebbero inizializzati solo per numPlaers
    }

    public void updateScore(COLORE player, int newPoints) {
        scores.put(player, scores.get(player) + newPoints);
    }

}