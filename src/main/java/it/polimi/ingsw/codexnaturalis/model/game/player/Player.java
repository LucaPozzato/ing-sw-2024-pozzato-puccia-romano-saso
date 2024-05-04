package it.polimi.ingsw.codexnaturalis.model.game.player;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;

import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 784093625109283L;


    private String nickname;
    private Color colorPlayer;

    public Player() {
    }

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public Player(String nickname, Color color) {
        this.nickname = nickname;
        this.colorPlayer = color;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Color getColor() {
        return colorPlayer;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setColor(Color colorPlayer) {
        this.colorPlayer = colorPlayer;
    }
}