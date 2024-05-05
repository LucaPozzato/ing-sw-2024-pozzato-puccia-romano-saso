package it.polimi.ingsw.codexnaturalis.view.tui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;
import it.polimi.ingsw.codexnaturalis.view.View;
import javafx.util.Pair;

public class Tui implements View {
    private Drawer drawer;
    private TerminalPrinter terminalPrinter;
    private InputVerifier inputVerifier;
    private Painter painter;
    private List<Player> players;
    private Player myPlayer;
    private Boolean chooseStage;
    private Boolean initialStage;
    private VirtualClient client;

    public Tui(MiniModel miniModel, VirtualClient client) {
        inputVerifier = new InputVerifier(miniModel);
        this.client = client;
        drawer = new Drawer();
        painter = new Painter();

        terminalPrinter = new TerminalPrinter();
        players = new ArrayList<>();
        initialStage = true;
    }

    @Override
    public void run() {
        terminalPrinter.clear();
        ReadThread readThread = new ReadThread();
        readThread.start();
        terminalPrinter.printInitialStage();
    }

    @Override
    public void updateChat(Chat chat) {
        terminalPrinter.updateChat(drawer.drawChat(chat, myPlayer));
    }

    @Override
    public void updateError(String error) {
        terminalPrinter.updateAlert(error);
        print();
        terminalPrinter.clearAlert();
    }

    @Override
    public void updateState(String state) {
        terminalPrinter.updateCurrentState(state);
        state = state.toUpperCase();
        if (state.equals("Wait")) {
            terminalPrinter.updateAlert("Waiting for other players...");
            initialStage = true;
            print();
            terminalPrinter.clearAlert();
        } else if (state.equals("Choose")) {
            initialStage = false;
            chooseStage = true;
        } else {
            initialStage = false;
            chooseStage = false;
        }
        print();
    }

    @Override
    public void updateMyPlayer(Player player) {
        myPlayer = player;
        terminalPrinter.updateMyPlayer(players.indexOf(player));
        print();
    }

    @Override
    public void updateCurrentPlayer(Player player) {
        terminalPrinter.updateCurrentPlayer(player.getNickname());
        print();
    }

    @Override
    public void updatePlayers(List<Player> players) {
        this.players = players;
        List<String> playersString = new ArrayList<>();
        for (Player player : players) {
            playersString.add(player.getNickname());
        }
        terminalPrinter.updatePlayers(playersString);
        print();
    }

    @Override
    public void updateStructures(List<Structure> structures) {
        List<String> structureStrings = new ArrayList<>();
        List<String> resources = new ArrayList<>();

        for (Structure structure : structures) {
            char[][] visualStructure = new char[174][506];
            int x = 0;
            int y = 0;
            for (Pair<Card, Boolean> cardPair : structure.getPlacedCards()) {
                x = structure.getCardToCoordinate().get(cardPair.getKey()).getFirst() / 100;
                y = structure.getCardToCoordinate().get(cardPair.getKey()).getFirst() % 100;

                // Center of the matrix + x offset (6 pixels per card)
                x = 253 + ((x - 40) * 6);
                // Inverting y coordinate to match the matrix structure, center of the matrix +
                // y offset (2 pixels per card)
                y = 87 - ((y - 40) * 2);

                try {
                    visualStructure = drawer.insertCardInMatrix(visualStructure, cardPair.getKey(), x, y,
                            cardPair.getValue());
                } catch (Exception e) {
                    printAlert(e.getMessage());
                }
            }
            try {
                structureStrings.add(painter.paintStructure(visualStructure, structure.getCoordinateToCard()));
            } catch (Exception e) {
                printAlert(e.getMessage());
            }
            resources.add(drawer.drawVisibleSymbols(structure.getvisibleSymbols()));
        }

        // FIXME: multiple structures
        terminalPrinter.updateStructures(structureStrings);

        // FIXME: multiple resources
        terminalPrinter.updateResources(resources);

        print();
    }

