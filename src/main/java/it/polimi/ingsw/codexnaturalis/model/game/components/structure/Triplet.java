package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

public class Triplet<X, Y, Z>{
    private final X x;
    private final Y y;
    private Z z;

    public Triplet(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public X getFirst() {
        return x;
    }

    public Y getSide() {
        return y;
    }

    public Z getVisited() {
        return z;
    }

    public void setVisited(Z z) {
        this.z = z;
    }
}
