package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.commands.notifications.*;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class FourPlayersGameController implements NotificationHandler {


    @FXML private GridPane firstWPC;

    @FXML private GridPane secondWPC;

    @FXML private GridPane thirdWPC;

    @FXML private GridPane fourthWPC;


    @Override
    public void handle(GameStartedNotification notification) {
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
    }

    @Override
    public void handle(PrivateObjExtractedNotification response) {
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {

    }

    @Override
    public void handle(UserPickedWpcNotification notification) {

    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {

    }

    @Override
    public void handle(PocsExtractedNotification notification) {

    }

    @Override
    public void handle(NewRoundNotification notification) {

    }

    @Override
    public void handle(NextTurnNotification notification) {

    }

    @Override
    public void handle(DiceChangedNotification notification) {

    }

    @Override
    public void handle(DicePlacedNotification notification) {

    }

    @Override
    public void handle(ToolCardCanceledNotification notification) {

    }

    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }
}
