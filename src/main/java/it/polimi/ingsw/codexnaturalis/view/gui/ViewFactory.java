package it.polimi.ingsw.codexnaturalis.view.gui;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * ViewFactory class uses the Factory Design Pattern to switch to different Scenes when buttons are clicked.
 */
public class ViewFactory {

    private final MiniModel miniModel;
    private final VirtualClient virtualClient;
    private String nickname;

    public static Stage staticJoinGame;


    public ViewFactory(MiniModel miniModel, VirtualClient virtualClient) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
    }

    /**
     * Create and display initialStage stage
     */
    public void showInitialStage(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initialStage.fxml"));
        createStage(loader);
        InitialStage initialStage = loader.getController();
        initialStage.setUp(this);
    }

    /**
     * Create and display startGame stage
     */
    public void showStartGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startGame.fxml"));
        createStage(loader);
        StartGame startGame = loader.getController();
        startGame.setUp(this);
    }

    /**
     * Create and display joinGame stage
     */
    public void showJoinGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGame.fxml"));
        staticJoinGame = createStage(loader);
        JoinGame joinGame = loader.getController();
        joinGame.setUp(this);
    }

    /**
     * Create and display rejoinGame stage
     */
    public void showRejoinGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rejoinGame.fxml"));
        createStage(loader);
        RejoinGame rejoinGame = loader.getController();
        rejoinGame.setUp(this);
    }

    /**
     * Create and display showWait stage
     */
    public Stage showWait(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("waitStage.fxml"));
        return createStage(loader);
    }

    /**
     * Create and display showChoose stage
     */
    public Stage showChoose() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chooseStage.fxml"));

        Scene scene = new Scene(loader.load());

        ChooseStage chooseStage = loader.getController();

        chooseStage.setUp(this);
        chooseStage.initializeController();

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex naturalis");
        stage.show();

        return stage;

    }

    /**
     * Create and display endGame stage
     */
    public void showEndGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("endGameStage.fxml"));
        createStage(loader);
        EndGameController endGameController = loader.getController();
        endGameController.setWinner(miniModel.getWinner());

    }

    /**
     * Used to create stages
     */
    private Stage createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex naturalis");
        stage.show();
        return stage;
    }

    /**
     * Used to close stages
     */
    public void closeStage(Stage stage) {
        stage.close();
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public MiniModel getMinimodel(){
        return miniModel;
    }

    public VirtualClient getVirtualClient(){
        return virtualClient;
    }

}
