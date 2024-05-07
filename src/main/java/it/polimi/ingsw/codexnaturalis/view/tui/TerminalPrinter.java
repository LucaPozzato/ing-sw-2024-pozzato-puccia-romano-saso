package it.polimi.ingsw.codexnaturalis.view.tui;

import java.util.ArrayList;
import java.util.List;

public class TerminalPrinter {
    // Cursor starts from 1, 1 -> \u001B[y;xH
    private List<List<String>> hands;
    private List<String> hand, board, decks, objectives, initialCard, chooseObjectives, structures, resourcesList,
            players;
    private String scoreBoard, alert, currentPlayer, currentState, structure, resources, chat;
    private int width = 175, height = 60, midHeight = 0, midWidth = 0, minX = 0, maxX = 0, delta = 0, indexPlayer = 0,
            indexMyPlayer = 0;

    // [x] Other structures and resources
    // [ ] FUTURE -> possible color coordinated boxes -> or just other color for
    // other player
    // [ ] FUTURE -> see other player's hand
    // [x] if other player -> box with "viewing plyer's name"
    // [x] box with current player's name
    // [x] box with current "state"
    // [x] alert box in choose phase
    // [ ] initial phase

    public TerminalPrinter() {
        this.structures = new ArrayList<>();
        this.players = new ArrayList<>();
        this.currentPlayer = "";
        this.currentState = "";
        this.hands = new ArrayList<>(List.of(List.of("")));
        this.board = new ArrayList<>(List.of(""));
        this.decks = new ArrayList<>(List.of("", ""));
        this.objectives = new ArrayList<>(List.of("", "", ""));
        this.initialCard = new ArrayList<>(List.of(""));
        this.chooseObjectives = new ArrayList<>(List.of(""));
        this.resourcesList = new ArrayList<>(List.of(""));
        this.structure = "";
        this.resources = "";
        this.scoreBoard = "";
        this.alert = "";
        this.chat = "";
    }

    public void updateChat(String chat) {
        this.chat = chat;
    }

    // TODO: define all other exceptions for bad parameters in update methods
    public void updateStructures(List<String> structures) {
        if (structure != null)
            this.structures = structures;
        indexPlayer = indexMyPlayer;
    }

    public void updateHands(List<List<String>> hands) {
        if (hands != null)
            this.hands = hands;
        indexPlayer = indexMyPlayer;
    }

    public void updateBoard(List<String> board) {
        if (board != null)
            this.board = board;
    }

    public void updateDecks(List<String> decks) {
        if (decks != null)
            this.decks = decks;
    }

    public void updateSecretObjective(String secretObjective) {
        if (secretObjective != null)
            this.objectives.set(0, secretObjective);
    }

    public void updateCommonObjectives(List<String> objectives) {
        if (objectives != null) {
            this.objectives.set(1, objectives.get(0));
            this.objectives.set(2, objectives.get(1));
        }
    }

    public void updateResources(List<String> resources) {
        if (resources != null)
            this.resourcesList = resources;
        indexPlayer = indexMyPlayer;
    }

    public void updateAlert(String alert) {
        if (alert != null)
            this.alert = alert;
    }

    public void updateScoreBoard(String scoreBoard) {
        if (scoreBoard != null)
            this.scoreBoard = scoreBoard;
    }

    public void updateInitialCard(List<String> initialCard) {
        if (initialCard != null)
            this.initialCard = initialCard;
    }

    public void updateChooseObjectives(List<String> chooseObjectives) {
        if (chooseObjectives != null)
            this.chooseObjectives = chooseObjectives;
    }

    public void updatePlayers(List<String> players) {
        if (players != null)
            this.players = players;
    }

    public void updateCurrentPlayer(String currentPlayer) {
        if (currentPlayer != null)
            this.currentPlayer = currentPlayer;
    }

    public void updateCurrentState(String currentState) {
        if (currentState != null)
            this.currentState = currentState;
    }

    public void clearAlert() {
        this.alert = "";
    }

    public void clear() {
        // set console size
        System.out.print("\u001B[8;" + height + ";" + width + "t");

        System.out.println("\033[?7h");

        // clear console
        System.out.println("\033[2J");
    }

