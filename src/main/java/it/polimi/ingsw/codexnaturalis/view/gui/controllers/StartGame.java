package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

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
    ToggleGroup toggleGroup = new ToggleGroup();



    @FXML
    private ImageView passwordVisibility;
    int cambiamentoTestoPassword = 0;




    @FXML
    void CreateGameFunct(MouseEvent event) {

        Stage stage = (Stage) CreateGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showGameInizializer(EnterNickname.getText()); //EnterNickname.getText()
    }

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showInitialStage();
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
