package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.usersdb.User;

import java.util.Set;

public abstract class AbstractGame {
    Set<ToolCard> toolCards;
    Set<ObjectiveCard> objectiveCards;
    int gameID;
    int numPlayers;
    RoundTrack roundTrack;
    Set<Dice> extractedDices;
    Set<PlayerInGame> users;

    public AbstractGame(User user, int numPlayers) {
        gameID = 0;
        this.numPlayers = numPlayers;
        roundTrack = new RoundTrack();
        PlayerInGame player = new PlayerInGame(user);
        users.add(player);
    }

    abstract void startGame();
    abstract void endGame();
    abstract void nextRound();
    abstract void nextTurn();
    abstract void extractWPC();
    abstract void calculateScore();
    abstract void saveScore();

    public Set<ToolCard> getToolCards() {
        return toolCards;
    }

    public Set<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    public int getGameID() {
        return gameID;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Set<Dice> getExtractedDices() {
        return extractedDices;
    }

    public Set<PlayerInGame> getUsers() {
        return users;
    }

    public void removeExtractedDice(Dice dice){
        extractedDices.remove(dice);
    }

}