package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.costants.GameCostants;
import it.polimi.ingsw.model.exceptions.gameExceptions.*;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.Collections;

public class MultiPlayerGame extends AbstractGame implements Runnable {
    private Thread t;
    private int turnPlayer;
    private int roundPlayer;
    private int currentTurn;

    public MultiPlayerGame(String user, int numPlayers) {
        super(user, numPlayers);
        turnPlayer = 0;
        roundPlayer = 0;
        currentTurn = 1;
        numOfPrivateObjectivesForPlayer = GameCostants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME;
        numOfToolCards = GameCostants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME;
        numOfPublicObjectiveCards = GameCostants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME;
    }

    public void start(){
        if (t == null){
            t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        //Codice che regola il funzionamento della partita
    }

    public int getTurnPlayer() { return turnPlayer; }

    public int getRoundPlayer() { return roundPlayer; }

    public int getCurrentTurn() { return currentTurn; }

    @Override
    public void startGame() throws NotEnoughPlayersException {
        if (players.size() < numPlayers){
            throw new NotEnoughPlayersException(this);
        }
        extractPrivateObjectives();
        extractWPCs();
        extractToolCards();
        extractPublicObjectives();
        shufflePlayers();
    }

    @Override
    protected void disconnectPlayer(PlayerInGame playerInGame) {

    }

    private void shufflePlayers(){ Collections.shuffle(players); }

    @Override
    public void endGame() {

    }

    @Override
    public void nextRound() {

    }

    @Override
    public void nextTurn() {
        if (currentTurn < GameCostants.NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME*getNumPlayers()) {
            turnPlayer = nextPlayer();
            currentTurn++;
        } else {
//            nextRound();
        }
    }

    private int nextPlayer() throws MaxNumberOfTurnsPlayedExeption {
        if (currentTurn == numPlayers*GameCostants.NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME) {
            throw new MaxNumberOfTurnsPlayedExeption(this);
        }

        if (currentTurn % numPlayers == 0) {
            return turnPlayer;
        } else if ((currentTurn / numPlayers) % 2 == 0){
            if (turnPlayer != numPlayers-1){
                return turnPlayer+1;
            } else {
                return 0;
            }
        } else {
            if (turnPlayer != 0){
                return turnPlayer-1;
            } else {
                return numPlayers-1;
            }
        }
    }

    //Da testare
    @Override
    public void calculateScore() {

    }

    //Da testare
    @Override
    public void saveScore() {

    }

    public void addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException {
        if (players.size() == numPlayers) throw new MaxPlayersExceededException(user, this);

        for (PlayerInGame player : players){
            if (player.getUser().equals(user)) throw new UserAlreadyInThisGameException(user, this);
        }

        PlayerInGame player = new PlayerInGame(user);
        players.add(player);
    }

    public void removePlayer(String user) throws UserNotInThisGameException {
        for (PlayerInGame player : players){
            if(player.getUser().equals(user)){
                players.remove(player);
                return;
            }
        }
        throw new UserNotInThisGameException(user, this);
    }
}
