package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientNextActions;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientWpc;

import java.util.ArrayList;

public class PlaceDiceResponse implements Response {
    public final ClientNextActions nextAction;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final Exception exception;


    public PlaceDiceResponse(ClientNextActions nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Exception exception) {
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.exception = exception;
    }

    public PlaceDiceResponse(Exception exception) {
        this.exception = exception;
        nextAction = null;
        wpc = null;
        extractedDices = null;
        roundTrack = null;
    }

    @Override
    public void handle(ResponseHandler handler) { handler.handle(this); }
}
