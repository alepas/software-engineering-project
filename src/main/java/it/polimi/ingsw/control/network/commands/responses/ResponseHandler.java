package it.polimi.ingsw.control.network.commands.responses;

public interface ResponseHandler{

    void handle(CreateUserResponse response);

    void handle(LoginResponse response);

    void handle(FindGameResponse response);

    void handle(PickWpcResponse response);

    void handle(PassTurnResponse response);

    void handle(ToolCardResponse response);

    void handle(PickDiceResponse response);

    void handle(PickPositionResponse response);

}