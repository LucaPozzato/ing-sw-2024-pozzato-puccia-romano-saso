package it.polimi.ingsw.codexnaturalis.controller;

import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class ChooseSetUpState extends ControllerState {
    Map<Player, Boolean> setUpMap = new HashMap<>();

    public ChooseSetUpState(Game game, Map<Player, Boolean> setUpMap) {
        super(game);
        if (setUpMap != null)
            this.setUpMap = setUpMap;
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        throw new IllegalCommandException("Game already joined");
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

    public void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        if (setUpMap.keySet().size() > 0 && setUpMap.containsKey(player) && setUpMap.get(player).equals(true)) {
            throw new IllegalCommandException("Player already made his choice");
        } else {
            Hand hand = super.game.getHandByPlayer(player);
            InitialCard initCard = hand.getInitCard();
            super.game.getStructureByPlayer(player).placeCard(null, initCard, null, side);

            hand.setSecretObjective(objCard);
            setUpMap.put(player, true);
        }

        if (setUpMap.keySet().size() == super.game.getNumPlayers())
            super.game.setState(new PlacedCardState(super.game));
        else
            super.game.setState(new ChooseSetUpState(super.game, setUpMap));
    }
}