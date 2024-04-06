package it.polimi.ingsw.codexnaturalis.model.game.player;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;

public class Player {

    private String nickName;
    private Color colorPlayer;

    public Player() {
    }

    public Player(String nickName) {
        this.nickName = nickName;
    }

    public String getNickname() {
        return this.nickName;
    }

    public Color getColorPlayer() {
        return colorPlayer;
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
    }

    public void setColor(Color colorPlayer) {
        this.colorPlayer = colorPlayer;
    }
}