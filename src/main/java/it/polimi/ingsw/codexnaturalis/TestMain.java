package it.polimi.ingsw.codexnaturalis;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.*;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.parser.GoldParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.gui.controllers.Game;
import javafx.application.Platform;


public class TestMain extends Thread{
    Game game;
    Player luca = new Player("Luca");
    Player nick = new Player("Nick");
    Player marco = new Player("Marco");
    Player filippo = new Player("Filippo");

    public TestMain (Game game){
        this.game = game;
    }




    @Override
    public void run() {

        Player luca = new Player("Luca", Color.BLUE);
        Player edo = new Player("Edoardo", Color.RED);
        Player ste = new Player("Stefano", Color.GREEN);
        Player nick = new Player("Niccolo",Color.YELLOW);

        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        Stack<GoldCard> goldDeck = new GoldParser().parse();
        List<InitialCard> initialCards = new InitialParser().parse();
        List<ObjectiveCard> objectiveCards = new ObjectiveParser().parse();


        //Simulazione flusso di gioco:
        Deck deck = new Deck(goldDeck, resourceDeck);
        Board board = new Board();
        Hand hand1 = new Hand();
        Structure structure1 = new Structure();

        deck.shuffleResourceDeck();
        deck.shuffleGoldDeck();
        board.setCommonObjectives(List.of(objectiveCards.get(5),objectiveCards.get(8)));

        hand1.setSecretObjective(objectiveCards.get(0));
        hand1.setInitCard(initialCards.get(0));
        try {
            hand1.addCard(deck.drawGoldCard());
            hand1.addCard(deck.drawResourceCard());
            hand1.addCard(deck.drawResourceCard());
        } catch (IllegalCommandException e) {throw new RuntimeException(e);}

        try {
            structure1.placeCard(null,hand1.getInitCard(),null,true);
            Card card1 = deck.drawResourceCard();
            structure1.placeCard(hand1.getInitCard(),card1,"TR",false);
            structure1.placeCard(hand1.getInitCard(),deck.drawResourceCard(),"TL",false);
            structure1.placeCard(hand1.getInitCard(),deck.drawResourceCard(),"BL",false);
            Card card2 = deck.drawResourceCard();
            structure1.placeCard(hand1.getInitCard(),card2,"BR",false);
            Card card3 = deck.drawResourceCard();
            structure1.placeCard(card2,card3,"BR",false);
            Card card4 = deck.drawResourceCard();
            structure1.placeCard(card3,card4,"BR",false);
            Card card5 = deck.drawResourceCard();
            structure1.placeCard(card4,card5,"BR",false);
            Card card6 = deck.drawResourceCard();
            structure1.placeCard(card5,card6,"BR",false);
            Card card7 = deck.drawResourceCard();
            structure1.placeCard(card6,card7,"BR",false);
            //structure1.placeCard(null,hand1.getInitCard(),null,true);

        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }



        //Aggiornamenti
        Platform.runLater(() ->{
            game.setInitialCard(hand1.getInitCard());
            game.updatePlayers(List.of(luca,edo,ste,nick));
            game.updateDeck(deck);
            game.updateHand(List.of(hand1));
            game.updateCurrentPlayer(luca);
            game.updateMyPlayer(luca);
            game.updateBoard(board);
            game.updateStructures(List.of(structure1));
        });


//        Deck deck = new Deck(goldDeck, resourceDeck);
//        deck.shuffleGoldDeck();
//        deck.shuffleResourceDeck();
//
//        //Prima hand
//        Hand hand = new Hand();
//        Hand secondHand = new Hand();
//        hand.setInitCard(initialCards.get(0));
//        hand.setSecretObjective(objectiveCards.get(0));
//
//        Board board = new Board();
//        board.setCommonObjectives(List.of(objectiveCards.get(5),objectiveCards.get(8)));
//
//        //Struttura
//        Structure structure1 = new Structure();
//        try { structure1.placeCard(null, hand.getInitCard(), null, true);
//        } catch (IllegalCommandException e) {throw new RuntimeException(e);}
//
////        try {
//            hand.addCard(deck.drawResourceCard());
//            hand.addCard(deck.drawResourceCard());
//            hand.addCard(deck.drawGoldCard());
//            secondHand.addCard(deck.drawResourceCard());
//            secondHand.addCard(deck.drawResourceCard());
//            secondHand.addCard(deck.drawGoldCard());
//        } catch (IllegalCommandException e) {
//            throw new RuntimeException(e);
//        }


//        Platform.runLater(() ->{
//            game.updatePlayers(List.of(luca,nick));
//
//        });

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Platform.runLater(() ->{
//            game.setInitialCard(hand.getInitCard());
//            game.updatePlayers(List.of(nick,filippo,luca,marco));
//            //game.updateDeck(deck);
//        });
//
//
//        Platform.runLater(() ->{
//            //game.showAlert("Hand update, click 'ok' to continue");
//            game.updateHand(List.of(hand,secondHand));
//            game.updateBoard(board);
//
//        });

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        Platform.runLater(() ->{
//            //game.showAlert("Deck update, click 'ok' to continue");
//            game.updateDeck(deck);
//
//        });
//
//
//        Platform.runLater(() ->{
//            //game.showAlert("Current player update, click 'ok' to continue");
//            game.updateCurrentPlayer(luca);
//
//        });

    }



    public void drawCommand(){

    }

    public void placeCommand(){

    }
}