package it.polimi.ingsw.codexnaturalis.view.tui;

import java.util.ArrayList;
import java.util.List;

public class TerminalPrinter {
    // Cursor starts from 1, 1 -> \u001B[y;xH
    private List<List<String>> hands;
    private List<String> hand, board, decks, objectives, initialCard, chooseObjectives, structures, resourcesList,
            players;
    private String scoreBoard, alert, currentPlayer, currentState, structure, resources, chat, input;
    private int width = 175, height = 60, midHeight = 0, midWidth = 0, minX = 0, maxX = 0, delta = 0, indexPlayer = 0,
            indexMyPlayer = 0;

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
        this.input = "";
        this.chat = "";
    }

    /**
     * Set the size of the terminal window
     * 
     * @param width  the width of the terminal window
     * @param height the height of the terminal window
     */
    public void setSize(int width, int height) {
        if (width < 10 || height < 10) {
            this.width = 175;
            this.height = 60;
        } else {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Update the chat
     * 
     * @param chat the chat to set
     */
    public void updateChat(String chat) {
        this.chat = chat;
    }

    /**
     * Update the structure
     * 
     * @param structure the structure to set
     */
    public void updateStructures(List<String> structures) {
        if (structure != null)
            this.structures = structures;
    }

    /**
     * Update the hands
     * 
     * @param hands the hands to set
     */
    public void updateHands(List<List<String>> hands) {
        if (hands != null)
            this.hands = hands;
    }

    /**
     * Update the board
     * 
     * @param board the board to set
     */
    public void updateBoard(List<String> board) {
        if (board != null)
            this.board = board;
    }

    /**
     * Update the decks
     * 
     * @param decks the decks to set
     */
    public void updateDecks(List<String> decks) {
        if (decks != null)
            this.decks = decks;
    }

    /**
     * Update the secret objective
     * 
     * @param objectives the secret objective to set
     */
    public void updateSecretObjective(String secretObjective) {
        if (secretObjective != null)
            this.objectives.set(0, secretObjective);
    }

    /**
     * Update the common objectives
     * 
     * @param objectives the common objectives to set
     */
    public void updateCommonObjectives(List<String> objectives) {
        if (objectives != null) {
            this.objectives.set(1, objectives.get(0));
            this.objectives.set(2, objectives.get(1));
        }
    }

    /**
     * Update the input
     * 
     * @param input the input to set
     */
    public void updateInput(String input) {
        if (input != null)
            this.input = input;
    }

    /**
     * Update the resources
     * 
     * @param resources the resources to set
     */
    public void updateResources(List<String> resources) {
        if (resources != null)
            this.resourcesList = resources;
    }

    /**
     * Update the alert
     * 
     * @param alert the alert to set
     */
    public void updateAlert(String alert) {
        if (alert != null)
            this.alert = alert;
    }

    /**
     * Update the score board
     * 
     * @param scoreBoard the score board to set
     */
    public void updateScoreBoard(String scoreBoard) {
        if (scoreBoard != null)
            this.scoreBoard = scoreBoard;
    }

    /**
     * Update the initial card
     * 
     * @param initialCard the initial card to set
     */
    public void updateInitialCard(List<String> initialCard) {
        if (initialCard != null)
            this.initialCard = initialCard;
    }

    /**
     * Update the objectives to choose from
     * 
     * @param chooseObjectives the objectives to set
     */
    public void updateChooseObjectives(List<String> chooseObjectives) {
        if (chooseObjectives != null)
            this.chooseObjectives = chooseObjectives;
    }

    /**
     * Update the players
     * 
     * @param players the players to set
     */
    public void updatePlayers(List<String> players) {
        if (players != null)
            this.players = players;
    }

    /**
     * Update the current player
     * 
     * @param currentPlayer the current player to set
     */
    public void updateCurrentPlayer(String currentPlayer) {
        if (currentPlayer != null)
            this.currentPlayer = currentPlayer;
    }

    /**
     * Update the current state
     * 
     * @param currentState the current state to set
     */
    public void updateCurrentState(String currentState) {
        if (currentState != null)
            this.currentState = currentState;
    }

    /**
     * Update the index of the my player
     * 
     * @param index the index of my player
     */
    public void updateMyPlayer(Integer index) {
        indexMyPlayer = index;
        indexPlayer = index;
    }

    /**
     * Clear the alert message
     */
    public void clearAlert() {
        this.alert = "";
    }

    /**
     * Clear the terminal window
     */
    public void clear() {
        System.out.println("\033[?7h");

        // clear console
        System.out.println("\033[2J");
    }

    /**
     * Resets the view to my player
     */
    public void resetView() {
        alert = "";
        indexPlayer = indexMyPlayer;
        structure = structures.get(indexPlayer);
        hand = hands.get(indexPlayer);
        resources = resourcesList.get(indexPlayer);
    }

    /**
     * Clear the input field
     */
    public void clearInput() {
        this.input = "";
    }

    /**
     * Print the help page on the terminal
     */
    public void printHelp() {
        String help = "\u001B[1mCommands (case insensitive):\u001B[1m\n"
                + "\u001B[38;5;242m>\u001B[0m create: <Game ID>, <nick name>, <password>, <color>, <number of players>\n"
                + "\u001B[38;5;242m>\u001B[0m join: <Game ID>, <nick name>, <password>, <color>\n"
                + "\u001B[38;5;242m>\u001B[0m rejoin: <Game ID>, <nick name>, <password>\n"
                + "\u001B[38;5;242m>\u001B[0m choose: <side of card>, <objective card>\n"
                + "\u001B[38;5;242m>\u001B[0m place: <ID of card to place>,  <ID of bottom card>,  <position>, <side of card>\n"
                + "\u001B[38;5;242m>\u001B[0m draw: <ID of card to draw (also RXX/GXX)>\n"
                + "\u001B[38;5;242m>\u001B[0m chat\n"
                + "\u001B[38;5;242m>\u001B[0m send: <message> [, <nick name of receiver>]\n"
                + "\u001B[38;5;242m>\u001B[0m next\n"
                + "\u001B[38;5;242m>\u001B[0m esc\n"
                + "\u001B[38;5;242m>\u001B[0m help\n"
                + "\u001B[38;5;242m>\u001B[0m quit\n\n"
                + "\u001B[1mPossible colors:\u001B[0m\n"
                + "- yellow\n"
                + "- red\n"
                + "- blue\n"
                + "- green\n\n"
                + "\u001B[1mPossible value of objective card:\u001B[0m\n"
                + "- 1 (objective card 1)\n"
                + "- 2 (objective card 2)\n\n"
                + "\u001B[1mPossible value of position:\u001B[0m\n"
                + "- TL (top-left)\n"
                + "- TR (top-right)\n"
                + "- BL (bottom-left)\n"
                + "- BR (bottom-right)\n\n"
                + "\u001B[1mPossible side of card:\u001B[0m\n"
                + "- front\n"
                + "- back\n";

        String[] helpLines = help.split("\n");

        // get max length of help lines
        int maxWidth = 0;
        for (int i = 0; i < helpLines.length; i++) {
            if (helpLines[i].length() > maxWidth)
                maxWidth = helpLines[i].length();
        }

        int x = (width - maxWidth) / 2;
        int y = (height - helpLines.length) / 2 - 2;

        printBox(x, y, maxWidth + 2, helpLines.length + 2, "HELP PAGE");
        x++;
        y++;
        for (int i = 0; i < helpLines.length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H" + helpLines[i]);
            y++;
        }

        x--;
        printBox(x, y + 1, maxWidth + 2, 3, "Input");
        System.out.print("\u001B[" + (y + 2) + ";" + (x + 1) + "H" + input);
    }

    /**
     * Print the chat on the terminal
     */
    public void printChat() {
        // function that prints the chat
        int x;
        int y;
        int boxWidth;
        int boxHeight;
        String title = "Chat";

        boxWidth = maxX - minX;
        boxHeight = (int) (height * 0.6);
        x = minX;
        y = (height - boxHeight) / 2 - 4;

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

        printAlert(y + boxHeight + 4,
                ((maxX - minX) / 2) + minX - (Math.max("Alert".length(), alert.length()) / 2) - 1);

        // print input box
        printBox(x, y + boxHeight + 1, boxWidth, 3, "Input");

        System.out.print("\u001B[" + (y + boxHeight + 2) + ";" + (x + 1) + "H" + input);
    }

    /**
     * Print the initial stage on the terminal
     */
    public void printInitialStage() {
        // print input box and error box

        midHeight = height / 2;
        midWidth = width / 2;

        printAlert(midHeight + 2, midWidth - Math.max("Alert".length(), alert.length()) / 2 - 1);

        printBox(midWidth - 21, midHeight - 2, 43, 3, "Input");
        System.out.print("\u001B[" + (midHeight - 1) + ";" + (midWidth - 20) + "H" + input);
    }

    /**
     * Print the choose phase on the terminal
     */
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
                "\u001B[" + (midHeight + chooseObjectives.get(0).split("\n").length + 3) + ";" + (minX + 1) + "H"
                        + input);
    }

    /**
     * Print the game details of the next player on the terminal
     */
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

    /**
     * Print the game (scoreboard, structure, deck, ...) details on the terminal
     */
    public void printGame() {
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
                    + (minX + 1) + "H" + input);
        } else {
            printAlert(midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 9,
                    (maxX + minX - Math.max("Alert".length(), alert.length())) / 2 - 1);
            printBox(minX, midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 6,
                    maxX - minX, 3, "Input");
            System.out.print(
                    "\u001B[" + (midHeight + structure.split("\n").length / 2 + hand.get(0).split("\n").length + 7)
                            + ";" + (minX + 1) + "H" + input);
        }
    }

    /**
     * Print the structure on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the structure
     * @param x the x coordinate of the terminal window where to print the structure
     */
    private void printStructure(int y, int x) {
        printBox(x, y, structure.split("\n")[0].length() + 2, structure.split("\n").length + 2, "Structure");
        x++;
        y++;
        for (int i = 0; i < structure.split("\n").length; i++) {
            System.out.print("\u001B[" + y + ";" + x + "H" + structure.split("\n")[i]);
            y++;
        }
    }

    /**
     * Print the hand on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the hand
     * @param x the x coordinate of the terminal window where to print the hand
     */
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

    /**
     * Print the board on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the board
     * @param x the x coordinate of the terminal window where to print the board
     */
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

    /**
     * Print the deck on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the deck
     * @param x the x coordinate of the terminal window where to print the deck
     */
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

    /**
     * Print the objectives on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the
     *          objectives
     * @param x the x coordinate of the terminal window where to print the
     *          objectives
     */
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

    /**
     * Print the resources on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the resources
     * @param x the x coordinate of the terminal window where to print the resources
     */
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

    /**
     * Print the alert on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the alert
     * @param x the x coordinate of the terminal window where to print the alert
     */
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

    /**
     * Print the scoreboard on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the
     *          scoreboard
     * @param x the x coordinate of the terminal window where to print the
     *          scoreboard
     */
    private void printScoreBoard(int y, int x) {
        printBox(x, y, scoreBoard.length() + 2, 3, "Scoreboard");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H" + scoreBoard);
    }

    /**
     * Print the current player on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the current
     *          player
     * @param x the x coordinate of the terminal window where to print the current
     *          player
     */
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

    /**
     * Print the current state on the terminal
     * 
     * @param y the y coordinate of the terminal window where to print the current
     *          state
     * @param x the x coordinate of the terminal window where to print the current
     *          state
     */
    private void printCurrentState(int y, int x) {
        printBox(x, y, Math.max("State".length(), currentState.length()) + 2, 3, "State");
        x++;
        y++;
        System.out.print("\u001B[" + y + ";" + x + "H" + currentState);
    }

    /**
     * Print the initial card on the terminal
     * 
     * @param y           the y coordinate of the terminal window where to print the
     *                    initial
     *                    card
     * @param x           the x coordinate of the terminal window where to print the
     *                    initial
     *                    card
     * @param initialCard the initial card to print in string format [front side &&
     *                    back side]
     */
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

    /**
     * Print the objectives choose from on the terminal
     * 
     * @param y                the y coordinate of the terminal window where to
     *                         print the
     *                         objectives
     * @param x                the x coordinate of the terminal window where to
     *                         print the
     *                         objectives
     * @param chooseObjectives the objectives to print in a string format [objective
     *                         1, objective 2]
     */
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

    /**
     * Print a box on the terminal
     * 
     * @param x         the x coordinate of the terminal window where to print the
     *                  box
     * @param y         the y coordinate of the terminal window where to print the
     *                  box
     * @param boxWidth  the width of the box
     * @param boxHeight the height of the box
     * @param title     the title of the box
     */
    private void printBox(int x, int y, int boxWidth, int boxHeight, String title) {
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
