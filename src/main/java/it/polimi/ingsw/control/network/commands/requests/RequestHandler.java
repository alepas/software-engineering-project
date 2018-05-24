package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public interface RequestHandler {

    Response handle(CreateUserRequest request);

    Response handle(LoginRequest request);

    Response handle(FindGameRequest request);

    Response handle(PickWpcRequest request);

    Response handle(PassTurnRequest request);

    Response handle(PickDiceRequest request);

    Response handle(PickPositionRequest request);

    Response handle(ToolCardPickColorRequest request);

    Response handle(ToolCardPickDiceRequest request);

    Response handle(ToolCardPickNumberRequest request);

    Response handle(ToolCardPickPositionRequest request);

    Response handle (UseToolCardRequest request);

}