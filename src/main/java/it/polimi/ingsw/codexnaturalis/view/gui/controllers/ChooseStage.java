package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.ChooseCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ChooseStage implements Initializable {

    @FXML
    private ImageView initialCardBack;

    @FXML
    private ImageView initialCardFront;

    @FXML
    private ImageView objectiveCard1;

    @FXML
    private ImageView objectiveCard2;



    private MiniModel miniModel;
    private VirtualClient virtualClient;
    private Hand hand;
    private Player player;
    int index = 0;

    private ViewFactory viewFactory;

    Boolean side;
    ObjectiveCard objectiveCard;

    public void setUP(Hand hand, MiniModel miniModel, VirtualClient virtualClient) {
        this.hand = hand;
        this.miniModel = miniModel;
        this.virtualClient = virtualClient;

    }

    public void setUp(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    @FXML
    void initialCardBackClicked(MouseEvent event) {
        side = false;
        initialCardBack.setOpacity(1);
        initialCardFront.setOpacity(.5);
    }

    @FXML
    void initialCardFrontClicked(MouseEvent event) {
        side = true;
        initialCardBack.setOpacity(.5);
        initialCardFront.setOpacity(1);
    }

    @FXML
    void objectiveCard1Clicked(MouseEvent event) {
        objectiveCard1.setOpacity(1);
        objectiveCard2.setOpacity(.5);
        objectiveCard = (ObjectiveCard) viewFactory.getMinimodel().getPlayerHands().get(index).getChooseBetweenObj().getFirst();
    }

    @FXML
    void objectiveCard2Clicked(MouseEvent event) {

        objectiveCard1.setOpacity(1);
        objectiveCard2.setOpacity(.5);
        objectiveCard = (ObjectiveCard) viewFactory.getMinimodel().getPlayerHands().get(index).getChooseBetweenObj().get(1);
    }

    @FXML
    void play(MouseEvent event) throws RemoteException {
        viewFactory.getVirtualClient().sendCommand(new ChooseCommand(viewFactory.getVirtualClient().getClientId(), viewFactory.getMinimodel().getGameId(), player, side, objectiveCard ));
        Stage stage = (Stage) initialCardBack.getScene().getWindow();
        //stage.hide();
    }

    public void inizializza(){

            String objCard1;
            String objCard2;
            String initialCard;
            String nickname = viewFactory.getNickname();
            int c = 0;


            for(Player player: viewFactory.getMinimodel().getPlayers()){
                if(player.getNickname().equals(nickname)){
                    this.player = player;
                    index = c;
                }
                c++;
            }


            objCard1 = viewFactory.getMinimodel().getPlayerHands().get(index).getChooseBetweenObj().get(0).getIdCard();
            objCard2 = viewFactory.getMinimodel().getPlayerHands().get(index).getChooseBetweenObj().get(1).getIdCard();
            initialCard = viewFactory.getMinimodel().getPlayerHands().get(index).getInitCard().getIdCard();

            initialCardFront.setImage(pathFront(initialCard));
            initialCardBack.setImage(pathBack(initialCard));

            objectiveCard1.setImage(pathFront(objCard1));
            objectiveCard2.setImage(pathFront(objCard2));


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
