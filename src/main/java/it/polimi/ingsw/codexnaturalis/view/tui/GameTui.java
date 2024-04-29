package it.polimi.ingsw.codexnaturalis.view.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.View;
import javafx.util.Pair;

public class GameTui implements View {
    Printer printer = new Printer();
    Renderer renderer = new Renderer();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[38;5;196m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    public char[][] insertCardInMatrix(char[][] matrix, Card card, int x, int y, Boolean side)
            throws IllegalCommandException {
        // TODO: update error when handling exeptions

        List<String> corners;
        List<String> cardString = new ArrayList<>();

        String topCornersLine = "";
        String bottomCornersLine = "";
        String className = "";

        if (side)
            corners = card.getFrontCorners();
        else
            corners = card.getBackCorners();

        String[] tmpStringArray = card.getClass().getName().split("\\.");
        className = tmpStringArray[tmpStringArray.length - 1];

        switch (className) {
            case "InitialCard":
                // top corners line
                if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                        || corners.get(0).equals("FEATHER"))
                    topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│";
                else
                    topCornersLine = "│" + corners.get(0).charAt(0) + "│";

                if (card.getFrontCenterResources().size() == 2 && side)
                    topCornersLine += " " + card.getFrontCenterResources().get(1).charAt(0) + " ";
                else if (card.getFrontCenterResources().size() == 3 && side)
                    topCornersLine += card.getFrontCenterResources().get(1).charAt(0) + " "
                            + card.getFrontCenterResources().get(2).charAt(0);
                else
                    topCornersLine += "   ";

                if (corners.get(1).equals("INK") || corners.get(1).equals("SCROLL")
                        || corners.get(1).equals("FEATHER"))
                    topCornersLine += "│" + Character.toLowerCase(corners.get(1).charAt(0)) + "│";
                else
                    topCornersLine += "│" + corners.get(1).charAt(0) + "│";

                // bottom corners line
                if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
                    bottomCornersLine = "│" + Character.toLowerCase(corners.get(2).charAt(0)) + "│";
                else
                    bottomCornersLine = "│" + corners.get(2).charAt(0) + "│";

                if (side) {
                    bottomCornersLine += " " + card.getFrontCenterResources().get(0).charAt(0) + " ";
                } else {
                    bottomCornersLine += "   ";
                }

                if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
                    bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
                else
                    bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";
                break;

            case "ResourceCard":
                // top corners line
                if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                        || corners.get(0).equals("FEATHER"))
                    topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│";
                else
                    topCornersLine = "│" + corners.get(0).charAt(0) + "│";

                if (card.getPoints() > 0 && side) {
                    topCornersLine += " " + Integer.toString(card.getPoints()).charAt(0) + " ";
                } else {
                    topCornersLine += "   ";
                }

                if (corners.get(1).equals("INK") || corners.get(1).equals("SCROLL")
                        || corners.get(1).equals("FEATHER"))
                    topCornersLine += "│" + Character.toLowerCase(corners.get(1).charAt(0)) + "│";
                else
                    topCornersLine += "│" + corners.get(1).charAt(0) + "│";

                // bottom corners line
                if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
                    bottomCornersLine = "│" + Character.toLowerCase(corners.get(2).charAt(0)) + "│";
                else
                    bottomCornersLine = "│" + corners.get(2).charAt(0) + "│";

                if (!side) {
                    bottomCornersLine += " " + card.getSymbol().charAt(0) + " ";
                } else {
                    bottomCornersLine += "   ";
                }

                if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
                    bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
                else
                    bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";
                break;

            case "GoldCard":
                // top corners line
                if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                        || corners.get(0).equals("FEATHER"))
                    topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│";
                else
                    topCornersLine = "│" + corners.get(0).charAt(0) + "│";

