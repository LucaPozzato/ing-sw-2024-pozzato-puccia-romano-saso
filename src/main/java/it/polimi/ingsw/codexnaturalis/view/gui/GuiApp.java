package it.polimi.ingsw.codexnaturalis.view.gui;

import java.io.IOException;
import java.util.List;
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

/**
 * GuiApp class is used to send updates to JavaFx thread.
 */
public class GuiApp implements View {

    private final VirtualClient virtualClient;
    private final MiniModel miniModel;
    private Game game;
    private Stage mainStage;
    private Stage waitingStage;
    private Stage chooseStage;
    private ViewFactory viewFactory;


    public GuiApp(VirtualClient virtualClient, MiniModel miniModel) {
        this.virtualClient = virtualClient;
        this.miniModel = miniModel;
    }

    /**
     * It's called when client starts the game with GUI option.
     * It is used to set up viewFactory and displays the initialStage stage
     */
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
        Platform.runLater(() -> {
            game.updateChat(chat);
        });
    }

    /**
     * Based on current stage it is used to manage the opening and closing of stages
     * @param state the state to set
     */
    @Override
    public void updateState(String state) {

        switch (state) {
            case "Wait" -> Platform.runLater(() -> {
                if (chooseStage != null)
                    chooseStage.hide();
                waitingStage = viewFactory.showWait();

                if (ViewFactory.staticJoinGame != null)
                    ViewFactory.staticJoinGame.close();

                if (ViewFactory.staticStartGame != null)
                    ViewFactory.staticStartGame.close();

            });
            case "Choose" -> Platform.runLater(() -> {
                waitingStage.hide();

                if (ViewFactory.staticJoinGame != null)
                    ViewFactory.staticJoinGame.close();

                try {
                    chooseStage = viewFactory.showChoose();
                } catch (IOException e) {
                }
            });
            case "End" -> Platform.runLater(() -> {
                waitingStage.hide();
                viewFactory.showEndGame();
            });
            default -> Platform.runLater(() -> {

                if (ViewFactory.staticRejoinGame != null)
                    ViewFactory.staticRejoinGame.close();

                mainStage.show();
                if (chooseStage != null)
                    chooseStage.hide();
                if (waitingStage != null)
                    waitingStage.hide();
            });
        }
    }

    /**
     * Used to create the gameStage stage, the one that clients are going to use to play the game
     */
    private void createGameStage()
    {
        FXMLLoader gameFxmlLoader = new FXMLLoader(getClass().getResource("gameStage.fxml"));
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
