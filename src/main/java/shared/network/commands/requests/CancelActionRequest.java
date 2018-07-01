package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class CancelActionRequest implements Request {
    public final String userToken;


    public CancelActionRequest(String userToken) {
        this.userToken = userToken;

    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}