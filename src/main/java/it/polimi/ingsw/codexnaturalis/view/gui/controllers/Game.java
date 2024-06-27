package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

/**
 * Game is the main GUI class. It manages and displays all the graphic content used while playing.
 */
public class Game implements Initializable {

    @FXML
    private Text mushroomsPoints, leafPoints, wolfPoints, butterflyPoints, featherPoints, manuscriptPoints, potionPoints;
    @FXML
    private ImageView goldDeckCard, goldCard1, goldCard2, resourceDeckCard, resourceCard1, resourceCard2, secreteObjective, publicObjective1, publicObjective2, chatNotification;
    @FXML
    private Text nickname1 , myStructure, nickname2, nickname3, nickname4;
    @FXML
    private ImageView nickname1Visibility, nickname2Visibility, nickname3Visibility, nickname4Visibility;
    @FXML
    private ImageView handCard1, handCard2, handCard3, showHand1, showHand2, showHand3;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane structurePane, otherStructuresPane, chatPane, boardPane, otherStructuresPane1, otherStructuresPane2;
    @FXML
    private ScrollPane scrollPane, scrollPaneOthers, scrollPaneOthers1, scrollPaneOthers2;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField inputText;
    @FXML
    private ChoiceBox<String> ChooseSender;
    @FXML
    private HBox handCard;
    @FXML
    private Text player1Points, player2Points, player3Points, player4Points, myPoints;

    private Command currentCommand;
    private Image currentImageNew;
    private String handCard1URL;
    private String handCard2URL;
    private String handCard3URL;
    private String currentAngle;
    private Card currentHandCard1, currentHandCard2, currentHandCard3, currentDeckGold1Card, currentDeckGold2Card, currentDeckGoldCard, currentDeckResource1Card, currentDeckResource2Card, currentDeckResourceCard, currentSelected = null, currentSelectedDeck;
    private Player myPlayer;
    private double lastX, lastY;
    private int rPressed = 0;
    private int fPressed = 1;
    private MiniModel miniModel;
    private VirtualClient virtualClient;
    private ImageView currentSelectedImage, pPawn, pPawn1, pPawn2, pPawn3;
    private boolean isCardPlaced = false, cardDrawn = true, isInitialSetupHand = true, isInitialChooseSetup = true, isHandCardSelected, currentSelectedFrontUp = true, initialCardSide, inChat = false;
    private int c = 0;
    private int handTurnCounter = 1;


