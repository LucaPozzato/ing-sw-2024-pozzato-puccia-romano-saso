package it.polimi.ingsw.codexnaturalis.view.gui;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.Game;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.GameInizializer;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.InitialStage;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.StartGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    private AnchorPane gameView;

    public ViewFactory(){};

    public AnchorPane getUserView(){
        if (gameView == null){
            try{
                gameView = new FXMLLoader(getClass().getResource("/view/gui/initialStage.fxml")).load();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return gameView;
    }

    public void showInitialStage(MiniModel miniModel, VirtualClient virtualClient){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initialStage.fxml"));
        createStage(loader);
        InitialStage initialStage = loader.getController();
        initialStage.setUP(miniModel, virtualClient);

    }

    public void showStartGame(MiniModel miniModel, VirtualClient virtualClient){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startGame.fxml"));
        createStage(loader);
        StartGame startGame = loader.getController();
        startGame.setUP(miniModel, virtualClient);

    }

    public void showGameInizializer(MiniModel miniModel, VirtualClient virtualClient){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameInizializer.fxml"));
        GameInizializer gameInizializer = loader.getController();
        createStage(loader);

    }

    public void showGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameStage.fxml"));
        createStage(loader);

    }

    public void showChat(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatStage.fxml"));
        createStage(loader);

    }



    public void showJoinGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGame.fxml"));
        createStage(loader);
    }

    public void showSettings(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settingsStage.fxml"));
        createStage(loader);
    }



    private void createStage(FXMLLoader loader) {
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
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

}
