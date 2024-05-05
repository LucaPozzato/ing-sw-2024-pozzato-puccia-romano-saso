package it.polimi.ingsw.codexnaturalis;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
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


        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        Stack<GoldCard> goldDeck = new GoldParser().parse();
        List<InitialCard> initialCards = new InitialParser().parse();
        List<ObjectiveCard> objectiveCards = new ObjectiveParser().parse();


        Deck deck = new Deck(goldDeck, resourceDeck);
        deck.shuffleGoldDeck();
        deck.shuffleResourceDeck();

        Hand hand = new Hand();
        hand.setInitCard(initialCards.get(0));
        try {
            hand.addCard(deck.drawResourceCard());
            hand.addCard(deck.drawResourceCard());
            hand.addCard(deck.drawGoldCard());
        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }

        Hand secondHand = new Hand();
        try {
            secondHand.addCard(deck.drawResourceCard());
            secondHand.addCard(deck.drawResourceCard());
            secondHand.addCard(deck.drawGoldCard());
        } catch (IllegalCommandException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() ->{
            game.updatePlayers(List.of(luca,nick));

        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() ->{
            game.updatePlayers(List.of(nick,filippo,luca,marco));
            //game.updateDeck(deck);
        });


        Platform.runLater(() ->{
            game.showAlert("Hand update, click 'ok' to continue");
            game.updateHand(List.of(hand,secondHand));

        });

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Platform.runLater(() ->{
//            game.showAlert("Deck update, click 'ok' to continue");
//            game.updateDeck(deck);
//
//        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() ->{
            game.showAlert("Current player update, click 'ok' to continue");
            game.updateCurrentPlayer(luca);

        });

    }

    public static void main(String[] args) throws Exception {

//
//        Game app = new Game();
//        Player luca = new Player("Luca");
//        Player nick = new Player("Nick");
//        app = app.getInstance();
//        app.run();
//
//        Game finalApp = app;
//        new Thread(() -> {
//            try{
//                Thread.sleep(2000);
//                Platform.runLater(finalApp::ciao);
//            }
//            catch (InterruptedException e){};
//        }).start();
//        app.ciao();

        //app.updatePlayers(List.of(luca,nick));








        //GameTui gameTui = new GameTui();

//        Hand hand = new Hand();
//        Hand secondHand = new Hand();
//        Board board = new Board();
//        Structure structure = new Structure();
//        Structure secondStructure = new Structure();
//        Deck deck = new Deck(goldDeck, resourceDeck);
//        Player luca = new Player("Luca");
//        Player nick = new Player("Nick");
//        gameTui.updatePlayers(List.of(luca, nick));
//        gameTui.updateMyPlayer(luca);
//        gameTui.updateCurrentPlayer(luca);

//
//
//        hand.setChooseBetweenObj(List.of(objectiveCards.get(0), objectiveCards.get(1)));
//        hand.setInitCard(initialCards.get(0));
//        hand.addCard(resourceDeck.pop());
//        hand.addCard(resourceDeck.pop());
//        hand.addCard(goldDeck.pop());
//        secondHand.addCard(resourceDeck.pop());
//        secondHand.addCard(resourceDeck.pop());
//        secondHand.addCard(goldDeck.pop());
//        secondStructure.placeCard(null, initialCards.get(1), null, true);
//        board.addUncoveredCard(resourceDeck.pop());
//        board.addUncoveredCard(resourceDeck.pop());
//        board.addUncoveredCard(goldDeck.pop());
//        board.addUncoveredCard(goldDeck.pop());
//        board.setCommonObjectives(List.of(objectiveCards.get(2), objectiveCards.get(3)));
//        board.updateActualScore(luca, 0);
//        board.updateActualScore(nick, 0);
//        gameTui.updateStructure(List.of(structure, secondStructure));
//        gameTui.updateDeck(deck);
//        gameTui.updateBoard(board);
//        gameTui.updateHand(List.of(hand, secondHand));
//        gameTui.setInitialStage(true);
//
//        ReadThread readThread = gameMain.new ReadThread(controller, gameTui, hand, structure, deck, board,
//                secondStructure,
//                secondHand);
//        readThread.start();




    }
}