package it.polimi.ingsw.codexnaturalis.model.game.player;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;

public class Player {
    // TODO: UML costruttore vuoto
    // aggiungere metodi setter

    private String nickname;
    private Color colorPlayer;

    public Player() {
    }

    public Player(String nickname, Color colorPlayer) {
        this.nickname = nickname;
        this.colorPlayer = colorPlayer;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Color getColorPlayer() {
        return colorPlayer;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setColor(Color colorPlayer) {
        this.colorPlayer = colorPlayer;
    }
}