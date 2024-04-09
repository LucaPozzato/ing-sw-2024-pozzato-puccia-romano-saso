package it.polimi.ingsw.codexnaturalis.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.Cli;

public class Main {

    // Occorre prevedere di stanziare anche le carte starter nel momento in cui
    // inialize() viene invocata e il giocatore iniziale mi ha detto quanti
    // giocatori ci sono

    public static void main(String[] args) throws Exception {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
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

        Structure structure = new Structure();
        Hand hand = new Hand();
        Board board = new Board();
        Player luca = new Player("Luca");
        board.updateActualScore(luca, 0);
        board.updateActualScore(new Player("Giovanni"), 0);
        board.updateActualScore(new Player("Andrea"), 0);
        board.updateActualScore(new Player("Davide"), 0);
        board.updateVirtualScore(luca, 0);
        board.updateVirtualScore(new Player("Giovanni"), 0);
        board.updateVirtualScore(new Player("Andrea"), 0);
        board.updateVirtualScore(new Player("Davide"), 0);

        List<Card> drawnCards = new ArrayList<>();
        Card drawnCard = null;

        Boolean noErr;
        String position = null;
        Card randomCard = null;
        Boolean upFront = false;

        InitialCard initialCard = (InitialCard) initialCards.get(0);
        List<String> initialDecision = cli.printInitial(initialCard.drawFullCard(),
                List.of(objectiveCards.get(0).drawDetailedVisual(upFront),
                        objectiveCards.get(1).drawDetailedVisual(upFront)));
        hand.setInitCard(initialCard);
        drawnCards.add(initialCard);
        if (initialDecision.get(0).equals("F"))
            structure.placeCard(null, initialCard, "", true);
        else if (initialDecision.get(0).equals("B"))
            structure.placeCard(null, initialCard, "", false);

        if (initialDecision.get(1).equals("1"))
            hand.setSecretObjective(objectiveCards.get(0));
        else
            hand.setSecretObjective(objectiveCards.get(1));

        board.setCommonObjectives(List.of(objectiveCards.get(2), objectiveCards.get(3)));
        cli.updateObjectives(new ArrayList<>(List.of(hand.getSecretObjective().drawDetailedVisual(true),
                board.getCommonObjectives().get(0).drawDetailedVisual(true),
                board.getCommonObjectives().get(1).drawDetailedVisual(true))));

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
        cli.updateScoreBoard(board.getActualScores());
        for (String scores : board.getVirtualScores().split(" \\| ")) {
            if (scores.contains("Luca"))
                cli.updateVirtualPoints(scores);
        }

        List<String> placeDecision;
        List<String> drawDecision;
        List<String> command = new ArrayList<>(List.of("place", "draw"));

        // while (!deck.emptyRes() || !deck.emptyGold()) {
        for (int i = 0; i < 20; i++) {
            noErr = false;
            while (!noErr) {
                try {
                    if (command.get(i % command.size()).equals("place")) {
                        placeDecision = cli.print(command.get(i % command.size()));
                        // BUG: drawnCard is same as before if not chosen from hand
                        for (Card card : hand.getCardsHand()) {
                            if (card.getIdCard().equals(placeDecision.get(0)))
                                drawnCard = card;
                        }
                        for (Card card : structure.getPlacedCards()) {
                            if (card.getIdCard().equals(placeDecision.get(1)))
                                randomCard = card;
                        }
                        position = placeDecision.get(2);
                        upFront = Boolean.valueOf(placeDecision.get(3));
                        structure.placeCard(randomCard, drawnCard, position, upFront);
                    } else if (command.get(i % command.size()).equals("draw")) {
                        drawDecision = cli.print(command.get(i % command.size()));
                        if (drawDecision.get(0).contains("R")) {
                            if (drawDecision.get(0).contains("XX"))
                                drawnCard = deck.drawResourceCard();
                            else {
                                List<Card> uncoveredCards = new ArrayList<>(board.getUncoveredCards());
                                for (Card card : uncoveredCards) {
                                    if (card.getIdCard().equals(drawDecision.get(0))) {
                                        drawnCard = card;
                                        board.removeUncoveredCard(card);
                                        board.addUncoveredCard(deck.drawResourceCard());
                                    }
                                }
                            }
                        } else if (drawDecision.get(0).contains("G")) {
                            if (drawDecision.get(0).contains("XX"))
                                drawnCard = deck.drawGoldCard();
                            else {
                                List<Card> uncoveredCards = new ArrayList<>(board.getUncoveredCards());
                                for (Card card : uncoveredCards) {
                                    if (card.getIdCard().equals(drawDecision.get(0))) {
                                        drawnCard = card;
                                        board.removeUncoveredCard(card);
                                        board.addUncoveredCard(deck.drawGoldCard());
                                    }
                                }
                            }
                        }
                    }
                    noErr = true;

                    if (command.get(i % command.size()).equals("place")) {
                        hand.removeCard(drawnCard);
                        board.updateActualScore(luca, structure.getPointsFromCard(drawnCard, upFront));
                    } else if (command.get(i % command.size()).equals("draw")) {
                        hand.addCard(drawnCard);
                    }

                    cli.updateStructure(structure.draw());
                    cli.updateHand(hand.drawCardsHand());
                    cli.updateBoard(board.drawUncoveredCards());
                    cli.updateDecks(deck.draw());
                    cli.updateScoreBoard(board.getActualScores());
                    cli.updateResources(structure.getVisibleObjects().toString() + "\n"
                            + structure.getVisibleResources().toString());
                    for (String scores : board.getVirtualScores().split(" \\| ")) {
                        if (scores.contains("Luca"))
                            cli.updateVirtualPoints(scores);
                    }
                } catch (Exception e) {
                    cli.updateError("Error: " + e.getMessage());
                    cli.print("Error");
                }
            }
        }
        stdin.close();
    }
}