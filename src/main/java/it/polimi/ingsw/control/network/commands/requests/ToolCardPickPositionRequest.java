package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientPosition;

public class ToolCardPickPositionRequest implements Request {
    public final String userToken;
    public final ClientPosition position;



    public ToolCardPickPositionRequest(String userToken, ClientPosition position) {
        this.userToken = userToken;
        this.position=position;

    }




    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

