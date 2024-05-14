package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
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
import java.util.ResourceBundle;

public class StartGame implements Initializable{

    @FXML
    private Button goBack;
    ViewFactory viewFactory = new ViewFactory();


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

    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();

    Color color;
    MiniModel miniModel;
    VirtualClient virtualClient;


    @FXML
    private ImageView passwordVisibility;

    int cambiamentoTestoPassword = 0;


    public void setUP(MiniModel miniModel, VirtualClient virtualClient) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
    }

    @FXML
    void CreateGameFunct(MouseEvent event) throws RemoteException {
        virtualClient.sendCommand(new CreateGameCommand(virtualClient.getClientId(), Integer.parseInt(EnterGameID.getText()), EnterNickname.getText(), EnterPassword.getText(), color,Integer.parseInt(ChoosePlayers.getValue()) ));
        Stage stage = (Stage) CreateGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showGameInizializer(miniModel, virtualClient); //EnterNickname.getText()
    }

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showInitialStage(miniModel, virtualClient);
    }

    @FXML
    void blueSelected(MouseEvent event) {
        color = Color.BLUE;
        blue.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        green.setOpacity(0.3);
        System.out.println("pedina Blu" + "\n");
    }

    @FXML
    void greenSelected(MouseEvent event) {
        color = Color.GREEN;
        green.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
        System.out.println("pedinaVerde" + "\n");

    }

    @FXML
    void redSelected(MouseEvent event) {
        color = Color.RED;
        red.setOpacity(1);
        green.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
        System.out.println("pedinaRossa" + "\n");
    }

    @FXML
    void yellowSelected(MouseEvent event) {
        color = Color.YELLOW;
        yellow.setOpacity(1);
        red.setOpacity(0.3);
        green.setOpacity(0.3);
        blue.setOpacity(0.3);
        System.out.println("pedinaGialla" + "\n");
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
