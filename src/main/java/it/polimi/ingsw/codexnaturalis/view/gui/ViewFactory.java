package it.polimi.ingsw.codexnaturalis.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    //User view
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

    public void showInitialStage(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initialStage.fxml"));
        createStage(loader);
    }

    public void showStartGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startGame.fxml"));
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


    public void showGame(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameStage.fxml"));
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
        stage.setTitle("Codex naturalis");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

}
