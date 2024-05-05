package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.TestMain;
import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class Game extends Application implements View, Initializable {
    
    
    //Utils FXML
    @FXML
    private Text mushroomsPoints , leafPoints, wolfPoints, butterflyPoints, featherPoints, manuscriptPoints, potionPoints;

    @FXML
    private Text connectionType;

    @FXML
    private ImageView initialCard, goldCard1, goldCard2, resourceCard1, resourceCard2, secreteObjective, publicObjective1, publicObjective2;

    @FXML
    private Text nickname1, nickname2, nickname3, nickname4;

    @FXML
    private ImageView nickname3Visibility, nickname4Visibility;

    @FXML
    private ImageView handCard1, handCard2, handCard3;

    String handCard1URL, handCard2URL, handCard3URL;

    private Player player;
    int rPressed = 0;
    int fPressed = 1;




    @FXML
    void handCard1Clicked(MouseEvent event) {
        handCard1.setOpacity(1);
        handCard2.setOpacity(.5);
        handCard3.setOpacity(.5);

    }

    @FXML
    void handCard2Clicked(MouseEvent event) {
        handCard1.setOpacity(.5);
        handCard2.setOpacity(1);
        handCard3.setOpacity(.5);

    }

    @FXML
    void handCard3Clicked(MouseEvent event) {
        handCard1.setOpacity(.5);
        handCard2.setOpacity(.5);
        handCard3.setOpacity(1);

    }
    @FXML
    void boardVisibility(MouseEvent event) {

    }

    @FXML
    void goldCard1Click(MouseEvent event) {

    }

    @FXML
    void goldCard2Click(MouseEvent event) {

    }

    @FXML
    void nickname2VisibilityFunct(MouseEvent event) {

    }

    @FXML
    void nickname3VisibilityFunct(MouseEvent event) {

    }

    @FXML
    void nickname4VisibilityFunct(MouseEvent event) {

    }

    @FXML
    void resourceCard1Click(MouseEvent event) {

    }

    @FXML
    void resourceCard2Click(MouseEvent event) {

    }

    public void add(int i1, int i2) {
        System.out.println("Sum "+ i1 + i2);
    }



    @Override
    public void run() {
        launch();
    }



    @Override
    public void updateChat(Chat chat) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public void updateMyPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void updateCurrentPlayer(Player player) {
        if(Objects.equals(nickname1.getText(), player.getNickname()))
            nickname1.setFill(Color.BLUE);
        else if(Objects.equals(nickname2.getText(), player.getNickname()))
            nickname2.setFill(Color.BLUE);
        else if(Objects.equals(nickname3.getText(), player.getNickname()))
            nickname3.setFill(Color.BLUE);
        else if(Objects.equals(nickname4.getText(), player.getNickname()))
            nickname4.setFill(Color.BLUE);
        else{
            System.out.println("Player not matching" + player.getNickname()); //Metti un alert in caso...
        }

//        System.out.println(player.getNickname() + "\n" +
//                nickname1.getText() + "\n" +
//                nickname2.getText() + "\n" +
//                nickname3.getText() + "\n" +
//                nickname4.getText() + "\n");

    }

    @Override
    public void updatePlayers(List<Player> players) {
        System.out.print("ciaop");
        nickname1.setText(players.get(0).getNickname());
        nickname2.setText(players.get(1).getNickname());

        if (players.size() == 3){
            nickname3.setText(players.get(2).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
        }
        if (players.size() == 4){
            nickname3.setText(players.get(2).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
            nickname4.setText(players.get(3).getNickname());
            nickname4.setVisible(true);
            nickname4Visibility.setVisible(true);
        }

    }

    @Override
    public void updateStructures(List<Structure> structures) {
        //Qui devo ricostruire le strutture degli altri players
    }


    @Override
    public void updateHand(List<Hand> hands) {
        Hand hand = hands.get(0);
        String card1;
        String card2;
        String card3;

        card1 = hand.getCardsHand().get(0).toString().substring(6,9);
        card2 = hand.getCardsHand().get(1).toString().substring(6,9);
        card3 = hand.getCardsHand().get(2).toString().substring(6,9);

        handCard1.setImage(pathFront(card1));
        handCard2.setImage(pathFront(card2));
        handCard3.setImage(pathFront(card3));

        handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "f.jpg";
        handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "f.jpg";
        handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "f.jpg";


        System.out.printf(card1 + " two: " + card2 + " three: " + card3);
    }

    @Override
    public void updateBoard(Board board) {

    }

    @Override
    public void updateDeck(Deck deck) {
        Stack<GoldCard> goldCardDeck = deck.getGoldDeck();
        Stack<ResourceCard> resourceCardDeck = deck.getResourceDeck();

        String goldCardOne = goldCardDeck.get(0).toString().substring(6,9);
        String goldCardTwo = goldCardDeck.get(1).toString().substring(6,9);
        String resourceCardOne = resourceCardDeck.get(0).toString().substring(6,9);
        String resourceCardTwo = resourceCardDeck.get(1).toString().substring(6,9);

        goldCard1.setImage(pathFront(goldCardOne));
        goldCard2.setImage(pathFront(goldCardTwo));
        resourceCard1.setImage(pathFront(resourceCardOne));
        resourceCard2.setImage(pathFront(resourceCardTwo));
    }

    @Override
    public void updateError(String error) {

    }

    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("What's happening?");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    void showBack(KeyEvent event) {
        if(event.getCode() == KeyCode.R && rPressed == 0){

            rPressed = 1;
            if (fPressed == 1)
                fPressed--;

            String card1 =  handCard1URL.substring(43,46);
            String card2 =  handCard2URL.substring(43,46);
            String card3 =  handCard2URL.substring(43,46);

            handCard1.setImage(pathBack(card1));
            handCard2.setImage(pathBack(card2));
            handCard3.setImage(pathBack(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card3 + "b.jpg";

        }
        else if(event.getCode() == KeyCode.F && fPressed == 0){

            fPressed = 1;
            if (rPressed == 1)
                rPressed--;

            String card1 =  handCard1URL.substring(42,45);
            String card2 =  handCard2URL.substring(42,45);
            String card3 =  handCard2URL.substring(42,45);

            handCard1.setImage(pathFront(card1));
            handCard2.setImage(pathFront(card2));
            handCard3.setImage(pathFront(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "b.jpg";
        }
    }
    public void setConnectionType(String tipoDiConnessione){
        connectionType.setText(tipoDiConnessione);
    }

    public Image pathFront(String oggetto){
        String imagePath =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + oggetto + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    public Image pathBack(String oggetto){
        String imagePath =  "/it/polimi/ingsw/codexnaturalis/BackCards/" + oggetto + "b.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handCard1.setFocusTraversable(true);
        handCard1.requestFocus();
        TestMain testMain = new TestMain(this);
        testMain.start();
    }
}
