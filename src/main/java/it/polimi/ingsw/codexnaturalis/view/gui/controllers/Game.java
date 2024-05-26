package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;
import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
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
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.ChatCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.DrawCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.PlaceCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;


public class Game implements Initializable {

    @FXML
    private Text mushroomsPoints, leafPoints, wolfPoints, butterflyPoints, featherPoints, manuscriptPoints,
            potionPoints;

    @FXML
    private Text connectionType;

    @FXML
    private ImageView goldDeckCard, goldCard1, goldCard2, resourceDeckCard, resourceCard1, resourceCard2,
            secreteObjective, publicObjective1, publicObjective2;

    @FXML
    private Text nickname1 = new Text(), nickname2 = new Text(), nickname3, nickname4;

    @FXML
    private ImageView nickname3Visibility, nickname4Visibility;

    @FXML
    private ImageView handCard1, handCard2, handCard3;

    @FXML
    private List<Rectangle> rectangleList = new ArrayList<Rectangle>();

    @FXML
    private BorderPane borderPane ;

    @FXML
    private Pane structurePane, otherStructuresPane, chatPane, boardPane,
                                otherStructuresPane1,
                                otherStructuresPane2;

    @FXML
    private ScrollPane scrollPane, scrollPaneOthers,
                        scrollPaneOthers1,
                        scrollPaneOthers2;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField inputText;



    @FXML
    private HBox handCard;

    @FXML
    private ChoiceBox<String> ChooseSender;

    private final List<String> gamePlayers = new ArrayList<>();;


    String handCard1URL, handCard2URL, handCard3URL;
    Card currentHandCard1, currentHandCard2, currentHandCard3, currentDeckGold1Card, currentDeckGold2Card,
            currentDeckGoldCard, currentDeckResource1Card, currentDeckResource2Card, currentDeckResourceCard;
    private ImageView structureCardSelected;

    private Player myPlayer;
    private InitialCard initialCardCard;
    private double lastX, lastY; // Memorizza le coordinate dell'ultimo evento
    int rPressed = 0;
    int fPressed = 1;

    private boolean errore = false;

    ViewFactory viewFactory;

    private MiniModel miniModel;
    private VirtualClient virtualClient;

    // Gestione evento di placeCard

    Card currentSelected = null, currentSelectedDeck;
    String currentAngle;
    boolean currentSelectedFrontUp = true;
    private ImageView currentSelectedImage;
    boolean isCardPlaced = false, cardDrawn = true, isInitialSetupHand = true, isInitialChooseSetup = true,
            isHandCardSelected;

    String currentState;
    FXMLLoader loader;
    ImageView pedina;

    //Pedine da far vedere nella board per gli altri player
    ImageView pedina1;
    ImageView pedina2;
    ImageView pedina3;

    int c = 0; //Usato per aggiungere le pedine al pane se esistono.. 1 sola volta (mettere apposto??)
    int contatoreHandTurn = 0;



    public void setUP(MiniModel miniModel, VirtualClient virtualClient) {
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;
    }

    public void setUp(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    @FXML
    void handCard1Clicked(MouseEvent event) {

        if (!isCardPlaced) {
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

        if (!isCardPlaced) {
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
        if (!isCardPlaced) {
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
    void goldCard1Click(MouseEvent event) throws IllegalCommandException, RemoteException {

        if (!cardDrawn) {
            goldDeckCard.setOpacity(.5);
            goldCard1.setOpacity(1);
            goldCard2.setOpacity(.5);
            resourceDeckCard.setOpacity(.5);
            resourceCard1.setOpacity(.5);
            resourceCard2.setOpacity(.5);

            isHandCardSelected = false;

            //System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckGold1Card);

            currentSelectedDeck = currentDeckGold1Card;

            Image currentImage = goldCard1.getImage();

            virtualClient.sendCommand(new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckGold1Card, ""));


            checkErrorOrElse(()->{

            }, ()->{
                cardDrawn = true;
                isCardPlaced = false;
                currentSelected = null;

                currentSelectedImage.setImage(currentImage);
                currentSelectedImage.setVisible(true);

            },()->{

                currentSelectedDeck = null;
                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
            });


        }

    }



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

            //System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckGold2Card);
            currentSelectedDeck = currentDeckGold2Card;

            Image currentImage = goldCard2.getImage();

            virtualClient.sendCommand(new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckGold2Card, ""));

            checkErrorOrElse(()->{
            }, ()->{
                cardDrawn = true;
                isCardPlaced = false;
                currentSelected = null;

                currentSelectedImage.setImage(currentImage);
                currentSelectedImage.setVisible(true);

            },()->{

                currentSelectedDeck = null;
                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
            });


        }

    }

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

            //System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckGoldCard);
            currentSelectedDeck = currentDeckGoldCard;

            Image currentImage = goldDeckCard.getImage();

            virtualClient.sendCommand(new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckGoldCard, "GOLD"));
            // testMain.drawCommand(currentDeckGoldCard);


