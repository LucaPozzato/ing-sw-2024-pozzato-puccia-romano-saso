import java.util.List;

public class Player {

    private String nickname;
    private Color colorPlayer;

    public Player( String nickname, Color colorPlayer ) {
        this.nickname = nickname;
        this.colorPlayer = colorPlayer;
    }

    public String getNickname(){
        return this.nickname;
    }

    public Color getColorPlayer() {
        return colorPlayer;
    }

}