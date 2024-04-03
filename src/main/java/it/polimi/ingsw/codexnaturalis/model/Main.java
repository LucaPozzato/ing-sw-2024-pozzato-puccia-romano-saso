package it.polimi.ingsw.codexnaturalis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.view.Cli;

public class Main {

    // Occorre prevedere di stanziare anche le carte starter nel momento in cui
    // inialize() viene invocata e il giocatore iniziale mi ha detto quanti
    // giocatori ci sono

    public static void main(String[] args) throws Exception {
        Scanner stdin = new Scanner(System.in);
        Cli cli = new Cli(stdin);

        ResourceParser resPars = new ResourceParser();
        Stack<ResourceCard> resourceDeck = resPars.parse();

        GoldParser goldPars = new GoldParser();
        Stack<GoldCard> goldDeck = goldPars.parse();

        Deck deck = new Deck(goldDeck, resourceDeck);

        InitialParser initPars = new InitialParser();
        ObjectiveParser objPars = new ObjectiveParser();

        deck.shuffleGoldDeck();
        deck.shuffleResourceDeck();
        List<InitialCard> initialCards = initPars.parse();
        Collections.shuffle(initialCards);
        List<ObjectiveCard> objectiveCards = objPars.parse();
        Collections.shuffle(objectiveCards);

        List<String> positions = List.of("TL", "TR", "BL", "BR");
        Random randGenerator = new Random();

        Structure structure = new Structure();
        Hand hand = new Hand();
        Board board = new Board();

        List<Card> drawnCards = new ArrayList<>();
        Card drawnCard = null;

        Boolean noErr;
        String position = null;
        Card randomCard = null;
        Boolean upFront = false;

        Card initialCard = initialCards.get(0);
        drawnCards.add(initialCard);
        hand.setSecretObjective(objectiveCards.get(0));
        board.setCommonObjectives(List.of(objectiveCards.get(1), objectiveCards.get(2)));
        structure.placeCard(null, initialCard, "", randGenerator.nextBoolean());
        cli.updateObjectives(new ArrayList<>(List.of(hand.getSecretObjective().drawDetailedVisual(true),
                board.getCommonObjectives().get(0).drawDetailedVisual(true),
                board.getCommonObjectives().get(1).drawDetailedVisual(true))));
        System.out.println(
                "\n\nplaced Initial card " + initialCard.getIdCard() + " front side up -> " + upFront);
        structure.draw();

        board.addUncoveredCard(deck.drawResourceCard());
        board.addUncoveredCard(deck.drawResourceCard());
        board.addUncoveredCard(deck.drawGoldCard());
        board.addUncoveredCard(deck.drawGoldCard());
        hand.addCard(deck.drawResourceCard());
        hand.addCard(deck.drawResourceCard());
        hand.addCard(deck.drawGoldCard());

        cli.updateStructure(structure.draw());
        cli.updateHand(hand.drawCardsHand());
        cli.updateBoard(board.drawUncoveredCards());
        cli.updateDecks(deck.draw());
        cli.updateResources(structure.getVisibleObjects().toString() + "\n"
                + structure.getVisibleResources().toString());
        cli.print();

        // while (!deck.emptyRes() || !deck.emptyGold()) {
        for (int i = 0; i < 20; i++) {
            noErr = false;
            while (!noErr) {
                try {
                    position = positions.get(randGenerator.nextInt(4));
                    upFront = randGenerator.nextBoolean();
                    drawnCard = hand.getCardsHand().get(randGenerator.nextInt(3));
                    randomCard = drawnCards.get(randGenerator.nextInt(drawnCards.size()));
                    structure.placeCard(randomCard, drawnCard, position, upFront);
                    noErr = true;

                    // if there are no errors continues here

                    drawnCards.add(drawnCard);
                    hand.removeCard(drawnCard);
                    drawnCard = board.getUncoveredCards().get(randGenerator.nextInt(4));
                    board.removeUncoveredCard(drawnCard);
                    hand.addCard(drawnCard);
                    if (drawnCard instanceof ResourceCard)
                        board.addUncoveredCard(deck.drawResourceCard());
                    else
                        board.addUncoveredCard(deck.drawGoldCard());
                    cli.updateStructure(structure.draw());
                    cli.updateHand(hand.drawCardsHand());
                    cli.updateBoard(board.drawUncoveredCards());
                    cli.updateDecks(deck.draw());
                    cli.updateResources(structure.getVisibleObjects().toString() + "\n"
                            + structure.getVisibleResources().toString());
                    cli.print();
                } catch (Exception e) {
                    if (randomCard != null)
                        cli.updateError(
                                "Error: " + e.getMessage() + " -> " + "tried to place " +
                                        drawnCard.getIdCard() + " on "
                                        + randomCard.getIdCard() + " in position " + position);
                    else
                        cli.updateError(
                                "Error: " + e.getMessage() + " -> " + "tried to place " +
                                        drawnCard.getIdCard()
                                        + " front side up -> " + upFront);
                    cli.print();
                }
            }
        }
        stdin.close();
    }
}