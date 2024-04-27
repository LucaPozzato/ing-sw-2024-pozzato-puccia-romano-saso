package it.polimi.ingsw.codexnaturalis.view.gui.controllers;


import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;

public class GameInizializer implements Initializable {


    @FXML
    private Label Errore;

    @FXML
    private ImageView initialCardElement;

    @FXML
    private ImageView res1Element;

    @FXML
    private ImageView res2Element;

    @FXML
    private ImageView goldELement;

    @FXML
    private ImageView OB1;

    @FXML
    private ImageView OB2;

    @FXML
    private ImageView blue;

    @FXML
    private ImageView green;

    @FXML
    private ImageView red;

    @FXML
    private ImageView yellow;

    @FXML
    private Button startGame;

    String nickname;
    String colorSelected;
    String objectiveSelected;
    String cartaIniziale;
    String resourceElement1;
    String resourceElement2;
    String goldElement;
    GoldParser goldParser = new GoldParser();
    ResourceParser resourceParser  = new ResourceParser();
    Deck deck = new Deck(goldParser.parse(),resourceParser.parse());
    ViewFactory viewFactory = new ViewFactory();

    public void displayName(String username) {
        nickname = username;
    }


    @FXML
    void OB1Selected(MouseEvent event) {
        objectiveSelected = "OB1";
        OB1.setOpacity(1);
        OB2.setOpacity(.3);
        System.out.println(objectiveSelected + "\n");
    }

    @FXML
    void OB2Selected(MouseEvent event) {
        objectiveSelected = "OB2";
        OB2.setOpacity(1);
        OB1.setOpacity(.3);
        System.out.println(objectiveSelected + "\n");
    }

    @FXML
    void blueSelected(MouseEvent event) {
        colorSelected = "pedinaBlu";
        blue.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        green.setOpacity(0.3);
        System.out.println(colorSelected + "\n");
    }

    @FXML
    void greenSelected(MouseEvent event) {
        colorSelected = "pedinaVerde";
        green.setOpacity(1);
        red.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
        System.out.println(colorSelected + "\n");

    }

    @FXML
    void redSelected(MouseEvent event) {
        colorSelected = "pedinaRossa";
        red.setOpacity(1);
        green.setOpacity(0.3);
        yellow.setOpacity(0.3);
        blue.setOpacity(0.3);
        System.out.println(colorSelected + "\n");
    }

    @FXML
    void yellowSelected(MouseEvent event) {
        colorSelected = "pedinaGialla";
        yellow.setOpacity(1);
        red.setOpacity(0.3);
        green.setOpacity(0.3);
        blue.setOpacity(0.3);
        System.out.println(colorSelected + "\n");
    }

    @FXML
    void goToGame(MouseEvent event) {

        //Manca la scelta delle objective... le seleziona a caso..
        if (objectiveSelected == null && colorSelected == null) {
            System.out.println("Seleziona una carta Objective e un colore");
            Errore.setText("Choose a color and an objective card");
            Errore.setVisible(true);
        } else if (colorSelected == null) {
            System.out.println("Seleziona un colore");
            Errore.setText("Choose a color");
            Errore.setVisible(true);
        } else if (objectiveSelected == null) {
            System.out.println("Seleziona una carta Objective");
            Errore.setText("Choose an objective card");
            Errore.setVisible(true);
        } else {
            Errore.setVisible(false);
            Stage stage = (Stage) startGame.getScene().getWindow(); //trick for getting current stage
            viewFactory.closeStage(stage);

            //No "OP1f" ma fai funzione per carte obiettivo..
            viewFactory.showGame(nickname, colorSelected, "OP1", cartaIniziale, resourceElement1, resourceElement2, goldElement, deck);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Generazione carta iniziale randomica per utente e inserimento

        List<InitialCard> initialCardList;
        InitialParser initialParser = new InitialParser();
        initialCardList = initialParser.parse();

        Random rand = new Random();
        InitialCard randomInitial = initialCardList.get(rand.nextInt(initialCardList.size())); //Carta iniziale scelta a caso
        initialCardList.remove(randomInitial); //rimuoviamola dall'elenco delle  carte per gli altri utenti
        //System.out.printf(String.valueOf(randomInitial));


        cartaIniziale = randomInitial.toString().substring(20,23);

        String imagePath =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + cartaIniziale+ ".jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            System.out.println("Errore: risorsa non trovata nel percorso: " + imagePath);
        } else {
            Image image = new Image(imageStream);
            initialCardElement.setImage(image);
        }

        //Generazione Gold card e Resource cards

        Stack<ResourceCard> resourceDeck;

        deck.shuffleResourceDeck();
        resourceDeck = deck.getResourceDeck();

        /*System.out.println("Contents of the stack:");
        for (ResourceCard card : resourceDeck) {
            System.out.println(card);
        }*/

        //Resource cards
        ResourceCard resourceRandom1 = deck.drawResourceCard();

        resourceElement1 = resourceRandom1.toString().substring(6,9);
        System.out.println("Elemento resource 1 " + resourceElement1);

        String imagePath1 =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + resourceElement1 + "f.jpg";
        InputStream imageStream1 = getClass().getResourceAsStream(imagePath1);
        if (imageStream1 == null) {
            System.out.println("Errore: risorsa non trovata nel percorso: " + imagePath1);
        } else {
            Image image1 = new Image(imageStream1);
            res1Element.setImage(image1);
        }

        ResourceCard resourceRandom2 = deck.drawResourceCard();

        resourceElement2 = resourceRandom2.toString().substring(6,9);
        System.out.println("Elemento resource 2 "  + resourceElement2);


        String imagePath2 =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + resourceElement2+ "f.jpg";
        InputStream imageStream2 = getClass().getResourceAsStream(imagePath2);
        if (imageStream2 == null) {
            System.out.println("Errore: risorsa non trovata nel percorso: " + imagePath2);
        } else {
            Image image2 = new Image(imageStream2);
            res2Element.setImage(image2);
        }

        System.out.println("\n");
        for (ResourceCard card : resourceDeck)
            System.out.println(card);





        //Gold card
        deck.shuffleGoldDeck();
        GoldCard goldRandom = deck.drawGoldCard();

        goldElement = goldRandom.toString().substring(6,9);
        System.out.println("Elemento gold " + goldElement);

        String imagePath3 =  "/it/polimi/ingsw/codexnaturalis/FrontCards/" + goldElement+ "f.jpg";
        InputStream imageStream3 = getClass().getResourceAsStream(imagePath3);
        if (imageStream3 == null) {
            System.out.println("Errore: risorsa non trovata nel percorso: " + imagePath3);
        }
        else {
            Image image3 = new Image(imageStream3);
            goldELement.setImage(image3);
        }
        }

}







