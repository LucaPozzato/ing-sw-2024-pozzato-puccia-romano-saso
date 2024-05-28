package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
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

    ViewFactory viewFactory;
    MiniModel miniModel;
    VirtualClient virtualClient;
    Game game;


    public void setUP(MiniModel miniModel, VirtualClient virtualClient, Game game){
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
        this.game = game;
    }

    public void setUp(ViewFactory viewFactory){
        this.viewFactory = viewFactory;
    }

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
        //viewFactory.showJoinGame(miniModel, virtualClient, game);
    }

    @FXML
    void rejoinGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showRejoinGame();
    }

}


