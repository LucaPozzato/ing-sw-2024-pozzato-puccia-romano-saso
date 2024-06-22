package it.polimi.ingsw.codexnaturalis.view.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Triplet;

public class Painter {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[38;5;196m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BG_BRIGHT_RED = "\u001B[48;5;124m";
    private static final String ANSI_DARK_GRAY = "\u001B[38;5;242m";
    private static final String ANSI_BG_YELLOW = "\u001B[48;5;94m";
    private static final String ANSI_BG_RED = "\u001B[48;5;88m";
    private static final String ANSI_BG_GREEN = "\u001B[48;5;22m";
    private static final String ANSI_BG_BLUE = "\u001B[48;5;19m";
    private static final String ANSI_BG_PURPLE = "\u001B[48;5;55m";

    /**
     * Paints the list of the strings representing the cards
     * 
     * @param stringList the list of strings that contain the cards to be painted
     * @param cardList   the list of cards to be painted
     * @return the updated colored list of strings
     * @throws IllegalCommandException
     */
    public List<String> paintListOfCards(List<String> stringList, List<Card> cardList)
            throws IllegalCommandException {
        List<String> updatedStringList = new ArrayList<>();
        String[] cardLines;
        String card;

        for (int k = 0; k < stringList.size(); k++) {
            card = stringList.get(k);
            updatedStringList.add("");
            int pos = updatedStringList.size() - 1;
            cardLines = card.split("\n");
            for (String cardLine : cardLines) {
                for (int i = 0; i < cardLine.length(); i++) {
                    switch (cardLine.charAt(i)) {
                        case 'A':
                            updatedStringList.set(pos, updatedStringList.get(pos) + ANSI_BLUE + cardLine.charAt(i)
                                    + ANSI_RESET);
                            break;
                        case 'V':
                            updatedStringList.set(pos, updatedStringList.get(pos) + ANSI_GREEN + cardLine.charAt(i)
                                    + ANSI_RESET);
                            break;
                        case 'I', 'І':
                            if (Character.isDigit(cardLine.charAt(i + 1))) {
                                updatedStringList.set(pos,
                                        updatedStringList.get(pos) + ANSI_BG_YELLOW + cardLine.charAt(i));
                                updatedStringList.set(pos, updatedStringList.get(pos) + cardLine.charAt(i + 1));
                                updatedStringList.set(pos,
                                        updatedStringList.get(pos) + cardLine.charAt(i + 2) + ANSI_RESET);
                                i += 2;
                            } else if (cardLine.charAt(i) == 'I')
                                updatedStringList.set(pos, updatedStringList.get(pos) + ANSI_PURPLE + cardLine.charAt(i)
                                        + ANSI_RESET);
                            else {
                                updatedStringList.set(pos, updatedStringList.get(pos) + cardLine.charAt(i));
                            }
                            break;
                        case 'R', 'G':
                            // Calculates the coordinates given the index of the matrix
                            switch (cardList.get(k).getSymbol()) {
                                case "SHROOM":
                                    updatedStringList.set(pos,
                                            updatedStringList.get(pos) + ANSI_BG_RED + cardLine.charAt(i));
                                    break;
                                case "ANIMAL":
                                    updatedStringList.set(pos,
                                            updatedStringList.get(pos) + ANSI_BG_BLUE + cardLine.charAt(i));
                                    break;
                                case "VEGETABLE":
                                    updatedStringList.set(pos,
                                            updatedStringList.get(pos) + ANSI_BG_GREEN + cardLine.charAt(i));
                                    break;
                                case "INSECT":
                                    updatedStringList.set(pos,
                                            updatedStringList.get(pos) + ANSI_BG_PURPLE + cardLine.charAt(i));
                                    break;
                                default:
                                    break;
                            }
                            updatedStringList.set(pos, updatedStringList.get(pos) + cardLine.charAt(i + 1));
                            updatedStringList.set(pos,
                                    updatedStringList.get(pos) + cardLine.charAt(i + 2) + ANSI_RESET);
                            i += 2;
                            break;
                        case 'S':
                            updatedStringList.set(pos, updatedStringList.get(pos) + ANSI_RED + cardLine.charAt(i)
                                    + ANSI_RESET);
                            break;
                        case 's':
                            updatedStringList.set(pos,
                                    updatedStringList.get(pos) + ANSI_YELLOW
                                            + Character.toUpperCase(cardLine.charAt(i))
                                            + ANSI_RESET);
                            break;
                        case 'i':
                            updatedStringList.set(pos,
                                    updatedStringList.get(pos) + ANSI_YELLOW
                                            + Character.toUpperCase(cardLine.charAt(i))
                                            + ANSI_RESET);
                            break;
                        case 'f':
                            updatedStringList.set(pos,
                                    updatedStringList.get(pos) + ANSI_YELLOW
                                            + Character.toUpperCase(cardLine.charAt(i))
                                            + ANSI_RESET);
                            break;
                        case 'N':
                            updatedStringList.set(pos,
                                    updatedStringList.get(pos) + ANSI_BG_BRIGHT_RED + 'X' + ANSI_RESET);
                            break;
                        case 'E':
                            updatedStringList.set(pos, updatedStringList.get(pos) + ANSI_DARK_GRAY + '░' + ANSI_RESET);
                            break;
                        default:
                            updatedStringList.set(pos, updatedStringList.get(pos) + cardLine.charAt(i));
                            break;
                    }
                }
                updatedStringList.set(pos, updatedStringList.get(pos) + "\n");
            }
            pos++;
        }

        return updatedStringList;
    }

    /**
     * Paints the structure
     * 
     * @param board            the strcuture to be painted as a matrix of characters
     * @param coordinateToCard the map that contains the coordinates of the cards
     *                         and how they are placed
     * @return the string representing the colored/painted board
     * @throws IllegalCommandException
     */
    public String paintStructure(char[][] board, Map<Integer, Triplet<Card, Boolean, Boolean>> coordinateToCard)
            throws IllegalCommandException {
        String structureString = "";

        if (coordinateToCard.isEmpty())
            return structureString;

        int minX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 253 + (i / 100 - 40) * 6)
                .min().getAsInt();
        int minY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 87 - (i % 100 - 40) * 2)
                .min().getAsInt();
        int maxX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 253 + (i / 100 - 40) * 6)
                .max().getAsInt();
        int maxY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 87 - (i % 100 - 40) * 2)
                .max().getAsInt();

        for (int i = minY - 2; i <= maxY + 2; i++) {
            for (int j = minX - 4; j <= maxX + 4; j++) {
                if (board[i][j] == 0)
                    structureString += " ";
                else
                    switch (board[i][j]) {
                        case 'A':
                            structureString += ANSI_BLUE + board[i][j] + ANSI_RESET;
                            break;
                        case 'V':
                            structureString += ANSI_GREEN + board[i][j] + ANSI_RESET;
                            break;
                        case 'I', 'І':
                            if (Character.isDigit(board[i][j + 1])) {
                                structureString += ANSI_BG_YELLOW + board[i][j];
                                structureString += board[i][j + 1];
                                structureString += board[i][j + 2] + ANSI_RESET;
                                j += 2;
                            } else if (board[i][j] == 'I')
                                structureString += ANSI_PURPLE + board[i][j] + ANSI_RESET;
                            else {
                                structureString += board[i][j];
                            }
                            break;
                        case 'R', 'G':
                            // Calculates the coordinates given the index of the matrix -> inverting the
                            // mathematical formula except for the x axis in which you take index of the
                            // center of the ID -> hence j - 252
                            switch (coordinateToCard.get((((j - 252) / 6 + 40) * 100) + (((87 - i) / 2) + 40))
                                    .getFirst()
                                    .getSymbol()) {
                                case "SHROOM":
                                    structureString += ANSI_BG_RED + board[i][j];
                                    break;
                                case "ANIMAL":
                                    structureString += ANSI_BG_BLUE + board[i][j];
                                    break;
                                case "VEGETABLE":
                                    structureString += ANSI_BG_GREEN + board[i][j];
                                    break;
                                case "INSECT":
                                    structureString += ANSI_BG_PURPLE + board[i][j];
                                    break;
                                default:
                                    break;
                            }
                            structureString += board[i][j + 1];
                            structureString += board[i][j + 2] + ANSI_RESET;
                            j += 2;
                            break;
                        case 'S':
                            structureString += ANSI_RED + board[i][j] + ANSI_RESET;
                            break;
                        case 's':
                            structureString += ANSI_YELLOW + Character.toUpperCase(board[i][j]) + ANSI_RESET;
                            break;
                        case 'i':
                            structureString += ANSI_YELLOW + Character.toUpperCase(board[i][j]) + ANSI_RESET;
                            break;
                        case 'f':
                            structureString += ANSI_YELLOW + Character.toUpperCase(board[i][j]) + ANSI_RESET;
                            break;
                        case 'N':
                            structureString += ANSI_BG_BRIGHT_RED + 'X' + ANSI_RESET;
                            break;
                        case 'E':
                            structureString += ANSI_DARK_GRAY + '░' + ANSI_RESET;
                            break;
                        default:
                            structureString += board[i][j];
                            break;
                    }
            }
            structureString += "\n";
        }
        return structureString;
    }

    /**
     * Paints the deck
     * 
     * @param decks the list of cards to be painted
     * @return the list of strings representing the painted deck
     * @throws IllegalCommandException
     */
    public List<String> paintDeck(List<Card> decks) throws IllegalCommandException {
        List<String> deckStringList = new ArrayList<>();
        for (int i = 0; i < decks.size(); i++) {
            deckStringList.add(
                    "╭─┬───┬─╮\n│"
                            + ANSI_DARK_GRAY + '░' + ANSI_RESET + "│   │" + ANSI_DARK_GRAY + '░' + ANSI_RESET
                            + "│\n├─┤");
            switch (decks.get(i).getSymbol()) {
                case "SHROOM":
                    deckStringList.set(i,
                            deckStringList.get(i) + ANSI_BG_RED + decks.get(i).getIdCard().charAt(0) + "XX"
                                    + ANSI_RESET + "├─┤\n│");
                    break;
                case "ANIMAL":
                    deckStringList.set(i,
                            deckStringList.get(i) + ANSI_BG_BLUE + decks.get(i).getIdCard().charAt(0) + "XX"
                                    + ANSI_RESET + "├─┤\n│");
                    break;
                case "VEGETABLE":
                    deckStringList.set(i,
                            deckStringList.get(i) + ANSI_BG_GREEN + decks.get(i).getIdCard().charAt(0) + "XX"
                                    + ANSI_RESET + "├─┤\n│");
                    break;
                case "INSECT":
                    deckStringList.set(i,
                            deckStringList.get(i) + ANSI_BG_PURPLE + decks.get(i).getIdCard().charAt(0) + "XX"
                                    + ANSI_RESET + "├─┤\n│");
                    break;
                default:
                    break;
            }
            deckStringList.set(i,
                    deckStringList.get(i) + ANSI_DARK_GRAY + '░' + ANSI_RESET + "│   │" + ANSI_DARK_GRAY + '░'
                            + ANSI_RESET + "│\n"
                            + "╰─┴───┴─╯\n");
        }
        return deckStringList;
    }

    /**
     * Paints the initial card
     * 
     * @param card the card to be painted
     * @return the list of strings representing the painted card
     */
    public List<String> paintInitialCard(List<String> card) {
        List<String> fullCard = new ArrayList<>();
        String cardFace = "";
        for (String face : card) {
            for (String line : face.split("\n")) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == 0)
                        cardFace += " ";
                    else
                        switch (line.charAt(i)) {
                            case 'A':
                                cardFace += ANSI_BLUE + line.charAt(i) + ANSI_RESET;
                                break;
                            case 'V':
                                cardFace += ANSI_GREEN + line.charAt(i) + ANSI_RESET;
                                break;
                            case 'I', 'І':
                                if (Character.isDigit(line.charAt(i + 1))) {
                                    cardFace += ANSI_BG_YELLOW + line.charAt(i);
                                    cardFace += line.charAt(i + 1);
                                    cardFace += line.charAt(i + 2) + ANSI_RESET;
                                    i += 2;
                                } else if (line.charAt(i) == 'I')
                                    cardFace += ANSI_PURPLE + line.charAt(i) + ANSI_RESET;
                                else {
                                    cardFace += line.charAt(i);
                                }
                                break;
                            case 'S':
                                cardFace += ANSI_RED + line.charAt(i) + ANSI_RESET;
                                break;
                            case 's':
                                cardFace += ANSI_YELLOW + Character.toUpperCase(line.charAt(i)) + ANSI_RESET;
                                break;
                            case 'i':
                                cardFace += ANSI_YELLOW + Character.toUpperCase(line.charAt(i)) + ANSI_RESET;
                                break;
                            case 'f':
                                cardFace += ANSI_YELLOW + Character.toUpperCase(line.charAt(i)) + ANSI_RESET;
                                break;
                            case 'N':
                                cardFace += ANSI_BG_BRIGHT_RED + 'X' + ANSI_RESET;
                                break;
                            case 'E':
                                cardFace += ANSI_DARK_GRAY + '░' + ANSI_RESET;
                                break;
                            default:
                                cardFace += line.charAt(i);
                                break;
                        }
                }
                cardFace += "\n";
            }
            cardFace = cardFace.substring(0, cardFace.length() - 1);
            fullCard.add(cardFace);
            cardFace = "";
        }
        return fullCard;
    }
}
