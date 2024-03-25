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
        Stack<ResourceCard> resourceDeck = resPars.Parse();

        GoldParser goldPars = new GoldParser();
        Stack<GoldCard> goldDeck = goldPars.Parse();

        Deck deck = new Deck(goldDeck, resourceDeck);
        deck.printGoldDeck();
        deck.printResourceDeck();

        ObjectiveParser secPars = new ObjectiveParser();
        secPars.Parse();
        InitialParser initPars = new InitialParser();
        initPars.Parse();
    }
}
