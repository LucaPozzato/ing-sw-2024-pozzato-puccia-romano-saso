package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class WaitPlayerState extends ControllerState {
    public WaitPlayerState(Game game) {
        super(game);
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        if (nickname.equals("")) {
            throw new IllegalCommandException("Nickname can't be empty");
        }

        for (Player p : super.game.getPlayers()) {
            if (p.getColor() != null && p.getColor().equals(color)) {
                throw new IllegalCommandException("Color already taken");
            }
            if (p.getNickname() != null && p.getNickname().equals(nickname)) {
                throw new IllegalCommandException("Nickname already taken");
            }
        }

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
        super.game.addParticipant();

        if (isFull())
            super.game.setState(new ChooseSetUpState(super.game, null));
        else
            super.game.setState(new WaitPlayerState(super.game));
    }

    private boolean isFull() {
        if (super.game.getPlayers().size() == super.game.getNumParticipants()) {
            return true;
        }
        return false;
    }

    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        throw new IllegalCommandException("Can't place card yet");
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        throw new IllegalCommandException("Can't draw card yet");
    }
}