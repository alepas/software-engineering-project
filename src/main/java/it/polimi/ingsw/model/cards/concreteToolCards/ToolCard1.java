package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DiceChangedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientNextActions;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ToolCard1 extends ToolCard {
    private Dice dice;
    private Dice oldDice;
    private ArrayList<ClientDice> tempExtractedDices;
    ArrayList<Integer> numbers;

    public ToolCard1() {
        this.id = ToolCardConstants.TOOLCARD1_ID;
        this.name = ToolCardConstants.TOOL1_NAME;
        this.description = ToolCardConstants.TOOL1_DESCRIPTION;
        this.colorForDiceSingleUser=Color.VIOLET;
        this.allowPlaceDiceAfterCard=false;
        this.cardBlocksNextTurn=false;
        this.maxCancelStatus=3;
        this.cardOnlyInFirstMove=true;
        this.used=false;
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.dice=null;
        this.singlePlayerGame=false;
        numbers=new ArrayList<>();
        numbers.add(-1);
        numbers.add(1);
    }



    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard1() ;
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);

        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        this.used = true;
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame=true;
            return new MoveData(ClientNextActions.PICKDICE_SINGLEPLAYER_ENABLECARD,ClientDiceLocations.EXTRACTED);
        }
        else {
            this.currentStatus = 1;
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, id));
            return new MoveData(ClientNextActions.PICK_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

        }
    }


    @Override
    public MoveData pickDice(Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus==0)&&(singlePlayerGame)){
            if (dice.getDiceColor()!=colorForDiceSingleUser)
                throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),location, 1);
            this.currentStatus = 1;
            this.dice=dice;
            currentGame.getExtractedDices().remove(dice);
            updateClientExtractedDices();
            return new MoveData(ClientNextActions.PICK_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null);
        }
        if(currentStatus!=1)
            throw new CannotPerformThisMoveException(username,2,false);
        currentStatus=2;
        this.dice=dice;
        return new MoveData(ClientNextActions.PICK_NUMBER_TOOLCARD,null,null,null,dice.getId(),numbers,false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        if(currentStatus!=2)
            throw new CannotPerformThisMoveException(username,2,false);
        if (!((number==-1)||(number==1)))
            throw new CannotPickNumberException(username,number);

        int tempNum=this.dice.getDiceNumber();
        if (((tempNum==6)&&(number==1))||((tempNum==1)&&(number==-1)))
            throw new CannotPickNumberException(username, number);
        oldDice=this.dice.copyDice();
        try {
            dice.setNumber(tempNum+number);
        } catch (IncorrectNumberException e) {
            throw new CannotPickNumberException(username,number);
        }
        currentStatus=3;
        updateClientExtractedDices();
        currentGame.changeAndNotifyObservers(new DiceChangedNotification(username,oldDice.getClientDice(),dice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.EXTRACTED,tempExtractedDices));
        return new MoveData(ClientNextActions.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,null,dice.getId());
    }

    @Override
    public MoveData placeDice(Dice dice, ClientDiceLocations startLocation, ClientDiceLocations finishLocation, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if(currentStatus!=3)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (dice.getId()!=this.dice.getId())
            throw new CannotPickDiceException(username,dice.getId(),ClientDiceLocations.EXTRACTED,3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(dice, pos, currentGame.getCurrentTurn()))
            throw new CannotPickPositionException(username, pos);
        currentStatus=4;
        currentPlayer.getGame().changeAndNotifyObservers(new DiceChangedNotification(username,oldDice.getClientDice(),dice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.EXTRACTED,tempExtractedDices));
        updateClientExtractedDices();
        currentPlayer.setToolCardUsedInTurn(true);
        PlayerInGame tempPlayer=currentPlayer;
                cleanCard();
        return new MoveData(true,tempPlayer.getWPC().getClientWpc(),tempExtractedDices,null);
    }



    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame){
                    currentGame.getExtractedDices().add(diceForSingleUser);
                    updateClientExtractedDices();
                    cleanCard();
                    return new MoveData(true,true,null,tempExtractedDices,null,null,null);
                }
                    return null;
            }
            case 1: {
                if (!singlePlayerGame){
                    cleanCard();
                    return new MoveData(true,true,null,null,null,null,null);

                }
                return new MoveData(ClientNextActions.PICKDICE_SINGLEPLAYER_ENABLECARD,ClientDiceLocations.EXTRACTED);
            }
            case 2: {
                this.dice=null;
                this.currentStatus=1;
                return new MoveData(ClientNextActions.PICK_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

            }
            case 3: {
                try {
                    dice.setNumber(oldDice.getDiceNumber());
                } catch (IncorrectNumberException e) {
                    throw new CannotCancelActionException(username,this.id,3);
                }
                updateClientExtractedDices();
                return new MoveData(ClientNextActions.PICK_NUMBER_TOOLCARD,null,tempExtractedDices,null,null,numbers,false);
            }
        }
        return null;

    }

    private void updateClientExtractedDices(){
        for (Dice tempdice:currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
    }

    @Override
    protected void cleanCard(){
        currentPlayer.setToolCardInUse(null);
        currentPlayer.setAllowPlaceDiceAfterCard(true);
        this.used=false;
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.dice=null;
        this.singlePlayerGame=false;
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(ClientNextActions.PICKDICE_SINGLEPLAYER_ENABLECARD,ClientDiceLocations.EXTRACTED);
                else return null;
            }
            case 1: return new MoveData(ClientNextActions.PICK_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null);
            case 2: return new MoveData(ClientNextActions.PICK_NUMBER_TOOLCARD,null,null,null,this.dice.getId(),numbers,false);


            case 3: return new MoveData(ClientNextActions.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,null,dice.getId());

        }
        return null;
    }


}

