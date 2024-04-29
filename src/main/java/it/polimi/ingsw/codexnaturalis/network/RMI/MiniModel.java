package it.polimi.ingsw.codexnaturalis.network.RMI;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiniModel {

    //TODO: define infos to have in this local copy of the view
    //TODO: getters, setters
    //Clients update will call the setters to save the changes
    //View will be notified of the changes and act consequentially


    private int gameId;
    private List<Player> players;
    private Hand playerHand;
    //necessario poter vedere anche le strutture degli altri
    private List<Structure> playerStructure;

    //informazioni visibili del deck
    private Card DrawableResourceCard;
    private Card DrawableGoldCard;

    //informazioni visibili della board
    private List<Card> commonObjectives;
    private Map<Player, Integer> actualScores;


    // informazioni che possono essere utili per la view,
    // non necessarie per la logica di questo model
    private Player currentPlayer;
    private Player nextPlayer;
    private Boolean lastTurn = false;
    private Integer turnCounter = 0;

}
