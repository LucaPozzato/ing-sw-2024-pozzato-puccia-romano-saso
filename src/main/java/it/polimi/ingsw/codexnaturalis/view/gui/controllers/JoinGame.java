package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.network.commands.JoinGameCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

/**
 * JoinGame class is a javaFx controller used to manage joinGame stage
 */
public class JoinGame implements Initializable {

    @FXML
    private TextField EnterNickname, EnterPassword, EnterGameID;

    @FXML
    private ImageView blue, green, yellow, red, passwordVisibility;

    @FXML
    private Button goBack;

    private ViewFactory viewFactory;
    private Color color;
    private int changingText = 0;


    public void setUp(ViewFactory viewFactory){
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
            assert imageStream != null;
            Image image = new Image(imageStream);
            passwordVisibility.setImage(image);
        }
        else{
            GaussianBlur blur = new GaussianBlur();
            blur.setRadius(7.5);
            EnterPassword.setEffect(blur);
            InputStream imageStream = getClass().getResourceAsStream("/it/polimi/ingsw/codexnaturalis/SymbolsPng/visibility.png");
            assert imageStream != null;
            Image image = new Image(imageStream);
            passwordVisibility.setImage(image);
        }
        changingText++;
    }

    /**
     * Selecting blue color and changing the button visually
     * @param event
     */
    @FXML
    void blueSelected(MouseEvent event) {
        color = Color.BLUE;
        blue.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        green.setOpacity(0.3);
    }

    /**
     * Selecting green color and changing the button visually
     * @param event
     */
    @FXML
    void greenSelected(MouseEvent event) {
        color = Color.GREEN;
        green.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
    }

    /**
     * Selecting red color and changing the button visually
     * @param event
     */
    @FXML
    void redSelected(MouseEvent event) {
        color = Color.RED;
        red.setOpacity(1);
        green.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
    }

    /**
     * Selecting yellow color and changing the button visually
     * @param event
     */
    @FXML
    void yellowSelected(MouseEvent event) {
        color = Color.YELLOW;
        yellow.setOpacity(1);
        red.setOpacity(0.3);
        green.setOpacity(0.3);
        blue.setOpacity(0.3);
    }

    /**
     * Used to join an existing game using a JoinGameCommand
     * @param event
     * @throws RemoteException
     */
    @FXML
    void JoinGameFunct(MouseEvent event) throws RemoteException {
        if (color != null && EnterGameID.getText() != null && EnterPassword.getText() != null && EnterNickname.getText() != null) {
            viewFactory.getVirtualClient().sendCommand(new JoinGameCommand(viewFactory.getVirtualClient().getClientId(), Integer.parseInt(EnterGameID.getText()), EnterNickname.getText(), EnterPassword.getText(), color));
            viewFactory.setNickname(EnterNickname.getText());
            Platform.runLater(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            Stage stage = (Stage) goBack.getScene().getWindow();
            viewFactory.closeStage(stage);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("What's happening?");
            alert.setContentText("Fields cannot be null");
        }
    }

    /**
     * Used to go back if a player wants to select another option from the initialStage stage
     * @param event
     */
    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
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
