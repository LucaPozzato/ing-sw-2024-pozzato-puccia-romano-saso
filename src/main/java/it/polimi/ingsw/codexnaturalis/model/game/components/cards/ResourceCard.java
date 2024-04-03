package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class ResourceCard extends Card {
    private String symbol;
    private int points;
    private List<String> frontCorners;
    private List<String> backCorners;

    public ResourceCard(String idCard, String symbol, int points, List<String> frontCorners) {
        super(idCard);
        this.symbol = symbol;
        this.points = points;
        this.frontCorners = frontCorners;
        this.backCorners = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            this.backCorners.add("EMPTY");
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public List<String> getFrontCorners() {
        return frontCorners;
    }

    @Override
    public List<String> getBackCorners() {
        return backCorners;
    }

    @Override
    public char[][] drawVisual(char[][] drawingBoard, int x, int y, Boolean side) {
        List<String> corners;
        List<String> card = new ArrayList<>();

        String topCornersLine;
        String bottomCornersLine;

        if (side)
            corners = frontCorners;
        else
            corners = backCorners;

        // top corners line
        if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                || corners.get(0).equals("FEATHER"))
            topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│";
        else
            topCornersLine = "│" + corners.get(0).charAt(0) + "│";

        if (points > 0) {
            topCornersLine += " " + Integer.toString(points).charAt(0) + " ";
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
            bottomCornersLine += " " + symbol.charAt(0) + " ";
        } else {
            bottomCornersLine += "   ";
        }

        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
        else
            bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";

        card.add("╭─┬───┬─╮");
        card.add(topCornersLine);
        card.add("├─┤" + idCard + "├─┤");
        card.add(bottomCornersLine);
        card.add("╰─┴───┴─╯");

        for (int i = -2; i <= 2; i++) {
            System.arraycopy(card.get(2 + i).toCharArray(), 0, drawingBoard[y + i], x - 4, 9);
        }

        return drawingBoard;
    }

    @Override
    public String drawDetailedVisual(Boolean side)
            throws IllegalCommandException {
        List<String> corners;
        String card;

        String topLine = "╭─┬───────┬─╮";
        String topCornersLine;
        String topMiddleLine;
        String centerLine;
        String bottomMiddleLine;
        String bottomCornersLine;
        String bottomLine;

        if (side)
            corners = frontCorners;
        else
            corners = backCorners;

        // top corners line
        if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL")
                || corners.get(0).equals("FEATHER"))
            topCornersLine = "│" + Character.toLowerCase(corners.get(0).charAt(0)) + "│ ";
        else
            topCornersLine = "│" + corners.get(0).charAt(0) + "│ ";

        if (points == 0)
            topCornersLine += "     ";
        else
            topCornersLine += "  " + Integer.toString(points).charAt(0) + "  ";

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
            centerLine = "│    " + idCard + "    │";
        else
            centerLine = "│    │" + symbol.charAt(0) + "│    │";

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
            bottomCornersLine += " " + idCard + " ";

        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            bottomCornersLine += " │" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
        else
            bottomCornersLine += " │" + corners.get(3).charAt(0) + "│";

        // bottom line
        bottomLine = "╰─┴───────┴─╯";

        card = topLine + "\n" + topCornersLine + "\n" + topMiddleLine + "\n" + centerLine + "\n" + bottomMiddleLine
                + "\n" + bottomCornersLine + "\n" + bottomLine;

        return card;
    }

    @Override
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tsymbol: " + symbol + "\n\tpoints: " + points + "\n\tcorners: " + frontCorners);
    }
}
