package it.polimi.ingsw.codexnaturalis.view.gui;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ViewFactory {

    private final MiniModel miniModel;
    private final VirtualClient virtualClient;
    private Stage currentStage;
    private String nickname;
    private Scene gameScene;

    public ViewFactory(MiniModel miniModel, VirtualClient virtualClient) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
    };


    //Initial Stage

    public void showInitialStage(MiniModel miniModel, VirtualClient virtualClient, Game game){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initialStage.fxml"));
        createStage(loader);
        InitialStage initialStage = loader.getController();
        initialStage.setUP(miniModel, virtualClient, game);

    }

    public void showInitialStage(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initialStage.fxml"));
        createStage(loader);
        InitialStage initialStage = loader.getController();
        initialStage.setUp(this);

    }

    //Start Game

    public void showStartGame(MiniModel miniModel, VirtualClient virtualClient, Game game){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startGame.fxml"));
        createStage(loader);
        StartGame startGame = loader.getController();
        startGame.setUP(miniModel, virtualClient, game);
    }

    public void showStartGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startGame.fxml"));
        createStage(loader);
        StartGame startGame = loader.getController();
        startGame.setUp(this);
    }


    //Join Game

    public void showJoinGame(MiniModel miniModel, VirtualClient virtualClient, Game game){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGame.fxml"));
        createStage(loader);
        JoinGame joinGame = loader.getController();
        joinGame.setUP(miniModel, virtualClient, game);
    }

    public void showJoinGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGame.fxml"));
        createStage(loader);
        JoinGame joinGame = loader.getController();
        joinGame.setUp(this);
    }

    //Show Game

    public void showGame(MiniModel miniModel, VirtualClient virtualClient, Game game){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameStage.fxml"));
        createStage(loader);
        game = loader.getController();
        game.setUP(miniModel, virtualClient);

    }

    public void showGame(FXMLLoader gameLoader){
        createStage(gameLoader);
        //game = loader.getController();
        //game.setUp(this);

    }


    public Stage showWait(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("waitStage.fxml"));
        return createStage(loader);
    }


    public void showChoose(Hand hand, MiniModel miniModel, VirtualClient virtualClient){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chooseStage.fxml"));
        createStage(loader);
        ChooseStage chooseStage = loader.getController();
        chooseStage.setUP(hand, miniModel, virtualClient);
    }

    public Stage showChoose() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chooseStage.fxml"));

        Scene scene = new Scene(loader.load());

        ChooseStage chooseStage = loader.getController();

        chooseStage.setUp(this);
        chooseStage.inizializza();

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex naturalis");
        stage.show();

        return stage;

    }

    public void showEndGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("endGameStage.fxml"));
        createStage(loader);
        EndGameController endGameController = loader.getController();
        endGameController.setVincitore(miniModel.getWinner().get(0).getNickname());

    }


    public void showSettings(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settingsStage.fxml"));
        createStage(loader);
    }



    private Stage createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        if(!(loader.getController() instanceof ChooseStage)){
            currentStage = stage;
        }
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex naturalis");
        stage.show();
        return stage;
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public void showGameInizializer(MiniModel miniModel, VirtualClient virtualClient){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameInizializer.fxml"));
        GameInizializer gameInizializer = loader.getController();
        createStage(loader);

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

    public Stage getCurrentStage(){
        return currentStage;
    }

    public void setScene(Scene scene){
        this.gameScene = scene;
    }

}
