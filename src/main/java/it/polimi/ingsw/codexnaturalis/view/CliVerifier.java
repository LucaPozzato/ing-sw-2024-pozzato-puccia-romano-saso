package it.polimi.ingsw.codexnaturalis.view;

import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class CliVerifier {
    private final Game game;

    public CliVerifier() {
        this.game = new Game(0);
    }

    // FIXME: delete after testing
    public Game getGame() {
        return game;
    }

    // TODΟ: method to add clients/observers to game

    public void move(String nickName, String command) {
        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(nickName)) {
                player = p;
                break;
            }
        }
        String[] commandArray = command.split(": ");
        String[] parameters = commandArray[1].split(", ");

        switch (commandArray[0]) {
            case "choose", "Choose", "CHOOSE":
                Boolean side = false;
                Integer objIndex = 0;
                ObjectiveCard objCard = null;

                if (parameters.length != 2)
                    game.throwException("Invalid number of parameters");

                if (parameters[0].equals("Front") || parameters[0].equals("F") || parameters[0].equals("f")
                        || parameters[0].equals("front") || parameters[0].equals("FRONT"))
                    side = true;
                else if (parameters[0].equals("Back") || parameters[0].equals("B") || parameters[0].equals("b")
                        || parameters[0].equals("back") || parameters[0].equals("BACK"))
                    side = false;
                else {
                    game.throwException("Invalid side choice");
                    break;
                }

                if (parameters[1].equals("1") || parameters[1].equals("one") || parameters[1].equals("ONE")
                        || parameters[1].equals("One"))
                    objIndex = 0;
                else if (parameters[1].equals("2") || parameters[1].equals("two") || parameters[1].equals("TWO")
                        || parameters[1].equals("Two"))
                    objIndex = 1;
                else {
                    game.throwException("Invalid objective card choice");
                    break;
                }

                try {
                    objCard = (ObjectiveCard) game.getHandByPlayer(player).getChooseBetweenObj().get(objIndex);
                    game.getState().chooseSetUp(player, side, objCard);
                } catch (IllegalCommandException e) {
                    game.throwException(e.getMessage());
                }
                break;

            case "place", "Place", "PLACE":
                Card placeThis = null;
                Card father = null;

                // Format check and replace
                for (int i = 0; i < 2; i++) {
                    if (parameters[i].startsWith("r"))
                        parameters[i] = parameters[i].replace("r", "R");
                    if (parameters[i].startsWith("g"))
                        parameters[i] = parameters[i].replace("g", "G");
                    if (parameters[i].startsWith("i"))
                        parameters[i] = parameters[i].replace("i", "I");
                }
                parameters[2] = parameters[2].toUpperCase();

                // checks if the placed card is in player's hand
                for (Card cards : game.getHandByPlayer(player).getCardsHand()) {
                    if (cards.getIdCard().equals(parameters[0])) {
                        placeThis = cards;
                    }
                }

                if (placeThis.equals(null)) {
                    game.throwException("Placed card not in hand");
                    break;
                }

                // checks if the placed card's father is in the structure

                List<String> list = game.getStructureByPlayer(player)
                        .getCardToCoordinate()
                        .keySet()
                        .stream()
                        .map(card -> card.getIdCard())
                        .collect(Collectors.toList());

                for (String ids : list) {
                    if (ids.equals(parameters[1])) {
                        for (Card card : game.getStructureByPlayer(player).getCardToCoordinate().keySet()) {
                            if (card.getIdCard().equals(parameters[1])) {
                                father = card;
                            }
                        }
                    }
                }

                if (father.equals(null)) {
                    game.throwException("Father absent in structure");
                    break;
                }

                // executes the translation and passes it to the model

                try {
                    game.getState().placedCard(player, father, placeThis, parameters[2],
                            Boolean.parseBoolean(parameters[3]));
                } catch (IllegalCommandException e) {
                    game.throwException(e.getMessage());
                }
                break;

            case "draw", "Draw", "DRAW":
                Card card = null;
                String fromDeck = "";

                if (parameters.length != 1)
                    game.throwException("Invalid number of parameters");

                if (parameters[0].contains("R") || parameters[0].contains("r")) {
                    if (parameters[0].contains("XX") || parameters[0].contains("xx"))
                        fromDeck = "RESOURCE";
                    else {
                        for (Card cards : game.getBoard().getUncoveredCards()) {
                            if (cards.getIdCard().equals(parameters[0]))
                                card = cards;
                        }
                        if (card.equals(null)) {
                            game.throwException("Invalid card ID");
                            break;
                        }
                    }
                } else if (parameters[0].contains("G") || parameters[0].contains("g")) {
                    if (parameters[0].contains("XX") || parameters[0].contains("xx"))
                        fromDeck = "GOLD";
                    else {
                        for (Card cards : game.getBoard().getUncoveredCards()) {
                            if (cards.getIdCard().equals(parameters[0]))
                                card = cards;
                        }
                        if (card.equals(null)) {
                            game.throwException("Invalid card ID");
                            break;
                        }
                    }
                } else {
                    game.throwException("Invalid card ID");
                    break;
                }

                try {
                    game.getState().drawnCard(player, card, fromDeck);
                } catch (IllegalCommandException e) {
                    game.throwException(e.getMessage());
                }
                break;

            default:
                game.throwException("Invalid command");
                break;
        }

    }

    public void initGame(String nickName, String color, Integer numPlayers) {
        try {
            switch (color) {
                case "RED", "Red", "red", "R", "r":
                    game.getState().initialized(nickName, Color.RED, numPlayers);
                    break;
                case "BLUE", "Blue", "blue", "B", "b":
                    game.getState().initialized(nickName, Color.BLUE, numPlayers);
                    break;
                case "GREEN", "Green", "green", "G", "g":
                    game.getState().initialized(nickName, Color.GREEN, numPlayers);
                    break;
                case "YELLOW", "Yellow", "yellow", "Y", "y":
                    game.getState().initialized(nickName, Color.YELLOW, numPlayers);
                    break;
                default:
                    game.throwException(color + " is not a valid color");
                    break;
            }
        } catch (IllegalCommandException e) {
            game.throwException(e.getMessage());
        }
    }

    public void joinGame(String nickName, String color) {
        try {
            switch (color) {
                case "RED", "Red", "red", "R", "r":
                    game.getState().joinGame(nickName, Color.RED);
                    break;
                case "BLUE", "Blue", "blue", "B", "b":
                    game.getState().joinGame(nickName, Color.BLUE);
                    break;
                case "GREEN", "Green", "green", "G", "g":
                    game.getState().joinGame(nickName, Color.GREEN);
                    break;
                case "YELLOW", "Yellow", "yellow", "Y", "y":
                    game.getState().joinGame(nickName, Color.YELLOW);
                    break;
                default:
                    break;
            }
        } catch (IllegalCommandException e) {
            game.throwException(e.getMessage());
        }
    }

    // public void matchEnded() throws IllegalCommandException {
    // this.game.getState().matchEnded();
    // }
}