package it.polimi.ingsw.codexnaturalis.model.game.player;

import java.io.Serial;
import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;

/**
 * Represents a player in the game with the associated nickname, password and
 * color.
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 784093625109283L;

    private String nickname;
    private String password;
    private Color colorPlayer;

    /**
     * Constructs a player with default values.
     */
    public Player() {
    }

    /**
     * Constructs a player with a given nickname and password.
     *
     * @param nickname The nickname of the player.
     * @param password The password associated with the player.
     */
    public Player(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    /**
     * Constructs a player with a given nickname, password, and color.
     *
     * @param nickname    The nickname of the player.
     * @param password    The password associated with the player.
     * @param colorPlayer The color assigned to the player.
     */
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

    public String getPassword() {
        return this.password;
    }

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