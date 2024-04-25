package it.polimi.ingsw.codexnaturalis;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.CliVerifier;
import it.polimi.ingsw.codexnaturalis.view.GameCli;

public class GameMain {

    // Occorre prevedere di stanziare anche le carte starter nel momento in cui
    // inialize() viene invocata e il giocatore iniziale mi ha detto quanti
    // giocatori ci sono

    public static void main(String[] args) throws Exception {
        GameCli cli = new GameCli();
        CliVerifier controller = new CliVerifier();

        cli.clear();
        (new GameMain().new ReadThread(controller, cli)).start();

        controller.initGame("Luca", "Blue", 2);
        controller.joinGame("Nick", "Yellow");
        cli.updateStructure(List.of("", ""));
        controller.move("Nick", "choose: f, 1");

        List<Card> objCards = controller.getGame().getHandByPlayer(controller.getGame().getPlayers().get(0))
                .getChooseBetweenObj();
        Card initCard = controller.getGame().getHandByPlayer(controller.getGame().getPlayers().get(0)).getInitCard();
        cli.updateChooseObjectives(List.of(((ObjectiveCard) objCards.get(0)).drawDetailedVisual(null),
                ((ObjectiveCard) objCards.get(1)).drawDetailedVisual(null)));
        cli.updateInitialCard(((InitialCard) initCard).drawFullCard());
        cli.printInitial();
    }

    class ReadThread extends Thread {
        CliVerifier controller;
        GameCli cli;

        ReadThread(CliVerifier controller, GameCli cli) {
            super();
            this.controller = controller;
            this.cli = cli;
        }

        public void run() {
            try {
                while (true) {
                    String move = System.console().readLine();
                    sendMove(move);
                    cli.clear();
                    cli.print();
                }
            } catch (IllegalCommandException e) {
                System.out.println(e.getMessage());
            }
        }

        private void sendMove(String move) throws IllegalCommandException {
            Player player = controller.getGame().getPlayers().get(0);
            Game game = controller.getGame();

            controller.move("Luca", move);

            cli.updateStructure(List.of(game.getStructureByPlayer(player).draw(), ""));
            cli.updateScoreBoard("luca = 0 | nick = 0");
            cli.updateHand(game.getHandByPlayer(player).drawCardsHand());
            cli.updateBoard(game.getBoard().drawUncoveredCards());
            cli.updateDecks(game.getDeck().draw());
            cli.updateResources(List.of(game.getStructureByPlayer(player).getVisibleResources(), ""));
            cli.updateObjectives(List.of(game.getHandByPlayer(player).getSecretObjective().drawDetailedVisual(true),
                    game.getBoard().getCommonObjectives().get(0).drawDetailedVisual(true),
                    game.getBoard().getCommonObjectives().get(1).drawDetailedVisual(true)));
        }
    }
}