package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.List;

public class ResourceCard extends Card{
    private String idCard;
    private String symbol;
    private int points;
    private List<String> corners;

    public ResourceCard(String idCard, String symbol, int points, List<String> corners){
        this.idCard = idCard;
        this.symbol = symbol;
        this.points = points;
        this.corners = corners;
    }
    public void print(){
        System.out.println("id: " + idCard + " symbol: " + symbol + " points: " + points + " corners: " + corners);
    }
}
