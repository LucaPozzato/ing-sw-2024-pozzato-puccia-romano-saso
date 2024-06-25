package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.network.commands.RejoinGameCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * RejoinGame class is a javaFx controller used to manage rejoinGame stage
 */
public class RejoinGame implements Initializable {

    @FXML
    private TextField EnterNickname, EnterPassword, EnterGameID;

    @FXML
    private Button goBack, JoinGame;
    
    @FXML
    private ImageView passwordVisibility;

    private ViewFactory viewFactory;
    private int changingText = 0;


    public void setUp(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    /**
     * Used for blur effect on password
     * @param event
     */
    @FXML
    void changePasswordText(MouseEvent event) {
        if (changingText %2 == 0){
            EnterPassword.setEffect(null);
            InputStream imageStream = getClass().getResourceAsStream("/it/polimi/ingsw/codexnaturalis/SymbolsPng/notvisible.png");
            Image image = new Image(imageStream);
            passwordVisibility.setImage(image);
        }
        else{
            GaussianBlur blur = new GaussianBlur();
            blur.setRadius(7.5);
            EnterPassword.setEffect(blur);
            InputStream imageStream = getClass().getResourceAsStream("/it/polimi/ingsw/codexnaturalis/SymbolsPng/visibility.png");
            Image image = new Image(imageStream);
            passwordVisibility.setImage(image);
        }
        changingText++;
    }

    /**
     * Used to rejoin a game using a RejoinGameCommand
     * @param event
     * @throws RemoteException
     */
    @FXML
    void rejoinGameFunct(MouseEvent event) throws RemoteException {
        if(EnterGameID.getText().isEmpty() || EnterNickname.getText().isEmpty() || EnterPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("What's happening?");
            alert.setContentText("Fields cannot be null");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            String clientId = viewFactory.getVirtualClient().getClientId();
            viewFactory.getVirtualClient().sendCommand(new RejoinGameCommand(clientId, Integer.parseInt(EnterGameID.getText()) , EnterNickname.getText(), EnterPassword.getText()));
            Platform.runLater(()-> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            Stage stage = (Stage) JoinGame.getScene().getWindow();
            viewFactory.closeStage(stage);
        }
    }

    /**
     * Used to go back if a player wants to select another option from the initialStage stage
     * @param event
     */
    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow();
        stage.close();
        viewFactory.showInitialStage();
    }

    /**
     * Initialized the controller class
     * Used to set up the blur effect on password TextField
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(7.5);
        EnterPassword.setEffect(blur);
    }
}
