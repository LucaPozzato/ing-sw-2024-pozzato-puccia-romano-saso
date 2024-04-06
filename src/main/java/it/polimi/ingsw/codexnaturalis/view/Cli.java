package it.polimi.ingsw.codexnaturalis.view;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Cli {
    // Cursor starts from 1, 1 -> /u001B[y;xH
    // TODO: draw scoreboard with players and points
    String structure;
    List<String> hand;
    List<String> board;
    List<String> decks;
    List<String> objectives;
    String resources;
    String scoreBoard;
    String virtualPoints;
    String error;
    String inputString;
    List<String> outputString;
    int width = 175;
    int height = 60;
    int midHeight = 0;
    int midWidth = 0;
    int minX = 0;
    int maxX = 0;
    BufferedReader stdin;

    public Cli(BufferedReader stdin) {
        this.structure = "";
        this.hand = new ArrayList<>(List.of(""));
        this.board = new ArrayList<>(List.of(""));
        this.decks = new ArrayList<>(List.of("", ""));
        this.objectives = new ArrayList<>(List.of(""));
        this.resources = "";
        this.scoreBoard = "";
        this.error = "";
        this.inputString = "";
        this.stdin = stdin;
    }

    // TODO: define all other exceptions for bad parameters in update methods
    public void updateStructure(String structure) {
        if (structure != null)
            this.structure = structure;
    }

    public void updateHand(List<String> hand) {
        if (hand != null)
            this.hand = hand;
    }

    public void updateBoard(List<String> board) {
        if (board != null)
            this.board = board;
    }

    public void updateDecks(List<String> decks) {
        if (decks != null)
            this.decks = decks;
    }

    public void updateObjectives(List<String> objectives) {
        if (objectives != null)
            this.objectives = objectives;
    }

    public void updateResources(String resources) {
        if (resources != null)
            this.resources = resources;
    }

    public void updateError(String error) {
        if (error != null)
            this.error = error;
    }

    public void updateScoreBoard(String scoreBoard) {
        if (scoreBoard != null)
            this.scoreBoard = scoreBoard;
    }

    public void updateVirtualPoints(String virtualPoints) {
        if (virtualPoints != null)
            this.virtualPoints = virtualPoints;
    }

    public List<String> printInitial(List<String> initialCard, List<String> chooseObjectives)
            throws IllegalArgumentException {
        if (initialCard == null || chooseObjectives == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        // TODO: throw all other exceptions for bad parameters

        // clear console
        System.out.println("\033c");

        // set console size
        System.out.print("\u001B[8;" + height + ";" + width + "t");

        midHeight = height / 2;
        midWidth = width / 2;

        printInitialCard(midHeight - initialCard.get(0).split("\n").length - 4,
                midWidth - initialCard.get(0).split("\n")[0].length(), initialCard);

        printChooseObjectives(midHeight,
                midWidth - chooseObjectives.get(0).split("\n")[0].length(), chooseObjectives);

        minX = midWidth - chooseObjectives.get(0).split("\n")[0].length() - 4;
        maxX = midWidth + chooseObjectives.get(0).split("\n")[0].length() + 6;

        printBox(minX, midHeight + chooseObjectives.get(0).split("\n").length + 4, maxX - minX, 3, "Input");

        List<String> messages = new ArrayList<>(
                List.of("Side of initial car <F, B>: ", "Objective chosen <1, 2>: "));
        outputString = new ArrayList<>();

        for (String message : messages) {
            // moves cursor to the left of input box
            System.out.print("\u001B[" + (midHeight + chooseObjectives.get(0).split("\n").length + 5) + ";"
                    + (minX + 1) + "H\u001B[38;5;242m" + message + "\u001B[0m");

            // read input from user
            try {
                char c = (char) stdin.read();
                while (c != '\n') {
                    inputString += Character.toString(c);
                    c = (char) stdin.read();
                }
                outputString.add(inputString);
                inputString = "";
            } catch (Exception e) {
                e.printStackTrace();
            }

            // clear input box
            System.out.print("\u001B[" + (midHeight + chooseObjectives.get(0).split("\n").length + 5) + ";"
                    + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));
        }
        return outputString;
    }

    public List<String> print(String command) {
        List<String> messages = new ArrayList<>(List.of(""));
        outputString = new ArrayList<>();

        if (command.equals("draw")) {
            messages = new ArrayList<>(List.of("ID of card: "));
            error = "";
        } else if (command.equals("place")) {
            messages = new ArrayList<>(
                    List.of("ID of card to place: ", "ID of card to cover: ", "Position <TL, TR, BL, BR>: ",
                            "Front side up <TRUE, FALSE>: "));
            error = "";
        }

        // clear console
        System.out.println("\033c");

        // set console size
        System.out.print("\u001B[8;" + height + ";" + width + "t");

        // TODO: fix true center of game field
        midHeight = height / 2 - 3;
        midWidth = width / 2;

        printStructure(midHeight - structure.split("\n").length / 2, midWidth - structure.split("\n")[0].length() / 2);

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
            printVirtualPoints(midHeight - structure.split("\n").length - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2);
            printScoreBoard(midHeight - structure.split("\n").length - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2 + virtualPoints.length()
                            + 3);
        } else {
            printVirtualPoints(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2);
            printScoreBoard(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2 + virtualPoints.length()
                            + 3);
        }

        if (board.get(0).split("\n").length * board.size() / 2 > structure.split("\n").length / 2 + 2
                + hand.get(0).split("\n").length) {
            printError(midHeight + board.get(0).split("\n").length * board.size() / 2 + 6,
                    (maxX + minX - error.length()) / 2 - 1);
            printBox(minX, midHeight + board.get(0).split("\n").length * board.size() / 2 + 3, maxX - minX, 3, "Input");

            for (String message : messages) {
                // writes message in input box and moves the cursor after the message
                System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                        + (minX + 1) + "H" + "\u001B[48;5;22m" + command + "\u001B[0m -> \u001B[38;5;242m" + message
                        + "\u001B[0m");

                // read input from user
                try {
                    char c = (char) stdin.read();
                    while (c != '\n') {
                        inputString += Character.toString(c);
                        c = (char) stdin.read();
                    }
                    outputString.add(inputString);
                    inputString = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // clear input box
                System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                        + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));
            }
        } else {
            printError(midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 9,
                    (maxX + minX - error.length()) / 2 - 1);
            printBox(minX, midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 6,
                    maxX - minX,
                    3,
                    "Input");

            for (String message : messages) {
                // writes message in input box and moves the cursor after the message
                System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                        + (minX + 1) + "H" + "\u001B[48;5;22m" + command + "\u001B[0m -> \u001B[38;5;242m" + message
                        + "\u001B[0m");

                // read input from user
                try {
                    char c = (char) stdin.read();
                    while (c != '\n') {
                        inputString += Character.toString(c);
                        c = (char) stdin.read();
                    }
                    outputString.add(inputString);
                    inputString = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // clear input box
                System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                        + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));
            }
        }
        return outputString;
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

    private void printError(int y, int x) {
        printBox(x, y, error.length() + 2, 3, "Error");
        x++;
        y++;
        for (int i = 0; i < error.split("\n").length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H\u001B[48;5;124m" + error + "\u001B[0m");
            y++;
        }
    }

    private void printScoreBoard(int y, int x) {
        printBox(x, y, scoreBoard.length() + 2, 3, "Scoreboard");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H" + scoreBoard);
    }

    private void printVirtualPoints(int y, int x) {
        printBox(x, y, virtualPoints.length() + 2, 3, "Tot");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H" + virtualPoints);
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
        System.out.print("\u001B[38;5;242m"); // set color to gray
        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (i == 0 && j == 0)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╭");
                else if (i == 0 && j == boxWidth - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╮");
                else if (i == boxHeight - 1 && j == 0)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╰");
                else if (i == boxHeight - 1 && j == boxWidth - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╯");
                else if (i == 0 && j == (boxWidth - title.length()) / 2) {
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j - 1) + "H " + title);
                    j = j + title.length();
                } else if (i == 0 || i == boxHeight - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H─");
                else if (j == 0 || j == boxWidth - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H│");
            }
        }
        System.out.print("\u001B[0m"); // reset color
    }
}