    @Override
    public void updateHand(List<Hand> hands) {
        List<String> visualHand = new ArrayList<>();
        List<String> initialCard = new ArrayList<>();
        List<String> chooseBetweenObj = new ArrayList<>();
        List<List<String>> handsString = new ArrayList<>();
        String secretObjective = "";

        try {
            for (Hand hand : hands) {
                visualHand.clear();
                if (hands.indexOf(hand) == players.indexOf(myPlayer)) {
                    for (Card card : hand.getCardsHand()) {
                        if (card instanceof GoldCard)
                            visualHand.add(drawer.drawGoldCard(card, true));
                        else if (card instanceof ResourceCard)
                            visualHand.add(drawer.drawResourceCard(card, true));
                    }
                    initialCard = drawer.drawFullInitialCard(hand.getInitCard());
                    chooseBetweenObj = List.of(drawer.drawObjectiveCard(hand.getChooseBetweenObj().get(0)),
                            drawer.drawObjectiveCard(hand.getChooseBetweenObj().get(1)));

                    if (hand.getSecretObjective() != null)
                        secretObjective = drawer.drawObjectiveCard(hand.getSecretObjective());
                } else {
                    visualHand.clear();
                    for (Card card : hand.getCardsHand()) {
                        if (card instanceof GoldCard)
                            visualHand.add(drawer.drawGoldCard(card, false));
                        else if (card instanceof ResourceCard)
                            visualHand.add(drawer.drawResourceCard(card, false));
                    }
                }
                handsString.add(painter.paintHand(visualHand, hand.getCardsHand()));
            }
        } catch (Exception e) {
            printAlert(e.getMessage());
        }

        terminalPrinter.updateHands(handsString);
        terminalPrinter.updateInitialCard(painter.paintInitialCard(initialCard));
        terminalPrinter.updateChooseObjectives(chooseBetweenObj);
        terminalPrinter.updateSecretObjective(secretObjective);

        print();
    }

    @Override
    public void updateBoard(Board board) {
        List<String> cards = new ArrayList<>();
        List<String> commonObjectives = new ArrayList<>();
        String scores = "";
        try {
            for (Card card : board.getUncoveredCards()) {
                if (card instanceof GoldCard)
                    cards.add(drawer.drawGoldCard(card, true));
                else if (card instanceof ResourceCard)
                    cards.add(drawer.drawResourceCard(card, true));
            }
            for (Card card : board.getCommonObjectives()) {
                commonObjectives.add(drawer.drawObjectiveCard(card));
            }
            scores = drawer.drawActualScores(board.getActualScores());

            terminalPrinter.updateBoard(painter.paintBoard(cards, board.getUncoveredCards()));
            terminalPrinter.updateCommonObjectives(commonObjectives);
            terminalPrinter.updateScoreBoard(scores);
        } catch (Exception e) {
            printAlert(e.getMessage());
        }

        print();
    }

    @Override
    public void updateDeck(Deck deck) {
        List<String> cards = new ArrayList<>();

        try {
            cards = painter.paintDeck(List.of(deck.getResourceDeck().peek(), deck.getGoldDeck().peek()));
        } catch (Exception e) {
            printAlert(e.getMessage());
        }

        terminalPrinter.updateDecks(cards);

        print();
    }

    private void printNextPlayerView() {
        terminalPrinter.clear();
        terminalPrinter.printNext();
        terminalPrinter.print();
    }

    private void resetView() {
        terminalPrinter.clear();
        terminalPrinter.resetView();
        terminalPrinter.print();
    }

    private void printChat() {
        terminalPrinter.clear();
        terminalPrinter.printChat();
    }

    private void print() {
        terminalPrinter.clear();
        if (initialStage)
            terminalPrinter.printInitialStage();
        else if (chooseStage)
            terminalPrinter.printChoosePhase();
        else
            terminalPrinter.print();
    }

    private void printAlert(String alert) {
        terminalPrinter.updateAlert(alert);
        print();
        terminalPrinter.clearAlert();
    }

    class ReadThread extends Thread {
        public void run() {
            try {
                while (true) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

                    String move = input.readLine();
                    move = move.toUpperCase();
                    switch (move) {
                        case "QUIT", "Q":
                            System.exit(0);
                            break;

                        case "CHAT":
                            if (!initialStage && !chooseStage)
                                printChat();
                            break;

                        case "NEXT":
                            if (!initialStage && !chooseStage)
                                printNextPlayerView();
                            break;

                        case "RESET", "R", "EXIT":
                            if (!initialStage && !chooseStage)
                                resetView();
                            break;

                        default:
                            try {
                                client.sendCommand(inputVerifier.move(myPlayer, move));
                                // print();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // terminalPrinter.updateAlert(e.getMessage());
                                // print();
                                // terminalPrinter.clearAlert();
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                terminalPrinter.updateAlert(e.getMessage());
                print();
                terminalPrinter.clearAlert();
            }
        }
    }
}