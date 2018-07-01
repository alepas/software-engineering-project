package shared.exceptions.gameExceptions;
import server.model.game.Game;

public class UserNotInThisGameException extends Exception {
    private final String username;
    private final Game game;

    public UserNotInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + " not in game: " + game.getID();
    }
}