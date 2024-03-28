package it.polimi.ingsw.codexnaturalis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.*;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
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
        // deck.printGoldDeck();
        // deck.printResourceDeck();

        // ObjectiveParser secPars = new ObjectiveParser();
        // for (ObjectiveCard card : secPars.parse()) {
        // card.print();
        // }

        // System.out.println("\n");

        InitialParser initPars = new InitialParser();
        // for (InitialCard card : initPars.parse()) {
        // card.print();
        // }

        deck.shuffleGoldDeck();
        deck.shuffleResourceDeck();
        Card drawnCard = initPars.parse().get(0);

        List<String> positions = List.of("TL", "TR", "BL", "BR");
        Random randGenerator = new Random();

        Structure structure = new Structure();
        structure.placeCard(null, drawnCard, positions.get(randGenerator.nextInt(4)), true);
        // System.out.println("Placed initial card: " + drawnCard.getIdCard());

        List<Card> drawnCards = new ArrayList<>();

        Boolean noErr;
        String position = null;
        Card randomCard = null;

        while (!deck.emptyRes()) {
            noErr = false;
            drawnCards.add(drawnCard);
            drawnCard = deck.drawResourceCard();
            while (!noErr) {
                try {
                    position = positions.get(randGenerator.nextInt(4));
                    randomCard = drawnCards.get(randGenerator.nextInt(drawnCards.size()));
                    structure.placeCard(randomCard, drawnCard, position, true);
                    noErr = true;
                } catch (Exception e) {
                    // System.out.println("Error: " + e.getMessage());
                }
            }
        }
        structure.print();
    }
}