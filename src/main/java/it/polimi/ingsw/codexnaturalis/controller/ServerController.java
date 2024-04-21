package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class ServerController {
    private final Game game;
    private final Chat chat;

    public ServerController() {
        this.game = new Game(0);
        this.chat = new Chat();
    }

    public void move(String nickName, String command) {
        Player player;
        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(nickName)) {
                player = p;
            }
        }

        String[] commandArray = command.split(": ");
        String[] parameters = commandArray[1].split(", ");
    }

    // comando : parametri
    // player place: ID, ID, POS, BOOL
    // player draw: ID

    // verifica del format corretto del comando
    // traduzione stringa player, verifica che nickname esista, verifica che si
    // current
    // traduzione ID, verifica sia carta valida [per id, id e pos]
    // traduzione BOOL

    // case per il comando

    // gestione verifica:
    // 1. gestita direttamente dal model, in ogni metodo, alzare eccezione
    // 2. gestita dal controller che chiami un metodo del model che alzi eccezione

    // TODO: metodo singolo che si prende stringa

    // inizializza game -> già tutto inizializzato
    // espone metodi degli stati
    // drawCard -> game.state.drawCard

    // da State
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        this.game.getState().initialized(nick, color, numPlayers);
    }

    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        this.game.getState().joinGame(nickname, color);
    }

    public void placedCard(Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        this.game.getState().placedCard(father, placeThis, position, frontUp);
    }

    public void drawnCard(String type, String id) throws IllegalCommandException {
        this.game.getState().drawnCard(type, id);
    }

    public void matchEnded() throws IllegalCommandException {
        this.game.getState().matchEnded();
    }

    // da Chat
    // ç
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