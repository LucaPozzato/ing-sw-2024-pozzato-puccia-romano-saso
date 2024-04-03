package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldCard extends Card {
    private String symbol;
    private int points;
    private String pointsType;
    private List<String> frontCorners;
    private List<String> backCorners;
    private Map<String, Integer> requirements;

    public GoldCard(String idCard, String symbol, int points, String pointsType, List<String> frontCorners,
            Map<String, Integer> requirements) {
        super(idCard);
        this.symbol = symbol;
        this.points = points;
        this.pointsType = pointsType;
        this.frontCorners = frontCorners;
        this.backCorners = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            this.backCorners.add("EMPTY");
        }
        this.requirements = requirements;
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
    public String getPointsType() {
        return pointsType;
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
    public Map<String, Integer> getRequirements() {
        return requirements;
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

        if (!pointsType.equals("NULL")) {
            switch (pointsType) {
                case "INK":
                    topCornersLine += Integer.toString(points).charAt(0) + " І";
                    break;
                case "SCROLL":
                    topCornersLine += Integer.toString(points).charAt(0) + " Ѕ";
                    break;
                case "FEATHER":
                    topCornersLine += Integer.toString(points).charAt(0) + " Ϝ";
                    break;
                case "ANGLE":
                    topCornersLine += Integer.toString(points).charAt(0) + " А";
                    break;
                default:
                    break;
            }
        } else
            topCornersLine += " " + Integer.toString(points).charAt(0) + " ";

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
    public String drawDetailedVisual(Boolean side) {
        List<String> corners;
        String card;
        String cardRequirements = "";

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

        if (!pointsType.equals("NULL")) {
            switch (pointsType) {
                case "INK":
                    topCornersLine += Integer.toString(points).charAt(0) + "   І";
                    break;
                case "SCROLL":
                    topCornersLine += Integer.toString(points).charAt(0) + "   Ѕ";
                    break;
                case "FEATHER":
                    topCornersLine += Integer.toString(points).charAt(0) + "   Ϝ";
                    break;
                case "ANGLE":
                    topCornersLine += Integer.toString(points).charAt(0) + "   А";
                    break;
                default:
                    break;
            }
        } else
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
            bottomMiddleLine = "├─╮╭─────╮╭─┤";
        else
            bottomMiddleLine = "├─╮  ╰─╯  ╭─┤";

        // bottom corners line
        if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
            bottomCornersLine = "│" + Character.toLowerCase(corners.get(2).charAt(0)) + "│";
        else
            bottomCornersLine = "│" + corners.get(2).charAt(0) + "│";

        if (side) {
            for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
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
            bottomCornersLine += "  " + idCard + "  ";

        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
        else
            bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";

        // bottom line
        if (side)
            bottomLine = "╰─┴┴─────┴┴─╯";
        else
            bottomLine = "╰─┴───────┴─╯";

        card = topLine + "\n" + topCornersLine + "\n" + topMiddleLine + "\n" + centerLine + "\n" + bottomMiddleLine
                + "\n" + bottomCornersLine + "\n" + bottomLine;

        return card;
    }

    @Override
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tsymbol: " + symbol + "\n\tpoints: " + points + "\n\tpointsType: " + pointsType
                        + "\n\tcorners: " + frontCorners + "\n\trequirements: ");
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            System.out.println("\t\t" + entry.getKey() + ": " + entry.getValue());
        }
    }

}