            checkErrorOrElse(()->{

            }, ()->{
                cardDrawn = true;
                isCardPlaced = false;
                currentSelected = null;

                currentSelectedImage.setImage(currentImage);
                currentSelectedImage.setVisible(true);

            },()->{

                currentSelectedDeck = null;
                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
            });

        }

    }

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

                Image currentImage = resourceCard1.getImage();

                virtualClient.sendCommand(new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckResource1Card, ""));
                // testMain.drawCommand(currentDeckResource1Card);


                checkErrorOrElse(()->{

                }, ()->{
                    cardDrawn = true;
                    isCardPlaced = false;
                    currentSelected = null;

                    currentSelectedImage.setImage(currentImage);
                    currentSelectedImage.setVisible(true);

                },()->{

                    currentSelectedDeck = null;
                    goldDeckCard.setOpacity(1);
                    goldCard1.setOpacity(1);
                    goldCard2.setOpacity(1);
                    resourceDeckCard.setOpacity(1);
                    resourceCard1.setOpacity(1);
                    resourceCard2.setOpacity(1);
                });
            }

    }

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

            Image currentImage = resourceCard2.getImage();

            virtualClient.sendCommand(new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckResource2Card, ""));

            // testMain.drawCommand(currentDeckResource2Card);

            checkErrorOrElse(()->{

            }, ()->{
                cardDrawn = true;
                isCardPlaced = false;
                currentSelected = null;

                currentSelectedImage.setImage(currentImage);
                currentSelectedImage.setVisible(true);

            },()->{

                currentSelectedDeck = null;
                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
            });

        }

    }

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

            //System.out.printf("\nHai selezionato questa carta dal deck: " + currentDeckResourceCard);
            currentSelectedDeck = currentDeckResourceCard;

            Image currentImage = resourceDeckCard.getImage();

            virtualClient.sendCommand(new DrawCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, currentDeckResourceCard, "RESOURCE"));

            // testMain.drawCommand(currentDeckResourceCard);
            checkErrorOrElse(()->{

            }, ()->{
                cardDrawn = true;
                isCardPlaced = false;
                currentSelected = null;

                currentSelectedImage.setImage(currentImage);
                currentSelectedImage.setVisible(true);

            },()->{

                currentSelectedDeck = null;
                goldDeckCard.setOpacity(1);
                goldCard1.setOpacity(1);
                goldCard2.setOpacity(1);
                resourceDeckCard.setOpacity(1);
                resourceCard1.setOpacity(1);
                resourceCard2.setOpacity(1);
            });
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

        Player messageReceiver = null;
        for(Player player: miniModel.getPlayers()){
            if(ChooseSender.getValue() == null){
            }
            else if(ChooseSender.getValue().equals(player.getNickname()))
                messageReceiver = player;
        }
        if (!Objects.equals(message, "")) {
            try {
                if(messageReceiver == null) {
                    virtualClient.sendCommand(new ChatCommand(virtualClient.getClientId(), miniModel.getGameId(), message, myPlayer, null));
                }
                else{

                    virtualClient.sendCommand(new ChatCommand(virtualClient.getClientId(), miniModel.getGameId(), message, myPlayer, messageReceiver));
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            inputText.clear();
            ChooseSender.setValue("All");
        }

    }

    @FXML // TO DO: vai a capo se string lunga tot
    void sendMessageByEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String message = inputText.getText();


            Player messageReceiver = null;
            for(Player player: miniModel.getPlayers()){
                if(ChooseSender.getValue() == null){
                }
                else if(ChooseSender.getValue().equals(player.getNickname()))
                    messageReceiver = player;
            }
            if (!Objects.equals(message, "")) {
                try {
                    if(messageReceiver == null) {
                        virtualClient.sendCommand(new ChatCommand(virtualClient.getClientId(), miniModel.getGameId(), message, myPlayer, null));
                    }
                    else{

                        virtualClient.sendCommand(new ChatCommand(virtualClient.getClientId(), miniModel.getGameId(), message, myPlayer, messageReceiver));
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                inputText.clear();
                ChooseSender.setValue("All");
            }
        }
    }

    @FXML
    void nickname2VisibilityFunct(MouseEvent event) {
         scrollPaneOthers1.setVisible(false);
         otherStructuresPane1.setVisible(false);
         scrollPaneOthers2.setVisible(false);
         otherStructuresPane2.setVisible(false);

         scrollPaneOthers.setVisible(true);
         otherStructuresPane.setVisible(true);
         borderPane.setCenter(scrollPaneOthers);

    }

    @FXML
    void nickname3VisibilityFunct(MouseEvent event) {
        scrollPaneOthers.setVisible(false);
        otherStructuresPane.setVisible(false);
        scrollPaneOthers2.setVisible(false);
        otherStructuresPane2.setVisible(false);

        scrollPaneOthers1.setVisible(true);
        otherStructuresPane1.setVisible(true);
        borderPane.setCenter(scrollPaneOthers1);
    }

    @FXML
    void nickname4VisibilityFunct(MouseEvent event) {
        scrollPaneOthers.setVisible(false);
        otherStructuresPane.setVisible(false);
        scrollPaneOthers1.setVisible(false);
        otherStructuresPane1.setVisible(false);

        scrollPaneOthers2.setVisible(true);
        otherStructuresPane2.setVisible(true);
        borderPane.setCenter(scrollPaneOthers2);

    }

    public void updateChat(Chat chat) {
        textArea.clear();


        for(ChatMessage chatMessage: chat.getChatMessages()){

            if(chatMessage.getReceiver() == null){
                textArea.appendText("From " + chatMessage.getSender().getNickname() + ": " + chatMessage.getMessage() + "\n\n");
            }
           else if(chatMessage.getReceiver().getNickname().equals(myPlayer.getNickname())){
                textArea.appendText("Private message from: " + chatMessage.getSender().getNickname() + ": " + chatMessage.getMessage() + "\n\n");
            }

        }
    }

    public void updateState(String state) {
        //System.out.println("\n\nLo stato corrente è: " + state);
        switch (state) {
            case "Wait":
                currentState = "Wait";
                break;
            case "Choose":
                currentState = "Choose";
                break;
            case "End":
                currentState = "End";
                break;
            default:
                break;
        }
    }



    public void updateMyPlayer(Player player) {

        //System.out.print("\n (My player updated) \n");
        this.myPlayer = player;

        pedina.setImage(symbolPath(myPlayer.getColor().toString()));

    }

    public void updateWinners(List<Player> winners) {

    }

    public void updateCurrentPlayer(Player player) {
        //System.out.print("\n (Current player updated) \n");

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
            System.out.println("Player not matching" + player.getNickname()); // Metti un alert in caso...
        }
        // System.out.print("\nCurrent Player updated!");

    }

    public void updatePlayers(List<Player> players) {



        //System.out.print("\n (Players updated) \n");


        List<Player> otherPlayers = players.stream().filter(p->p != miniModel.getMyPlayer()).collect(Collectors.toList());

        if(isInitialChooseSetup){

            pedina1 = new ImageView(symbolPath(otherPlayers.get(0).getColor().toString()));

            if(otherPlayers.size() == 2 && otherPlayers.get(1) != null)
                pedina2 = new ImageView(symbolPath(otherPlayers.get(1).getColor().toString()));
            else
                if(otherPlayers.size() == 3 && otherPlayers.get(2) != null)
                {
                    pedina2 = new ImageView(symbolPath(otherPlayers.get(1).getColor().toString()));
                    pedina3 = new ImageView(symbolPath(otherPlayers.get(2).getColor().toString()));
                }


            otherPlayers.forEach(p->ChooseSender.getItems().add(p.getNickname()));

            ChooseSender.getItems().add("All");
            isInitialChooseSetup = false;

        }





        nickname1.setText(miniModel.getMyPlayer().getNickname());


        nickname2.setText(otherPlayers.get(0).getNickname());

        if (players.size() == 3) {
            nickname3.setText(players.get(1).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
        }
        if (players.size() == 4) {
            nickname3.setText(players.get(1).getNickname());
            nickname3.setVisible(true);
            nickname3Visibility.setVisible(true);
            nickname4.setText(players.get(2).getNickname());
            nickname4.setVisible(true);
            nickname4Visibility.setVisible(true);
        }
        // System.out.print("\nPlayers updated!");

    }

    public void createGhostRectangles(Card card, double x, double y) throws IllegalCommandException {
        int position = 0;
        int width = 111;
        int height = 74;
        for (String s : card.getFrontCorners()) {
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

    public void updateStructures(List<Structure> structures) {

        //System.out.printf("\n (Structures updated) \n");

        int index = miniModel.getPlayers().indexOf(myPlayer);

        Structure myStructure = structures.get(index);

        structures.remove(myStructure);

        int position = 0;
        int points;


        Image image = null;
        Image pawnImage;


        // Aggiungo punti in base alle risorse
        points = Integer.parseInt(myStructure.getVisibleResources().substring(11, 12));
        addPoints("VEGETABLE", points);

        points = Integer.parseInt(myStructure.getVisibleResources().substring(21, 22));
        addPoints("ANIMAL", points);

        points = Integer.parseInt(myStructure.getVisibleResources().substring(31, 32));
        addPoints("INSECT", points);

        points = Integer.parseInt(myStructure.getVisibleResources().substring(41, 42));
        addPoints("SHROOM", points);

        points = Integer.parseInt(myStructure.getVisibleObjects().substring(9, 10));
        addPoints("FEATHER", points);

        points = Integer.parseInt(myStructure.getVisibleObjects().substring(16, 17));
        addPoints("INK", points);

        points = Integer.parseInt(myStructure.getVisibleObjects().substring(26, 27));
        addPoints("SCROLL", points);

        // TO DO: Capisci se quando la funzione viene creata una seconda volta le carte
        // precedenti vengono eliminate?

        pawnImage = symbolPath("BLACK");
        ImageView imageView1 = new ImageView(pawnImage);
        ImageView imageView2 = new ImageView(pawnImage);


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


            else if (position == 1) {
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

                // Inizio
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
                            virtualClient.sendCommand(new PlaceCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, card.getKey(), currentSelected, currentAngle, currentSelectedFrontUp));


                            checkErrorOrElse(()->{
                                currentSelectedImage = null;
                                isHandCardSelected = false;

                            }, ()->{
                                currentSelectedImage.setVisible(false);

                                isCardPlaced = true;
                                cardDrawn = false;

                            },()->{

                                handCard1.setOpacity(1);
                                handCard2.setOpacity(1);
                                handCard3.setOpacity(1);
                            });



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



    public void updateHand(List<Hand> hands) {

        System.out.print("\n (Hand updated) \n");



        int index = miniModel.getPlayers().indexOf(myPlayer);
        Hand hand = hands.get(index);

        System.out.print("\nHand dopo update: ");
        for (Card card : hand.getCardsHand())
            System.out.print(card.toString().substring(6, 9) + "-");
        System.out.printf("\n Questa è la current selected: " + currentSelected);

        String secretObjectiveCard;
        if (hand.getSecretObjective() != null) {
            secretObjectiveCard = hand.getSecretObjective().toString().substring(22, 25);
            secreteObjective.setImage(pathFront(secretObjectiveCard));
        }


        //A ogni client arriva l evento di Wait, Place, ecc....
        if (contatoreHandTurn == 1) {

            if (isInitialSetupHand) {
                System.out.printf("Siamo nello stato iniziale di updateHand");
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
                System.out.printf("Siamo nello stato secondario di updateHand");

                System.out.printf("Questa è la current selected: " + currentSelected);
                System.out.printf("Questa è la current hand card1: " + currentHandCard1);



                if (currentSelected == currentHandCard1 && currentSelectedDeck!=null) {
                    System.out.printf("\n Hand" + hand);
                    System.out.printf("\n Carta non aggiornata: " + currentHandCard1);
                    currentHandCard1 = hand.getCardsHand().getFirst();
                    System.out.printf("\n Hand" + hand);
                    System.out.printf("\n Carta aggiornata: " + currentHandCard1);

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
                System.out.println("\nUpdate card1: " + handCard1URL);
                System.out.println("\nUpdate card2: " + handCard2URL);
                System.out.println("\nUpdate card3: " + handCard3URL);


            }
            System.out.printf("\nHand: ");
            for (Card card : hand.getCardsHand())
                System.out.printf(card.toString().substring(6, 9) + "-");

            // TO DO: problema quando le hand card sono verso il back e ne prendo un'altra
            // dal deck.. se faccio B o F non va piu.. questo sotto non ha funzionato per
            // fixxare
            // KeyEvent simulatedEvent = new KeyEvent(KeyEvent.KEY_PRESSED, null, null,
            // KeyCode.F, false, false, false, false);
            // showBack(simulatedEvent);

            // System.out.print("\nHand updated!");

        }
        contatoreHandTurn = 1;
    }

    public void updateBoard(Board board) {

        //System.out.print("\n (Board updated) \n");

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


        String myPlayerColor = myPlayer.getColor().toString();


        int points = 0;
        List<Player> otherPlayers = miniModel.getPlayers().stream().filter(p->p != miniModel.getMyPlayer()).toList();

        int index = miniModel.getPlayers().indexOf(myPlayer);
        for(var entry : board.getActualScores().entrySet()){
            if(entry.getKey().getNickname().equals(myPlayer.getNickname())){
                points = entry.getValue();
                addPoint(points, pedina);
            }

            if(entry.getKey().getNickname().equals(otherPlayers.get(0).getNickname())){
                pedina1.setImage(symbolPath(otherPlayers.get(0).getColor().toString()));
                if(c==0)
                    boardPane.getChildren().add(pedina1);
                addPoint(entry.getValue(), pedina1);
            }

            else if(otherPlayers.size() == 2 && entry.getKey().getNickname().equals(otherPlayers.get(1).getNickname())){
                pedina2.setImage(symbolPath(otherPlayers.get(1).getColor().toString()));
                if(c==0)
                    boardPane.getChildren().add(pedina2);
                addPoint(entry.getValue(), pedina2);
                
            }

            else if(otherPlayers.size() == 3 && entry.getKey().getNickname().equals(otherPlayers.get(1).getNickname())) {
                pedina2.setImage(symbolPath(otherPlayers.get(1).getColor().toString()));
                pedina3.setImage(symbolPath(otherPlayers.get(2).getColor().toString()));
                if(c==0){
                    boardPane.getChildren().add(pedina3);
                    boardPane.getChildren().add(pedina2);
                }
                addPoint(entry.getValue(), pedina2);

            }

            else if(otherPlayers.size() == 3 && entry.getKey().getNickname().equals(otherPlayers.get(2).getNickname())) {
                addPoint(entry.getValue(), pedina3);
            }

        }
        c++;


        handCard.setFocusTraversable(true);
        handCard.requestFocus();
    }

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

    public void updateDeck(Deck deck) {

        //System.out.print("\n (Deck updated) \n");

        Stack<GoldCard> goldCardDeck = deck.getGoldDeck();
        Stack<ResourceCard> resourceCardDeck = deck.getResourceDeck();

        currentDeckGoldCard = deck.getGoldDeck().get(deck.getGoldDeck().size() - 1);
        String goldDeckCardOne = currentDeckGoldCard.toString().substring(6, 9);
        goldDeckCard.setImage(pathBack(goldDeckCardOne));

        currentDeckResourceCard = deck.getResourceDeck().get(deck.getResourceDeck().size() - 3);
        String resourceDeckCardOne = currentDeckResourceCard.toString().substring(6, 9);
        resourceDeckCard.setImage(pathBack(resourceDeckCardOne));
    }

    public void updateError(String error) {
        errore = true;
        showAlert(error);
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

            System.out.println("\nFront1: " + handCard1URL);
            System.out.println("\nFront2: " + handCard2URL);
            System.out.println("\nFront3: " + handCard3URL);

        } else if (event.getCode() == KeyCode.F && fPressed == 0) {

            // if (currentSelected != null)
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

            System.out.println("\nBack: " + handCard1URL);
            System.out.println("\nBack: " + handCard2URL);
            System.out.println("\nBack: " + handCard3URL);
        }
    }

    private void checkErrorOrElse(Runnable onError, Runnable success, Runnable finallyCallback)
    {
        Thread checker = new Thread(()->{

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (errore)
            {
                errore = false;
                Platform.runLater(()->onError.run());
            }
            else
                Platform.runLater(()->success.run());

            Platform.runLater(()->finallyCallback.run());

        });

        checker.start();

    }

    public void setInitialCard(InitialCard initialCard1) {
        int x, y;
        int width = 29;
        int height = 30;
        int position = 0; // 0=up left, 1=up right, 2=bottom left, 3=bottom right
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
                    virtualClient.sendCommand(new PlaceCommand(virtualClient.getClientId(), miniModel.getGameId(), myPlayer, initialCard1, currentSelected, currentAngle, currentSelectedFrontUp));


                    checkErrorOrElse(()->{
                        Platform.runLater(()->{
                            currentSelectedImage = null;
                            isHandCardSelected = false;
                        });
                    }, ()->{
                        Platform.runLater(()->currentSelectedImage.setVisible(false));

                        isCardPlaced = true;
                        cardDrawn = false;
                    }, ()->{
                        handCard1.setOpacity(1);
                        handCard2.setOpacity(1);
                        handCard3.setOpacity(1);
                    });

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }




            }

        });

        structurePane.getChildren().add(initialCard);


        for (String s : initialCardCard.getFrontCorners()) {
            if (!Objects.equals(s, "NULL") && position == 0) { // TOP LEFT
                Rectangle rectangle = new Rectangle(x, y, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
            } else if (!Objects.equals(s, "NULL") && position == 1) { // TOP RIGHT
                Rectangle rectangle = new Rectangle(x + 82, y, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
            } else if (!Objects.equals(s, "NULL") && position == 2) { // BOTTOM RIGHT
                Rectangle rectangle = new Rectangle(x + 82, y + 44, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
            } else if (!Objects.equals(s, "NULL") && position == 3) { // BOTTOM LEFT
                Rectangle rectangle = new Rectangle(x, y + 44, width, height);
                rectangleList.add(rectangle);
                rectangle.setFill(Color.web("#808080", 0.2));
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

    // mushroomsPoints, leafPoints, wolfPoints, butterflyPoints, featherPoints,
    // manuscriptPoints, potionPoints
    public void addPoints(String obj) {
        int valueInteger = 0;
        String valueString;
        switch (obj) {
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

    public void setConnectionType(String tipoDiConnessione) {
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
 
    public void showInitialStage(Game game) {
        viewFactory.showInitialStage(this.miniModel, this.virtualClient, game);
    }

    public void showStartGame(){
        viewFactory.showStartGame(miniModel, virtualClient, this);
    }

    public void showJoinGame(){
        viewFactory.showJoinGame(miniModel, virtualClient, this);
    }

    public void showWait(){
        viewFactory.showWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pedina = new ImageView();
        pedina1 = new ImageView();
        pedina2 = new ImageView();
        pedina3 = new ImageView();

        ChooseSender.setValue("All");
        boardPane.getChildren().add(pedina);

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

        scrollPaneOthers1.setHvalue(hCenter);
        scrollPaneOthers1.setVvalue(vCenter);

        scrollPaneOthers2.setHvalue(hCenter);
        scrollPaneOthers2.setVvalue(vCenter);


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
            // focus per funzione di back
            handCard.setFocusTraversable(true);
            handCard.requestFocus();
        });



    }
}
