package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

/*TO DO (GENERAL):
-Gestisci i ritardi dei giocatori (x non gioca da 10min... che si fa...)
-Gestisci flusso con GameInizialize, ecc
-
-
*/
public class Game extends Application implements View, Initializable {

    // Utils FXML
    @FXML
    private Text mushroomsPoints, leafPoints, wolfPoints, butterflyPoints, featherPoints, manuscriptPoints, potionPoints;

    @FXML
    private Text connectionType;

    @FXML
    private ImageView goldDeckCard, goldCard1, goldCard2, resourceDeckCard, resourceCard1, resourceCard2,
            secreteObjective, publicObjective1, publicObjective2;

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
    private Pane structurePane,otherStructuresPane, chatPane, boardPane;

    @FXML
    private ScrollPane scrollPane, scrollPaneOthers;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField inputText;

    @FXML
    private HBox handCard;

    String handCard1URL, handCard2URL, handCard3URL;
    Card currentHandCard1, currentHandCard2, currentHandCard3, currentDeckGold1Card, currentDeckGold2Card, currentDeckGoldCard, currentDeckResource1Card, currentDeckResource2Card, currentDeckResourceCard;
    private ImageView structureCardSelected;

    private Player myPlayer;
    private TestMain testMain;
    private InitialCard initialCardCard;
    private double lastX, lastY; // Memorizza le coordinate dell'ultimo evento
    int rPressed = 0;
    int fPressed = 1;

    List<ImageView> cardPlaced = new ArrayList<>();

    ViewFactory viewFactory = new ViewFactory();

    //Gestione evento di placeCard

    Card  currentSelected = null, currentSelectedDeck;
    String currentAngle;
    boolean currentSelectedFrontUp = true;
    private ImageView currentSelectedImage;
    boolean isCardPlaced = false, cardDrawn = true, isInitialSetupHand = true, isInitialSetupDeck = true, isHandCardSelected;


    @FXML
    void handCard1Clicked(MouseEvent event) {

        if(!isCardPlaced){
            handCard1.setOpacity(1);
            handCard2.setOpacity(.5);
            handCard3.setOpacity(.5);

            currentSelected = currentHandCard1;
            currentSelectedImage = handCard1;
            isHandCardSelected = true;
            System.out.printf("\nHai selezionato questa carta dalla hand: " + currentSelected);
        }


    }

    @FXML
    void handCard2Clicked(MouseEvent event) {

        if(!isCardPlaced){
            handCard1.setOpacity(.5);
            handCard2.setOpacity(1);
            handCard3.setOpacity(.5);

            currentSelected = currentHandCard2;
            currentSelectedImage = handCard2;
            isHandCardSelected = true;
            System.out.printf("\nHai selezionato questa carta dalla hand: " + currentSelected);
        }



    }

    @FXML
    void handCard3Clicked(MouseEvent event) {
        if(!isCardPlaced){
            handCard1.setOpacity(.5);
            handCard2.setOpacity(.5);
            handCard3.setOpacity(1);

            currentSelected = currentHandCard3;
            currentSelectedImage = handCard3;
            isHandCardSelected = true;
            System.out.printf("\nHai selezionato questa carta dalla hand: " + currentSelected);
        }




    }

