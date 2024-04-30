package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
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
import java.util.ResourceBundle;
import java.util.Stack;

public class Game implements Initializable {

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
    private ImageView SO;

    @FXML
    private ImageView color;

    @FXML
    private ImageView initialCard;

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
    Deck deck;
    private Stack<ResourceCard> resourceDeck;
    String cartaDeckRes1;
    String cartaDeckRes2;
    private Stack<GoldCard> goldDeck;
    String cartaDeckGold1;
    String cartaDeckGold2;

    @FXML
    private HBox card1Box;

    DraggableMaker draggableMaker = new DraggableMaker();

    //For testing

    @FXML
    private Pane panino;

    @FXML
    void addCard(MouseEvent event) {
        String imagePath =  "/it/polimi/ingsw/codexnaturalis/FrontCards/G18f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        Image image = new Image(imageStream);

        ImageView imageView = new ImageView(image);
        panino.getChildren().add(imageView);

        imageView.setFitWidth(144);
        imageView.setFitHeight(98);
        imageView.setLayoutX(279);
        imageView.setLayoutY(442);

        String imagePath1 =  "/it/polimi/ingsw/codexnaturalis/FrontCards/G19f.jpg";
        InputStream imageStream1 = getClass().getResourceAsStream(imagePath1);
        assert imageStream1 != null;
        Image image1 = new Image(imageStream1);

        ImageView imageView1 = new ImageView(image1);
        panino.getChildren().add(imageView1);

        imageView1.setFitWidth(144);
        imageView1.setFitHeight(98);
        imageView1.setLayoutX(497);
        imageView1.setLayoutY(442);

        String imagePath2 =  "/it/polimi/ingsw/codexnaturalis/FrontCards/G36f.jpg";
        InputStream imageStream2 = getClass().getResourceAsStream(imagePath2);
        assert imageStream2 != null;
        Image image2 = new Image(imageStream2);

        ImageView imageView2 = new ImageView(image2);
        panino.getChildren().add(imageView2);

        imageView2.setFitWidth(144);
        imageView2.setFitHeight(98);
        imageView2.setLayoutX(497);
        imageView2.setLayoutY(558);



    }



    public void setUp(String username, String colorSelected, String objSelected, String cartaIniziale, String res1, String res2, String gold, Deck deck1){

        nickname2.setText(username);
        SO.setImage(path(objSelected));
        initialCard.setImage(path(cartaIniziale));
        card1.setImage(path(res1));
        card2.setImage(path(res2));
        card3.setImage(path(gold));
        color.setImage(symbolPath(colorSelected));

        this.deck = deck1;

        resourceDeck = deck1.getResourceDeck();

        cartaDeckRes1 = resourceDeck.get(0).toString().substring(6,9);
        cartaDeckRes2 = resourceDeck.get(1).toString().substring(6,9);
        RD1.setImage(path(cartaDeckRes1));
        RD2.setImage(path(cartaDeckRes2));

        goldDeck = deck1.getGoldDeck();

        cartaDeckGold1 = goldDeck.get(0).toString().substring(6,9);
        cartaDeckGold2 = goldDeck.get(1).toString().substring(6,9);
        GD1.setImage(path(cartaDeckGold1));
        GD2.setImage(path(cartaDeckGold2));





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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/codexnaturalis/view/gui/provaInGame.fxml"));
        try {
            panino.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialCard.setLayoutX(388);
        initialCard.setLayoutY(500);

        color.setLayoutX(445);
        color.setLayoutY(583);

        panino.getChildren().addAll(initialCard, color);

        draggableMaker.makeDraggable(card1Box, card1);

    }

    public void spostaStruttura(){

        for (int i=1; i < panino.getChildren().size(); i++){
            Node elemento = panino.getChildren().get(i);
            String element = elemento.toString();
            element = element.substring(13, element.indexOf(","));

            System.out.println(element + "\n");
        }

    }

}