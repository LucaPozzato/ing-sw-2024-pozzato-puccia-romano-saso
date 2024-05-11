package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class InLobbyEvent extends Event {
    @Serial
    private static final long serialVersionUID = 693215087341934L;

    private String clientId;
    private Integer gameId;
    private String state;
    private String nickName;

    public InLobbyEvent(String clientId, Integer gameId, String state, String nickName) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.state = state;
        this.nickName = nickName;
    }

    @Override
    public void doJob(MiniModel miniModel) {
        miniModel.setGameId(gameId);
        miniModel.setNickName(nickName);
        miniModel.setState(state);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}