    @FXML
    void goldCard1Click(MouseEvent event) throws IllegalCommandException {
        if(!cardDrawn){
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(1);
            goldCard2.setOpacity(.5);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            currentSelectedImage.setImage(goldCard1.getImage());
            currentSelectedImage.setVisible(true);
            isHandCardSelected = false;

            System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckGold1Card);
            currentSelectedDeck = currentDeckGold1Card;
            //System.out.printf("Il quale current e': \n" + currentDeckGold1Card);
            testMain.drawCommand(currentDeckGold1Card);
            cardDrawn = true;
            isCardPlaced = false;
        }


    }

    @FXML
    void goldCard2Click(MouseEvent event) throws IllegalCommandException {
        if (!cardDrawn){
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(1);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            currentSelectedImage.setImage(goldCard2.getImage());
            currentSelectedImage.setVisible(true);
            isHandCardSelected = false;

            System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckGold2Card);
            currentSelectedDeck = currentDeckGold2Card;
            //System.out.printf("Il quale current e': \n" + currentDeckGold1Card);
            testMain.drawCommand(currentDeckGold2Card);
            cardDrawn = true;
            isCardPlaced = false;
        }


    }

    @FXML
    void goldDeckCardClick(MouseEvent event) throws IllegalCommandException {

        if (!cardDrawn){
            goldDeckCard.setOpacity(1);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            currentSelectedImage.setImage(goldDeckCard.getImage());
            currentSelectedImage.setVisible(true);
            isHandCardSelected = false;

            System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckGoldCard);
            currentSelectedDeck = currentDeckGoldCard;
            //System.out.printf("Il quale current e': \n" + currentDeckGold1Card);
            testMain.drawCommand(currentDeckGoldCard);
            cardDrawn = true;
            isCardPlaced = false;
        }

    }

    @FXML
    void resourceCard1Click(MouseEvent event) throws IllegalCommandException {

        if(!cardDrawn){
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(1);
            resourceCard2.setOpacity(.5);
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);

            currentSelectedImage.setImage(resourceCard1.getImage());
            currentSelectedImage.setVisible(true);
            isHandCardSelected = false;

            System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckResource1Card);
            currentSelectedDeck = currentDeckResource1Card;
            testMain.drawCommand(currentDeckResource1Card);
            cardDrawn = true;
            isCardPlaced = false;
        }

    }

    @FXML
    void resourceCard2Click(MouseEvent event) throws IllegalCommandException {

        if(!cardDrawn){
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(1);
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);

            currentSelectedImage.setImage(resourceCard2.getImage());
            currentSelectedImage.setVisible(true);
            isHandCardSelected = false;

            System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckResource2Card);
            currentSelectedDeck = currentDeckResource2Card;
            testMain.drawCommand(currentDeckResource2Card);
            cardDrawn = true;
            isCardPlaced = false;
        }

    }

    @FXML
    void resourceDeckCardClick(MouseEvent event) throws IllegalCommandException {

        if(!cardDrawn){
            resourceDeckCard.setOpacity(1);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);

            currentSelectedImage.setImage(resourceDeckCard.getImage());
            currentSelectedImage.setVisible(true);
            isHandCardSelected = false;

            System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckResourceCard);
            currentSelectedDeck = currentDeckResourceCard;
            testMain.drawCommand(currentDeckResourceCard);
            cardDrawn = true;
            isCardPlaced = false;
        }

    }

    @FXML
    void boardVisibility(MouseEvent event) {
        boardPane.setVisible(true);
        borderPane.setCenter(boardPane);
    }

    @FXML // TO DO: vai a capo se string lunga tot
    void sendMessage(MouseEvent event) {
        String message = inputText.getText();
        if (!Objects.equals(message, "")) {
            textArea.appendText("You: " + message + "\n\n");
            inputText.clear();
        }

    }

    @FXML // TO DO: vai a capo se string lunga tot
    void sendMessageByEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String message = inputText.getText();
            if (!Objects.equals(message, "")) {
                textArea.appendText("You: " + message + "\n\n");
                inputText.clear();
            }
        }
    }

    @FXML
    void nickname2VisibilityFunct(MouseEvent event) {
//        otherStructuresPane.setVisible(true);
//        borderPane.setCenter(scrollPaneOthers);

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
    public void updateChat(Chat chat)      {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public void updateMyPlayer(Player player) {
        this.myPlayer = player;
    }

    @Override
    public void updateWinners(List<Player> winners) {

    }

    @Override
    public void updateCurrentPlayer(Player player) {
        if (Objects.equals(nickname1.getText(), player.getNickname()))
            nickname1.setFill(Color.BLUE);
        else if (Objects.equals(nickname2.getText(), player.getNickname()))
            nickname2.setFill(Color.BLUE);
        else if (Objects.equals(nickname3.getText(), player.getNickname()))
            nickname3.setFill(Color.BLUE);
        else if (Objects.equals(nickname4.getText(), player.getNickname()))
            nickname4.setFill(Color.BLUE);
        else {
            System.out.println("Player not matching" + player.getNickname()); // Metti un alert in caso...
        }
        //System.out.print("\nCurrent Player updated!");

    }

    @Override
    public void updatePlayers(List<Player> players) {
        nickname1.setText(players.get(0).getNickname());
        nickname2.setText(players.get(1).getNickname());

        if (players.size() == 3) {
            nickname3.setText(players.get(2).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
        }
        if (players.size() == 4) {
            nickname3.setText(players.get(2).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
            nickname4.setText(players.get(3).getNickname());
            nickname4.setVisible(true);
            nickname4Visibility.setVisible(true);
        }
        //System.out.print("\nPlayers updated!");

    }

    public void createGhostRectangles(Card card, double x, double y) throws IllegalCommandException {
        int position = 0;
        int width = 111;
        int height = 74;
        for (String s : card.getFrontCorners()) {
            // System.out.println("\nCorner corrente: \n" + s);
            if (s != null && position == 0) {
                Rectangle rectangle = new Rectangle(x - 82, y - 44, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            } else if (s != null && position == 1) {
                Rectangle rectangle = new Rectangle(x + 82, y - 44, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            } else if (s != null && position == 2) {
                Rectangle rectangle = new Rectangle(x + 82, y + 44, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            } else if (s != null && position == 3) {
                Rectangle rectangle = new Rectangle(x - 82, y + 44, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                structurePane.getChildren().add(rectangle);
            }
            position++;
        }
    }

    // TO DO: metti l'inserimento della initialCard da qui.. e poi leva metodo
    // sotto..
    @Override
    public void updateStructures(List<Structure> structures) {
        int position = 0;
        int position1= 0;
        int points;
        Structure myStructure = structures.getFirst();

        Image image = null;
        Card cardForAngles;

//        System.out.println("\n\nVisible Resources: \n\n" + myStructure.getVisibleObjects());
//        System.out.println("\n\nVisible Resources: \n\n" + myStructure.getVisibleObjects().substring(26,27));

        //TO DO: si arrivera' mai a >9?

        //Aggiungo punti in base alle risorse
        points = Integer.parseInt(myStructure.getVisibleResources().substring(11,12));
        addPoints("VEGETABLE", points);

        points = Integer.parseInt(myStructure.getVisibleResources().substring(21,22));
        addPoints("ANIMAL", points);

        points = Integer.parseInt(myStructure.getVisibleResources().substring(31,32));
        addPoints("INSECT", points);

        points = Integer.parseInt(myStructure.getVisibleResources().substring(41,42));
        addPoints("SHROOM", points);

        points = Integer.parseInt(myStructure.getVisibleObjects().substring(9,10));
        addPoints("FEATHER", points);

        points = Integer.parseInt(myStructure.getVisibleObjects().substring(16,17));
        addPoints("INK", points);

        points = Integer.parseInt(myStructure.getVisibleObjects().substring(26,27));
        addPoints("SCROLL", points);







        //TO DO: Capisci se quando la funzione viene creata una seconda volta le carte precedenti vengono eliminate?
        for(Pair<Card, Boolean> card: myStructure.getPlacedCards()){

            if (position == 1) {
                int x = myStructure.getCardToCoordinate().get(card.getKey()).getFirst() / 100;
                int y = myStructure.getCardToCoordinate().get(card.getKey()).getFirst() % 100;
                // System.out.println("Carta: " + card.getKey() + "Posizione: " + x + y + "\n");
                if (card.getValue().toString().equals("true")) {
                    image = pathFront(card.getKey().toString().substring(6, 9));
                } else if (card.getValue().toString().equals("false")) {
                    image = pathBack(card.getKey().toString().substring(6, 9));
                }

                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(111);
                imageView.setFitHeight(74);
                imageView.setLayoutX(945+82*(x-40));
                imageView.setLayoutY(1000-44*(y-40));

                //Inizio
                imageView.setOnMouseClicked(event -> {

                    if (!isCardPlaced && isHandCardSelected){
                        double currentAngleSelectedX = event.getX();
                        double currentAngleSelectedY = event.getY();

                        if (currentAngleSelectedX <= 29){
                            if(currentAngleSelectedY <=30)
                                currentAngle = "TL";
                            else if(currentAngleSelectedY >=44)
                                currentAngle = "BL";
                        } else if (currentAngleSelectedX >=82) {
                            if(currentAngleSelectedY <=30)
                                currentAngle = "TR";
                            else if(currentAngleSelectedY >=44)
                                currentAngle = "BR";
                        }

                        try {
                            testMain.placeCommand(card.getKey(), currentSelected, currentAngle, currentSelectedFrontUp);

                            currentSelectedImage.setVisible(false);


                            isCardPlaced = true;
                            cardDrawn = false;


                        } catch (IllegalCommandException e) {
                            showAlert("Sei sicuro che la carta vada li'? Ritenta"); //TO DO: cambia
                            throw new RuntimeException(e);
                        }

                    }



                });
                //Fine

                //System.out.println("posi " + imageView.getLayoutX() + " " +  imageView.getLayoutY() + "\n");
                structurePane.getChildren().add(imageView);
            } else
                position++;

            handCard.setFocusTraversable(true);
            handCard.requestFocus();


        }


        //TO DO: sistema
//        for(Pair<Card, Boolean> card: hisStructure.getPlacedCards()){
//            System.out.printf("\nCard:" + card.getKey() + "\n");
//            if (position1 == 1){
//                int x1 = hisStructure.getCardToCoordinate().get(card.getKey()).getFirst() / 100;
//                int y1 = hisStructure.getCardToCoordinate().get(card.getKey()).getFirst() % 100;
//                //System.out.println("Carta: " + card.getKey() + "Posizione: " + x + y + "\n");
//                if (card.getValue().toString().equals("true")){
//                    image = pathFront(card.getKey().toString().substring(6,9));
//                }
//                else if (card.getValue().toString().equals("false")){
//                    image = pathBack(card.getKey().toString().substring(6,9));
//                }
//
//                ImageView imageView = new ImageView(image);
//
//                imageView.setFitWidth(111);
//                imageView.setFitHeight(74);
//                imageView.setLayoutX(945+82*(x1-40));
//                imageView.setLayoutY(1000-44*(y1-40)); //TO DO: vedi qui... -

                //TO DO: Rivedi per creareGhostRectangles negli angoli liberi, ora vanno anche sopra le carte piazzate..NO!
//                try{
//                    createGhostRectangles(card.getKey(), imageView.getLayoutX(), imageView.getLayoutY());}
//                catch (Exception ignored){}

                //System.out.println("posi " + imageView.getLayoutX() + " " +  imageView.getLayoutY() + "\n");
//                otherStructuresPane.getChildren().add(imageView);
            }
//            else{
//                position1++;
//                image = pathFront(initialCardCard.getIdCard());
//                ImageView imageView = new ImageView(image);
//                imageView.setFitWidth(111);
//                imageView.setFitHeight(74);
//                imageView.setLayoutX(945);
//                imageView.setLayoutY(1000);
//                otherStructuresPane.getChildren().add(imageView);
//            }



//        }
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
// }

    @Override
    public void updateHand(List<Hand> hands) {
        Hand hand = hands.get(0);
        if(isInitialSetupHand){
            String card1;
            String card2;
            String card3;
            String secretObjectiveCard;

            currentHandCard1 = hand.getCardsHand().get(0);
            currentHandCard2 = hand.getCardsHand().get(1);
            currentHandCard3 = hand.getCardsHand().get(2);

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


            isInitialSetupHand = false;
        }
        else{
            if(currentSelected == currentHandCard1){
                currentHandCard1 = hand.getCardsHand().get(0);
                if(currentSelectedFrontUp)
                    handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + currentSelectedDeck.toString().substring(6,9) + "f.jpg";
                else if(!currentSelectedFrontUp)
                    handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + currentSelectedDeck.toString().substring(6,9) + "b.jpg";
            }

            else if(currentSelected == currentHandCard2){
                currentHandCard2 = hand.getCardsHand().get(1);
                if(currentSelectedFrontUp)
                    handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + currentSelectedDeck.toString().substring(6,9) + "f.jpg";
                else if(!currentSelectedFrontUp)
                    handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + currentSelectedDeck.toString().substring(6,9) + "b.jpg";

            }

            else if(currentSelected == currentHandCard3){
                currentHandCard3 = hand.getCardsHand().get(2);
                if(currentSelectedFrontUp)
                    handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + currentSelectedDeck.toString().substring(6,9) + "f.jpg";
                else if(!currentSelectedFrontUp)
                    handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + currentSelectedDeck.toString().substring(6,9) + "b.jpg";

            }
            System.out.println("\nUpdate card1: " + handCard1URL);
            System.out.println("\nUpdate card2: " + handCard2URL);
            System.out.println("\nUpdate card3: " + handCard3URL);
            currentSelected = null;
        }
        System.out.printf("\nHand: ");
        for (Card card: hand.getCardsHand())
            System.out.printf(card.toString().substring(6,9) + "-");

        handCard.setFocusTraversable(true);
        handCard.requestFocus();
        //System.out.print("\nHand updated!");

    }

    // TO DO: Manca la board effettiva... solo la parte delle obj comuni e' stata
    // trattata
    @Override
    public void updateBoard(Board board) {
        String publicObjectiveOne;
        String publicObjectiveTwo;
        int points;
        String myPlayerColor = myPlayer.getColor().toString();
        //System.out.printf("My player color: " + myPlayerColor);
        ImageView pedina;
        if(board.getActualPoints(myPlayer) == null)
            points = 1;
        else
            points = board.getActualPoints(myPlayer);
        //int points = board.getActualPoints(myPlayer);

        publicObjectiveOne = board.getCommonObjectives().get(0).toString().substring(22, 25);
        publicObjectiveTwo = board.getCommonObjectives().get(1).toString().substring(22, 25);

        publicObjective1.setImage(pathFront(publicObjectiveOne));
        publicObjective2.setImage(pathFront(publicObjectiveTwo));

        switch (points) {
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

        handCard.setFocusTraversable(true);
        handCard.requestFocus();
        //System.out.print("\nDeck updated!");
    }

    public void setDimension(ImageView pedina) {
        pedina.setFitWidth(40);
        pedina.setFitHeight(40);
        boardPane.getChildren().add(pedina);
    }

    @Override
    public void updateDeck(Deck deck) {

        if(isInitialSetupDeck){
            Stack<GoldCard> goldCardDeck = deck.getGoldDeck();
            Stack<ResourceCard> resourceCardDeck = deck.getResourceDeck();

            currentDeckGold1Card = deck.getGoldDeck().get(deck.getGoldDeck().size()-1);
            currentDeckGold2Card = deck.getGoldDeck().get(deck.getGoldDeck().size()-2);
            currentDeckGoldCard = deck.getGoldDeck().get(deck.getGoldDeck().size()-3);

            currentDeckResource1Card = deck.getResourceDeck().get(deck.getResourceDeck().size()-1);
            currentDeckResource2Card = deck.getResourceDeck().get(deck.getResourceDeck().size()-2);
            currentDeckResourceCard = deck.getResourceDeck().get(deck.getResourceDeck().size()-3);


            String goldDeckCardOne = currentDeckGoldCard.toString().substring(6,9);
            String goldCardOne = currentDeckGold1Card.toString().substring(6,9);
            String goldCardTwo = currentDeckGold2Card.toString().substring(6,9);

            String resourceDeckCardOne = currentDeckResourceCard.toString().substring(6,9);
            String resourceCardOne = currentDeckResource1Card.toString().substring(6,9);
            String resourceCardTwo = currentDeckResource2Card.toString().substring(6,9);

            goldDeckCard.setImage(pathBack(goldDeckCardOne));
            goldCard1.setImage(pathFront(goldCardOne));
            goldCard2.setImage(pathFront(goldCardTwo));

            resourceDeckCard.setImage(pathBack(resourceDeckCardOne));
            resourceCard1.setImage(pathFront(resourceCardOne));
            resourceCard2.setImage(pathFront(resourceCardTwo));
            isInitialSetupDeck = false;
        }
        else{

            if(currentSelectedDeck == currentDeckResourceCard){
                currentDeckResourceCard = deck.getResourceDeck().get(deck.getResourceDeck().size()-3);
                String changeImage = currentDeckResourceCard.toString().substring(6,9);
                resourceDeckCard.setImage(pathBack(changeImage));
            }

            else if(currentSelectedDeck == currentDeckResource1Card){
                currentDeckResource1Card = deck.getResourceDeck().get(deck.getResourceDeck().size()-3);
                String changeImage = currentDeckResource1Card.toString().substring(6,9);
                resourceCard1.setImage(pathFront(changeImage));
            }

            else if(currentSelectedDeck == currentDeckResource2Card){
                currentDeckResource2Card = deck.getResourceDeck().get(deck.getResourceDeck().size()-3);
                String changeImage = currentDeckResource2Card.toString().substring(6,9);
                resourceCard2.setImage(pathFront(changeImage));
            }

            else if(currentSelectedDeck == currentDeckGoldCard){
                currentDeckGoldCard = deck.getGoldDeck().get(deck.getGoldDeck().size()-3);
                String changeImage = currentDeckGoldCard.toString().substring(6,9);
                goldDeckCard.setImage(pathBack(changeImage));
            }

            else if(currentSelectedDeck == currentDeckGold1Card){
                currentDeckGold1Card = deck.getGoldDeck().get(deck.getGoldDeck().size()-3);
                String changeImage = currentDeckGold1Card.toString().substring(6,9);
                goldCard1.setImage(pathFront(changeImage));
            }

            else if(currentSelectedDeck == currentDeckGold2Card){
                currentDeckGold2Card = deck.getGoldDeck().get(deck.getGoldDeck().size()-3);
                String changeImage = currentDeckGold2Card.toString().substring(6,9);
                goldCard2.setImage(pathFront(changeImage));
            }

        }

        //.print("\nDeck updated!");

        System.out.print("\n\nResource Deck:");
        for(Card card: deck.getResourceDeck())
            System.out.printf(card.toString().substring(6,9) + "-");
        System.out.print("\nGold Deck: ");
        for(Card card: deck.getGoldDeck())
            System.out.printf(card.toString().substring(6,9) + "-");
    }

    @Override
    public void updateError(String error) {

    }

    public void showAlert(String message) {
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
        if (event.getCode() == KeyCode.B && rPressed == 0) {

            if (currentSelected != null)
                currentSelectedFrontUp = false;
            rPressed = 1;
            if (fPressed == 1)
                fPressed--;

            String card1 = handCard1URL.substring(43, 46);
            String card2 = handCard2URL.substring(43, 46);
            String card3 = handCard3URL.substring(43, 46);

            handCard1.setImage(pathBack(card1));
            handCard2.setImage(pathBack(card2));
            handCard3.setImage(pathBack(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card3 + "b.jpg";

            System.out.println("\nFront1: " + handCard1URL);
            System.out.println("\nFront2: " + handCard2URL);
            System.out.println("\nFront3: " + handCard3URL);

        }
        else if(event.getCode() == KeyCode.F && fPressed == 0){

            if (currentSelected != null)
                currentSelectedFrontUp = true;

            fPressed = 1;
            if (rPressed == 1)
                rPressed--;

            String card1 = handCard1URL.substring(42, 45);
            String card2 = handCard2URL.substring(42, 45);
            String card3 = handCard3URL.substring(42, 45);

            handCard1.setImage(pathFront(card1));
            handCard2.setImage(pathFront(card2));
            handCard3.setImage(pathFront(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "b.jpg";

            System.out.println("\nBack: " + handCard1URL);
            System.out.println("\nBack: " + handCard2URL);
            System.out.println("\nBack: " + handCard3URL);
        }
    }

    public void setInitialCard(InitialCard initialCard1){
        int x,y;
        int width = 29;
        int height = 30;
        int position = 0; //0=up left, 1=up right, 2=bottom left, 3=bottom right
        this.initialCardCard = initialCard1;

        String imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + initialCard1.getIdCard() + "f.jpg";
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

        initialCard.setOnMouseClicked(event -> {

            if (isCardPlaced == false ){
                double currentAngleSelectedX = event.getX();
                double currentAngleSelectedY = event.getY();

                if (currentAngleSelectedX <= 29){
                    if(currentAngleSelectedY <=30)
                        currentAngle = "TL";
                    else if(currentAngleSelectedY >=44)
                        currentAngle = "BL";
                } else if (currentAngleSelectedX >=82) {
                    if(currentAngleSelectedY <=30)
                        currentAngle = "TR";
                    else if(currentAngleSelectedY >=44)
                        currentAngle = "BR";
                }

                try {
                    //.println("1: \n" + initialCard1 + "2: \n" + currentSelected + " 3: \n" + currentAngle + "4: \n" + currentSelectedFrontUp);
                    testMain.placeCommand(initialCard1, currentSelected, currentAngle, currentSelectedFrontUp);


                    currentSelectedImage.setVisible(false);


                    isCardPlaced = true;
                    cardDrawn = false;


                } catch (IllegalCommandException e) {
                    showAlert("Sei sicuro che la carta vada li'? Ritenta");
                    throw new RuntimeException(e);
                }

            }






        });

        cardPlaced.add(initialCard);
        structurePane.getChildren().add(initialCard);

        for (String s: initialCardCard.getFrontCenterResources()){
            addPoints(s);
        }

        for (String s: initialCardCard.getFrontCorners()){
            //System.out.println("\nCorner corrente: \n" + s);
            if(!Objects.equals(s, "NULL") && position==0){ //TOP LEFT
                Rectangle rectangle = new Rectangle(x, y,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                //structurePane.getChildren().add(rectangle);
            }
            else if(!Objects.equals(s, "NULL") && position==1){ //TOP RIGHT
                Rectangle rectangle = new Rectangle(x+82, y,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                //structurePane.getChildren().add(rectangle);
            }
            else if(!Objects.equals(s, "NULL") && position==2){ //BOTTOM RIGHT
                Rectangle rectangle = new Rectangle(x+82, y+44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                //structurePane.getChildren().add(rectangle);
            }
            else if(!Objects.equals(s, "NULL") && position==3){ //BOTTOM LEFT
                Rectangle rectangle = new Rectangle(x, y+44,width,height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
                //structurePane.getChildren().add(rectangle);
            }

            position++;

        }

        for (Rectangle rectangle : rectangleList) {
            rectangle.setOnMouseEntered(event -> {
                rectangle.setFill(Color.web("#808080", 0.8));
            });

            // Gestore per quando il mouse esce dal rettangolo
            rectangle.setOnMouseExited(event -> {
                rectangle.setFill(Color.web("#808080", 0.2));
            });
            rectangle.setOnMouseClicked(event -> {


            });

        }

    }

    //mushroomsPoints, leafPoints, wolfPoints, butterflyPoints, featherPoints, manuscriptPoints, potionPoints
    public void addPoints(String obj){
        int valueInteger = 0;
        String valueString;
        switch (obj){
            case "SHROOM":
                valueInteger = Integer.parseInt(mushroomsPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                mushroomsPoints.setText(valueString);
                break;
            case "VEGETABLE":
                valueInteger = Integer.parseInt(leafPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                leafPoints.setText(valueString);
                break;
            case "ANIMAL":
                valueInteger = Integer.parseInt(wolfPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                wolfPoints.setText(valueString);
                break;
            case "INSECT":
                valueInteger = Integer.parseInt(butterflyPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                butterflyPoints.setText(valueString);
                break;
            case "FEATHER":
                valueInteger = Integer.parseInt(featherPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                featherPoints.setText(valueString);
                break;
            case "SCROLL":
                valueInteger = Integer.parseInt(manuscriptPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                manuscriptPoints.setText(valueString);
                break;
            case "INK":
                valueInteger = Integer.parseInt(potionPoints.getText());
                valueInteger++;
                valueString = String.valueOf(valueInteger);
                potionPoints.setText(valueString);
                break;


        }
    }
    public void addPoints(String obj, int points){
        int valueInteger = 0;
        String valueString;
        switch (obj){
            case "SHROOM":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                mushroomsPoints.setText(valueString);
                break;
            case "VEGETABLE":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                leafPoints.setText(valueString);
                break;
            case "ANIMAL":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                wolfPoints.setText(valueString);
                break;
            case "INSECT":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                butterflyPoints.setText(valueString);
                break;
            case "FEATHER":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                featherPoints.setText(valueString);
                break;
            case "SCROLL":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                manuscriptPoints.setText(valueString);
                break;
            case "INK":
                valueInteger = points;
                valueString = String.valueOf(valueInteger);
                potionPoints.setText(valueString);
                break;


        }
    }

    public void setConnectionType(String tipoDiConnessione){
        connectionType.setText(tipoDiConnessione);
    }

    public Image pathFront(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + oggetto + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    public Image pathBack(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/BackCards/" + oggetto + "b.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    public Image symbolPath(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/SymbolsPng/" + oggetto + ".png";
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

        mushroomsPoints.setText("0");
        leafPoints.setText("0");
        wolfPoints.setText("0");
        butterflyPoints.setText("0");
        featherPoints.setText("0");
        manuscriptPoints.setText("0");
        potionPoints.setText("0");

//        resourceDeckCard.setDisable(true);
//        resourceCard1.setDisable(true);
//        resourceCard2.setDisable(true);
//        goldDeckCard.setDisable(true);
//        goldCard1.setDisable(true);
//        goldCard2.setDisable(true);



        handCard.setFocusTraversable(true);
        handCard.requestFocus();
        TestMain testMain = new TestMain(this);
        this.testMain = testMain;
        testMain.start();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        double hCenter = (structurePane.getPrefWidth() - scrollPane.getViewportBounds().getWidth()) / 2
                / structurePane.getPrefWidth();
        double vCenter = (structurePane.getPrefHeight() - scrollPane.getViewportBounds().getHeight()) / 2
                / structurePane.getPrefHeight();
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
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        });

    }
}
