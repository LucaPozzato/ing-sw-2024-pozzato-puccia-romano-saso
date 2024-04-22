package it.polimi.ingsw.codexnaturalis;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.controller.ServerController;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.view.GameCli;

public class GameMain {

    // Occorre prevedere di stanziare anche le carte starter nel momento in cui
    // inialize() viene invocata e il giocatore iniziale mi ha detto quanti
    // giocatori ci sono

    public static void main(String[] args) throws Exception {
        GameCli cli = new GameCli();
        ServerController controller = new ServerController();
        cli.clear();

        controller.initGame("Luca", "Blue", 3);
        controller.joinGame("Angels", "Red");
        controller.joinGame("Nick", "Yellow");

        List<Card> objCards = controller.getGame().getHandByPlayer(controller.getGame().getPlayers().get(0))
                .getChooseBetweenObj();
        Card initCard = controller.getGame().getHandByPlayer(controller.getGame().getPlayers().get(0)).getInitCard();
        cli.updateChooseObjectives(List.of(((ObjectiveCard) objCards.get(0)).drawDetailedVisual(null),
                ((ObjectiveCard) objCards.get(1)).drawDetailedVisual(null)));
        cli.updateInitialCard(((InitialCard) initCard).drawFullCard());
        cli.printInitial();
        cli.clear();
        try {
            cli.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(100000);
    }
}