    public void setUP(MiniModel miniModel, VirtualClient virtualClient) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
    }

    /**
     * When the first card of the hand is clicked, it is highlighted and its values (image value and card value) are saved.
     * @param event
     */
    @FXML
    void handCard1Clicked(MouseEvent event) {
        if (!isCardPlaced) {
            handCard1.setOpacity(1);
            handCard2.setOpacity(.5);
            handCard3.setOpacity(.5);

            currentSelected = currentHandCard1;
            currentSelectedImage = handCard1;
            isHandCardSelected = true;
        }
    }

    /**
     * When the second card of the hand is clicked, it is highlighted and its values (image value and card value) are saved.
     * @param event
     */
    @FXML
    void handCard2Clicked(MouseEvent event) {
        if (!isCardPlaced) {
            handCard1.setOpacity(.5);
            handCard2.setOpacity(1);
            handCard3.setOpacity(.5);

            currentSelected = currentHandCard2;
            currentSelectedImage = handCard2;
            isHandCardSelected = true;
        }
    }

    /**
     * When the third card of the hand is clicked, it is highlighted and its values (image value and card value) are saved.
     * @param event
     */
    @FXML
    void handCard3Clicked(MouseEvent event) {
        if (!isCardPlaced) {
            handCard1.setOpacity(.5);
            handCard2.setOpacity(.5);
            handCard3.setOpacity(1);

            currentSelected = currentHandCard3;
            currentSelectedImage = handCard3;
            isHandCardSelected = true;
        }
    }

    /**
     * When the first gold card is clicked, it is highlighted and its values (image value and card value) are saved.
     * A DrawCommand method is sent.
     * @param event
     * @throws IllegalCommandException
     * @throws RemoteException
     */
    @FXML
    void goldCard1Click(MouseEvent event) throws IllegalCommandException, RemoteException {
        if (!cardDrawn) {
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(1);
            goldCard2.setOpacity(.5);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            isHandCardSelected = false;
            currentSelectedDeck = currentDeckGold1Card;
            currentImageNew = goldCard1.getImage();
            currentCommand = new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckGold1Card, "");
            virtualClient.sendCommand(currentCommand);
        }

    }

    /**
     * When the second gold card is clicked, it is highlighted and its values (image value and card value) are saved.
     * A DrawCommand method is sent.
     * @param event
     * @throws IllegalCommandException
     * @throws RemoteException
     */
    @FXML
    void goldCard2Click(MouseEvent event) throws IllegalCommandException, RemoteException {
        if (!cardDrawn) {
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(1);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            isHandCardSelected = false;
            currentSelectedDeck = currentDeckGold2Card;
            currentImageNew = goldCard2.getImage();
            currentCommand = new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckGold2Card, "");
            virtualClient.sendCommand(currentCommand);
        }

    }

    /**
     * When the gold deck card is clicked, it is highlighted and its values (image value and card value) are saved.
     * A DrawCommand method is sent.
     * @param event
     * @throws IllegalCommandException
     * @throws RemoteException
     */
    @FXML
    void goldDeckCardClick(MouseEvent event) throws IllegalCommandException, RemoteException {

        if (!cardDrawn) {
            goldDeckCard.setOpacity(1);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            isHandCardSelected = false;
            currentSelectedDeck = currentDeckGoldCard;
            currentImageNew = pathFront(currentDeckGoldCard.getIdCard());
            currentCommand = new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckGoldCard, "GOLD");
            virtualClient.sendCommand(currentCommand);
        }

    }

    /**
     * When the first resource card is clicked, it is highlighted and its values (image value and card value) are saved.
     * A DrawCommand method is sent.
     * @param event
     * @throws IllegalCommandException
     * @throws RemoteException
     */
    @FXML
    void resourceCard1Click(MouseEvent event) throws IllegalCommandException, RemoteException {
            if (!cardDrawn) {
                resourceDeckCard.setOpacity(.5);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(.5);
                goldDeckCard.setOpacity(.5);
                goldCard1.setOpacity(.5);
                goldCard2.setOpacity(.5);

                isHandCardSelected = false;
                currentSelectedDeck = currentDeckResource1Card;
                currentImageNew = resourceCard1.getImage();
                currentCommand = new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckResource1Card, "");
                virtualClient.sendCommand(currentCommand);
            }

    }

    /**
     * When the second resource card is clicked, it is highlighted and its values (image value and card value) are saved.
     * A DrawCommand method is sent.
     * @param event
     * @throws IllegalCommandException
     * @throws RemoteException
     */
    @FXML
    void resourceCard2Click(MouseEvent event) throws IllegalCommandException, RemoteException {
        if (!cardDrawn) {
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(1);
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);

            isHandCardSelected = false;
            currentSelectedDeck = currentDeckResource2Card;
            currentImageNew = resourceCard2.getImage();
            currentCommand = new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckResource2Card, "");
            virtualClient.sendCommand(currentCommand);
        }
    }

    /**
     * When the resource deck card is clicked, it is highlighted and its values (image value and card value) are saved.
     * A DrawCommand method is sent.
     * @param event
     * @throws IllegalCommandException
     * @throws RemoteException
     */
    @FXML
    void resourceDeckCardClick(MouseEvent event) throws IllegalCommandException, RemoteException {
        if (!cardDrawn) {
            resourceDeckCard.setOpacity(1);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(.5);
            goldCard2.setOpacity(.5);

            isHandCardSelected = false;
            currentSelectedDeck = currentDeckResourceCard;
            currentImageNew = pathFront(currentDeckResourceCard.getIdCard());
            currentCommand = new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckResourceCard, "RESOURCE");
            virtualClient.sendCommand(currentCommand);
        }
    }

    /**
     * Used to select the board view and see the player's points.
     * @param event
     */
    @FXML
    void boardVisibility(MouseEvent event) {
        inChat = false;
        boardPane.setVisible(true);
        borderPane.setCenter(boardPane);
    }

    /**
     * Used to send a message with the message button.
     * @param event
     */
    @FXML
    void sendMessage(MouseEvent event) {
        textMessageFunct();
    }

    /**
     * Used to send a message by clicking "ENTER" on the keyboard.
     * @param event
     */
    @FXML
    void sendMessageByEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            textMessageFunct();
        }
    }

    /**
     * Used to send a message. The sender (and eventually receiver) is selected and a ChatCommand is sent.
     */
    private void textMessageFunct() {
        String message = inputText.getText();
        Player messageReceiver = null;
        for(Player player: miniModel.getPlayers()){
            if(ChooseSender.getValue() == null){
            }
            else if(ChooseSender.getValue().equals(player.getNickname()))
                messageReceiver = player;
        }
        if (!Objects.equals(message, "")) {
            if(messageReceiver == null) {
                new Thread(()-> {
                    try {
                        virtualClient.sendCommand(new ChatCommand(virtualClient.getClientId(), miniModel.getGameId(), message, myPlayer, null));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
            else{
                Player finalMessageReceiver = messageReceiver;
                new Thread(()-> {
                    try {
                        virtualClient.sendCommand(new ChatCommand(virtualClient.getClientId(), miniModel.getGameId(), message, myPlayer, finalMessageReceiver));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
            inputText.clear();
            ChooseSender.setValue("All");
        }
    }

    /**
     * When the "eye button" next to the first player is clicked, it shows is structure and underlines the name.
     * @param event
     */
    @FXML
    void nickname1VisibilityFunct(MouseEvent event) {
        borderPane.setCenter(scrollPane);

        showHand1.setVisible(false);
        showHand2.setVisible(false);
        showHand3.setVisible(false);

        nickname1Visibility.setOpacity(0);
        nickname2Visibility.setOpacity(1);
        nickname3Visibility.setOpacity(1);
        nickname4Visibility.setOpacity(1);

        myStructure.setUnderline(true);
        nickname2.setUnderline(false);
        nickname3.setUnderline(false);
        nickname4.setUnderline(false);
        inChat = false;
    }

    /**
     * When the "eye button" next to the second player is clicked, it shows is structure and underlines the name.
     * @param event
     */
    @FXML
    void nickname2VisibilityFunct(MouseEvent event) {
        scrollPaneOthers1.setVisible(false);
        otherStructuresPane1.setVisible(false);
        scrollPaneOthers2.setVisible(false);
        otherStructuresPane2.setVisible(false);
        scrollPaneOthers.setVisible(true);
        otherStructuresPane.setVisible(true);
        borderPane.setCenter(scrollPaneOthers);

        int index = 0;
        for(Player player: miniModel.getPlayers()){

            if(Objects.equals(nickname2.getText(), player.getNickname())){
                showHand1.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(0).toString().substring(6,9)));
                showHand2.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(1).toString().substring(6,9)));
                showHand3.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(2).toString().substring(6,9)));
            }
            index++;
        }

        showHand1.setVisible(true);
        showHand2.setVisible(true);
        showHand3.setVisible(true);

        nickname1Visibility.setOpacity(1);
        nickname2Visibility.setOpacity(0);
        nickname3Visibility.setOpacity(1);
        nickname4Visibility.setOpacity(1);

        myStructure.setUnderline(false);
        nickname2.setUnderline(true);
        nickname3.setUnderline(false);
        nickname4.setUnderline(false);
        inChat = false;

    }

    /**
     * When the "eye button" next to the third player is clicked, it shows is structure and underlines the name.
     * @param event
     */
    @FXML
    void nickname3VisibilityFunct(MouseEvent event) {
        scrollPaneOthers.setVisible(false);
        otherStructuresPane.setVisible(false);
        scrollPaneOthers2.setVisible(false);
        otherStructuresPane2.setVisible(false);
        scrollPaneOthers1.setVisible(true);
        otherStructuresPane1.setVisible(true);
        borderPane.setCenter(scrollPaneOthers1);

        int index = 0;
        for(Player player: miniModel.getPlayers()){
            if(Objects.equals(nickname3.getText(), player.getNickname())){
                showHand1.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(0).toString().substring(6,9)));
                showHand2.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(1).toString().substring(6,9)));
                showHand3.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(2).toString().substring(6,9)));
            }
            index++;
        }

        showHand1.setVisible(true);
        showHand2.setVisible(true);
        showHand3.setVisible(true);

        nickname1Visibility.setOpacity(1);
        nickname2Visibility.setOpacity(1);
        nickname3Visibility.setOpacity(0);
        nickname4Visibility.setOpacity(1);

        myStructure.setUnderline(false);
        nickname2.setUnderline(false);
        nickname3.setUnderline(true);
        nickname4.setUnderline(false);
        inChat = false;
    }

    /**
     * When the "eye button" next to the fourth player is clicked, it shows is structure and underlines the name.
     * @param event
     */
    @FXML
    void nickname4VisibilityFunct(MouseEvent event) {
        scrollPaneOthers.setVisible(false);
        otherStructuresPane.setVisible(false);
        scrollPaneOthers1.setVisible(false);
        otherStructuresPane1.setVisible(false);
        scrollPaneOthers2.setVisible(true);
        otherStructuresPane2.setVisible(true);
        borderPane.setCenter(scrollPaneOthers2);

        int index = 0;
        for(Player player: miniModel.getPlayers()){

            if(Objects.equals(nickname4.getText(), player.getNickname())){
                showHand1.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(0).toString().substring(6,9)));
                showHand2.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(1).toString().substring(6,9)));
                showHand3.setImage(pathBack(miniModel.getPlayerHands().get(index).getCardsHand().get(2).toString().substring(6,9)));
            }
            index++;
        }

        showHand1.setVisible(true);
        showHand2.setVisible(true);
        showHand3.setVisible(true);

        nickname1Visibility.setOpacity(1);
        nickname2Visibility.setOpacity(1);
        nickname3Visibility.setOpacity(1);
        nickname4Visibility.setOpacity(0);

        myStructure.setUnderline(false);
        nickname2.setUnderline(false);
        nickname3.setUnderline(false);
        nickname4.setUnderline(true);
        inChat = false;
    }

    /**
     * Used to display chat messages.
     * @param chat
     */
    public void updateChat(Chat chat) {
        if(!inChat)
            chatNotification.setImage(symbolPath("notification"));
        textArea.clear();
        for(ChatMessage chatMessage: chat.getChatMessages()){
            if(chatMessage.getReceiver() == null){
                textArea.appendText("From " + chatMessage.getSender().getNickname() + ": " + chatMessage.getMessage() + "\n\n");
            }
           else if(chatMessage.getReceiver().getNickname().equals(myPlayer.getNickname())){
                textArea.appendText("Private message from " + chatMessage.getSender().getNickname() + ": " + chatMessage.getMessage() + "\n\n");
            }
           else if(chatMessage.getReceiver() != null && chatMessage.getSender().getNickname().equals(myPlayer.getNickname())){
                textArea.appendText("Private message to " + chatMessage.getReceiver().getNickname() + ": " + chatMessage.getMessage() + "\n\n");
            }
        }
    }

    /**
     * Used to update client player.
     * @param player
     */
    public void updateMyPlayer(Player player) {
        this.myPlayer = player;
        pPawn.setImage(symbolPath(myPlayer.getColor().toString()));
    }

    /**
     * Used to update current player text color to blue.
     * @param player
     */
    public void updateCurrentPlayer(Player player) {
        nickname1.setFill(Color.BLACK);
        nickname2.setFill(Color.BLACK);
        nickname3.setFill(Color.BLACK);
        nickname4.setFill(Color.BLACK);

        if (Objects.equals(nickname1.getText(), player.getNickname()))
            nickname1.setFill(Color.BLUE);
        else if (Objects.equals(nickname2.getText(), player.getNickname()))
            nickname2.setFill(Color.BLUE);
        else if (Objects.equals(nickname3.getText(), player.getNickname()))
            nickname3.setFill(Color.BLUE);
        else if (Objects.equals(nickname4.getText(), player.getNickname()))
            nickname4.setFill(Color.BLUE);
        else {
            System.out.println("Player not matching" + player.getNickname());
        }
    }

    /**
     * Used to update players features.
     * @param players
     */
    public void updatePlayers(List<Player> players) {
        List<Player> otherPlayers = players.stream().filter(p->p != miniModel.getMyPlayer()).toList();
        if(isInitialChooseSetup){
            pPawn1 = new ImageView(symbolPath(otherPlayers.get(0).getColor().toString()));
            if(otherPlayers.size() == 2 && otherPlayers.get(1) != null)
                pPawn2 = new ImageView(symbolPath(otherPlayers.get(1).getColor().toString()));
            else
                if(otherPlayers.size() == 3 && otherPlayers.get(2) != null)
                {
                    pPawn2 = new ImageView(symbolPath(otherPlayers.get(1).getColor().toString()));
                    pPawn3 = new ImageView(symbolPath(otherPlayers.get(2).getColor().toString()));
                }
            otherPlayers.forEach(p->ChooseSender.getItems().add(p.getNickname()));
            ChooseSender.getItems().add("All");
            isInitialChooseSetup = false;
        }
        nickname1.setText(miniModel.getMyPlayer().getNickname());
        nickname2.setText(otherPlayers.get(0).getNickname());
        if (players.size() == 3) {
            nickname3.setText(otherPlayers.get(1).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
        }
        if (players.size() == 4) {
            nickname3.setText(otherPlayers.get(1).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
            nickname4.setText(otherPlayers.get(2).getNickname());
            nickname4.setVisible(true);
            nickname4Visibility.setVisible(true);
        }
    }

    /**
     * Used to update structures. When the method is called, every card in a structure is displayed in a specif pane.
     * On every initial card is displayed the player's pawn.
     * The method is also used to add points for every visible resource.
     * If the player is ready to draw, when a structure card's angle is clicked a place command is sent.
     * @param structures
     */
    public void updateStructures(List<Structure> structures) {
        int index = miniModel.getPlayers().indexOf(myPlayer);
        Structure myStructure = structures.get(index);
        structures.remove(myStructure);
        int position = 0;
        Image image = null;
        Image pawnImage;

        for (Map.Entry<String, Integer> entry : myStructure.getvisibleSymbols().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(key != "NULL")
                addPoints(key, value);
        }

        initialCardSide = myStructure.getPlacedCards().getFirst().getValue();
        pawnImage = symbolPath("BLACK");
        ImageView imageView1 = new ImageView(pawnImage);
        ImageView imageView2 = new ImageView(pawnImage);

        structurePane.getChildren().clear();
        for (Pair<Card, Boolean> card : myStructure.getPlacedCards()) {

            if(position == 0){
                setInitialCard((InitialCard) card.getKey());
                pawnImage = symbolPath(myPlayer.getColor().toString());
                ImageView imageView = new ImageView(pawnImage);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                imageView.setLayoutX(990);
                imageView.setLayoutY(1062);
                structurePane.getChildren().add(imageView);

                position = 1;

                if (miniModel.getPlayers().getFirst() == myPlayer){
                    imageView2.setFitWidth(20);
                    imageView2.setFitHeight(20);
                    imageView2.setLayoutX(990);
                    imageView2.setLayoutY(990);
                    structurePane.getChildren().add(imageView2);
                }
            }

            else {
                int x = myStructure.getCardToCoordinate().get(card.getKey()).getFirst() / 100;
                int y = myStructure.getCardToCoordinate().get(card.getKey()).getFirst() % 100;
                if (card.getValue().toString().equals("true")) {
                    image = pathFront(card.getKey().toString().substring(6, 9));
                } else if (card.getValue().toString().equals("false")) {
                    image = pathBack(card.getKey().toString().substring(6, 9));
                }

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(111);
                imageView.setFitHeight(74);
                imageView.setLayoutX(945 + 82 * (x - 40));
                imageView.setLayoutY(1000 - 44 * (y - 40));

                imageView.setOnMouseClicked(event -> {
                    if (!isCardPlaced && isHandCardSelected) {
                        double currentAngleSelectedX = event.getX();
                        double currentAngleSelectedY = event.getY();

                        if (currentAngleSelectedX <= 29) {
                            if (currentAngleSelectedY <= 30)
                                currentAngle = "TL";
                            else if (currentAngleSelectedY >= 44)
                                currentAngle = "BL";
                        } else if (currentAngleSelectedX >= 82) {
                            if (currentAngleSelectedY <= 30)
                                currentAngle = "TR";
                            else if (currentAngleSelectedY >= 44)
                                currentAngle = "BR";
                        }

                        try {
                            currentCommand = new PlaceCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, card.getKey(), currentSelected, currentAngle, currentSelectedFrontUp);
                            virtualClient.sendCommand(currentCommand);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                structurePane.getChildren().add(imageView);
            }
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        }
        int contaStruttura = 0;
        int test = 0;
        List<Player> otherPlayers = miniModel.getPlayers().stream().filter(p->p != miniModel.getMyPlayer()).toList();

        otherStructuresPane.getChildren().clear();
        otherStructuresPane1.getChildren().clear();
        otherStructuresPane2.getChildren().clear();

        for(Structure structure: structures){

            position = 0;
            for (Pair<Card, Boolean> card : structure.getPlacedCards()) {
                if(position == 0){
                    int x, y;
                    int width = 29;
                    int height = 30;

                    String imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card.getKey().getIdCard() + "f.jpg";
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

                    pawnImage = symbolPath(otherPlayers.get(contaStruttura).getColor().toString());
                    ImageView imageView = new ImageView(pawnImage);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    imageView.setLayoutX(990);
                    imageView.setLayoutY(1062);

                    if (miniModel.getPlayers().getFirst() == otherPlayers.get(contaStruttura)){
                        imageView1.setFitWidth(20);
                        imageView1.setFitHeight(20);
                        imageView1.setLayoutX(990);
                        imageView1.setLayoutY(990);

                        test = contaStruttura;
                    }

                    if(contaStruttura == 0){
                        otherStructuresPane.getChildren().add(initialCard);
                        otherStructuresPane.getChildren().add(imageView);
                        if(test == 0)
                            otherStructuresPane.getChildren().add(imageView1);
                    }
                    else if(contaStruttura == 1){
                        otherStructuresPane1.getChildren().add(initialCard);
                        otherStructuresPane1.getChildren().add(imageView);
                        if(test == 1)
                            otherStructuresPane1.getChildren().add(imageView1);
                    }
                    else if(contaStruttura == 2){
                        otherStructuresPane2.getChildren().add(initialCard);
                        otherStructuresPane2.getChildren().add(imageView);
                        if(test == 2)
                            otherStructuresPane2.getChildren().add(imageView1);
                    }
                    position = 1;
                }
                else {
                    int x = structure.getCardToCoordinate().get(card.getKey()).getFirst() / 100;
                    int y = structure.getCardToCoordinate().get(card.getKey()).getFirst() % 100;
                    if (card.getValue().toString().equals("true")) {
                        image = pathFront(card.getKey().toString().substring(6, 9));
                    } else if (card.getValue().toString().equals("false")) {
                        image = pathBack(card.getKey().toString().substring(6, 9));
                    }

                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(111);
                    imageView.setFitHeight(74);
                    imageView.setLayoutX(945 + 82 * (x - 40));
                    imageView.setLayoutY(1000 - 44 * (y - 40));

                    if(contaStruttura == 0){
                        otherStructuresPane.getChildren().add(imageView);
                    }
                    else if(contaStruttura == 1){
                        otherStructuresPane1.getChildren().add(imageView);
                    }
                    else if(contaStruttura == 2){
                        otherStructuresPane2.getChildren().add(imageView);
                    }
                }
            }
            contaStruttura++;
        }
    }

    /**
     * Used to update player's hand.
     * @param hands
     */
    public void updateHand(List<Hand> hands) {

        System.out.printf("Sono dentro update hand \n Initial setupHand: " + isInitialSetupHand + " \n handturn:" + handTurnCounter);
        if (currentCommand != null && currentCommand instanceof PlaceCommand)
        {
            Platform.runLater(()->{
                currentSelectedImage.setVisible(false); //errore
                isCardPlaced = true;
                cardDrawn = false;

                handCard1.setOpacity(1);
                handCard2.setOpacity(1);
                handCard3.setOpacity(1);
                currentCommand = null;
            });
        }
        else
        if (currentCommand != null && currentCommand instanceof DrawCommand)
        {
            Platform.runLater(()->{
                cardDrawn = true;
                isCardPlaced = false;
                currentSelected = null;
                currentSelectedImage.setImage(currentImageNew); //FIXME
                currentSelectedImage.setVisible(true);
                currentSelectedDeck = null;

                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
                currentCommand = null;
            });
        }

        int index = miniModel.getPlayers().indexOf(myPlayer);
        Hand hand = hands.get(index);
        String secretObjectiveCard;

        if (hand.getSecretObjective() != null) {
            secretObjectiveCard = hand.getSecretObjective().toString().substring(22, 25);
            secreteObjective.setImage(pathFront(secretObjectiveCard));
        }
        if (handTurnCounter == 1) {
            if (isInitialSetupHand) {
                String card1;
                String card2;
                String card3;
                currentHandCard1 = hand.getCardsHand().get(0);
                currentHandCard2 = hand.getCardsHand().get(1);
                currentHandCard3 = hand.getCardsHand().get(2);

                card1 = hand.getCardsHand().get(0).toString().substring(6, 9);
                card2 = hand.getCardsHand().get(1).toString().substring(6, 9);
                card3 = hand.getCardsHand().get(2).toString().substring(6, 9);

                handCard1.setImage(pathFront(card1));
                handCard2.setImage(pathFront(card2));
                handCard3.setImage(pathFront(card3));

                handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "f.jpg";
                handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "f.jpg";
                handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "f.jpg";

                isInitialSetupHand = false;
            }
            else {
                if (currentSelected == currentHandCard1 && currentSelectedDeck!=null) {
                    currentHandCard1 = hand.getCardsHand().getFirst();

                    if (currentSelectedFrontUp) {
                        handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/"
                                + currentSelectedDeck.toString().substring(6, 9) + "f.jpg";
                    } else if (!currentSelectedFrontUp) {
                        handCard1URL = "/it/polimi/ingsw/codexnaturalis/BackCards/"
                                + currentSelectedDeck.toString().substring(6, 9) + "b.jpg";
                    }

                } else if (currentSelected == currentHandCard2 && currentSelectedDeck!=null) {
                    currentHandCard2 = hand.getCardsHand().get(1);
                    if (currentSelectedFrontUp) {
                        handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/"
                                + currentSelectedDeck.toString().substring(6, 9) + "f.jpg";
                    } else if (!currentSelectedFrontUp) {
                        handCard2URL = "/it/polimi/ingsw/codexnaturalis/BackCards/"
                                + currentSelectedDeck.toString().substring(6, 9) + "b.jpg";
                    }

                } else if (currentSelected == currentHandCard3 && currentSelectedDeck!=null) {
                    currentHandCard3 = hand.getCardsHand().get(2);
                    if (currentSelectedFrontUp) {
                        handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/"
                                + currentSelectedDeck.toString().substring(6, 9) + "f.jpg";
                    } else if (!currentSelectedFrontUp) {
                        handCard3URL = "/it/polimi/ingsw/codexnaturalis/BackCards/"
                                + currentSelectedDeck.toString().substring(6, 9) + "b.jpg";
                    }
                }

                if(isCardPlaced){
                    KeyEvent simulatedEvent1 = new KeyEvent(KeyEvent.KEY_PRESSED, null, null,
                            KeyCode.F, false, false, false, false);
                    showBack(simulatedEvent1);
                }
            }

        }
        handTurnCounter = 1;
    }

    /**
     * Used to display the board. Paws images are created for every player and displayed in the correct
     * spot according to points made.
     * @param board
     */
    public void updateBoard(Board board) {
        currentDeckResource1Card = board.getUncoveredCards().get(0);
        currentDeckResource2Card = board.getUncoveredCards().get(1);

        currentDeckGold1Card = board.getUncoveredCards().get(2);
        currentDeckGold2Card = board.getUncoveredCards().get(3);

        String resourceCardOne = currentDeckResource1Card.toString().substring(6,9);
        String resourceCardTwo = currentDeckResource2Card.toString().substring(6,9);

        String goldCardOne = currentDeckGold1Card.toString().substring(6,9);
        String goldCardTwo = currentDeckGold2Card.toString().substring(6,9);

        resourceCard1.setImage(pathFront(resourceCardOne));
        resourceCard2.setImage(pathFront(resourceCardTwo));

        goldCard1.setImage(pathFront(goldCardOne));
        goldCard2.setImage(pathFront(goldCardTwo));

        String publicObjectiveOne = board.getCommonObjectives().get(0).toString().substring(22, 25);
        String publicObjectiveTwo = board.getCommonObjectives().get(1).toString().substring(22, 25);

        publicObjective1.setImage(pathFront(publicObjectiveOne));
        publicObjective2.setImage(pathFront(publicObjectiveTwo));

        int points;
        List<Player> otherPlayers = miniModel.getPlayers().stream().filter(p->p != miniModel.getMyPlayer()).toList();

        for(var entry : board.getActualScores().entrySet()){
            if(entry.getKey().getNickname().equals(myPlayer.getNickname())){
                points = entry.getValue();
                myPoints.setText(entry.getValue().toString());
                player1Points.setText(myPlayer.getNickname() + ": "  + entry.getValue().toString());
                addPoint(points, pPawn);
            }

            if(entry.getKey().getNickname().equals(otherPlayers.get(0).getNickname())){
                pPawn1.setImage(symbolPath(otherPlayers.getFirst().getColor().toString()));
                player2Points.setText(otherPlayers.getFirst().getNickname() + ": "  + entry.getValue().toString());
                if(c==0)
                    boardPane.getChildren().add(pPawn1);
                addPoint(entry.getValue(), pPawn1);
            }

            else if(otherPlayers.size() == 2 && entry.getKey().getNickname().equals(otherPlayers.get(1).getNickname())){
                pPawn2.setImage(symbolPath(otherPlayers.get(1).getColor().toString()));
                player3Points.setText(otherPlayers.get(1).getNickname() + ": "  + entry.getValue().toString());
                player3Points.setVisible(true);
                if(c==0)
                    boardPane.getChildren().add(pPawn2);
                addPoint(entry.getValue(), pPawn2);
            }

            else if(otherPlayers.size() == 3 && entry.getKey().getNickname().equals(otherPlayers.get(1).getNickname())) {
                pPawn2.setImage(symbolPath(otherPlayers.get(1).getColor().toString()));
                pPawn3.setImage(symbolPath(otherPlayers.get(2).getColor().toString()));
                player3Points.setText(otherPlayers.get(1).getNickname() + ": "  + entry.getValue().toString());
                player3Points.setVisible(true);
                if(c==0){
                    boardPane.getChildren().add(pPawn3);
                    boardPane.getChildren().add(pPawn2);
                }
                addPoint(entry.getValue(), pPawn2);

            }

            else if(otherPlayers.size() == 3 && entry.getKey().getNickname().equals(otherPlayers.get(2).getNickname())) {
                player4Points.setText(otherPlayers.get(2).getNickname() + ": "  + entry.getValue().toString());
                player4Points.setVisible(true);
                addPoint(entry.getValue(), pPawn3);
            }
        }
        c++;

        handCard.setFocusTraversable(true);
        handCard.requestFocus();
    }

    /**
     * Used to move pawns according to points.
     * @param points
     * @param posto
     */
    public void addPoint(int points, ImageView posto){
        switch (points) {
            case 0:
                posto.setLayoutX(399);
                posto.setLayoutY(403);
                setDimension(posto);
                break;
            case 1:
                posto.setLayoutX(449);
                posto.setLayoutY(403);
                setDimension(posto);
                break;
            case 2:
                posto.setLayoutX(499);
                posto.setLayoutY(403);
                setDimension(posto);
                break;
            case 3:
                posto.setLayoutX(524);
                posto.setLayoutY(358);
                setDimension(posto);
                break;
            case 4:
                posto.setLayoutX(474);
                posto.setLayoutY(358);
                setDimension(posto);
                break;
            case 5:
                posto.setLayoutX(424);
                posto.setLayoutY(358);
                setDimension(posto);
                break;
            case 6:
                posto.setLayoutX(374);
                posto.setLayoutY(358);
                setDimension(posto);
                break;
            case 7:
                posto.setLayoutX(374);
                posto.setLayoutY(313);
                setDimension(posto);
                break;
            case 8:
                posto.setLayoutX(424);
                posto.setLayoutY(313);
                setDimension(posto);
                break;
            case 9:
                posto.setLayoutX(474);
                posto.setLayoutY(313);
                setDimension(posto);
                break;
            case 10:
                posto.setLayoutX(524);
                posto.setLayoutY(313);
                setDimension(posto);
                break;
            case 11:
                posto.setLayoutX(524);
                posto.setLayoutY(267);
                setDimension(posto);
                break;
            case 12:
                posto.setLayoutX(474);
                posto.setLayoutY(267);
                setDimension(posto);
                break;
            case 13:
                posto.setLayoutX(424);
                posto.setLayoutY(267);
                setDimension(posto);
                break;
            case 14:
                posto.setLayoutX(374);
                posto.setLayoutY(267);
                setDimension(posto);
                break;
            case 15:
                posto.setLayoutX(374);
                posto.setLayoutY(222);
                setDimension(posto);
                break;
            case 16:
                posto.setLayoutX(424);
                posto.setLayoutY(222);
                setDimension(posto);
                break;
            case 17:
                posto.setLayoutX(474);
                posto.setLayoutY(222);
                setDimension(posto);
                break;
            case 18:
                posto.setLayoutX(524);
                posto.setLayoutY(222);
                setDimension(posto);
                break;
            case 19:
                posto.setLayoutX(524);
                posto.setLayoutY(176);
                setDimension(posto);
                break;
            case 20:
                posto.setLayoutX(449);
                posto.setLayoutY(152);
                setDimension(posto);
                break;
            case 21:
                posto.setLayoutX(374);
                posto.setLayoutY(176);
                setDimension(posto);
                break;
            case 22:
                posto.setLayoutX(374);
                posto.setLayoutY(129);
                setDimension(posto);
                break;
            case 23:
                posto.setLayoutX(374);
                posto.setLayoutY(83);
                setDimension(posto);
                break;
            case 24:
                posto.setLayoutX(403);
                posto.setLayoutY(45);
                setDimension(posto);
                break;
            case 25:
                posto.setLayoutX(449);
                posto.setLayoutY(38);
                setDimension(posto);
                break;
            case 26:
                posto.setLayoutX(495);
                posto.setLayoutY(46);
                setDimension(posto);
                break;
            case 27:
                posto.setLayoutX(524);
                posto.setLayoutY(83);
                setDimension(posto);
                break;
            case 28:
                posto.setLayoutX(524);
                posto.setLayoutY(129);
                setDimension(posto);
                break;
            case 29:
                posto.setLayoutX(449);
                posto.setLayoutY(93);
                setDimension(posto);
                break;
        }
    }

    public void setDimension(ImageView posto) {
        posto.setFitWidth(40);
        posto.setFitHeight(40);
    }

    /**
     * Used to update deck cards.
     * @param deck
     */
    public void updateDeck(Deck deck) {

        currentDeckGoldCard = deck.getGoldDeck().peek();
        String goldDeckCardOne = currentDeckGoldCard.toString().substring(6, 9);
        goldDeckCard.setImage(pathBack(goldDeckCardOne));

        currentDeckResourceCard = deck.getResourceDeck().peek();
        String resourceDeckCardOne = currentDeckResourceCard.toString().substring(6, 9);
        resourceDeckCard.setImage(pathBack(resourceDeckCardOne));
    }

    /**
     * used to reset values if errors occur.
     * @param error
     */
    public void updateError(String error) {
        if (currentCommand != null && currentCommand instanceof PlaceCommand)
        {
            Platform.runLater(()->{
                currentSelectedImage = null;
                isHandCardSelected = false;

                handCard1.setOpacity(1);
                handCard2.setOpacity(1);
                handCard3.setOpacity(1);
                currentCommand = null;
            });
        }

        if (currentCommand != null && currentCommand instanceof DrawCommand)
        {
            Platform.runLater(()->{
                currentSelectedDeck = null;
                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
                currentCommand = null;
            });
        }
        showAlert(error);
    }

    /**
     * Used to visually display an error and notify the client.
     * @param message
     */
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("What's happening?");
        alert.setContentText(message);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = alert.showAndWait();
    }

    /**
     * Used to go back to the client structure.
     * @param event
     */
    @FXML
    void goBackFunct(MouseEvent event) {
        borderPane.setCenter(scrollPane);
    }

    /**
     * Used to "deselect" cards. When the pane is clicked the card opacities go back to one.
     * @param event
     */
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

    /**
     * Used to open chat pane.
     * @param event
     * @throws IOException
     */
    @FXML
    void openChatFunct(MouseEvent event) throws IOException {
        inChat = true;
        chatNotification.setImage(symbolPath("chat"));
        chatPane.setVisible(true);
        borderPane.setCenter(chatPane);
    }

    /**
     * Used to display back and front of hand cards by pressing "B" and "F".
     * @param event
     */
    @FXML
    void showBack(KeyEvent event) {
        if (event.getCode() == KeyCode.B && rPressed == 0) {

            // if (currentSelected != null)
            currentSelectedFrontUp = false;
            rPressed = 1;
            if (fPressed == 1)
                fPressed = 0;

            String card1 = handCard1URL.substring(43, 46);
            String card2 = handCard2URL.substring(43, 46);
            String card3 = handCard3URL.substring(43, 46);

            handCard1.setImage(pathBack(card1));
            handCard2.setImage(pathBack(card2));
            handCard3.setImage(pathBack(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/BackCards/" + card3 + "b.jpg";

        } else if (event.getCode() == KeyCode.F && fPressed == 0) {
            currentSelectedFrontUp = true;

            fPressed = 1;
            if (rPressed == 1)
                rPressed = 0;

            String card1 = handCard1URL.substring(42, 45);
            String card2 = handCard2URL.substring(42, 45);
            String card3 = handCard3URL.substring(42, 45);

            handCard1.setImage(pathFront(card1));
            handCard2.setImage(pathFront(card2));
            handCard3.setImage(pathFront(card3));

            handCard1URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card1 + "b.jpg";
            handCard2URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card2 + "b.jpg";
            handCard3URL = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + card3 + "b.jpg";

        }
    }

    /**
     * Used to display and set initial card for the player structure.
     * When the player's turn occur and a hand card is selected, if the player click one of the available
     * borders on the initial card it calls the place command placing the card in the correct spot.
     * @param initialCard1
     */
    public void setInitialCard(InitialCard initialCard1) {
        int x, y;
        String imagePath;

        if(initialCardSide)
            imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + initialCard1.getIdCard() + "f.jpg";
        else
            imagePath = "/it/polimi/ingsw/codexnaturalis/BackCards/" + initialCard1.getIdCard() + "b.jpg";

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

            if (!isCardPlaced && isHandCardSelected) {
                double currentAngleSelectedX = event.getX();
                double currentAngleSelectedY = event.getY();

                if (currentAngleSelectedX <= 29) {
                    if (currentAngleSelectedY <= 30)
                        currentAngle = "TL";
                    else if (currentAngleSelectedY >= 44)
                        currentAngle = "BL";
                } else if (currentAngleSelectedX >= 82) {
                    if (currentAngleSelectedY <= 30)
                        currentAngle = "TR";
                    else if (currentAngleSelectedY >= 44)
                        currentAngle = "BR";
                }

                try {

                    currentCommand = new PlaceCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, initialCard1, currentSelected, currentAngle, currentSelectedFrontUp);
                    virtualClient.sendCommand(currentCommand);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        structurePane.getChildren().add(initialCard);
    }

    /**
     * Used to add points given the obj (e.g. "mushroom") and the points associated.
     * @param obj
     * @param points
     */
    public void addPoints(String obj, int points) {
        int valueInteger = 0;
        String valueString;
        switch (obj) {
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

    /**
     * Used to create a front image of a given object.
     * @param oggetto
     * @return
     */
    public Image pathFront(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + oggetto + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    /**
     * Used to create a back image of a given object.
     * @param oggetto
     * @return
     */
    public Image pathBack(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/BackCards/" + oggetto + "b.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    /**
     * Used to create an image of a given object.
     * @param oggetto
     * @return
     */
    public Image symbolPath(String oggetto) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/SymbolsPng/" + oggetto + ".png";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    /**
     * Used when a player wants to quit the game.
     * @param event
     */
    @FXML
    void exitGame(MouseEvent event) {
        Platform.runLater(Platform::exit);
        System.exit(0);
    }

    /**
     * Used to initialize some visual objects and to enable the mouse drag feature on structure panes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pPawn = new ImageView();
        pPawn1 = new ImageView();
        pPawn2 = new ImageView();
        pPawn3 = new ImageView();

        nickname1Visibility.setOpacity(0);
        nickname2Visibility.setOpacity(1);
        nickname3Visibility.setOpacity(1);
        nickname4Visibility.setOpacity(1);

        myStructure.setUnderline(true);
        nickname2.setUnderline(false);
        nickname3.setUnderline(false);
        nickname4.setUnderline(false);

        ChooseSender.setValue("All");
        boardPane.getChildren().add(pPawn);

        scrollPaneOthers.setVisible(false);
        chatPane.setVisible(false);

        mushroomsPoints.setText("0");
        leafPoints.setText("0");
        wolfPoints.setText("0");
        butterflyPoints.setText("0");
        featherPoints.setText("0");
        manuscriptPoints.setText("0");
        potionPoints.setText("0");

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        double hCenter = (structurePane.getPrefWidth() - scrollPane.getViewportBounds().getWidth()) / 2
                / structurePane.getPrefWidth();
        double vCenter = (structurePane.getPrefHeight() - scrollPane.getViewportBounds().getHeight()) / 2
                / structurePane.getPrefHeight();
        scrollPane.setHvalue(hCenter);
        scrollPane.setVvalue(vCenter);

        scrollPaneOthers.setHvalue(hCenter);
        scrollPaneOthers.setVvalue(vCenter);

        scrollPaneOthers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneOthers.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPaneOthers1.setHvalue(hCenter);
        scrollPaneOthers1.setVvalue(vCenter);

        scrollPaneOthers1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneOthers1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPaneOthers2.setHvalue(hCenter);
        scrollPaneOthers2.setVvalue(vCenter);

        scrollPaneOthers2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneOthers2.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        structurePane.setOnMousePressed(event -> {
            lastX = event.getX();
            lastY = event.getY();
        });

        structurePane.setOnMouseDragged(event -> {
            double deltaX = event.getX() - lastX;
            double deltaY = event.getY() - lastY;

            scrollPane.setHvalue(scrollPane.getHvalue() - deltaX / structurePane.getWidth());
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / structurePane.getHeight());
        });

        scrollPane.setOnMouseReleased(event -> {
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        });

        otherStructuresPane.setOnMousePressed(event -> {
            lastX = event.getX();
            lastY = event.getY();
        });

        otherStructuresPane.setOnMouseDragged(event -> {
            double deltaX = event.getX() - lastX;
            double deltaY = event.getY() - lastY;

            scrollPaneOthers.setHvalue(scrollPaneOthers.getHvalue() - deltaX / otherStructuresPane.getWidth());
            scrollPaneOthers.setVvalue(scrollPaneOthers.getVvalue() - deltaY / otherStructuresPane.getHeight());
        });

        otherStructuresPane.setOnMouseReleased(event -> {
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        });

        otherStructuresPane1.setOnMousePressed(event -> {
            lastX = event.getX();
            lastY = event.getY();
        });

        otherStructuresPane1.setOnMouseDragged(event -> {
            double deltaX = event.getX() - lastX;
            double deltaY = event.getY() - lastY;

            scrollPaneOthers1.setHvalue(scrollPaneOthers1.getHvalue() - deltaX / otherStructuresPane1.getWidth());
            scrollPaneOthers1.setVvalue(scrollPaneOthers1.getVvalue() - deltaY / otherStructuresPane1.getHeight());
        });

        otherStructuresPane1.setOnMouseReleased(event -> {
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        });

        otherStructuresPane2.setOnMousePressed(event -> {
            lastX = event.getX();
            lastY = event.getY();
        });

        otherStructuresPane2.setOnMouseDragged(event -> {
            double deltaX = event.getX() - lastX;
            double deltaY = event.getY() - lastY;

            scrollPaneOthers2.setHvalue(scrollPaneOthers2.getHvalue() - deltaX / otherStructuresPane2.getWidth());
            scrollPaneOthers2.setVvalue(scrollPaneOthers2.getVvalue() - deltaY / otherStructuresPane2.getHeight());
        });

        otherStructuresPane2.setOnMouseReleased(event -> {
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        });
    }
}
