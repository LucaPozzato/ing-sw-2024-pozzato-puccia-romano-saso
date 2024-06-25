package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * StartGame class is a javaFx controller used to manage joinGame stage
 */
public class StartGame implements Initializable{

    @FXML
    private Button goBack, CreateGame;

    @FXML
    private ChoiceBox<String> ChoosePlayers;

    @FXML
    private TextField EnterNickname, EnterPassword, EnterGameID;

    @FXML
    private ImageView blue, green, red, yellow, passwordVisibility;

    private ViewFactory viewFactory;
    private Color color;
    private final String[] playersNum = {"2", "3", "4"};
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
     * Used to start a game using a CreateGameCommand
     * @param event
     * @throws RemoteException
     */
    @FXML
    void CreateGameFunct(MouseEvent event) throws RemoteException {
        if(EnterNickname.getText().isEmpty() || EnterPassword.getText().isEmpty() || EnterGameID.getText().isEmpty() || ChoosePlayers.getValue().isEmpty() || color == null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("What's happening?");
            alert.setContentText("Fields cannot be null");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            String clientId = viewFactory.getVirtualClient().getClientId();
            viewFactory.getVirtualClient().sendCommand(new CreateGameCommand(clientId, Integer.parseInt(EnterGameID.getText()), EnterNickname.getText(), EnterPassword.getText(), color,Integer.parseInt(ChoosePlayers.getValue()) ));
            Platform.runLater(()-> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            Stage stage = (Stage) CreateGame.getScene().getWindow(); //trick for getting current stage
            viewFactory.setNickname(EnterNickname.getText());
            viewFactory.closeStage(stage);
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
     * Used to set up the blur effect on password TextField and ChoosePlayer values
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoosePlayers.setValue("2");
        ChoosePlayers.getItems().addAll(playersNum);

        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(7.5);
        EnterPassword.setEffect(blur);
    }
}
