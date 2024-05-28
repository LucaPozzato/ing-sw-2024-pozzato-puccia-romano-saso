package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
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

public class JoinGame implements Initializable {

    ViewFactory viewFactory;

    @FXML
    private TextField EnterNickname, EnterPassword, EnterGameID;

    @FXML
    private ImageView blue, green, yellow, red;

    @FXML
    private Button JoinGame;

    @FXML
    private Button goBack;

    @FXML
    private ImageView passwordVisibility;

    private MiniModel miniModel;
    private VirtualClient virtualClient;
    private Game game;
    private Color color;

    int cambiamentoTestoPassword = 0;


    public void setUP(MiniModel miniModel, VirtualClient virtualClient, Game game) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
        this.game = game;

        Set<Color> allColors = EnumSet.allOf(Color.class);
        Color[] allColorsArray = allColors.toArray(new Color[0]);

    }

    @FXML
    void changePasswordText(MouseEvent event) {
        if (cambiamentoTestoPassword %2 == 0){
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
        cambiamentoTestoPassword++;

    }

    public void setUp(ViewFactory viewFactory){
        this.viewFactory = viewFactory;
    }

    @FXML
    void blueSelected(MouseEvent event) {
        color = Color.BLUE;
        blue.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        green.setOpacity(0.3);
    }

    @FXML
    void greenSelected(MouseEvent event) {
        color = Color.GREEN;
        green.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
    }

    @FXML
    void redSelected(MouseEvent event) {
        color = Color.RED;
        red.setOpacity(1);
        green.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
    }

    @FXML
    void yellowSelected(MouseEvent event) {
        color = Color.YELLOW;
        yellow.setOpacity(1);
        red.setOpacity(0.3);
        green.setOpacity(0.3);
        blue.setOpacity(0.3);
    }

    @FXML
    void JoinGameFunct(MouseEvent event) throws RemoteException {
        if (color != null && EnterGameID.getText() != null && EnterPassword.getText() != null && EnterNickname.getText() != null) {
            viewFactory.getVirtualClient().sendCommand(new JoinGameCommand(viewFactory.getVirtualClient().getClientId(), Integer.parseInt(EnterGameID.getText()), EnterNickname.getText(), EnterPassword.getText(), color));
            //Stage stage = (Stage) JoinGame.getScene().getWindow();
            //stage.close();
            viewFactory.setNickname(EnterNickname.getText());
            Platform.runLater(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("What's happening?");
            alert.setContentText("Fields cannot be null");
            Optional<ButtonType> result = alert.showAndWait();
        }

    }

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showInitialStage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(7.5);
        EnterPassword.setEffect(blur);
    }
}
