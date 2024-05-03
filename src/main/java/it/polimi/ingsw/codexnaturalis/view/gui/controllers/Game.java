package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;

import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class Game implements Initializable, View {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setUp(String username, Color colorSelected, String objSelected, String cartaIniziale, String res1, String res2, String gold, Deck deck1){


    }


    @Override
    public void updateState(String state) throws IllegalCommandException {

    }

    @Override
    public void updateMyPlayer(Player player) throws IllegalCommandException {

    }

    @Override
    public void updateCurrentPlayer(Player player) throws IllegalCommandException {

    }

    @Override
    public void updatePlayers(List<Player> players) throws IllegalCommandException {

    }

    @Override
    public void updateStructure(List<Structure> structures) throws IllegalCommandException {

    }

    @Override
    public void updateHand(List<Hand> hands) throws IllegalCommandException {


    }

    @Override
    public void updateBoard(Board board) throws IllegalCommandException {

    }

    @Override
    public void updateDeck(Deck deck) throws IllegalCommandException {

    }


    public Image path(String oggetto){
        String imagePath =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + oggetto + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        //System.out.println("\nOggetto: " + oggetto + "\n");
        assert imageStream != null;
        return new Image(imageStream);
    }

    public Image symbolPath(String oggetto){
        String imagePath =  "/it/polimi/ingsw/codexnaturalis/SymbolsPng/" + oggetto + ".png";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        //System.out.println("\nOggetto: " + oggetto + "\n");
        assert imageStream != null;
        return new Image(imageStream);
    }
}