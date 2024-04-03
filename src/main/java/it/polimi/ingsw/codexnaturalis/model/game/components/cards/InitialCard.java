package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.ArrayList;
import java.util.List;

public class InitialCard extends Card {
    private List<String> frontCorners;
    private List<String> frontCenterResources;
    private List<String> backCorners;

    public InitialCard(String idCard, List<String> frontCornerRes, List<String> frontCenterRes,
            List<String> backCornerRes) {
        super(idCard);
        this.frontCorners = frontCornerRes;
        this.frontCenterResources = frontCenterRes;
        this.backCorners = backCornerRes;
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
    public List<String> getFrontCenterResources() {
        return frontCenterResources;
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

        if (frontCenterResources.size() == 2 && side)
            topCornersLine += " " + frontCenterResources.get(1).charAt(0) + " ";
        else if (frontCenterResources.size() == 3 && side)
            topCornersLine += frontCenterResources.get(1).charAt(0) + " " + frontCenterResources.get(2).charAt(0);
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
            bottomCornersLine += " " + frontCenterResources.get(0).charAt(0) + " ";
        } else {
            bottomCornersLine += "   ";
        }

        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            bottomCornersLine += "│" + Character.toLowerCase(corners.get(3).charAt(0)) + "│";
        else
            bottomCornersLine += "│" + corners.get(3).charAt(0) + "│";

        card.add("╭─┬───┬─╮");
        card.add(topCornersLine);
        card.add("├─┤" + "І" + idCard.charAt(1) + idCard.charAt(2) + "├─┤");
        card.add(bottomCornersLine);
        card.add("╰─┴───┴─╯");

        for (int i = -2; i <= 2; i++) {
            System.arraycopy(card.get(2 + i).toCharArray(), 0, drawingBoard[y + i], x - 4, 9);
        }

        return drawingBoard;
    }

    public String toString() {
        return "InitialCard{" +
                "idCard='" + idCard + '\'' +
                ", frontCornerRes=" + frontCorners +
                ", frontCenterRes=" + frontCenterResources +
                ", backCornerRes=" + backCorners +
                '}';
    }

    @Override
    public void print() {
        System.out.println("id: " + idCard + "\n\tfrontCornerRes: " + frontCorners + "\n\tfrontCentreRes: "
                + frontCenterResources + "\n\tbackCornerRes: " + backCorners);
    }
}