package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartGame implements Initializable{

    @FXML
    private Button goBack;
    ViewFactory viewFactory = new ViewFactory();

    @FXML
    private ChoiceBox<String> ChooseColor;

    private String[] colors = {"Blue", "Red", "Green", "Yellow"};

    @FXML
    private Button CreateGame;

    @FXML
    private TextField EnterNickname;

    @FXML
    private TextField EnterPassword;

    @FXML
    private RadioButton RMI;

    @FXML
    private RadioButton SOCKET;

    @FXML
    void CreateGameFunct(MouseEvent event) {
        Stage stage = (Stage) CreateGame.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showGame();
    }

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showInitialStage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChooseColor.setValue("Blue");
        ChooseColor.getItems().addAll(colors);
    }
}
