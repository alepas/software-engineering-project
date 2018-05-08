package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.game.Game;

public class FindGameResponse implements Response {
    public final String gameID;
    public final int actualPlayers;
    public final int numPlayers;
    public final String error;

    public FindGameResponse(String gameID, int actualPlayers, int numPlayers, String error) {
        this.gameID = gameID;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
        this.error = error;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}