    public void clearInput() {
        if (board.get(0).split("\n").length * board.size() / 2 > structure.split("\n").length / 2 + 2
                + hand.get(0).split("\n").length)
            System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                    + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));
        else
            System.out.print("\u001B["
                    + (midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 7) + ";"
                    + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));

    }

    public void printChat() {
        // function that prints the chat
        int x = 2;
        int y = 2;
        int boxWidth;
        int boxHeight;
        String title = "Chat";

        boxWidth = width - 2;
        boxHeight = height - 5;

        printBox(x, y, boxWidth, boxHeight, title);

        // get chat to fit in the box
        String[] chatLines = chat.split("\n");
        // if line is bigger than box width, split in the line and create new chat
        // string with the \n at the right place
        chat = "";
        for (int i = 0; i < chatLines.length; i++) {
            if (chatLines[i].length() > boxWidth - 2) {
                String[] splitLine = chatLines[i].split("(?<=\\G.{" + (boxWidth - 2) + "})");
                for (int j = 0; j < splitLine.length; j++) {
                    chat += splitLine[j] + "\n";
                }
            } else
                chat += chatLines[i] + "\n";
        }

        // print only the last boxHeight lines of the chat
        chatLines = chat.split("\n");
        int start = chatLines.length - boxHeight + 2;
        if (start < 0)
            start = 0;
        for (int i = start; i < chatLines.length; i++) {
            System.out.print("\u001B[" + (y + i - start + 1) + ";" + (x + 1) + "H" + chatLines[i]);
        }

        // print input box
        printBox(x, y + boxHeight, boxWidth, 3, "Input");

        System.out.print("\u001B[" + (y + boxHeight + 1) + ";" + (x + 1) + "H");
    }

    public void printInitialStage() {
        // print input box and error box

        midHeight = height / 2;
        midWidth = width / 2;

        printAlert(midHeight + 2, midWidth - Math.max("Alert".length(), alert.length()) / 2 - 1);

        printBox(midWidth - 21, midHeight - 2, 43, 3, "Input");
        System.out.print("\u001B[" + (midHeight - 1) + ";" + (midWidth - 20) + "H");
    }

    public void printChoosePhase() {
        midHeight = height / 2;
        midWidth = width / 2;

        printInitialCard(midHeight - initialCard.get(0).split("\n").length - 6,
                midWidth - initialCard.get(0).split("\n")[0].length(), initialCard);

        printChooseObjectives(midHeight - 2,
                midWidth - chooseObjectives.get(0).split("\n")[0].length(), chooseObjectives);

        minX = midWidth - chooseObjectives.get(0).split("\n")[0].length() - 4;
        maxX = midWidth + chooseObjectives.get(0).split("\n")[0].length() + 6;

        printAlert(midHeight + chooseObjectives.get(0).split("\n").length + 5,
                (maxX + minX - Math.max("Alert".length(), alert.length())) / 2 - 1);

        printBox(minX, midHeight + chooseObjectives.get(0).split("\n").length + 2, maxX - minX, 3, "Input");
        System.out.print(
                "\u001B[" + (midHeight + chooseObjectives.get(0).split("\n").length + 3) + ";" + (minX + 1) + "H");
    }

    public void updateMyPlayer(Integer index) {
        indexMyPlayer = index;
    }

    public void printNext() {
        indexPlayer = (indexPlayer + 1) % players.size();
        if (indexPlayer != indexMyPlayer)
            alert = "Warning: viewing " + players.get(indexPlayer) + "'s game details";
        else
            alert = "";
        structure = structures.get(indexPlayer);
        hand = hands.get(indexPlayer);
        resources = resourcesList.get(indexPlayer);
    }

    public void resetView() {
        alert = "";
        indexPlayer = indexMyPlayer;
        structure = structures.get(indexPlayer);
        hand = hands.get(indexPlayer);
        resources = resourcesList.get(indexPlayer);
    }

    public void print() {
        midHeight = height / 2 - 3;
        delta = (maxX + minX) / 2 - width / 2;
        if (delta > 0)
            midWidth = (maxX + minX) / 2 - delta;
        else
            midWidth = (maxX + minX) / 2 + Math.abs(delta);

        structure = structures.get(indexPlayer);
        hand = hands.get(indexPlayer);
        resources = resourcesList.get(indexPlayer);

        printStructure(midHeight - structure.split("\n").length / 2,
                midWidth - structure.split("\n")[0].length() / 2);

        printHand(midHeight + structure.split("\n").length / 2 + 3,
                midWidth - hand.get(0).split("\n")[0].length() * hand.size() / 2);

        if (hand.get(0).split("\n")[0].length() * hand.size() > structure.split("\n")[0].length()) {
            printBoard(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + hand.get(0).split("\n")[0].length() * hand.size() / 2 + 5);
            printDeck(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + hand.get(0).split("\n")[0].length() * hand.size() / 2 + 5
                            + board.get(0).split("\n")[0].length() + 3);
        } else {
            printBoard(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + structure.split("\n")[0].length() / 2 + 5);
            printDeck(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + structure.split("\n")[0].length() / 2 + 5
                            + board.get(0).split("\n")[0].length() + 3);
        }
        if (structure.split("\n").length < resources.split("\n").length)
            printResources(midHeight + structure.split("\n").length / 2 - resources.split("\n").length + 1,
                    midWidth - structure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length());
        else
            printResources(midHeight - resources.split("\n").length / 2,
                    midWidth - structure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length());

        printObjectives(midHeight - objectives.get(0).split("\n").length * objectives.size() / 2,
                midWidth - structure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length() - 4
                        - objectives.get(0).split("\n")[0].length());

        if (board.get(0).split("\n").length * board.size() < structure.split("\n").length) {
            printCurrentPlayer(midHeight - structure.split("\n").length - 4,
                    (maxX + minX) / 2
                            - (Math.max("Current Player".length(), currentPlayer.length()) + scoreBoard.length()
                                    + Math.max("State".length(), currentState.length()) + 6) / 2);
            printScoreBoard(midHeight - structure.split("\n").length - 4,
                    (maxX + minX) / 2
                            - (Math.max("Current Player".length(), currentPlayer.length()) + scoreBoard.length()
                                    + Math.max("State".length(), currentState.length()) + 6)
                                    / 2
                            + Math.max("Current Player".length(), currentPlayer.length()) + 3);
            printCurrentState(midHeight - structure.split("\n").length - 4,
                    (maxX + minX) / 2
                            - (Math.max("Current Player".length(), currentPlayer.length()) + scoreBoard.length()
                                    + Math.max("State".length(), currentState.length()) + 6) / 2
                            + Math.max("Current Player".length(), currentPlayer.length()) + 3 + scoreBoard.length()
                            + 3);

        } else {
            printCurrentPlayer(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2
                            - (Math.max("Current Player".length(), currentPlayer.length()) + scoreBoard.length()
                                    + Math.max("State".length(), currentState.length()) + 6)
                                    / 2);
            printScoreBoard(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2
                            - (Math.max("Current Player".length(), currentPlayer.length()) + scoreBoard.length()
                                    + Math.max("State".length(), currentState.length()) + 6)
                                    / 2
                            + Math.max("Current Player".length(), currentPlayer.length()) + 3);
            printCurrentState(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2
                            - (Math.max("Current Player".length(), currentPlayer.length()) + scoreBoard.length()
                                    + Math.max("State".length(), currentState.length()) + 6) / 2
                            + Math.max("Current Player".length(), currentPlayer.length()) + 3 + scoreBoard.length()
                            + 3);
        }

        if (board.get(0).split("\n").length * board.size() / 2 > structure.split("\n").length / 2 + 2
                + hand.get(0).split("\n").length) {
            printAlert(midHeight + board.get(0).split("\n").length * board.size() / 2 + 6,
                    (maxX + minX - Math.max("Alert".length(), alert.length())) / 2 - 1);
            printBox(minX, midHeight + board.get(0).split("\n").length * board.size() / 2 + 3, maxX - minX, 3, "Input");
            System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                    + (minX + 1) + "H");
        } else {
            printAlert(midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 9,
                    (maxX + minX - Math.max("Alert".length(), alert.length())) / 2 - 1);
            printBox(minX, midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 6,
                    maxX - minX, 3, "Input");
            System.out.print(
                    "\u001B[" + (midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 7)
                            + ";" + (minX + 1) + "H");
        }
    }

    private void printStructure(int y, int x) {
        printBox(x, y, structure.split("\n")[0].length() + 2, structure.split("\n").length + 2, "Structure");
        x++;
        y++;
        for (int i = 0; i < structure.split("\n").length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H" + structure.split("\n")[i]);
            y++;
        }
    }

    private void printHand(int y, int x) {
        printBox(x, y, hand.get(0).split("\n")[0].length() * hand.size() + 2, hand.get(0).split("\n").length + 2,
                "Hand");
        x++;
        y++;
        for (int i = 0; i < hand.size(); i++) {
            for (int j = 0; j < hand.get(i).split("\n").length; j++) {
                System.out.print("\u001B[" + (y + j) + ";" + (x + 13 * i) + "H"
                        + hand.get(i).split("\n")[j]);
            }
        }
    }

    private void printBoard(int y, int x) {
        printBox(x, y, board.get(0).split("\n")[0].length() + 2, board.get(0).split("\n").length * board.size() + 2,
                "Board");
        x++;
        y++;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).split("\n").length; j++) {
                System.out.print("\u001B[" + (y + j + 7 * i) + ";" + x + "H"
                        + board.get(i).split("\n")[j]);
            }
        }
    }

    private void printDeck(int y, int x) {
        maxX = x + decks.get(0).split("\n")[0].length() + 2;
        printBox(x, y, decks.get(0).split("\n")[0].length() + 2, board.get(0).split("\n").length * board.size() + 2,
                "Decks");
        x++;
        y += 6;
        for (int i = 0; i < decks.size(); i++) {
            for (int j = 0; j < decks.get(i).split("\n").length; j++) {
                System.out.print("\u001B[" + (y + j) + ";" + x + "H"
                        + decks.get(i).split("\n")[j]);
            }
            y += 13;
        }
    }

    private void printObjectives(int y, int x) {
        minX = x;
        printBox(x, y, objectives.get(0).split("\n")[0].length() + 2,
                objectives.get(0).split("\n").length * objectives.size() + 2, "Objective");
        x++;
        y++;
        for (int i = 0; i < objectives.size(); i++) {
            for (int j = 0; j < objectives.get(i).split("\n").length; j++) {
                System.out.print("\u001B[" + (y + j + 7 * i) + ";" + x + "H"
                        + objectives.get(i).split("\n")[j]);
            }
        }
    }

    private void printResources(int y, int x) {
        int maxWidth = 0;
        for (int i = 0; i < resources.split("\n").length; i++) {
            if (resources.split("\n")[i].length() > maxWidth)
                maxWidth = resources.split("\n")[i].length();
        }

        printBox(x, y, maxWidth + 2, resources.split("\n").length + 2, "Resource");
        x++;
        y++;
        for (int i = 0; i < resources.split("\n").length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H" + resources.split("\n")[i]);
            y++;
        }
    }

    private void printAlert(int y, int x) {
        printBox(x, y, Math.max("Alert".length(), alert.length()) + 2, 3, "Alert");
        x++;
        y++;
        if (alert.contains("Warning"))
            System.out.print("\u001B[" + y + ";" + x + "H\u001B[48;5;94m" + alert + "\u001B[0m");
        else if (alert.contains("Error"))
            System.out.print("\u001B[" + y + ";" + x + "H\u001B[48;5;124m" + alert + "\u001B[0m");
        else
            System.out.println("\u001B[" + y + ";" + x + "H\u001B[48;5;55m" + alert + "\u001B[0m");
    }

    private void printScoreBoard(int y, int x) {
        printBox(x, y, scoreBoard.length() + 2, 3, "Scoreboard");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H" + scoreBoard);
    }

    private void printCurrentPlayer(int y, int x) {
        printBox(x, y, Math.max("Current Player".length(), currentPlayer.length()) + 2, 3, "Current Player");
        x++;
        y++;
        if (currentPlayer.length() > "Current Player".length())
            System.out.print("\u001B[" + y + ";" + x + "H" + currentPlayer);
        else
            System.out.print(
                    "\u001B[" + y + ";" + x + "H"
                            + " ".repeat("Current Player".length() / 2 - currentPlayer.length() / 2)
                            + currentPlayer);
    }

    private void printCurrentState(int y, int x) {
        printBox(x, y, Math.max("State".length(), currentState.length()) + 2, 3, "State");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H" + currentState);
    }

    private void printInitialCard(int y, int x, List<String> initialCard) {
        printBox(x, y, initialCard.get(0).split("\n")[0].length() * initialCard.size() + 2,
                initialCard.get(0).split("\n").length + 3, "Initial Card");
        x++;
        y++;
        for (int i = 0; i < initialCard.size(); i++) {
            for (int j = 0; j < initialCard.get(i).split("\n").length; j++) {
                System.out.print("\u001B[" + (y + j) + ";" + (x + 9 * i) + "H"
                        + initialCard.get(i).split("\n")[j]);
            }
        }
        // write in the bottom center of the first card: front in gray
        System.out.print("\u001B[" + (y + initialCard.get(0).split("\n").length) + ";"
                + (x + initialCard.get(0).split("\n")[0].length() / 2 - new String("Front").length() / 2)
                + "H\u001B[38;5;242mFRONT\u001B[0m");

        // write in the bottom center of the second card: back in gray
        System.out.print("\u001B[" + (y + initialCard.get(0).split("\n").length) + ";"
                + (x + (int) (initialCard.get(0).split("\n")[0].length() * 1.5)
                        - new String("Back").length() / 2)
                + "H\u001B[38;5;242mBACK\u001B[0m");
    }

    private void printChooseObjectives(int y, int x, List<String> chooseObjectives) {
        printBox(x, y, chooseObjectives.get(0).split("\n")[0].length() * chooseObjectives.size() + 2,
                chooseObjectives.get(0).split("\n").length + 3, "Objectives");
        x++;
        y++;
        for (int i = 0; i < chooseObjectives.size(); i++) {
            for (int j = 0; j < chooseObjectives.get(i).split("\n").length; j++) {
                System.out.print("\u001B[" + (y + j) + ";" + (x + 13 * i) + "H"
                        + chooseObjectives.get(i).split("\n")[j]);
            }
        }
        // write in the bottom center of the first card: Objective 1 in gray
        System.out.print("\u001B[" + (y + chooseObjectives.get(0).split("\n").length) + ";"
                + (x + chooseObjectives.get(0).split("\n")[0].length() / 2 - new String("Objective 1").length() / 2)
                + "H\u001B[38;5;242mObjective 1\u001B[0m");
        // write in the bottom center of the second card: Objective 2 in gray
        System.out.print("\u001B[" + (y + chooseObjectives.get(0).split("\n").length) + ";"
                + (x + (int) (chooseObjectives.get(0).split("\n")[0].length() * 1.5)
                        - new String("Objective 2").length() / 2)
                + "H\u001B[38;5;242mObjective 2\u001B[0m");
    }

    private void printBox(int x, int y, int boxWidth, int boxHeight, String title) {
        // function that prints a box with a title in the center
        // TODO: add color to box considering player
        String boxString = "";

        if (indexPlayer != indexMyPlayer
                && (title.equals("Structure") || title.equals("Resource") || title.equals("Hand")))
            boxString += "\u001B[33m"; // set color to yellow
        else
            boxString += "\u001B[38;5;242m"; // set color to gray

        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (i == 0 && j == 0)
                    boxString += "╭";
                else if (i == 0 && j == boxWidth - 1)
                    boxString += "╮\n";
                else if (i == boxHeight - 1 && j == 0)
                    boxString += "╰";
                else if (i == boxHeight - 1 && j == boxWidth - 1)
                    boxString += "╯";
                else if (i == 0 && j == (boxWidth - title.length()) / 2) {
                    boxString += title;
                    j += title.length() - 1;
                } else if (i == 0 || i == boxHeight - 1)
                    boxString += "─";
                else if (j == 0 || j == boxWidth - 1) {
                    boxString += "│";
                    if (j == boxWidth - 1)
                        boxString += "\n";
                } else
                    boxString += " ";
            }
        }

        String[] lines = boxString.split("\n");

        for (int i = 0; i < lines.length; i++) {
            System.out.print("\u001B[" + (y + i) + ";" + x + "H" + lines[i]);
        }

        System.out.print("\u001B[0m"); // reset color
    }
}
