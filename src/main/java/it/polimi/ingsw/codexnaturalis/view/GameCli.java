package it.polimi.ingsw.codexnaturalis.view;

import java.util.ArrayList;
import java.util.List;

public class GameCli {
    // Cursor starts from 1, 1 -> \u001B[y;xH
    List<String> hand, board, decks, objectives, initialCard, chooseObjectives;
    String resources, scoreBoard, virtualPoints, error, structure, nextPlayerStructure;
    int width = 175, height = 60, midHeight = 0, midWidth = 0, minX = 0, maxX = 0, delta = 0;

    // [ ] Other structures and resources
    // [ ] possible color coordinated boxes -> or just other color for other player
    // [ ] if other player -> box with "viewing plyer's name"
    // [ ] box with current player's name
    // [ ] box with current "state"
    // [ ] error box in initial phase

    public GameCli() {
        this.structure = "";
        this.nextPlayerStructure = "";
        this.hand = new ArrayList<>(List.of(""));
        this.board = new ArrayList<>(List.of(""));
        this.decks = new ArrayList<>(List.of("", ""));
        this.objectives = new ArrayList<>(List.of(""));
        this.initialCard = new ArrayList<>(List.of(""));
        this.chooseObjectives = new ArrayList<>(List.of(""));
        this.resources = "";
        this.scoreBoard = "";
        this.virtualPoints = "";
        this.error = "";
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

    public void updateNextPlayerStructure(String nextPlayerStructure) {
        if (nextPlayerStructure != null)
            this.nextPlayerStructure = nextPlayerStructure;
    }

    public void updateInitialCard(List<String> initialCard) {
        if (initialCard != null)
            this.initialCard = initialCard;
    }

    public void updateChooseObjectives(List<String> chooseObjectives) {
        if (chooseObjectives != null)
            this.chooseObjectives = chooseObjectives;
    }

    public void clearError() {
        this.error = "";
    }

    public void clear() {
        // set console size
        System.out.print("\u001B[8;" + height + ";" + width + "t");

        // clear console
        System.out.println("\033c");
    }

    public void clearInput() {
        String tmpStructure = "";
        // TODO: when "NEXT" -> maybe get index and update the player's structure
        // if (command.contains("viewing"))
        // tmpStructure = this.nextPlayerStructure;
        // else
        tmpStructure = this.structure;

        if (board.get(0).split("\n").length * board.size() / 2 > tmpStructure.split("\n").length / 2 + 2
                + hand.get(0).split("\n").length)
            System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                    + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));
        else
            System.out.print("\u001B["
                    + (midHeight + tmpStructure.split("\n").length / 2 + hand.get(0).split("\n").length + 7) + ";"
                    + (minX + 1) + "H" + " ".repeat(maxX - minX - 2));

    }

    public void printInitial()
            throws IllegalArgumentException {
        // TODO: add error box for initial phase

        if (initialCard == null || chooseObjectives == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        // TODO: throw all other exceptions for bad parameters

        midHeight = height / 2;
        midWidth = width / 2;

        printInitialCard(midHeight - initialCard.get(0).split("\n").length - 6,
                midWidth - initialCard.get(0).split("\n")[0].length(), initialCard);

        printChooseObjectives(midHeight - 2,
                midWidth - chooseObjectives.get(0).split("\n")[0].length(), chooseObjectives);

        minX = midWidth - chooseObjectives.get(0).split("\n")[0].length() - 4;
        maxX = midWidth + chooseObjectives.get(0).split("\n")[0].length() + 6;

        printBox(minX, midHeight + chooseObjectives.get(0).split("\n").length + 2, maxX - minX, 3, "Input");
        System.out.print(
                "\u001B[" + (midHeight + chooseObjectives.get(0).split("\n").length + 3) + ";" + (minX + 1) + "H");
    }

    // gets as input command and message -> need method that clears input box every
    // time it's called
    public void print() {
        midHeight = height / 2 - 3;
        delta = (maxX + minX) / 2 - width / 2;
        if (delta > 0)
            midWidth = (maxX + minX) / 2 - delta;
        else
            midWidth = (maxX + minX) / 2 + Math.abs(delta);

        String tmpStructure = "";
        // TODO: when "NEXT" -> maybe get index
        // if (command.contains("viewing"))
        // tmpStructure = this.nextPlayerStructure;
        // else
        tmpStructure = this.structure;

        printStructure(midHeight - tmpStructure.split("\n").length / 2,
                midWidth - tmpStructure.split("\n")[0].length() / 2, tmpStructure);

        printHand(midHeight + tmpStructure.split("\n").length / 2 + 3,
                midWidth - hand.get(0).split("\n")[0].length() * hand.size() / 2);

        if (hand.get(0).split("\n")[0].length() * hand.size() > tmpStructure.split("\n")[0].length()) {
            printBoard(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + hand.get(0).split("\n")[0].length() * hand.size() / 2 + 5);
            printDeck(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + hand.get(0).split("\n")[0].length() * hand.size() / 2 + 5
                            + board.get(0).split("\n")[0].length() + 3);
        } else {
            printBoard(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + tmpStructure.split("\n")[0].length() / 2 + 5);
            printDeck(midHeight - board.get(0).split("\n").length * board.size() / 2,
                    midWidth + tmpStructure.split("\n")[0].length() / 2 + 5
                            + board.get(0).split("\n")[0].length() + 3);
        }
        if (tmpStructure.split("\n").length < resources.split("\n").length)
            printResources(midHeight + tmpStructure.split("\n").length / 2 - resources.split("\n").length + 1,
                    midWidth - tmpStructure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length());
        else
            printResources(midHeight - resources.split("\n").length / 2,
                    midWidth - tmpStructure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length());

        printObjectives(midHeight - objectives.get(0).split("\n").length * objectives.size() / 2,
                midWidth - tmpStructure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length() - 4
                        - objectives.get(0).split("\n")[0].length());

        if (board.get(0).split("\n").length * board.size() < tmpStructure.split("\n").length) {
            printVirtualPoints(midHeight - tmpStructure.split("\n").length - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2);
            printScoreBoard(midHeight - tmpStructure.split("\n").length - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2 + virtualPoints.length()
                            + 3);
        } else {
            printVirtualPoints(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2);
            printScoreBoard(midHeight - board.get(0).split("\n").length * 2 - 4,
                    (maxX + minX) / 2 - (virtualPoints.length() + scoreBoard.length() + 4) / 2 + virtualPoints.length()
                            + 3);
        }

        if (board.get(0).split("\n").length * board.size() / 2 > tmpStructure.split("\n").length / 2 + 2
                + hand.get(0).split("\n").length) {
            printError(midHeight + board.get(0).split("\n").length * board.size() / 2 + 6,
                    (maxX + minX - error.length()) / 2 - 1);
            printBox(minX, midHeight + board.get(0).split("\n").length * board.size() / 2 + 3, maxX - minX, 3, "Input");
            System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 4) + ";"
                    + (minX + 1) + "H");
        } else {
            printError(midHeight + tmpStructure.split("\n").length / 2 + hand.get(0).split("\n").length + 9,
                    (maxX + minX - error.length()) / 2 - 1);
            printBox(minX, midHeight + tmpStructure.split("\n").length / 2 + hand.get(0).split("\n").length + 6,
                    maxX - minX, 3, "Input");
            System.out.print(
                    "\u001B[" + (midHeight + tmpStructure.split("\n").length / 2 + hand.get(0).split("\n").length + 7)
                            + ";" + (minX + 1) + "H");
        }
    }

    private void printStructure(int y, int x, String tmpStructure) {
        printBox(x, y, tmpStructure.split("\n")[0].length() + 2, tmpStructure.split("\n").length + 2, "Structure");
        x++;
        y++;
        for (int i = 0; i < tmpStructure.split("\n").length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H" + tmpStructure.split("\n")[i]);
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
        if (error.equals("")) {
            x -= 4;
            printBox(x, y, 9, 3, "Error");
        } else
            printBox(x, y, error.length() + 2, 3, "Error");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H\u001B[48;5;124m" + error + "\u001B[0m");
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
        // TODO: add color to box considering player
        // if (this.command.contains("viewing") && title.equals("Structure"))
        // System.out.print("\u001B[33m"); // set color to blue
        // else
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
