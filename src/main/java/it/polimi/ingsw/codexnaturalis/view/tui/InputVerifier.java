package it.polimi.ingsw.codexnaturalis.view.tui;

import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.commands.*;

public class InputVerifier {
    private MiniModel miniModel;
    private VirtualClient client;

    public InputVerifier(MiniModel miniModel, VirtualClient client) {
        this.miniModel = miniModel;
        this.client = client;
    }

    public Command move(Player player, String command) throws IllegalCommandException {
        for (Player p : miniModel.getPlayers()) {
            if (p.equals(player)) {
                player = p;
                break;
            }
        }
        String[] commandArray = command.split(": ");
        if (commandArray.length != 2)
            throw new IllegalCommandException("Invalid command format");

        String commandString = commandArray[0].toUpperCase();
        String[] parameters = commandArray[1].split(", ");
        Color color = null;
        Boolean side = null;

        switch (commandString) {
            case "CREATE":
                color = null;

                if (parameters.length != 5)
                    throw new IllegalCommandException("Invalid number of parameters");

                switch (parameters[3].toUpperCase()) {
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
                        throw new IllegalCommandException("Invalid color choice");
                }

                try {
                    return new CreateGameCommand(client.getClientId(), Integer.parseInt(parameters[0]), parameters[1],
                            parameters[2], color, Integer.parseInt(parameters[4]));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case "JOIN":
                color = null;
                if (parameters.length != 4)
                    throw new IllegalCommandException("Invalid number of parameters");

                switch (parameters[3].toUpperCase()) {
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
                        throw new IllegalCommandException("Invalid color choice");
                }

                try {
                    return new JoinGameCommand(client.getClientId(), Integer.parseInt(parameters[0]), parameters[1],
                            parameters[2], color);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case "CHOOSE":
                side = null;
                Integer objIndex = 0;
                ObjectiveCard objCard = null;

                if (parameters.length != 2)
                    throw new IllegalCommandException("Invalid number of parameters");

                if (parameters[0].equals("FRONT") || parameters[0].equals("F"))
                    side = true;
                else if (parameters[0].equals("BACK") || parameters[0].equals("B"))
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
                try {
                    return new ChooseCommand(client.getClientId(), miniModel.getGameId(), player, side, objCard);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case "PLACE":
                Card placeThis = null;
                Card father = null;
                side = null;

                // checks if the placed card is in player's hand
                for (Card cards : miniModel.getPlayerHands().get(miniModel.getPlayers().indexOf(player))
                        .getCardsHand()) {
                    if (cards.getIdCard().equals(parameters[0])) {
                        placeThis = cards;
                    }
                }

                if (placeThis == null) {
                    throw new IllegalCommandException("Card to place not in hand");
                }

                if (parameters[3].equals("FRONT") || parameters[3].equals("F"))
                    side = true;
                else if (parameters[3].equals("BACK") || parameters[3].equals("B"))
                    side = false;
                else {
                    throw new IllegalCommandException("Invalid side choice");
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

                if (father == null) {
                    throw new IllegalCommandException("Bottom card not in structure");
                }

                // TODO: gameid
                try {
                    return new PlaceCommand(client.getClientId(), miniModel.getGameId(), player, father, placeThis,
                            parameters[2], side);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                        if (card == null) {
                            throw new IllegalCommandException("Card not in board");
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
                        if (card == null) {
                            throw new IllegalCommandException("Card not in board");
                        }
                    }
                } else {
                    throw new IllegalCommandException("Invalid card ID");
                }

                // TODO: gameid
                try {
                    return new DrawCommand(client.getClientId(), miniModel.getGameId(), player, card, fromDeck);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case "SEND":
                Player receiver = null;

                if (parameters.length == 2) {
                    for (Player p : miniModel.getPlayers()) {
                        if (p.getNickname().equals(parameters[1])) {
                            receiver = p;
                            break;
                        }
                    }
                    if (receiver == null) {
                        throw new IllegalCommandException("Receiver not found");
                    }
                } else if (parameters.length == 1) {
                    receiver = null;
                } else {
                    throw new IllegalCommandException("Invalid number of parameters");
                }

                try {
                    return new ChatCommand(client.getClientId(), miniModel.getGameId(), parameters[0], player,
                            receiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case "REJOIN":
                if (parameters.length != 3)
                    throw new IllegalCommandException("Invalid number of parameters");
                try {
                    return new RejoinGameCommand(client.getClientId(), Integer.parseInt(parameters[0]), parameters[1],
                            parameters[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            default:
                throw new IllegalCommandException("Invalid command");
        }
    }
}