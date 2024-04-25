package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @FXML
    private ChoiceBox<String> ChoosePlayers;

    private String[] colors = {"Blue", "Red", "Green", "Yellow"};
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
        RMI.setSelected(true);
        RMI.setToggleGroup(toggleGroup);
        SOCKET.setToggleGroup(toggleGroup);

        ChooseColor.setValue("Blue");
        ChooseColor.getItems().addAll(colors);

        ChoosePlayers.setValue("2");
        ChoosePlayers.getItems().addAll(playersNum);

    }
}
