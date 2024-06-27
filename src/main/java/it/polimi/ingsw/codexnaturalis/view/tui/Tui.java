package it.polimi.ingsw.codexnaturalis.view.tui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
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

/**
 * This class is used to represent the Text User Interface (TUI) of the game. It
 * implements the View interface and is used to display the game state and
 * handle the user input.
 */
public class Tui implements View {
    private Drawer drawer;
    private TerminalPrinter terminalPrinter;
    private InputVerifier inputVerifier;
    private Painter painter;
    private List<Player> players;
    private List<Player> winners;
    private Player myPlayer;
    private Boolean chooseStage;
    private Boolean initialStage;
    private Boolean chatStage;
    private Boolean helpStage;
    private VirtualClient client;

    public Tui(MiniModel miniModel, VirtualClient client) {
        inputVerifier = new InputVerifier(miniModel, client);
        this.client = client;
        drawer = new Drawer();
        painter = new Painter();
        terminalPrinter = new TerminalPrinter();
        players = new ArrayList<>();
        initialStage = true;
        chooseStage = false;
        chatStage = false;
        helpStage = false;
    }

    /**
     * This method is used to start the TUI. It clears the terminal and starts
     * a new thread to read the user input. It also prints the initial stage of the
     * game (where a players decides to join/create/rejoin a game).
     */
    @Override
    public void run() {
        terminalPrinter.clear();
        ReadThread readThread = new ReadThread();
        readThread.start();
        terminalPrinter.updateAlert("Create, join or rejoin a game to start playing");
        terminalPrinter.printInitialStage();
    }

    /**
     * This method is used to update the chat messages.
     * 
     * @param chat The chat object containing the chat messages
     */
    @Override
    public void updateChat(Chat chat) {
        terminalPrinter.updateChat(drawer.drawChat(chat, myPlayer));
        print();
    }

    /**
     * This method is used to update the error message.
     * 
     * @param error The error message
     */
    @Override
    public void updateError(String error) {
        System.out.println("got error: " + error);
        printAlert("Error: " + error);
    }

    /**
     * This method is used to update the state message.
     * 
     * @param state The state message
     */
    @Override
    public void updateState(String state) {
        terminalPrinter.updateCurrentState(state);
        String alert = "";
        switch (state) {
            case "Wait":
                alert = "Warning: waiting for other players ...";
                initialStage = true;
                break;
            case "Choose":
                alert = "Choose a card to play";
                initialStage = false;
                chooseStage = true;
                break;
            case "End":
                alert = "Congratulations to the winners: ";
                for (Player player : winners) {
                    alert += player.getNickname() + ", ";
                }
                alert = alert.substring(0, alert.length() - 2);
                break;
            default:
                initialStage = false;
                chooseStage = false;
                break;
        }
        terminalPrinter.updateAlert(alert);
        print();
    }

    /**
     * This method is used to update my player.
     * 
     * @param player My player
     */
    @Override
    public void updateMyPlayer(Player player) {
        myPlayer = player;
        terminalPrinter.updateMyPlayer(players.indexOf(player));
        print();
    }

    /**
     * This method is used to update the current player.
     * 
     * @param player The current player
     */
    @Override
    public void updateCurrentPlayer(Player player) {
        terminalPrinter.updateCurrentPlayer(player.getNickname());
        print();
    }

    /**
     * This method is used to update the players.
     * 
     * @param players The list of players
     */
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

    /**
     * This method is used to update the winners.
     * 
     * @param winners The list of winners
     */
    @Override
    public void updateWinners(List<Player> winners) {
        this.winners = winners;
        print();
    }

    /**
     * This method is used to update the structures of the players.
     * 
     * @param structures The list of structures
     */
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

        terminalPrinter.updateStructures(structureStrings);

        terminalPrinter.updateResources(resources);

        print();
    }

    /**
     * This method is used to update the hands of the players.
     * 
     * @param hands The list of hands
     */
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
                handsString.add(painter.paintListOfCards(visualHand, hand.getCardsHand()));
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

    /**
     * This method is used to update the board.
     * 
     * @param board The board
     */
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

            terminalPrinter.updateBoard(painter.paintListOfCards(cards, board.getUncoveredCards()));
            terminalPrinter.updateCommonObjectives(commonObjectives);
            terminalPrinter.updateScoreBoard(scores);
        } catch (Exception e) {
            printAlert(e.getMessage());
        }

        print();
    }

    /**
     * This method is used to update the deck.
     * 
     * @param deck The deck
     */
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

    /**
     * This method is used to print the next player details.
     */
    private void printNextPlayerView() {
        terminalPrinter.clear();
        terminalPrinter.printNext();
        terminalPrinter.printGame();
    }

    /**
     * This method is used to reset the view to my player's details.
     */
    private void resetView() {
        terminalPrinter.clear();
        terminalPrinter.resetView();
        terminalPrinter.printGame();
    }

    /**
     * This method is used to print the current view [help, initial, choose, chat,
     * game]
     */
    private void print() {
        terminalPrinter.clear();
        if (helpStage)
            terminalPrinter.printHelp();
        else if (initialStage)
            terminalPrinter.printInitialStage();
        else if (chooseStage)
            terminalPrinter.printChoosePhase();
        else if (chatStage)
            terminalPrinter.printChat();
        else
            terminalPrinter.printGame();
    }

    /**
     * This method is used to print an alert message.
     * 
     * @param alert The alert message
     */
    private void printAlert(String alert) {
        terminalPrinter.updateAlert(alert);
        print();
    }

    /**
     * This class is used to read the user input from the terminal.
     */
    class ReadThread extends Thread {
        public void run() {
            try {
                while (true) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                    String move = "";
                    int c;

                    move = input.readLine();

                    String tempMove = move.toUpperCase();
                    if (!tempMove.contains("JOIN") && !tempMove.contains("CREATE") && !tempMove.contains("SEND")
                            && !tempMove.contains("REJOIN")) {
                        move = move.toUpperCase();
                    }

                    switch (move) {
                        case "QUIT", "Q":
                            System.exit(0);
                            break;

                        case "CHAT":
                            if (!initialStage && !chooseStage)
                                chatStage = true;
                            print();
                            break;

                        case "NEXT", "N":
                            if (!initialStage && !chooseStage)
                                printNextPlayerView();
                            break;

                        case "RESET", "RST", "EXIT", "ESC":
                            if (helpStage) {
                                helpStage = false;
                                print();
                            } else if (chatStage) {
                                chatStage = false;
                                print();
                            } else if (!initialStage && !chooseStage)
                                resetView();
                            break;

                        case "HELP", "H":
                            helpStage = true;
                            print();
                            break;

                        default:
                            switch (move) {
                                case "C2":
                                    move = "create: 0, r, r, r, 2".toUpperCase();
                                    break;

                                case "C3":
                                    move = "create: 0, r, r, r, 3".toUpperCase();
                                    break;

                                case "J1":
                                    move = "join: 0, g, g, g".toUpperCase();
                                    break;

                                case "J2":
                                    move = "join: 0, b, b, b".toUpperCase();
                                    break;

                                case "CH":
                                    move = "choose: f, 1".toUpperCase();
                                    break;

                                default:
                                    break;
                            }
                            try {
                                client.sendCommand(inputVerifier.move(myPlayer, move));
                            } catch (IllegalCommandException e) {
                                e.printStackTrace();
                                printAlert("Error: " + e.getMessage());
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                printAlert("Error: " + e.getMessage());
            }
        }
    }
}