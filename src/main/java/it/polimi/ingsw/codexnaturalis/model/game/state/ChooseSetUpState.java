package it.polimi.ingsw.codexnaturalis.model.game.state;

import java.util.HashMap;
import java.util.List;
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
    Map<Player, Boolean> playerSetUp;

    public ChooseSetUpState(Game game) {
        super(game);
        playerSetUp = new HashMap<>();
        // for()
        // playerSetUp.put()
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
        throw new IllegalCommandException("Game not ended yet");
    }

    public void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {

        if (!playerSetUp.get(player).equals(null)) {
            throw new IllegalCommandException("Player already made his choice");
        } else {
            Hand hand = super.game.getHandByPlayer(player);
            InitialCard initCard = hand.getInitCard();
            super.game.getStructureByPlayer(player).placeCard(null, initCard, null, side);
            hand.removeCard(initCard);

            List<Card> objList = hand.getChooseBetweenObj();
            if (objList.contains(objCard))
                hand.setSecretObjective(objCard);
            else
                throw new IllegalCommandException("Objective card not contained");
        }
    }
}