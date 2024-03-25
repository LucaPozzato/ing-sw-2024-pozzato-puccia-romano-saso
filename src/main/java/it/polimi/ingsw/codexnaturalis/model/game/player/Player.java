package it.polimi.ingsw.codexnaturalis.model.game.player;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;

public class Player {

    private String nickname;
    private Color colorPlayer;

    public Player() {
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