package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.state.State;
import it.polimi.ingsw.codexnaturalis.model.chat.Chat;


public class ServerController {
    // model
    private final Game game;
    private final Chat chat; //qualche dubbio sul fatto che la chat sia unica per client e che non vada gestita qui nel caso cancellare tutti i ç


    public ServerController() {
        this.game = new Game(0);
        this.chat = new Chat();//ç
    }


    // inizializza game -> già tutto inizializzato
    // espone metodi degli stati
    // drawCard -> game.state.drawCard

    //ho inserito alcuni metodi che potrebbe essere necessario esporre
    //così da poter fare prove e cercare di capire dove implementarli nei due network
    //non ho ragionato sulla logica e verifica degli input

    //da State
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        this.game.getState().initialized(nick, color, numPlayers);
    }

    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        this.game.getState().joinGame(nickname, color);
    }

    public void placedCard(Card father, Card placeThis, String position, Boolean frontUp) throws IllegalCommandException {
        this.game.getState().placedCard(father, placeThis, position, frontUp);
    }

    public void drawnCard(String type, String id) throws IllegalCommandException {
        this.game.getState().drawnCard(type, id);
    }

    public void matchEnded() throws IllegalCommandException {
        this.game.getState().matchEnded();
    }

    //da Chat
    //ç
    public void addMessage(ChatMessage message) {
        this.chat.addMessage(message);
    }

    public void removeMessage(ChatMessage message) {
        this.chat.removeMessage(message);
    }

    public String draw(String player) {
        return this.chat.draw(player);
    }
}