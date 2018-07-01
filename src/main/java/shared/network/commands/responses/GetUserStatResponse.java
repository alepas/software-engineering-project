package shared.network.commands.responses;

import shared.clientInfo.ClientUser;

public class GetUserStatResponse implements Response {
    public final ClientUser user;
    public final Exception exception;

    public GetUserStatResponse(ClientUser user, Exception exception) {
        this.user = user;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler){
        handler.handle(this);
    }
}