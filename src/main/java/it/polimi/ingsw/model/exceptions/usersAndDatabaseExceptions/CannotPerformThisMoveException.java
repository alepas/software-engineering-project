package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotPerformThisMoveException extends Exception{
    private int cause;
    private String user;
    private boolean endTurn;


    public CannotPerformThisMoveException(String username, int cause, boolean endTurn) {

        this.cause=cause;
        this.user = username;
        this.endTurn=endTurn;
    }
    @Override
    public String getMessage() {
        String nameOfMove;
        if (endTurn)
            nameOfMove="end the current turn";
        else nameOfMove="perform this move";

        if (cause==0)
            return "Can't "+nameOfMove+" for "+user+" because there is a move pending: placing a dice. Complete or cancelCard this move befor end the turn.";

        else if (cause==1)
            return "Can't "+nameOfMove+" for "+user+" because there is a move pending: using a card. Complete or cancelCard this move befor end the turn.";
        else if (cause==2)
            return "Can't perform this move because it's unavailable right now. Follow the steps on screen";

        else if (cause==3)
            return "Can't perform this move because you had already done it in this turn. Follow the steps on screen";

        return "Can't "+nameOfMove+" for "+user+" because it's impossible.";
    }


    public int getErrorId() {
        return cause;
    }
}