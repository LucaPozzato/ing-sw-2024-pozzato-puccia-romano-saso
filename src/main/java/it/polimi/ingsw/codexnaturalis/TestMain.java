package it.polimi.ingsw.codexnaturalis;

import java.util.List;
import java.util.Random;
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
    Deck deckMain;
    Board boardMain;
    Structure structureMain;
    Hand handMain;
    Player luca, edo, ste, nick;

    public TestMain (Game game){
        this.game = game;
    }




    @Override
    public void run() {

        //Simulazione flusso di gioco: SETUP

        luca = new Player("Luca", "", Color.BLUE);
        edo = new Player("Edoardo", "", Color.RED);
        ste = new Player("Stefano", "",  Color.GREEN);
        nick = new Player("Niccolo", "", Color.YELLOW);

        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        Stack<GoldCard> goldDeck = new GoldParser().parse();
        List<InitialCard> initialCards = new InitialParser().parse();
        List<ObjectiveCard> objectiveCards = new ObjectiveParser().parse();
        Random random = new Random();


        Deck deck = new Deck(goldDeck, resourceDeck);
        this.deckMain = deck;
        Board board = new Board();
        this.boardMain = board;
        Hand hand1 = new Hand();
        this.handMain = hand1;
        Structure structure1 = new Structure();
        this.structureMain = structure1;

        deck.shuffleResourceDeck();
        deck.shuffleGoldDeck();
        board.setCommonObjectives(List.of(objectiveCards.get(5),objectiveCards.get(8)));


        hand1.setSecretObjective(objectiveCards.get(1));
        int randomIndexInitialCard = random.nextInt(initialCards.size());
        hand1.setInitCard(initialCards.get(randomIndexInitialCard));


        try {
            hand1.addCard(deck.drawGoldCard());
            hand1.addCard(deck.drawResourceCard());
            hand1.addCard(deck.drawResourceCard());
            structure1.placeCard(null, hand1.getInitCard(), null, true);
        } catch (IllegalCommandException e) {throw new RuntimeException(e);}


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



    }



    public void drawCommand(Card card) throws IllegalCommandException {
        if(card.toString().substring(6).startsWith("G"))
            deckMain.drawGoldCard();
        else if(card.toString().substring(6).startsWith("R"))
            deckMain.drawResourceCard();
        game.updateDeck(deckMain);
        handMain.addCard(card);
        game.updateHand(List.of(handMain));


    }

    public void placeCommand(Card father, Card card, String position, Boolean frontUp) throws IllegalCommandException {
        structureMain.placeCard(father, card, position, frontUp);
        handMain.removeCard(card);
        game.updateStructures(List.of(structureMain));
        System.out.println("\nPunti: \n" + boardMain.getActualPoints(luca));
        game.updateBoard(boardMain);

    }
}