                if (side) {
                    if (!card.getPointsType().equals("NULL")) {
                        switch (card.getPointsType()) {
                            case "INK":
                                topCornersLine += Integer.toString(card.getPoints()).charAt(0) + " І";
                                break;
                            case "SCROLL":
                                topCornersLine += Integer.toString(card.getPoints()).charAt(0) + " Ѕ";
                                break;
                            case "FEATHER":
                                topCornersLine += Integer.toString(card.getPoints()).charAt(0) + " Ϝ";
                                break;
                            case "ANGLE":
                                topCornersLine += Integer.toString(card.getPoints()).charAt(0) + " А";
                                break;
                            default:
                                break;
                        }
                    } else
                        topCornersLine += " " + Integer.toString(card.getPoints()).charAt(0) + " ";
                } else
                    topCornersLine += "   ";

                if (corners.get(1).equals("INK") || corners.get(1).equals("SCROLL")
                        || corners.get(1).equals("FEATHER"))
                    topCornersLine += "│" + Character.toLowerCase(corners.get(1).charAt(0)) + "│";
                else
                    topCornersLine += "│" + corners.get(1).charAt(0) + "│";

                // bottom corners line
                if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
                    bottomCornersLine = "│" + Character.toLowerCase(corners.get(2).charAt(0)) + "│";
                else
                    bottomCornersLine = "│" + corners.get(2).charAt(0) + "│";

                if (!side) {
                    bottomCornersLine += " " + card.getSymbol().charAt(0) + " ";
                } else {
                    bottomCornersLine += "   ";
                }

                if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
                    bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
                else
                    bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";
                break;

