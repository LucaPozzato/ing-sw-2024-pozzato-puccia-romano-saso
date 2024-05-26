package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class EndGameController {

    @FXML
    private Text vincitore;

    @FXML
    void exitGameFunction(MouseEvent event) {
        Platform.runLater(Platform::exit);
    }

    public void setVincitore(String vincitore) {
        this.vincitore.setText(vincitore);
    }

}
