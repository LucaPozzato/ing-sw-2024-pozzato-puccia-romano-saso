package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.commands.ChooseCommand;
import it.polimi.ingsw.codexnaturalis.view.gui.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Optional;

/**
 * ChooseStage class is a javaFx controller used to manage chooseStage stage.
 * Players here are able to choose the initialCard side and the secrete objectiveCard.
 */
public class ChooseStage  {

    @FXML
    private ImageView initialCardBack, initialCardFront, objectiveCard1, objectiveCard2;

    private Player player;
    private int index = 0;
    private ViewFactory viewFactory;
    private Boolean side, objectiveCardSelected = false;
    private ObjectiveCard objectiveCard;


    public void setUp(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    /**
     * Selecting initialCard (front side) and changing it visually
     * @param event the mouse event that triggered this method
     */
    @FXML
    void initialCardBackClicked(MouseEvent event) {
        side = false;
        initialCardBack.setOpacity(1);
        initialCardFront.setOpacity(.5);
    }

    /**
     * Selecting initialCard (back side) and changing it visually
     * @param event the mouse event that triggered this method
     */
    @FXML
    void initialCardFrontClicked(MouseEvent event) {
        side = true;
        initialCardBack.setOpacity(.5);
        initialCardFront.setOpacity(1);
    }

    /**
     * Selecting objectiveCard1 and changing it visually
     * @param event the mouse event that triggered this method
     */
    @FXML
    void objectiveCard1Clicked(MouseEvent event) {
        objectiveCardSelected = true;
        objectiveCard1.setOpacity(1);
        objectiveCard2.setOpacity(.5);
        objectiveCard = (ObjectiveCard) viewFactory.getMinimodel().getPlayerHands().get(index).getChooseBetweenObj().getFirst();
    }

    /**
     * Selecting objectiveCard2 and changing it visually
     * @param event the mouse event that triggered this method
     */
    @FXML
    void objectiveCard2Clicked(MouseEvent event) {
        objectiveCardSelected = true;
        objectiveCard1.setOpacity(.5);
        objectiveCard2.setOpacity(1);
        objectiveCard = (ObjectiveCard) viewFactory.getMinimodel().getPlayerHands().get(index).getChooseBetweenObj().get(1);
    }

    /**
     * Used to choose the initial cards before a game using a ChooseCommand
     * @param event the mouse event that triggered this method
     * @throws RemoteException if a remote communication error occurs
     */
    @FXML
    void play(MouseEvent event) throws RemoteException {
        if(side == null || !objectiveCardSelected) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("What's happening?");
            alert.setContentText("Make sure to select both the options");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            viewFactory.getVirtualClient().sendCommand(new ChooseCommand(viewFactory.getVirtualClient().getClientId(), viewFactory.getMinimodel().getGameId(), player, side, objectiveCard ));
        }
    }

    /**
     * Used the return a front image when given the name of it
     * @param objectForImage object to use
     * @return image given an object
     */
    public Image pathFront(String objectForImage) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/FrontCards/" + objectForImage + "f.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    /**
     * Used the return a back image when given the name of it
     * @param objectForImage object to use
     * @return image given an object
     */
    public Image pathBack(String objectForImage) {
        String imagePath = "/it/polimi/ingsw/codexnaturalis/BackCards/" + objectForImage + "b.jpg";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        assert imageStream != null;
        return new Image(imageStream);
    }

    /**
     * Used to initialize the controller with the initialCard and the objectiveCards to choose from
     */
    public void initializeController(){
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
}
