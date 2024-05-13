package it.polimi.ingsw.codexnaturalis.view.gui;

import java.rmi.RemoteException;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.Game;
import javafx.application.Platform;

public class GuiApp implements View {

    private Game game;
    VirtualClient virtualClient;
    MiniModel miniModel;

    public GuiApp(VirtualClient virtualClient, MiniModel miniModel) {
        this.virtualClient = virtualClient;
        this.miniModel = miniModel;
    }

    @Override
    public void run() {
        game = new Game();
        new Thread(() -> {
            game.run();
        }).start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        try {
            virtualClient.sendCommand(new CreateGameCommand(virtualClient.getClientId(), 0, "edo", "", Color.RED, 2));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateChat(Chat chat) {

    }

    @Override
    public void updateState(String state) {
        System.out.println("got state in guiapp");
        Platform.runLater(() -> {
            game.updateState(state);
        });
    }

    @Override
    public void updateMyPlayer(Player player) {

    }

    @Override
    public void updateWinners(List<Player> winners) {

    }

    @Override
    public void updateCurrentPlayer(Player player) {

    }

    @Override
    public void updatePlayers(List<Player> players) {

    }

    @Override
    public void updateStructures(List<Structure> structures) {

    }

    @Override
    public void updateHand(List<Hand> hands) {

    }

    @Override
    public void updateBoard(Board board) {

    }

    @Override
    public void updateDeck(Deck deck) {

    }

    @Override
    public void updateError(String error) {
        System.out.print("Eroor");
    }

}
