package shared.exceptions.gameExceptions;

import server.model.game.Game;

public class UserAlreadyInThisGameException extends Exception {
    private final String username;
    private final Game game;

    public UserAlreadyInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + " already joined this game: " + game.getID();
    }
}