package it.polimi.ingsw.codexnaturalis;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.tui.CliVerifier;
import it.polimi.ingsw.codexnaturalis.view.tui.GameTui;

public class GameMain {
    public static void main(String[] args) throws Exception {
        GameMain gameMain = new GameMain();
        CliVerifier controller = new CliVerifier();

        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        Stack<GoldCard> goldDeck = new GoldParser().parse();
        List<InitialCard> initialCards = new InitialParser().parse();
        List<ObjectiveCard> objectiveCards = new ObjectiveParser().parse();
        Collections.shuffle(resourceDeck);
        Collections.shuffle(goldDeck);
        Collections.shuffle(initialCards);
        Collections.shuffle(objectiveCards);

        GameTui gameTui = new GameTui();
        Hand hand = new Hand();
        Board board = new Board();
        Structure structure = new Structure();
        Deck deck = new Deck(goldDeck, resourceDeck);
        Player luca = new Player("Luca");
        Player nick = new Player("Nick");

        gameTui.getPrinter().updatePlayers(List.of("luca", "nick"));

        hand.setChooseBetweenObj(List.of(objectiveCards.get(0), objectiveCards.get(1)));
        hand.setInitCard(initialCards.get(0));
        hand.addCard(resourceDeck.pop());
        hand.addCard(resourceDeck.pop());
        hand.addCard(goldDeck.pop());
        board.addUncoveredCard(resourceDeck.pop());
        board.addUncoveredCard(resourceDeck.pop());
        board.addUncoveredCard(goldDeck.pop());
        board.addUncoveredCard(goldDeck.pop());
        board.setCommonObjectives(List.of(objectiveCards.get(2), objectiveCards.get(3)));
        board.updateActualScore(luca, 0);
        board.updateActualScore(nick, 0);
        gameTui.updateStructure(structure);
        gameTui.updateHand(hand);
        gameTui.updateBoard(board);
        gameTui.updateDeck(deck);

        gameTui.clear();
        gameTui.printInitial();

        ReadThread readThread = gameMain.new ReadThread(controller, gameTui, hand, structure, deck, board);
        readThread.start();
    }

    class ReadThread extends Thread {
        CliVerifier controller;
        GameTui gameTui;
        Hand hand;
        Structure structure;
        Deck deck;
        Board board;

        ReadThread(CliVerifier controller, GameTui printer, Hand hand, Structure structure, Deck deck, Board board) {
            super();
            this.controller = controller;
            this.gameTui = printer;
            this.hand = hand;
            this.structure = structure;
            this.deck = deck;
            this.board = board;
        }

        public void run() {
            try {
                while (true) {
                    String move = System.console().readLine();
                    sendMove(move);
                }
            } catch (IllegalCommandException e) {
                System.out.println(e.getMessage());
            }
        }

        private void sendMove(String move) throws IllegalCommandException {
            String[] commandArray = move.split(": ");
            String[] parameters = commandArray[1].split(", ");

            switch (commandArray[0]) {
                case "choose", "Choose", "CHOOSE":
                    Boolean side = false;
                    Integer objIndex = 0;

                    if (parameters[0].equals("Front") || parameters[0].equals("F") || parameters[0].equals("f")
                            || parameters[0].equals("front") || parameters[0].equals("FRONT"))
                        side = true;
                    else if (parameters[0].equals("Back") || parameters[0].equals("B") || parameters[0].equals("b")
                            || parameters[0].equals("back") || parameters[0].equals("BACK"))
                        side = false;

                    if (parameters[1].equals("1") || parameters[1].equals("one") || parameters[1].equals("ONE")
                            || parameters[1].equals("One"))
                        objIndex = 0;
                    else if (parameters[1].equals("2") || parameters[1].equals("two") || parameters[1].equals("TWO")
                            || parameters[1].equals("Two"))
                        objIndex = 1;

                    try {
                        hand.setSecretObjective(hand.getChooseBetweenObj().get(objIndex));
                        structure.placeCard(null, hand.getInitCard(), null, side);

                        gameTui.updateHand(hand);
                        gameTui.updateStructure(structure);
                    } catch (IllegalCommandException e) {
                        System.out.println(e.getMessage());
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
                    for (Card cards : hand.getCardsHand()) {
                        if (cards.getIdCard().equals(parameters[0])) {
                            placeThis = cards;
                        }
                    }

                    // checks if the placed card's father is in the structure
                    for (Card card : structure.getCardToCoordinate().keySet()) {
                        if (card.getIdCard().equals(parameters[1])) {
                            father = card;
                        }
                    }

                    try {
                        hand.removeCard(placeThis);
                        structure.placeCard(father, placeThis, parameters[2], Boolean.parseBoolean(parameters[3]));

                        gameTui.updateHand(hand);
                        gameTui.updateStructure(structure);
                    } catch (IllegalCommandException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "draw", "Draw", "DRAW":
                    Card card = null;
                    String fromDeck = "";

                    parameters[0] = parameters[0].toUpperCase();

                    if (parameters[0].contains("R") || parameters[0].contains("r")) {
                        if (parameters[0].contains("XX") || parameters[0].contains("xx"))
                            fromDeck = "RESOURCE";
                        else {
                            for (Card cards : board.getUncoveredCards()) {
                                if (cards.getIdCard().equals(parameters[0]))
                                    card = cards;
                            }
                        }
                    } else if (parameters[0].contains("G") || parameters[0].contains("g")) {
                        if (parameters[0].contains("XX") || parameters[0].contains("xx"))
                            fromDeck = "GOLD";
                        else {
                            for (Card cards : board.getUncoveredCards()) {
                                if (cards.getIdCard().equals(parameters[0]))
                                    card = cards;
                            }
                        }
                    }

                    try {
                        if (fromDeck.equals("RESOURCE"))
                            hand.addCard(deck.drawResourceCard());
                        else if (fromDeck.equals("GOLD"))
                            hand.addCard(deck.drawGoldCard());
                        else {
                            hand.addCard(card);
                            board.removeUncoveredCard(card);
                            if (card instanceof ResourceCard)
                                board.addUncoveredCard(deck.drawResourceCard());
                            else if (card instanceof GoldCard)
                                board.addUncoveredCard(deck.drawGoldCard());
                        }

                        gameTui.updateHand(hand);
                        gameTui.updateBoard(board);
                        gameTui.updateDeck(deck);
                    } catch (IllegalCommandException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                default:
                    break;
            }
        }
    }
}