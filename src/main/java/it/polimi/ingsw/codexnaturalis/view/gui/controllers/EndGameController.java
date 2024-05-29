package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;

/**
 * EndGameController class is a javaFx controller used to manage endGame stage
 * Used to display the winner or eventually winners of a game
 */
public class EndGameController {

    @FXML
    private Text winnerText;

    private String winnerString = "";


    /**
     * When clicked it terminates JavaFX thread
     * @param event
     */
    @FXML
    void exitGameFunction(MouseEvent event) {
        Platform.runLater(Platform::exit);
    }

    /**
     * Used to change the winnerText with the current winner/winners
     * @param winners
     */
    public void setWinner(List<Player> winners) {
        for(Player player : winners) {
            winnerString = winnerString + player.getNickname() + " ";
        }
        winnerText.setText(winnerString);
    }
}
