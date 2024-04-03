package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class ObjectiveCard extends Card {
    private int points;
    private String shape;
    private String mustHave;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[38;5;196m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    public ObjectiveCard(String idCard, int points, String shape, String mustHave) {
        super(idCard);
        this.points = points;
        this.shape = shape;
        this.mustHave = mustHave;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public String getShape() {
        return shape;
    }

    @Override
    public String getMustHave() {
        return mustHave;
    }

    @Override
    public String toString() {
        return "ObjectiveCard{" +
                "idCard='" + idCard + '\'' +
                ", points=" + points +
                ", shape='" + shape + '\'' +
                ", mustHave='" + mustHave + '\'' +
                '}';
    }

    @Override
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tpoints: " + points + "\n\tshape: " + shape + "\n\tmustHave: " + mustHave);
    }

    @Override
    public String getPointsType() throws IllegalCommandException {
        throw new IllegalCommandException();
    }

    @Override
    public String drawDetailedVisual(Boolean side) {
        String cardString = "";
        switch (shape) {
            case "STAIRS":
                switch (mustHave) {
                    case "RED":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│  " + ANSI_RED + "╭─╮" + ANSI_RESET
                                + "      │\n│  " + ANSI_RED + "╰─╭─╮" + ANSI_RESET + "    │\n│    " + ANSI_RED + "╰─╭─╮"
                                + ANSI_RESET + "  │\n│      " + ANSI_RED + "╰─╯" + ANSI_RESET + "  │\n╰───────────╯";
                        break;
                    case "BLUE":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│  " + ANSI_BLUE + "╭─╮" + ANSI_RESET
                                + "      │\n│  " + ANSI_BLUE + "╰─╭─╮" + ANSI_RESET + "    │\n│    " + ANSI_BLUE
                                + "╰─╭─╮"
                                + ANSI_RESET + "  │\n│      " + ANSI_BLUE + "╰─╯" + ANSI_RESET + "  │\n╰───────────╯";
                        break;
                    case "GREEN":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│      " + ANSI_GREEN + "╭─╮"
                                + ANSI_RESET
                                + "  │\n│    " + ANSI_GREEN + "╭─╮─╯" + ANSI_RESET + "  │\n│  " + ANSI_GREEN + "╭─╮─╯"
                                + ANSI_RESET + "    │\n│  " + ANSI_GREEN + "╰─╯" + ANSI_RESET
                                + "      │\n╰───────────╯";
                        break;
                    case "PURPLE":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│      " + ANSI_PURPLE + "╭─╮"
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
                switch (mustHave) {
                    case "RED":
                        cardString = "╭───────────╮\n│  " + idCard + "   3  │\n│   " + ANSI_RED + "╭─╮" + ANSI_RESET
                                + "     │\n│   " + ANSI_RED + "╭─╮" + ANSI_RESET + "     │\n│   " + ANSI_RED + "╰─╯"
                                + ANSI_GREEN + "─╮" + ANSI_RESET + "   │\n│     " + ANSI_GREEN + "╰─╯" + ANSI_RESET
                                + "   │\n╰───────────╯";
                        break;
                    case "GREEN":
                        cardString = "╭───────────╮\n│  " + idCard + "   3  │\n│     " + ANSI_GREEN + "╭─╮" + ANSI_RESET
                                + "   │\n│     " + ANSI_GREEN + "╭─╮" + ANSI_RESET + "   │\n│   " + ANSI_PURPLE + "╭─"
                                + ANSI_GREEN + "╰─╯"
                                + ANSI_RESET + "   │\n│   " + ANSI_PURPLE + "╰─╯" + ANSI_RESET
                                + "     │\n╰───────────╯";
                        break;
                    case "BLUE":
                        cardString = "╭───────────╮\n│  " + idCard + "   3  │\n│     " + ANSI_RED + "╭─╮" + ANSI_RESET
                                + "   │\n│   " + ANSI_BLUE + "╭─╮" + ANSI_RED + "─╯" + ANSI_RESET + "   │\n│   "
                                + ANSI_BLUE + "╰─╯"
                                + ANSI_RESET + "     │\n│   " + ANSI_BLUE + "╰─╯" + ANSI_RESET
                                + "     │\n╰───────────╯";
                        break;
                    case "PURPLE":
                        cardString = "╭───────────╮\n│  " + idCard + "   3  │\n│   " + ANSI_BLUE + "╭─╮" + ANSI_RESET
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
                switch (mustHave) {
                    case "SHROOM":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│   " + ANSI_RED + "╭───╮" + ANSI_RESET
                                + "   │\n│   " + ANSI_RED + "│ S │" + ANSI_RESET + "   │\n│   " + ANSI_RED + "│S S│"
                                + ANSI_RESET + "   │\n│   " + ANSI_RED + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "VEGETABLES":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│   " + ANSI_GREEN + "╭───╮" + ANSI_RESET
                                + "   │\n│   " + ANSI_GREEN + "│ V │" + ANSI_RESET + "   │\n│   " + ANSI_GREEN + "│V V│"
                                + ANSI_RESET + "   │\n│   " + ANSI_GREEN + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "ANIMALS":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│   " + ANSI_BLUE + "╭───╮" + ANSI_RESET
                                + "   │\n│   " + ANSI_BLUE + "│ A │" + ANSI_RESET + "   │\n│   " + ANSI_BLUE + "│A A│"
                                + ANSI_RESET + "   │\n│   " + ANSI_BLUE + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "INSECTS":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│   " + ANSI_PURPLE + "╭───╮"
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
                switch (mustHave) {
                    case "SCROLL":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│           │\n│   " + ANSI_YELLOW
                                + "╭───╮" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "│S S│" + ANSI_RESET
                                + "   │\n│   "
                                + ANSI_YELLOW + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "INK":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│           │\n│   " + ANSI_YELLOW
                                + "╭───╮" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "│I I│" + ANSI_RESET
                                + "   │\n│   "
                                + ANSI_YELLOW + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "FEATHER":
                        cardString = "╭───────────╮\n│  " + idCard + "   2  │\n│           │\n│   " + ANSI_YELLOW
                                + "╭───╮" + ANSI_RESET + "   │\n│   " + ANSI_YELLOW + "│F F│" + ANSI_RESET
                                + "   │\n│   "
                                + ANSI_YELLOW + "╰───╯" + ANSI_RESET + "   │\n╰───────────╯";
                        break;
                    case "FOLDEDHANDS":
                        cardString = "╭───────────╮\n│  " + idCard + "   3  │\n│   " + ANSI_YELLOW + "╭───╮"
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
}
