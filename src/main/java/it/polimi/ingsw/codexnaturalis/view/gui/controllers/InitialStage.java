package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.view.gui.GuiApp;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialStage{

    ViewFactory viewFactory = new ViewFactory();

    @FXML
    private Button startGame;
    @FXML
    private Button joinGame;
    @FXML
    private Button settings;



    @FXML
    void startGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showStartGame();
    }
    @FXML
    void joinGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showJoinGame();
    }

    @FXML
    void settingsFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showSettings();
    }

}


