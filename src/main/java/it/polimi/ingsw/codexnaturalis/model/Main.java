package it.polimi.ingsw.codexnaturalis.model;

import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.*;
import it.polimi.ingsw.codexnaturalis.model.game.parser.*;

public class Main {

    // Occorre prevedere di stanziare anche le carte starter nel momento in cui
    // inialize() viene invocata e il giocatore iniziale mi ha detto quanti
    // giocatori ci sono

    public static void main(String[] args) throws Exception {

        ResourceParser resPars = new ResourceParser();
        Stack<ResourceCard> resourceDeck = resPars.parse();

        GoldParser goldPars = new GoldParser();
        Stack<GoldCard> goldDeck = goldPars.parse();

        Deck deck = new Deck(goldDeck, resourceDeck);
        deck.printGoldDeck();
        deck.printResourceDeck();

        ObjectiveParser secPars = new ObjectiveParser();
        for (ObjectiveCard card : secPars.parse()) {
            card.print();
        }

        System.out.println("\n");

        InitialParser initPars = new InitialParser();
        for (InitialCard card : initPars.parse()) {
            card.print();
        }
    }
}
