package it.polimi.ingsw.codexnaturalis.model.game.player;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;

import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 784093625109283L;


    private String nickname;
    private String password;
    private Color colorPlayer;

    public Player() {
    }

    public Player(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Player(String nickname, String password, Color color) {
        this.nickname = nickname;
        this.password = password;
        this.colorPlayer = color;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Color getColor() {
        return this.colorPlayer;
    }
    public String getPassword(){ return this.password;}

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setColor(Color colorPlayer) {
        this.colorPlayer = colorPlayer;
    }
}