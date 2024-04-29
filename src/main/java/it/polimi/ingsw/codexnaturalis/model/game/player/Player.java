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

    public Player(String nickName, Color color) {
        this.nickName = nickName;
        this.colorPlayer = color;
    }

    public String getNickname() {
        return this.nickName;
    }

    public Color getColor() {
        return colorPlayer;
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
    }

    public void setColor(Color colorPlayer) {
        this.colorPlayer = colorPlayer;
    }
}