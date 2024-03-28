package it.polimi.ingsw.codexnaturalis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;

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
        List<InitialCard> initialCards = initPars.parse();

        List<String> positions = List.of("TL", "TR", "BL", "BR");
        Random randGenerator = new Random();

        Structure structure = new Structure();
        // System.out.println("Placed initial card: " + drawnCard.getIdCard());

        List<Card> drawnCards = new ArrayList<>();

        Boolean noErr;
        String position = null;
        Card randomCard = null;
        Boolean upFront = false;
        Card drawnCard = initialCards.get(randGenerator.nextInt(initialCards.size()));

        // while (!deck.emptyRes() || !deck.emptyGold()) {
        for (int i = 0; i < 20; i++) {
            noErr = false;
            while (!noErr) {
                try {
                    position = positions.get(randGenerator.nextInt(4));
                    upFront = randGenerator.nextBoolean();
                    if (drawnCards.size() > 0)
                        randomCard = drawnCards.get(randGenerator.nextInt(drawnCards.size()));
                    else
                        randomCard = null;
                    structure.placeCard(randomCard, drawnCard, position, upFront);
                    noErr = true;
                    drawnCards.add(drawnCard);
                    if (drawnCard instanceof InitialCard) {
                        System.out.println(
                                "\n\nplaced Initial card " + drawnCard.getIdCard() + "front side up -> " + upFront);
                    } else {
                        System.out.println(
                                "\n\nplaced " + drawnCard.getIdCard() + " on "
                                        + randomCard.getIdCard() + " in position " + position + ", front side up -> "
                                        + upFront);
                    }
                    structure.printVisual();
                    System.out.println("Resources:");
                    for (Map.Entry<String, Integer> entry : structure.getVisibleObjects().entrySet()) {
                        System.out.println("\t" + entry.getKey() + " " + entry.getValue());
                    }
                    for (Map.Entry<String, Integer> entry : structure.getVisibleResources().entrySet()) {
                        System.out.println("\t" + entry.getKey() + " " + entry.getValue());
                    }
                    System.out.println("Timestamps:");
                    for (Card card : structure.getTimestamp()) {
                        System.out.print(card.getIdCard() + " ");
                    }
                    System.out.print("\n\n");
                } catch (Exception e) {
                    System.out.println(
                            "Error: " + e.getMessage() + " -> " + "tried to place " + drawnCard.getIdCard() + " on "
                                    + randomCard.getIdCard() + " in position " + position);
                    if (drawnCard instanceof GoldCard && !deck.emptyRes())
                        drawnCard = deck.drawResourceCard();
                }
            }
            if (randGenerator.nextBoolean() && !deck.emptyRes())
                drawnCard = deck.drawResourceCard();
            else if (!deck.emptyGold())
                drawnCard = deck.drawGoldCard();
        }
        System.out.println("Skeleton:");
        structure.printSkeleton();
    }
}