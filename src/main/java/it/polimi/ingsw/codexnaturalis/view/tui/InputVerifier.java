package it.polimi.ingsw.codexnaturalis.view.tui;

import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.ChooseCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.DrawCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.JoinGameCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.PlaceCommand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class InputVerifier {
    private MiniModel miniModel;

    public InputVerifier(MiniModel miniModel) {
        this.miniModel = miniModel;
    }

    public Command move(Player player, String command) throws IllegalCommandException {
        for (Player p : miniModel.getPlayers()) {
            if (p.equals(player)) {
                player = p;
                break;
            }
        }
        String[] commandArray = command.split(": ");
        String[] parameters = commandArray[1].split(", ");

        switch (commandArray[0]) {
            case "CHOOSE":
                Boolean side = false;
                Integer objIndex = 0;
                ObjectiveCard objCard = null;

                if (parameters.length != 2)
                    throw new IllegalCommandException("Invalid number of parameters");

                if (parameters[0].equals("FRONT"))
                    side = true;
                else if (parameters[0].equals("BACK"))
                    side = false;
                else {
                    throw new IllegalCommandException("Invalid side choice");
                }

                if (parameters[1].equals("1") || parameters[1].equals("ONE"))
                    objIndex = 0;
                else if (parameters[1].equals("2") || parameters[1].equals("TWO"))
                    objIndex = 1;
                else {
                    throw new IllegalCommandException("Invalid objective card choice");
                }

                objCard = (ObjectiveCard) miniModel.getPlayerHands().get(miniModel.getPlayers().indexOf(player))
                        .getChooseBetweenObj().get(objIndex);
                return new ChooseCommand(player, side, objCard);

            case "PLACE":
                Card placeThis = null;
                Card father = null;

                // checks if the placed card is in player's hand
                for (Card cards : miniModel.getPlayerHands().get(miniModel.getPlayers().indexOf(player))
                        .getCardsHand()) {
                    if (cards.getIdCard().equals(parameters[0])) {
                        placeThis = cards;
                    }
                }

                if (placeThis.equals(null)) {
                    throw new IllegalCommandException("Placed card not in hand");
                }

                // checks if the placed card's father is in the structure

                List<String> list = miniModel.getPlayerStructures().get(miniModel.getPlayers().indexOf(player))
                        .getCardToCoordinate()
                        .keySet()
                        .stream()
                        .map(card -> card.getIdCard())
                        .collect(Collectors.toList());

                for (String ids : list) {
                    if (ids.equals(parameters[1])) {
                        for (Card card : miniModel.getPlayerStructures().get(miniModel.getPlayers().indexOf(player))
                                .getCardToCoordinate().keySet()) {
                            if (card.getIdCard().equals(parameters[1])) {
                                father = card;
                            }
                        }
                    }
                }

                if (father.equals(null)) {
                    throw new IllegalCommandException("Father absent in structure");
                }

                return new PlaceCommand(player, father, placeThis, parameters[2], Boolean.parseBoolean(parameters[3]));

            case "DRAW":
                Card card = null;
                String fromDeck = "";

                if (parameters.length != 1)
                    throw new IllegalCommandException("Invalid number of parameters");

                if (parameters[0].contains("R")) {
                    if (parameters[0].contains("XX"))
                        fromDeck = "RESOURCE";
                    else {
                        for (Card cards : miniModel.getBoard().getUncoveredCards()) {
                            if (cards.getIdCard().equals(parameters[0]))
                                card = cards;
                        }
                        if (card.equals(null)) {
                            throw new IllegalCommandException("Invalid card ID");
                        }
                    }
                } else if (parameters[0].contains("G")) {
                    if (parameters[0].contains("XX"))
                        fromDeck = "GOLD";
                    else {
                        for (Card cards : miniModel.getBoard().getUncoveredCards()) {
                            if (cards.getIdCard().equals(parameters[0]))
                                card = cards;
                        }
                        if (card.equals(null)) {
                            throw new IllegalCommandException("Invalid card ID");
                        }
                    }
                } else {
                    throw new IllegalCommandException("Invalid card ID");
                }

                return new DrawCommand(player, card, fromDeck);

            case "JOIN":
                Color color = null;
                if (parameters.length != 3)
                    throw new IllegalCommandException("Invalid number of parameters");

                switch (parameters[2]) {
                    case "RED", "R":
                        color = Color.RED;
                        break;

                    case "BLUE", "B":
                        color = Color.BLUE;
                        break;

                    case "GREEN", "G":
                        color = Color.GREEN;
                        break;

                    case "YELLOW", "Y":
                        color = Color.YELLOW;
                        break;

                    default:
                        break;
                }

                if (color.equals(null)) {
                    throw new IllegalCommandException("Invalid color choice");
                }

                return new JoinGameCommand(Integer.parseInt(parameters[0]), parameters[1], color);

            case "CREATE":
                // TODO: implement create command
                // return new CreateCommand();

            default:
                throw new IllegalCommandException("Invalid command");
        }
    }
}