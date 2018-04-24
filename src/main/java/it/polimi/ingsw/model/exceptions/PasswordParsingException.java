package it.polimi.ingsw.model.exceptions;

public class PasswordParsingException extends Exception {
    public PasswordParsingException(){}
    @Override
    public String getMessage() {
        return "There was a problem parsing the password";
    }
}

