package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
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
    public void run() {

    }

    @Override
    public void updateError(String error) {

    }

    @Override
    public void updateChat(Chat chat) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public void updateMyPlayer(Player player) {

    }

    @Override
    public void updateCurrentPlayer(Player player) {

    }

    @Override
    public void updatePlayers(List<Player> players) {

    }

    @Override
    public void updateStructures(List<Structure> structures) {

    }

    @Override
    public void updateHand(List<Hand> hands) {

    }

    @Override
    public void updateBoard(Board board) {

    }

    @Override
    public void updateDeck(Deck deck) {

    }

    public Image path(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + oggetto + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        // System.out.println("\nOggetto: " + oggetto + "\n");
        assert imageStream != null;
        return new Image(imageStream);
    }

    public Image symbolPath(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/SymbolsPng/" + oggetto + ".png";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        // System.out.println("\nOggetto: " + oggetto + "\n");
        assert imageStream != null;
        return new Image(imageStream);
    }
}