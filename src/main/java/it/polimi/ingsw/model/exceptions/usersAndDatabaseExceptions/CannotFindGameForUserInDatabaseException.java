package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindGameForUserInDatabaseException extends Exception{


    public CannotFindGameForUserInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "Impossibile trovare una partita nel database per il giocatore.";
    }



}
