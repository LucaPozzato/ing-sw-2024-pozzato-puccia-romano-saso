package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a Triplet of three generic objects.
 * It is used as an elements of the maps that keep track of the corrispondence
 * between coordinate and cards in the matrix representing the structure of the
 * player.
 */
public class Triplet<X, Y, Z> implements Serializable {
    @Serial
    private static final long serialVersionUID = 198312934098263L;
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
