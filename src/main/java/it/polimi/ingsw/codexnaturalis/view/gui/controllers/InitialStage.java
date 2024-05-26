package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
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

    ViewFactory viewFactory;

    @FXML
    private Button startGame;
    @FXML
    private Button joinGame;
    @FXML
    private Button settings;

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
        //viewFactory.showStartGame(miniModel, virtualClient, game);
    }
    @FXML
    void joinGameFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showJoinGame();
        //viewFactory.showJoinGame(miniModel, virtualClient, game);
    }

    @FXML
    void settingsFunct(MouseEvent event) {
        Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showSettings();
    }

}


