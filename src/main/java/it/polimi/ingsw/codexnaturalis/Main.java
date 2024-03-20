package it.polimi.ingsw.codexnaturalis;

import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;

public class Main {
    public static void main(String[] args) {
        GoldParser goldParser = new GoldParser();
        try {
            goldParser.Parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