            default:
                break;
        }

        cardString.add("╭─┬───┬─╮");
        cardString.add(topCornersLine);

        if (card instanceof InitialCard)
            cardString.add("├─┤" + "І" + card.getIdCard().charAt(1) + card.getIdCard().charAt(2) + "├─┤");
        else
            cardString.add("├─┤" + card.getIdCard() + "├─┤");

        cardString.add(bottomCornersLine);
        cardString.add("╰─┴───┴─╯");

        for (int i = -2; i <= 2; i++) {
            System.arraycopy(cardString.get(2 + i).toCharArray(), 0, matrix[y + i], x - 4, 9);
        }

        return matrix;
    }

    private List<String> drawFullInitialCard(Card card) throws IllegalCommandException {
        char drawingBoard[][] = new char[5][9];
        drawingBoard = insertCardInMatrix(drawingBoard, card, 4, 2, true);
        String cardFrontUp = "";
        for (int i = 0; i < 5; i++) {
            cardFrontUp += new String(drawingBoard[i]) + "\n";
        }
        drawingBoard = insertCardInMatrix(drawingBoard, card, 4, 2, false);
        String cardBackUp = "";
        for (int i = 0; i < 5; i++) {
            cardBackUp += new String(drawingBoard[i]) + "\n";
        }
        return new Renderer().printInitialCard(List.of(cardFrontUp, cardBackUp));
    }

    private String drawResourceCard(Card card, Boolean side) throws IllegalCommandException {
        List<String> corners;
        String cardString;

        String topLine = "╭─┬───────┬─╮";
        String topCornersLine;
        String topMiddleLine;
        String centerLine;
        String bottomMiddleLine;
        String bottomCornersLine;
        String bottomLine;

        if (side)
            corners = card.getFrontCorners();
        else
            corners = card.getBackCorners();

        // top corners line
        if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                || corners.get(0).equals("FEATHER"))
            topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│ ";
        else
            topCornersLine = "│" + corners.get(0).charAt(0) + "│ ";

        if (card.getPoints() == 0)
            topCornersLine += "     ";
        else
            topCornersLine += "  " + Integer.toString(card.getPoints()).charAt(0) + "  ";

        if (corners.get(1).equals("INK") || corners.get(1).equals("SCROLL")
                || corners.get(1).equals("FEATHER"))
            topCornersLine += " │" + Character.toLowerCase(corners.get(1).charAt(0)) + "│";
        else
            topCornersLine += " │" + corners.get(1).charAt(0) + "│";

        // top middle line
        if (side)
            topMiddleLine = "├─╯       ╰─┤";
        else
            topMiddleLine = "├─╯  ╭─╮  ╰─┤";

        // center line
        if (side)
            centerLine = "│    " + card.getIdCard() + "    │";
        else
            centerLine = "│    │" + card.getSymbol().charAt(0) + "│    │";

        // bottom middle line
        if (side)
            bottomMiddleLine = "├─╮       ╭─┤";
        else
            bottomMiddleLine = "├─╮  ╰─╯  ╭─┤";

        // bottom corners line
        if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
            bottomCornersLine = "│" + Character.toLowerCase(corners.get(2).charAt(0)) + "│ ";
        else
            bottomCornersLine = "│" + corners.get(2).charAt(0) + "│ ";

        if (side)
            bottomCornersLine += "     ";
        else
            bottomCornersLine += " " + card.getIdCard() + " ";

        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            bottomCornersLine += " │" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
        else
            bottomCornersLine += " │" + corners.get(3).charAt(0) + "│";

        // bottom line
        bottomLine = "╰─┴───────┴─╯";

        cardString = topLine + "\n" + topCornersLine + "\n" + topMiddleLine + "\n" + centerLine + "\n"
                + bottomMiddleLine
                + "\n" + bottomCornersLine + "\n" + bottomLine;

        return cardString;
    }

    private String drawGoldCard(Card card, Boolean side) throws IllegalCommandException {
        List<String> corners;
        String cardString;
        String cardRequirements = "";

        String topLine = "╭─┬───────┬─╮";
        String topCornersLine;
        String topMiddleLine;
        String centerLine;
        String bottomMiddleLine;
        String bottomCornersLine;
        String bottomLine;

        if (side)
            corners = card.getFrontCorners();
        else
            corners = card.getBackCorners();

        // top corners line
        if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                || corners.get(0).equals("FEATHER"))
            topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│ ";
        else
            topCornersLine = "│" + corners.get(0).charAt(0) + "│ ";

        if (!card.getPointsType().equals("NULL")) {
            switch (card.getPointsType()) {
                case "INK":
                    topCornersLine += Integer.toString(card.getPoints()).charAt(0) + "   І";
                    break;
                case "SCROLL":
                    topCornersLine += Integer.toString(card.getPoints()).charAt(0) + "   Ѕ";
                    break;
                case "FEATHER":
                    topCornersLine += Integer.toString(card.getPoints()).charAt(0) + "   Ϝ";
                    break;
                case "ANGLE":
                    topCornersLine += Integer.toString(card.getPoints()).charAt(0) + "   А";
                    break;
                default:
                    break;
            }
        } else
            topCornersLine += "  " + Integer.toString(card.getPoints()).charAt(0) + "  ";

        if (corners.get(1).equals("INK") || corners.get(1).equals("SCROLL")
                || corners.get(1).equals("FEATHER"))
            topCornersLine += " │" + Character.toLowerCase(corners.get(1).charAt(0)) + "│";
        else
            topCornersLine += " │" + corners.get(1).charAt(0) + "│";

        // top middle line
        if (side)
            topMiddleLine = "├─╯       ╰─┤";
        else
            topMiddleLine = "├─╯  ╭─╮  ╰─┤";

        // center line
        if (side)
            centerLine = "│    " + card.getIdCard() + "    │";
        else
            centerLine = "│    │" + card.getSymbol().charAt(0) + "│    │";

        // bottom middle line
        if (side)
            bottomMiddleLine = "├─╮╭─────╮╭─┤";
        else
            bottomMiddleLine = "├─╮  ╰─╯  ╭─┤";

        // bottom corners line
        if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
            bottomCornersLine = "│" + Character.toLowerCase(corners.get(2).charAt(0)) + "│";
        else
            bottomCornersLine = "│" + corners.get(2).charAt(0) + "│";

        if (side) {
            for (Map.Entry<String, Integer> entry : card.getRequirements().entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    cardRequirements += entry.getKey().charAt(0);
                }
            }
            switch (cardRequirements.length()) {
                case 3:
                    bottomCornersLine += "│ " + cardRequirements.charAt(0) + cardRequirements.charAt(1)
                            + cardRequirements.charAt(2) + " │";
                    break;
                case 4:
                    bottomCornersLine += "│" + cardRequirements.charAt(0) + cardRequirements.charAt(1) + " "
                            + cardRequirements.charAt(2) + cardRequirements.charAt(3) + "│";
                    break;
                case 5:
                    bottomCornersLine += "│" + cardRequirements.charAt(0) + cardRequirements.charAt(1)
                            + cardRequirements.charAt(2) + cardRequirements.charAt(3) + cardRequirements.charAt(4)
                            + "│";
                    break;
                default:
                    break;
            }
        } else
            bottomCornersLine += "  " + card.getIdCard() + "  ";

        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
        else
            bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";

        // bottom line
        if (side)
            bottomLine = "╰─┴┴─────┴┴─╯";
        else
            bottomLine = "╰─┴───────┴─╯";

        cardString = topLine + "\n" + topCornersLine + "\n" + topMiddleLine + "\n" + centerLine + "\n"
                + bottomMiddleLine
                + "\n" + bottomCornersLine + "\n" + bottomLine;

        return cardString;
    }

    private String drawObjectiveCard(Card card) throws IllegalCommandException {
        String cardString = "";
        switch (card.getShape()) {
            case "STAIRS":
                switch (card.getMustHave()) {
                    case "SHROOM":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│  " + ANSI_RED + "╭─╮"
                                + ANSI_RESET
                                + "      │\n│  " + ANSI_RED + "╰─╭─╮" + ANSI_RESET + "    │\n│    " + ANSI_RED + "╰─╭─╮"
                                + ANSI_RESET + "  │\n│      " + ANSI_RED + "╰─╯" + ANSI_RESET + "  │\n╰───────────╯";
                        break;
                    case "ANIMALS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│  " + ANSI_BLUE + "╭─╮"
                                + ANSI_RESET
                                + "      │\n│  " + ANSI_BLUE + "╰─╭─╮" + ANSI_RESET + "    │\n│    " + ANSI_BLUE
                                + "╰─╭─╮"
                                + ANSI_RESET + "  │\n│      " + ANSI_BLUE + "╰─╯" + ANSI_RESET + "  │\n╰───────────╯";
                        break;
                    case "VEGETABLES":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│      " + ANSI_GREEN + "╭─╮"
                                + ANSI_RESET
                                + "  │\n│    " + ANSI_GREEN + "╭─╮─╯" + ANSI_RESET + "  │\n│  " + ANSI_GREEN + "╭─╮─╯"
                                + ANSI_RESET + "    │\n│  " + ANSI_GREEN + "╰─╯" + ANSI_RESET
                                + "      │\n╰───────────╯";
                        break;
                    case "INSECTS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│      " + ANSI_PURPLE + "╭─╮"
                                + ANSI_RESET
                                + "  │\n│    " + ANSI_PURPLE + "╭─╮─╯" + ANSI_RESET + "  │\n│  " + ANSI_PURPLE + "╭─╮─╯"
                                + ANSI_RESET + "    │\n│  " + ANSI_PURPLE + "╰─╯" + ANSI_RESET
                                + "      │\n╰───────────╯";
                        break;
                    default:
                        break;
                }
                break;
            case "CHAIR":
                switch (card.getMustHave()) {
                    case "SHROOM":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   3  │\n│   " + ANSI_RED + "╭─╮"
                                + ANSI_RESET
                                + "     │\n│   " + ANSI_RED + "╭─╮" + ANSI_RESET + "     │\n│   " + ANSI_RED + "╰─╯"
                                + ANSI_GREEN + "─╮" + ANSI_RESET + "   │\n│     " + ANSI_GREEN + "╰─╯" + ANSI_RESET
                                + "   │\n╰───────────╯";
                        break;
                    case "VEGETABLES":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   3  │\n│     " + ANSI_GREEN + "╭─╮"
                                + ANSI_RESET
                                + "   │\n│     " + ANSI_GREEN + "╭─╮" + ANSI_RESET + "   │\n│   " + ANSI_PURPLE + "╭─"
                                + ANSI_GREEN + "╰─╯"
                                + ANSI_RESET + "   │\n│   " + ANSI_PURPLE + "╰─╯" + ANSI_RESET
                                + "     │\n╰───────────╯";
                        break;
                    case "ANIMALS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   3  │\n│     " + ANSI_RED + "╭─╮"
                                + ANSI_RESET
                                + "   │\n│   " + ANSI_BLUE + "╭─╮" + ANSI_RED + "─╯" + ANSI_RESET + "   │\n│   "
                                + ANSI_BLUE + "╰─╯"
                                + ANSI_RESET + "     │\n│   " + ANSI_BLUE + "╰─╯" + ANSI_RESET
                                + "     │\n╰───────────╯";
                        break;
                    case "INSECTS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   3  │\n│   " + ANSI_BLUE + "╭─╮"
                                + ANSI_RESET
                                + "     │\n│   " + ANSI_BLUE + "╰─" + ANSI_PURPLE + "╭─╮" + ANSI_RESET + "   │\n│     "
                                + ANSI_PURPLE
                                + "╰─╯" + ANSI_RESET + "   │\n│     " + ANSI_PURPLE + "╰─╯" + ANSI_RESET
                                + "   │\n╰───────────╯";
                        break;
                    default:
                        break;
                }
                break;
            case "IDOL":
                switch (card.getMustHave()) {
                    case "SHROOM":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│   " + ANSI_RED + "╭───╮"
                                + ANSI_RESET
                                + "   │\n│   " + ANSI_RED + "│ S │" + ANSI_RESET + "   │\n│   " + ANSI_RED + "│S S│"
                                + ANSI_RESET + "   │\n│   " + ANSI_RED + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "VEGETABLES":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│   " + ANSI_GREEN + "╭───╮"
                                + ANSI_RESET
                                + "   │\n│   " + ANSI_GREEN + "│ V │" + ANSI_RESET + "   │\n│   " + ANSI_GREEN + "│V V│"
                                + ANSI_RESET + "   │\n│   " + ANSI_GREEN + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "ANIMALS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│   " + ANSI_BLUE + "╭───╮"
                                + ANSI_RESET
                                + "   │\n│   " + ANSI_BLUE + "│ A │" + ANSI_RESET + "   │\n│   " + ANSI_BLUE + "│A A│"
                                + ANSI_RESET + "   │\n│   " + ANSI_BLUE + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "INSECTS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│   " + ANSI_PURPLE + "╭───╮"
                                + ANSI_RESET
                                + "   │\n│   " + ANSI_PURPLE + "│ I │" + ANSI_RESET + "   │\n│   " + ANSI_PURPLE
                                + "│I I│"
                                + ANSI_RESET + "   │\n│   " + ANSI_PURPLE + "╰───╯" + ANSI_RESET
                                + "   │\n╰───────────╯";
                        break;
                    default:
                        break;
                }
            case "WISEMAN":
                switch (card.getMustHave()) {
                    case "SCROLL":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│           │\n│   "
                                + ANSI_YELLOW
                                + "╭───╮" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "│S S│" + ANSI_RESET
                                + "   │\n│   "
                                + ANSI_YELLOW + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "INK":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│           │\n│   "
                                + ANSI_YELLOW
                                + "╭───╮" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "│I I│" + ANSI_RESET
                                + "   │\n│   "
                                + ANSI_YELLOW + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "FEATHER":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   2  │\n│           │\n│   "
                                + ANSI_YELLOW
                                + "╭───╮" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "│F F│" + ANSI_RESET
                                + "   │\n│   "
                                + ANSI_YELLOW + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "FOLDEDHANDS":
                        cardString = "╭───────────╮\n│  " + card.getIdCard() + "   3  │\n│   " + ANSI_YELLOW + "╭───╮"
                                + ANSI_RESET
                                + "   │\n│   " + ANSI_YELLOW + "│ I │" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW
                                + "│F S│"
                                + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "╰───╯" + ANSI_RESET
                                + "   │\n╰───────────╯";
                        break;
                    default:
                        break;
                }
            default:
                break;
        }
        return cardString;
    }

    private String drawVisibleSymbold(Map<String, Integer> visibleSymbols) {
        String visibleResources = "";
        for (Map.Entry<String, Integer> entry : visibleSymbols.entrySet()) {
            if (!(entry.getKey().equals("EMPTY")) && !(entry.getKey().equals("NULL")))
                visibleResources += entry.getKey() + ": " + visibleSymbols.get(entry.getKey()) + "\n";
        }
        visibleResources = visibleResources.substring(0, visibleResources.length() - 1);
        return visibleResources;
    }

    private String drawActualScores(Map<Player, Integer> actualScores) {
        String actualScoreString = "";
        for (Map.Entry<Player, Integer> entry : actualScores.entrySet()) {
            actualScoreString += entry.getKey().getNickname() + ": " + entry.getValue() + " | ";
        }
        actualScoreString = actualScoreString.substring(0, actualScoreString.length() - 3);
        return actualScoreString;
    }

    @Override
    public void updateStructure(Structure structure) throws IllegalCommandException {
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

            visualStructure = insertCardInMatrix(visualStructure, cardPair.getKey(), x, y, cardPair.getValue());
        }

        // FIXME: multiple structures
        printer.updateStructure(List.of(renderer.printStructure(visualStructure,
                structure.getCoordinateToCard())));

        // FIXME: multiple resources
        printer.updateResources(List.of(drawVisibleSymbold(structure.getvisibleSymbols())));

        printer.clear();
        printer.print();
    }

    @Override
    public void updateHand(Hand hand) throws IllegalCommandException {
        List<String> visualHand = new ArrayList<>();
        List<String> initialCard = new ArrayList<>();
        List<String> chooseBetweenObj = new ArrayList<>();
        String secretObjective = "";
        for (Card card : hand.getCardsHand()) {
            if (card instanceof GoldCard)
                visualHand.add(drawGoldCard(card, true));
            else if (card instanceof ResourceCard)
                visualHand.add(drawResourceCard(card, true));
        }
        initialCard = drawFullInitialCard(hand.getInitCard());
        chooseBetweenObj = List.of(drawObjectiveCard(hand.getChooseBetweenObj().get(0)),
                drawObjectiveCard(hand.getChooseBetweenObj().get(1)));

        if (hand.getSecretObjective() != null)
            secretObjective = drawObjectiveCard(hand.getSecretObjective());

        printer.updateHand(renderer.printHand(visualHand, hand.getCardsHand()));
        printer.updateInitialCard(renderer.printInitialCard(initialCard));
        printer.updateChooseObjectives(chooseBetweenObj);
        printer.updateSecretObjective(secretObjective);

        printer.clear();
        printer.print();
    }

    @Override
    public void updateBoard(Board board) throws IllegalCommandException {
        List<String> cards = new ArrayList<>();
        List<String> commonObjectives = new ArrayList<>();
        String scores = "";
        for (Card card : board.getUncoveredCards()) {
            if (card instanceof GoldCard)
                cards.add(drawGoldCard(card, true));
            else if (card instanceof ResourceCard)
                cards.add(drawResourceCard(card, true));
        }
        for (Card card : board.getCommonObjectives()) {
            commonObjectives.add(drawObjectiveCard(card));
        }
        scores = drawActualScores(board.getActualScores());

        printer.updateBoard(renderer.printBoard(cards, board.getUncoveredCards()));
        printer.updateCommonObjectives(commonObjectives);
        printer.updateScoreBoard(scores);

        printer.clear();
        printer.print();
    }

    @Override
    public void updateDeck(Deck deck) throws IllegalCommandException {
        List<String> cards = new ArrayList<>();
        cards = renderer.printDeck(List.of(deck.getGoldDeck().peek(), deck.getResourceDeck().peek()));

        printer.updateDecks(cards);

        printer.clear();
        printer.print();
    }

    public void printInitial() {
        printer.printInitial();
    }

    public void print() {
        printer.print();
    }

    public void clear() {
        printer.clear();
    }

    public Printer getPrinter() {
        return printer;
    }
}
