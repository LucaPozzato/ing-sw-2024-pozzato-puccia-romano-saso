package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.awt.Desktop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Settings {

    ViewFactory viewFactory = new ViewFactory();

    @FXML
    private Button goBack;

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        viewFactory.showInitialStage();
    }

    @FXML
    void openLinkl(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://cdn.1j1ju.com/medias/a7/d7/66-codex-naturalis-rulebook.pdf"));
    }


}
