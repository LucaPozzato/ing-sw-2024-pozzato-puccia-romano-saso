package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Initialstage is a controller used to manage the first stage that clients see.
 * You can choose between StartGame, JoinGame, RejoinGame.
 */
public class InitialStage{

    @FXML
    private Button startGame;

    private ViewFactory viewFactory;

    public void setUp(ViewFactory viewFactory){
        this.viewFactory = viewFactory;
    }


    /**
     * Calls Viewfactory class to open startGame stage and closes initialStage stage
     * @param event the mouse event that triggered this method
     */
    @FXML
    void startGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.showStartGame();
    }

    /**
     * Calls Viewfactory class to open joinGame stage and closes initialStage stage
     * @param event the mouse event that triggered this method
     */
    @FXML
    void joinGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.showJoinGame();
    }

    /**
     * Calls Viewfactory class to open rejoinGame stage and closes initialStage stage
     * @param event the mouse event that triggered this method
     */
    @FXML
    void rejoinGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showRejoinGame();
    }

}


