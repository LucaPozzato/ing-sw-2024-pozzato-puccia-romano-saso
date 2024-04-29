package it.polimi.ingsw.codexnaturalis.network.events;

import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class EndGameEvent extends Event {
    private Map<Player, Integer> actualScores;
    private Player winner;

    public EndGameEvent(Map<Player, Integer> actualScores, Player winner) {
        this.actualScores = actualScores;
        this.winner = winner;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setActualScore(actualScores);
        miniModel.setWinner(winner);
    }
}