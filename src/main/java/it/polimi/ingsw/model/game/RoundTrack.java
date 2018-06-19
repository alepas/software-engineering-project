package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.DiceAndPosition;

import java.io.Serializable;
import java.util.ArrayList;

import static it.polimi.ingsw.model.constants.GameConstants.NUM_OF_ROUNDS;

public class RoundTrack implements Serializable {
    private int currentRound;
    private Dice[][] dicesNotUsed;


    /**
     * Constructor of the class, at the beginning of the game all dices are null. The matrix has dimension 10x10
     * because, if all players pass all turns all dices (max 90) must be inside the roundTrack.
     */
    public RoundTrack(){
        currentRound = 0;
        dicesNotUsed = new Dice[NUM_OF_ROUNDS][NUM_OF_ROUNDS];
        for (int row = 0;  row< NUM_OF_ROUNDS; row++ ) {

            for (int column = 0; column < NUM_OF_ROUNDS; column++)
                dicesNotUsed[row][column] = null;
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }


    /**
     * @param row in the matrix
     * @param column in the matrix
     * @return true if there's a dice in the given position, false if the matrix doesn't contain any dice in the chosen
     * position
     */
    private Boolean isThereADice(int row, int column){
        return dicesNotUsed[row][column]!= null;
    }


    /**
     * @param row in the matrix
     * @param column in the matrix
     * @return the dice in the given position
     */
    private Dice getDice(int row, int column){
        return dicesNotUsed[row][column];    
    }


    /**
     * @return an arrayList composed by all the dices inside the roundTrack
     */
    public ArrayList<Dice> getDicesNotUsed() {
        ArrayList<Dice> allRoundTrackDices = new ArrayList<>();

        for (int row = 0;  row< NUM_OF_ROUNDS; row++ ) {

            for (int column = 0; column < NUM_OF_ROUNDS; column++)
                if (isThereADice(row, column))
                allRoundTrackDices.add(getDice(row, column));
        }
        return allRoundTrackDices;
    }


    /**
     * Adds one to the current round.
     */
    void nextRound( ){
        currentRound++;
    }


    /**
     * Adds the new dice in the first available position.
     *
     * @param dice that must be add to the roundTrack
     */
    void addDice(Dice dice){
            int row = currentRound - 1;
            int column = 0;
            
            do {
                column++;
            }while(isThereADice(column,row) && column < NUM_OF_ROUNDS);
            
            dicesNotUsed[column][row] = dice;
                
    }


    /*public Position getPositionFromDice(Dice roundTrackDice) {
        for (int row = 0; row < NUM_OF_ROUNDS; row++) {

            for (int column = 0; column < NUM_OF_ROUNDS; column++)
                if (isThereADice(row, column))
                    if (getDice(row, column).equals(roundTrackDice)) {
                        return new Position(row,column);
                    }
        }
        return null;
    }*/


    /**
     * Removes a dice selected by his position and places at the same position a new dice.
     *
     * @param addedDice is the dice that must be add to the roundTrack
     * @param position is the position of the dice that must be removed
     * @return the removed dice
     */
    public Dice swapDice (Dice addedDice, Position position){
        Dice removedDice = dicesNotUsed[position.getRow()][position.getColumn()];
        dicesNotUsed[position.getRow()][position.getColumn()] = addedDice;
        return removedDice;

    }

    /**
     * @param diceId is the ID of a chosen dice
     * @return the dice and position associated to the with the chosen id
     */
    public DiceAndPosition getDiceAndPosition(int diceId){
        for (int row = 0; row < NUM_OF_ROUNDS; row++) {

            for (int column = 0; column < NUM_OF_ROUNDS; column++) {
                if (isThereADice(row, column))
                    if (getDice(row, column).getId() == diceId)
                        return new DiceAndPosition(getDice(row, column), new Position(row, column));
            }
        }
        return null;
    }


    
    /*public ArrayList<Dice> getRoundDices ( int round) {
        ArrayList<Dice> roundNotUsedDices = new ArrayList<>();
        int column = round - 1;

        for (int row = 0; row < NUM_OF_ROUNDS; row++){
            if (isThereADice(row, column))
                roundNotUsedDices.add(dicesNotUsed[row][column]);
        }
        
        return roundNotUsedDices;
    }*/

    /**
     * Creates a clientRoundTrack exactly equals to this.
     *
     * @return the clientRoundTrack
     */
    public ClientRoundTrack getClientRoundTrack(){
        ClientDice[][] roundTrackTable=new ClientDice[NUM_OF_ROUNDS][NUM_OF_ROUNDS];
        for (int i=0; i<NUM_OF_ROUNDS; i++){
            for (int j=0;j<NUM_OF_ROUNDS;j++){
                if(dicesNotUsed[i][j]!= null)
                    roundTrackTable[i][j] = dicesNotUsed[i][j].getClientDice();
                else
                    roundTrackTable[i][j] = null;
            }
        }
        return new ClientRoundTrack(currentRound, roundTrackTable);
    }


    /**
     * Counts how many dices there are in the roundTrack.
     *
     * @return the number of dices inside the roundTrack
     */
    public int getNumberOfDices(){
        int count=0;
        for (int row = 0;  row< NUM_OF_ROUNDS; row++ ) {
            for (int column = 0; column < NUM_OF_ROUNDS; column++)
                if (isThereADice(row, column))
                    count++;
        }
        return count;
    }

}
