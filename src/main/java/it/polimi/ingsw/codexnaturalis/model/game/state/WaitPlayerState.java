package it.polimi.ingsw.codexnaturalis.model.game.state;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class WaitPlayerState extends State {
    public WaitPlayerState(Game game) {
        super(game);
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        if (isFull()) {
            throw new IllegalCommandException("Game already full");
        }
        createNewPlayers(nickname, color);
    }

    @Override
    public void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Game not set up yet");
    }

    private void createNewPlayers(String nickname, Color color) {
        super.game.getPlayers().get(super.game.getPlayers().size() - 1).setNickname(nickname);
        super.game.getPlayers().get(super.game.getPlayers().size() - 1).setColor(color);
        if (isFull()) {
            super.game.setState(new WaitPlayerState(super.game));
        } else {
            super.game.setState(new PlacedCardState(super.game));
        }
    }

    private boolean isFull() {
        if (super.game.getPlayers().size() == super.game.getNumPlayers()) {
            return true;
        }
        return false;
    }

    @Override
    public void placedCard(Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        throw new IllegalCommandException("Can't place card yet");
    }

    @Override
    public void drawnCard(String type, String id) throws IllegalCommandException {
        throw new IllegalCommandException("Can't draw card yet");
    }

    @Override
    public void matchEnded() throws IllegalCommandException {
        throw new IllegalCommandException("Game not started yet");
    }
}