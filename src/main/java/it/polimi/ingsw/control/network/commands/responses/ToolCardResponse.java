package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.*;

import java.util.ArrayList;

public class ToolCardResponse implements Response {
    public final NextAction nextAction;
    public final ClientDiceLocations wherePickNewDice;
    public final ClientDiceLocations wherePutNewDice;
    public final ArrayList<Integer> numbersToChoose;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final ClientDice diceChosen;
    public final ClientDiceLocations diceChosenLocation;
    public final Exception exception;
    public final String stringForStopToolCard;
    public final boolean bothYesAndNo;
    public final boolean showBackButton;


    public ToolCardResponse(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice, ArrayList<Integer> numbersToChoose, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, Exception exception, String stringForStopToolCard, boolean bothYesAndNo, boolean showBackButton) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.numbersToChoose = numbersToChoose;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.exception = exception;
        this.stringForStopToolCard = stringForStopToolCard;
        this.bothYesAndNo = bothYesAndNo;
        this.showBackButton = showBackButton;
    }

    public ToolCardResponse(Exception exception) {
        this.exception = exception;
        nextAction = null;
        wherePickNewDice = null;
        numbersToChoose=null;
        wpc=null;
        extractedDices=null;
        roundTrack=null;
        diceChosen = null;
        wherePutNewDice=null;
        diceChosenLocation=null;
        stringForStopToolCard = null;
        bothYesAndNo = false;
        showBackButton = false;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}