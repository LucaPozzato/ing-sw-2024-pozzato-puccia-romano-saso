package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class DrawCommand extends Command {
    @Serial
    private static final long serialVersionUID = 572903618492730L;
    private Player player;
    private Card card;
    private String fromDeck;
    private Integer gameId;

    public DrawCommand(Integer gameId, Player player, Card card, String fromDeck) {
        this.gameId = gameId;
        this.player = player;
        this.card = card;
        this.fromDeck = fromDeck;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.drawnCard(player, card, fromDeck);
    }

    @Override
    public int getGameId() {
        return this.gameId;
    }
}