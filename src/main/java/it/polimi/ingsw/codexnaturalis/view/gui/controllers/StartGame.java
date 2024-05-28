package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
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
 * La classe "StartGame" è un controller utilizzato per creare le partite.
 * I
 * È possibile includere dettagli aggiuntivi sulla classe.
 */

public class StartGame implements Initializable{

    @FXML
    private Button goBack;
    ViewFactory viewFactory;


    @FXML
    private ChoiceBox<String> ChoosePlayers;

    private String[] playersNum = {"2", "3", "4"};

    @FXML
    private Button CreateGame;

    @FXML
    private TextField EnterNickname;

    @FXML
    private TextField EnterPassword;

    @FXML
    private TextField EnterGameID;

    @FXML
    private ImageView blue;

    @FXML
    private ImageView green;

    @FXML
    private ImageView red;

    @FXML
    private ImageView yellow;



    Color color;
    MiniModel miniModel;
    VirtualClient virtualClient;
    Game game; //DA LEVARE


    @FXML
    private ImageView passwordVisibility;

    int cambiamentoTestoPassword = 0;


    public void setUP(MiniModel miniModel, VirtualClient virtualClient, Game game) {
        //this.miniModel = miniModel;
        //this.virtualClient = virtualClient;
        this.game = game;
    }


    public void setUp(ViewFactory viewFactory){
        this.viewFactory = viewFactory;
    }

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

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showInitialStage();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoosePlayers.setValue("2");
        ChoosePlayers.getItems().addAll(playersNum);

        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(7.5);
        EnterPassword.setEffect(blur);

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

}
