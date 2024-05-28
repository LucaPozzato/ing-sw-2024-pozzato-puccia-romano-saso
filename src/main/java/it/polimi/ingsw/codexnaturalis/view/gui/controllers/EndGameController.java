package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;

public class EndGameController {

    @FXML
    private Text vincitore;
    private String allVincitori = "";

    @FXML
    void exitGameFunction(MouseEvent event) {
        Platform.runLater(Platform::exit);
    }

    public void setVincitore(List<Player> vincitori) {
        for(Player player : vincitori) {
            allVincitori = allVincitori + player.getNickname() + " ";
        }
        vincitore.setText(allVincitori);
    }

}
