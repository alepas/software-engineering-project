package it.polimi.ingsw.model.dicebag;
import it.polimi.ingsw.model.dicebag.Color;

import java.util.Random;

public class Dice {
    private Color color;
    private int num;

    public Dice(Color color) {
        this.color = color;
        num = rollDice();
    }

    public Dice(Color color, int number) {
        this.color = color;
        num = number;
    }

    //metodo invocato sia dal costruttore per assegnare un numero tra 1 e 6
    // ma anche nel caso in cui possa ritirare il dado
    int rollDice( ){
        Random random = new Random();
        num = random.nextInt(5) + 1;
        return num;
    }

    public Color getDiceColour( ){
        return color;
    }

    public int getDiceNumber( ){
        return num;
    }


}