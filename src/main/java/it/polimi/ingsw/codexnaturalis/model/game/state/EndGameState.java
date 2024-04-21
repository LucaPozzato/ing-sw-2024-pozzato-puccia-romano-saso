package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class EndGameState extends State {

    // TODO: implement methods that compute the points resulting from resource
    // objective for each player
    // TODO: implement method that declare the winner
    public EndGameState(Game game) {
        super(game);
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");

    }

    @Override
    public void chooseSetUp(Player nickName, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void placedCard(Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void drawnCard(String type, String id) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }
}
