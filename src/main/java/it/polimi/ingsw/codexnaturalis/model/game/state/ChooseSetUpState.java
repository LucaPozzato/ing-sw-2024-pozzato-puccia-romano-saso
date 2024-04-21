package it.polimi.ingsw.codexnaturalis.model.game.state;

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

public class ChooseSetUpState extends State {
    Map<Player, Boolean> setUpMap;

    public ChooseSetUpState(Game game) {
        super(game);
        setUpMap = new HashMap<>();
        for (Player player : super.game.getPlayers()) {
            setUpMap.put(player, false);
        }
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
        if (setUpMap.get(player).equals(true)) {
            throw new IllegalCommandException("Player already made his choice");
        } else {
            Hand hand = super.game.getHandByPlayer(player);
            InitialCard initCard = hand.getInitCard();
            super.game.getStructureByPlayer(player).placeCard(null, initCard, null, side);
            hand.removeCard(initCard);

            hand.setSecretObjective(objCard);
            setUpMap.put(player, true);
        }

        if (setUpMap.containsValue(false))
            super.game.setState(new ChooseSetUpState(super.game));
        else
            super.game.setState(new PlacedCardState(super.game));
    }
}