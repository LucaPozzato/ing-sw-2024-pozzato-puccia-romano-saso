package it.polimi.ingsw.codexnaturalis.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli {
    // Cursor starts from 1, 1 -> /u001B[y;xH
    // TODO: draw scoreboard with players and points
    String structure;
    List<String> hand;
    List<String> board;
    List<String> decks;
    List<String> objectives;
    String resources;
    String error;
    int width = 175;
    int height = 60;
    int midHeight = 0;
    int midWidth = 0;
    int minX = 0;
    int maxX = 0;
    Scanner stdin;

    public Cli(Scanner stdin) {
        this.structure = "";
        this.hand = new ArrayList<>(List.of(""));
        this.board = new ArrayList<>(List.of(""));
        this.decks = new ArrayList<>(List.of("", ""));
        this.objectives = new ArrayList<>(List.of(""));
        this.resources = "";
        this.error = "";
        this.stdin = stdin;
    }

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

    public void print() {
        // clear console
        System.out.println("\033c");

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
                midWidth - structure.split("\n")[0].length() / 2 - 6 - resources.split("\n")[0].length() - 5
                        - objectives.get(0).split("\n")[0].length());

        if (board.get(0).split("\n").length * board.size() / 2 > structure.split("\n").length / 2 + 2
                + hand.get(0).split("\n").length) {
            printError(midHeight + board.get(0).split("\n").length * board.size() / 2 + 3,
                    (maxX + minX - error.length()) / 2);
            printBox(minX, midHeight + board.get(0).split("\n").length * board.size() / 2 + 6, maxX - minX, 3, "Input");

            // moves cursor to the left of input box
            System.out.print("\u001B[" + (midHeight + board.get(0).split("\n").length * board.size() / 2 + 7) + ";"
                    + (minX + 1) + "H");
        } else {
            printError(midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 6,
                    (maxX + minX - error.length()) / 2);
            printBox(minX, midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 9,
                    maxX - minX,
                    3,
                    "Input");

            // moves cursor to the left of input box
            System.out.print("\u001B["
                    + (midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 10) + ";"
                    + (minX + 1) + "H");
        }

        // read input from user

        if (stdin.hasNextLine()) {
            String input = stdin.nextLine();
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

    private void printError(int y, int x) {
        printBox(x, y, error.length() + 2, 3, "Error");
        x++;
        y++;
        for (int i = 0; i < error.split("\n").length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H\u001B[48;5;124m" + error + "\u001B[0m");
            y++;
        }
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
