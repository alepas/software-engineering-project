package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public class ClientDice implements Serializable {
    private ClientColor color;
    private int number;
    private int diceID;

    public ClientDice(ClientColor color, int number, int diceID) {
        this.color = color;
        this.number = number;
        this.diceID = diceID;
    }

    public ClientColor getDiceColor( ){
        return color;
    }

    public int getDiceNumber( ){
        return number;
    }

    public int getDiceID( ){ return diceID ;}
}