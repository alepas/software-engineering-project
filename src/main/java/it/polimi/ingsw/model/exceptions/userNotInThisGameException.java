package it.polimi.ingsw.model.exceptions;
import it.polimi.ingsw.model.game.AbstractGame;

public class userNotInThisGameException extends RuntimeException {
    private final String username;
    private final AbstractGame game;

    public userNotInThisGameException(String username, AbstractGame game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + " not in game: " + game.getGameID();
    }
}
