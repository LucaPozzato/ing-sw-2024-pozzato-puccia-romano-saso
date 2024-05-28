package it.polimi.ingsw.codexnaturalis.view.gui;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiApp implements View {

    private VirtualClient virtualClient;
    private MiniModel miniModel;


    private Game game;
    private FXMLLoader gameFxmlLoader;
    private Stage mainStage;
    private Stage waitingStage;
    private Stage chooseStage;
    private Stage endGameStage;

    int p = 0;


    private String currentState;
    ViewFactory viewFactory;
    private static final CountDownLatch latch = new CountDownLatch(1);

    public GuiApp(VirtualClient virtualClient, MiniModel miniModel) {
        this.virtualClient = virtualClient;
        this.miniModel = miniModel;

    }

    @Override
    public void run() {
        viewFactory = new ViewFactory(miniModel,virtualClient);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            createGameStage();
            viewFactory.showInitialStage();

        });

    }

    @Override
    public void updateChat(Chat chat) {
        game.updateChat(chat);
    }

    @Override
    public void updateState(String state) {

        game.updateState(state);

        if (Objects.equals(state, "Wait")){
            Platform.runLater(()-> {
                if (chooseStage != null)
                    chooseStage.hide();
                waitingStage = viewFactory.showWait();
            });
        }
        else if (Objects.equals(state, "Choose")){
            Platform.runLater(()-> {
                waitingStage.hide();

                if (ViewFactory.staticJoinGame != null)
                    ViewFactory.staticJoinGame.close();

                try {
                    chooseStage = viewFactory.showChoose();
                } catch (IOException e) {
                }
            });

        }
        //RIVEDILO per bene
        else if (Objects.equals(state, "End")){
            Platform.runLater(()-> {
                waitingStage.hide();

                viewFactory.showEndGame();
            });

        }

        else{
            Platform.runLater(()-> {

                mainStage.show();
                if (chooseStage != null)
                    chooseStage.hide();
                if (waitingStage != null)
                 waitingStage.hide();

            });

        }
    }

    private void createGameStage()
    {
        gameFxmlLoader = new FXMLLoader(getClass().getResource("gameStage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(gameFxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainStage = new Stage();
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.setOnCloseRequest(e->{e.consume();});

        game = gameFxmlLoader.getController();
        game.setUP(miniModel, virtualClient);


    }

    @Override
    public void updateMyPlayer(Player player) {
        Platform.runLater(() -> {
            game.updateMyPlayer(player);
        });


    }

    @Override
    public void updateWinners(List<Player> winners) {
        Platform.runLater(() -> {
            game.updateWinners(winners);
        });

    }

    @Override
    public void updateCurrentPlayer(Player player) {
        Platform.runLater(() -> {
            game.updateCurrentPlayer(player);
        });

    }

    @Override
    public void updatePlayers(List<Player> players) {
        Platform.runLater(() -> {
            game.updatePlayers(players);
        });

    }

    @Override
    public void updateStructures(List<Structure> structures) {
        Platform.runLater(() -> {
            game.updateStructures(structures);
        });

    }

    @Override
    public void updateHand(List<Hand> hands) {

        Platform.runLater(() -> {
            game.updateHand(hands);
        });


    }

    @Override
    public void updateBoard(Board board) {
        Platform.runLater(() -> {
            game.updateBoard(board);
        });


    }

    @Override
    public void updateDeck(Deck deck) {
        Platform.runLater(() -> {
            game.updateDeck(deck);
        });

    }

    @Override
    public void updateError(String error) {
        Platform.runLater(() -> {
            game.updateError(error);
        });

    }

}
