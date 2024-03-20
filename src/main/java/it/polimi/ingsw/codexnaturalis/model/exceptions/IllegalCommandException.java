package it.polimi.ingsw.codexnaturalis.model.exceptions;

public class IllegalCommandException extends Exception {
    public IllegalCommandException(String message) {
        super(message);
    }

    public IllegalCommandException() {
        super();
    }
}
