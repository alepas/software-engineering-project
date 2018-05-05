package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class NullTokenException extends RuntimeException{
    private String user;

    public NullTokenException(){}
    @Override
    public String getMessage() {
       return "The provided token is null";
    }


}