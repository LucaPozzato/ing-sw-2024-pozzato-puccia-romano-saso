import java.util.List;

public class Player {

    private String nickname;
    private COLORE colorPlayer;

    public void player( String nickname, COLORE colorPlayer ) {
        this.nickname = nickname;
        this.colorPlayer = colorPlayer;
    }

    public String getNickname(){
        return this.nickname;
    }

    public COLORE getColorPlayer() {
        return colorPlayer;
    }

}