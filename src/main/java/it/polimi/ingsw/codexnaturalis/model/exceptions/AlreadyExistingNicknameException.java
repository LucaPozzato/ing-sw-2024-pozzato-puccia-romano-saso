package it.polimi.ingsw.codexnaturalis.model.exceptions;

public class AlreadyExistingNicknameException extends Exception {

    // message will be derived from throw
    public AlreadyExistingNicknameException(String message) {
        super(message);
    }
}
