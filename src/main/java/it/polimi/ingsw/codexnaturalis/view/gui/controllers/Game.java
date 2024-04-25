package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Game {

    @FXML
    private ImageView CO1;

    @FXML
    private ImageView CO2;

    @FXML
    private ImageView GD1;

    @FXML
    private ImageView GD2;

    @FXML
    private ImageView RD1;

    @FXML
    private ImageView RD2;

    @FXML
    private ImageView SO1;

    @FXML
    private ImageView SO2;

    @FXML
    private ImageView Tabellone;

    @FXML
    private Text butterflyPoints;

    @FXML
    private ImageView card1;

    @FXML
    private ImageView card2;

    @FXML
    private ImageView card3;

    @FXML
    private Text featherPoints;

    @FXML
    private Text leafPoints;

    @FXML
    private Text manuscriptPoints;

    @FXML
    private Text mushroomPoints;

    @FXML
    private Text nickname2;

    @FXML
    private Text potionPoints;

    @FXML
    private Text wolfPoints;

    public void displayName(String username){
        nickname2.setText(username);
    }

}