package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.TestMain;
import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ScrollPane;
import javafx.util.Pair;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Game extends Application implements View, Initializable {
    
    
    //Utils FXML
    @FXML
    private Text mushroomsPoints , leafPoints, wolfPoints, butterflyPoints, featherPoints, manuscriptPoints, potionPoints;

    @FXML
    private Text connectionType;

    @FXML
    private ImageView goldDeckCard, goldCard1, goldCard2, resourceDeckCard, resourceCard1, resourceCard2, secreteObjective, publicObjective1, publicObjective2;

    @FXML
    private Text nickname1, nickname2, nickname3, nickname4;

    @FXML
    private ImageView nickname3Visibility, nickname4Visibility;

    @FXML
    private ImageView handCard1, handCard2, handCard3;

    @FXML
    private List<Rectangle> rectangleList = new ArrayList<Rectangle>();
    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane structurePane, chatPane, boardPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField inputText;

    String handCard1URL, handCard2URL, handCard3URL;

    private Player myPlayer;
    private InitialCard initialCardCard;
    private double lastX, lastY; // Memorizza le coordinate dell'ultimo evento
    int rPressed = 0;
    int fPressed = 1;

    ViewFactory viewFactory = new ViewFactory();


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
    void goldCard1Click(MouseEvent event) {
        goldDeckCard.setOpacity(.5);
        goldCard1.setOpacity(1);
        goldCard2.setOpacity(.5);
        resourceDeckCard.setOpacity(.5);
        resourceCard1.setOpacity(.5);
        resourceCard2.setOpacity(.5);
    }

    @FXML
    void goldCard2Click(MouseEvent event) {
        goldDeckCard.setOpacity(.5);
        goldCard1.setOpacity(.5);
        goldCard2.setOpacity(1);
        resourceDeckCard.setOpacity(.5);
        resourceCard1.setOpacity(.5);
        resourceCard2.setOpacity(.5);
    }

    @FXML
    void goldDeckCardClick(MouseEvent event) {
        goldDeckCard.setOpacity(1);
        goldCard1.setOpacity(.5);
        goldCard2.setOpacity(.5);
        resourceDeckCard.setOpacity(.5);
        resourceCard1.setOpacity(.5);
        resourceCard2.setOpacity(.5);
    }

    @FXML
    void resourceCard1Click(MouseEvent event) {
        resourceDeckCard.setOpacity(.5);
        resourceCard1.setOpacity(1);
        resourceCard2.setOpacity(.5);
        goldDeckCard.setOpacity(.5);
        goldCard1.setOpacity(.5);
        goldCard2.setOpacity(.5);
    }

    @FXML
    void resourceCard2Click(MouseEvent event) {
        resourceDeckCard.setOpacity(.5);
        resourceCard1.setOpacity(.5);
        resourceCard2.setOpacity(1);
        goldDeckCard.setOpacity(.5);
        goldCard1.setOpacity(.5);
        goldCard2.setOpacity(.5);
    }

    @FXML
    void resourceDeckCardClick(MouseEvent event) {
        resourceDeckCard.setOpacity(1);
        resourceCard1.setOpacity(.5);
        resourceCard2.setOpacity(.5);
        goldDeckCard.setOpacity(.5);
        goldCard1.setOpacity(.5);
        goldCard2.setOpacity(.5);
    }

    @FXML
    void boardVisibility(MouseEvent event) {
        boardPane.setVisible(true);
        borderPane.setCenter(boardPane);
    }

    @FXML //TO DO: vai a capo se string lunga tot
    void sendMessage(MouseEvent event) {
        String message = inputText.getText();
        if (!Objects.equals(message, "")){
            textArea.appendText("You: " + message + "\n\n");
            inputText.clear();
        }

    }

    @FXML //TO DO: vai a capo se string lunga tot
    void sendMessageByEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER ) {
            String message = inputText.getText();
            if (!Objects.equals(message, "")){
                textArea.appendText("You: " + message + "\n\n");
                inputText.clear();
            }
        }
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


    @Override
    public void run() {
        launch();
    }



    @Override
    public void updateChat(Chat chat) throws IOException {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public void updateMyPlayer(Player player) {
        this.myPlayer = player;
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
        System.out.print("\nCurrent Player updated!");


    }

    @Override
    public void updatePlayers(List<Player> players) {
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
        System.out.print("\nPlayers updated!");

    }

    public void createGhostRectangles(Card card, double x, double y) throws IllegalCommandException {
        int position = 0;
        int width = 111;
        int height = 74;
        for (String s: card.getFrontCorners()){
            //System.out.println("\nCorner corrente: \n" + s);
            if(s != null && position==0){
                Rectangle rectangle = new Rectangle(x-82, y-44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            else if(s != null && position==1){
                Rectangle rectangle = new Rectangle(x+82, y-44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            else if(s != null && position==2){
                Rectangle rectangle = new Rectangle(x+82, y+44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            else if(s != null && position==3){
                Rectangle rectangle = new Rectangle(x-82, y+44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            position++;
    }
}

    //TO DO: metti l'inserimento della initialCard da qui.. e poi leva metodo sotto..
    @Override
    public void updateStructures(List<Structure> structures) {
        int position = 0;
        Structure myStructure = structures.get(0);
        Image image = null;
        Card cardForAngles;

        //TO DO: Capisci se quando la funzione viene creata una seconda volta le carte precedenti vengono eliminate?
        for(Pair<Card, Boolean> card: myStructure.getPlacedCards()){

            if (position == 1){
                int x = myStructure.getCardToCoordinate().get(card.getKey()).getFirst() / 100;
                int y = myStructure.getCardToCoordinate().get(card.getKey()).getFirst() % 100;
                //System.out.println("Carta: " + card.getKey() + "Posizione: " + x + y + "\n");
                if (card.getValue().toString().equals("true")){
                    image = pathFront(card.getKey().toString().substring(6,9));
                }
                else if (card.getValue().toString().equals("false")){
                    image = pathBack(card.getKey().toString().substring(6,9));
                }

                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(111);
                imageView.setFitHeight(74);
                imageView.setLayoutX(945+82*(x-40));
                imageView.setLayoutY(1000-44*(y-40)); //TO DO: vedi qui... -

                //TO DO: Rivedi per creareGhostRectangles negli angoli liberi, ora vanno anche sopra le carte piazzate..NO!
//                try{
//                    createGhostRectangles(card.getKey(), imageView.getLayoutX(), imageView.getLayoutY());}
//                catch (Exception ignored){}

                //System.out.println("posi " + imageView.getLayoutX() + " " +  imageView.getLayoutY() + "\n");
                structurePane.getChildren().add(imageView);
            }
            else
                position++;


        }
//        for(Rectangle rectangle: rectangleList){
//            rectangle.setOnMouseEntered(event -> {
//                rectangle.setFill(Color.web("#808080", 0.8));
//            });
//
//            // Gestore per quando il mouse esce dal rettangolo
//            rectangle.setOnMouseExited(event -> {
//                rectangle.setFill(Color.web("#808080", 0.1));
//            });
//
//        }
    }


    @Override
    public void updateHand(List<Hand> hands) {
        Hand hand = hands.get(0);
        String card1;
        String card2;
        String card3;
        String secretObjectiveCard;

        card1 = hand.getCardsHand().get(0).toString().substring(6,9);
        card2 = hand.getCardsHand().get(1).toString().substring(6,9);
        card3 = hand.getCardsHand().get(2).toString().substring(6,9);
        secretObjectiveCard = hand.getSecretObjective().toString().substring(22,25);

        handCard1.setImage(pathFront(card1));
        handCard2.setImage(pathFront(card2));
        handCard3.setImage(pathFront(card3));
        secreteObjective.setImage(pathFront(secretObjectiveCard));

        handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "f.jpg";
        handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "f.jpg";
        handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "f.jpg";

        System.out.print("\nHand updated!");

    }

    //TO DO: Manca la board effettiva... solo la parte delle obj comuni e' stata trattata
    @Override
    public void updateBoard(Board board) {
        String publicObjectiveOne;
        String publicObjectiveTwo;
        String myPlayerColor = myPlayer.getColor().toString();
        System.out.printf("My player color: " + myPlayerColor);
        ImageView pedina;
        int points = 23;
        //int points = board.getActualPoints(myPlayer);

        publicObjectiveOne = board.getCommonObjectives().get(0).toString().substring(22,25);
        publicObjectiveTwo = board.getCommonObjectives().get(1).toString().substring(22,25);

        publicObjective1.setImage(pathFront(publicObjectiveOne));
        publicObjective2.setImage(pathFront(publicObjectiveTwo));


        switch(points) {
            case 1:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(399);
                pedina.setLayoutY(403);
                setDimension(pedina);
                break;
            case 2:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(449);
                pedina.setLayoutY(403);
                setDimension(pedina);
                break;
            case 3:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(499);
                pedina.setLayoutY(403);
                setDimension(pedina);
                break;
            case 4:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(358);
                setDimension(pedina);
                break;
            case 5:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(474);
                pedina.setLayoutY(358);
                setDimension(pedina);
                break;
            case 6:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(424);
                pedina.setLayoutY(358);
                setDimension(pedina);
                break;
            case 7:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(358);
                setDimension(pedina);
                break;
            case 8:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(313);
                setDimension(pedina);
                break;
            case 9:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(424);
                pedina.setLayoutY(313);
                setDimension(pedina);
                break;
            case 10:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(474);
                pedina.setLayoutY(313);
                setDimension(pedina);
                break;
            case 11:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(313);
                setDimension(pedina);
                break;
            case 12:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(267);
                setDimension(pedina);
                break;
            case 13:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(474);
                pedina.setLayoutY(267);
                setDimension(pedina);
                break;
            case 14:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(424);
                pedina.setLayoutY(267);
                setDimension(pedina);
                break;
            case 15:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(267);
                setDimension(pedina);
                break;
            case 16:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(222);
                setDimension(pedina);
                break;
            case 17:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(424);
                pedina.setLayoutY(222);
                setDimension(pedina);
                break;
            case 18:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(474);
                pedina.setLayoutY(222);
                setDimension(pedina);
                break;
            case 19:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(222);
                setDimension(pedina);
                break;
            case 20:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(176);
                setDimension(pedina);
                break;
            case 21:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(449);
                pedina.setLayoutY(152);
                setDimension(pedina);
                break;
            case 22:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(176);
                setDimension(pedina);
                break;
            case 23:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(129);
                setDimension(pedina);
                break;
            case 24:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(374);
                pedina.setLayoutY(83);
                setDimension(pedina);
                break;
            case 25:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(403);
                pedina.setLayoutY(45);
                setDimension(pedina);
                break;
            case 26:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(449);
                pedina.setLayoutY(38);
                setDimension(pedina);
                break;
            case 27:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(495);
                pedina.setLayoutY(46);
                setDimension(pedina);
                break;
            case 28:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(83);
                setDimension(pedina);
                break;
            case 29:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(524);
                pedina.setLayoutY(129);
                setDimension(pedina);
                break;
            case 30:
                pedina = new ImageView(symbolPath(myPlayerColor));
                pedina.setLayoutX(449);
                pedina.setLayoutY(93);
                setDimension(pedina);
                break;


        }


        System.out.print("\nDeck updated!");
    }

    public void setDimension(ImageView pedina){
        pedina.setFitWidth(40);
        pedina.setFitHeight(40);
        boardPane.getChildren().add(pedina);
    }
    @Override
    public void updateDeck(Deck deck) {
        Stack<GoldCard> goldCardDeck = deck.getGoldDeck();
        Stack<ResourceCard> resourceCardDeck = deck.getResourceDeck();

        String goldDeckCardOne = deck.drawGoldCard().toString().substring(6,9);
        String goldCardOne = deck.drawGoldCard().toString().substring(6,9);
        String goldCardTwo = deck.drawGoldCard().toString().substring(6,9);

        String resourceDeckCardOne = deck.drawResourceCard().toString().substring(6,9);
        String resourceCardOne = deck.drawResourceCard().toString().substring(6,9);
        String resourceCardTwo = deck.drawResourceCard().toString().substring(6,9);

        goldDeckCard.setImage(pathBack(goldDeckCardOne));
        goldCard1.setImage(pathFront(goldCardOne));
        goldCard2.setImage(pathFront(goldCardTwo));

        resourceDeckCard.setImage(pathBack(resourceDeckCardOne));
        resourceCard1.setImage(pathFront(resourceCardOne));
        resourceCard2.setImage(pathFront(resourceCardTwo));
        System.out.print("\nDeck updated!");
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
    void goBackFunct(MouseEvent event) {
        borderPane.setCenter(scrollPane);
    }


    @FXML
    void paneClickReset(MouseEvent event) {
        handCard1.setOpacity(1);
        handCard2.setOpacity(1);
        handCard3.setOpacity(1);

        goldDeckCard.setOpacity(1);
        goldCard1.setOpacity(1);
        goldCard2.setOpacity(1);
        resourceDeckCard.setOpacity(1);
        resourceCard1.setOpacity(1);
        resourceCard2.setOpacity(1);
    }

    @FXML
    void openChatFunct(MouseEvent event) throws IOException {
        chatPane.setVisible(true);
        borderPane.setCenter(chatPane);
    }

    @FXML
    void showBack(KeyEvent event) {
        if(event.getCode() == KeyCode.B && rPressed == 0){

            rPressed = 1;
            if (fPressed == 1)
                fPressed--;

            String card1 =  handCard1URL.substring(43,46);
            String card2 =  handCard2URL.substring(43,46);
            String card3 =  handCard3URL.substring(43,46);

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
            String card3 =  handCard3URL.substring(42,45);

            handCard1.setImage(pathFront(card1));
            handCard2.setImage(pathFront(card2));
            handCard3.setImage(pathFront(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "b.jpg";
        }
    }

    public void setInitialCard(InitialCard initialCard1){
        int x,y;
        int width = 111;
        int height = 74;
        int position = 0; //0=up left, 1=up right, 2=bottom left, 3=bottom right
        this.initialCardCard = initialCard1;

        String imagePath =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + initialCard1.getIdCard() + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        Image initialCardImage = new Image(imageStream);
        ImageView initialCard = new ImageView(initialCardImage);
        initialCard.setFitWidth(111);
        initialCard.setFitHeight(74);
        initialCard.setLayoutX(945);
        initialCard.setLayoutY(1000);
        x = (int) initialCard.getLayoutX();
        y = (int) initialCard.getLayoutY();

        structurePane.getChildren().add(initialCard);

        for (String s: initialCardCard.getFrontCorners()){
            //System.out.println("\nCorner corrente: \n" + s);
            if(s != null && position==0){
                Rectangle rectangle = new Rectangle(x-82, y-44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            else if(s != null && position==1){
                Rectangle rectangle = new Rectangle(x+82, y-44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            else if(s != null && position==2){
                Rectangle rectangle = new Rectangle(x+82, y+44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            else if(s != null && position==3){
                Rectangle rectangle = new Rectangle(x-82, y+44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }

            position++;

        }

        for(Rectangle rectangle: rectangleList){
            rectangle.setOnMouseEntered(event -> {
                rectangle.setFill(Color.web("#808080", 0.8));
            });

            // Gestore per quando il mouse esce dal rettangolo
            rectangle.setOnMouseExited(event -> {
                rectangle.setFill(Color.web("#808080", 0.2));
            });

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

    public Image symbolPath(String oggetto){
        String imagePath =  "/it/polimi/ingsw/codexnaturalis/SymbolsPng/" + oggetto + ".png";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    @Override
    public void start(Stage stage) throws Exception {
        viewFactory.showGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        handCard1.setFocusTraversable(true);
        handCard1.requestFocus();
        TestMain testMain = new TestMain(this);
        testMain.start();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        double hCenter = (structurePane.getPrefWidth() - scrollPane.getViewportBounds().getWidth()) / 2 / structurePane.getPrefWidth();
        double vCenter = (structurePane.getPrefHeight() - scrollPane.getViewportBounds().getHeight()) / 2 / structurePane.getPrefHeight();
        scrollPane.setHvalue(hCenter);
        scrollPane.setVvalue(vCenter);

        structurePane.setOnMousePressed(event -> {
            lastX = event.getX();
            lastY = event.getY();
        });

        structurePane.setOnMouseDragged(event -> {
            double deltaX = event.getX() - lastX;
            double deltaY = event.getY() - lastY;

            // Aggiorna la posizione dello scroll
            scrollPane.setHvalue(scrollPane.getHvalue() - deltaX / structurePane.getWidth());
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / structurePane.getHeight());
        });


        scrollPane.setOnMouseReleased(event -> {
            //focus per funzione di back
            handCard1.setFocusTraversable(true);
            handCard1.requestFocus();
        });


    }
}
