package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.JoinGameCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.*;

public class JoinGame {

    ViewFactory viewFactory;

    @FXML
    private TextField EnterNickname, EnterPassword, EnterGameID;

    @FXML
    private ImageView blue, green, yellow, red;

    @FXML
    private Button JoinGame;

    @FXML
    private Button goBack;

    private MiniModel miniModel;
    private VirtualClient virtualClient;
    private Game game;
    private Color color;


    public void setUP(MiniModel miniModel, VirtualClient virtualClient, Game game) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
        this.game = game;

        for (Player player : miniModel.getPlayers()) {
            System.out.println("QUesto è un giocatore" + player.getNickname());
        }

        Set<Color> allColors = EnumSet.allOf(Color.class);
        Color[] allColorsArray = allColors.toArray(new Color[0]);

    }

    public void setUP(MiniModel miniModel, VirtualClient virtualClient) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;

        for (Player player : miniModel.getPlayers()) {
            System.out.println("QUesto è un giocatore" + player.getNickname());
        }

        Set<Color> allColors = EnumSet.allOf(Color.class);
        Color[] allColorsArray = allColors.toArray(new Color[0]);

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

    @FXML
    void JoinGameFunct(MouseEvent event) throws RemoteException {
        viewFactory.getVirtualClient().sendCommand(new JoinGameCommand(viewFactory.getVirtualClient().getClientId(), Integer.parseInt(EnterGameID.getText()), EnterNickname.getText(), EnterPassword.getText(), color));
        Stage stage = (Stage) JoinGame.getScene().getWindow();
        stage.close();
        viewFactory.setNickname(EnterNickname.getText());
        Platform.runLater(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void goBackFunct(MouseEvent event) {
        Stage stage = (Stage) goBack.getScene().getWindow(); //trick for getting current stage
        viewFactory.closeStage(stage);
        //viewFactory.showInitialStage();
    }

}
