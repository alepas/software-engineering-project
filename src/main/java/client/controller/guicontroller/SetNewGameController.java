package client.controller.guicontroller;

import client.constants.ClientWpcConstants;
import client.network.ClientInfo;
import client.network.NetworkClient;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.clientInfo.ClientCell;
import shared.clientInfo.ClientColor;
import shared.clientInfo.ClientWpc;
import shared.exceptions.gameExceptions.CannotCreatePlayerException;
import shared.exceptions.gameExceptions.InvalidNumOfPlayersException;
import shared.exceptions.gameExceptions.NotYourWpcException;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import shared.network.commands.notifications.*;

import java.io.IOException;
import java.util.*;

import static java.lang.String.valueOf;

public class SetNewGameController implements Observer, NotificationHandler {

    private String wpc0ID;
    private String wpc1ID;
    private String wpc2ID;
    private String wpc3ID;

    private final Object playerWaiter = new Object();
    private final Object cardWaiter = new Object();
    private final Object wpcWaiter = new Object();

    private boolean gameStarted = false;
    private boolean areWpcExtracted = false;
    private boolean allWpcsPicked = false;
    private boolean pocExtracted = false;
    private boolean privateObjExtractd = false;
    private boolean toolExtracted = false;
    private boolean reconnected = false;

    private ClientColor[] colors = new ClientColor[2];
    private ArrayList<ClientWpc> userWpcs = new ArrayList<>();

    private NetworkClient networkClient;
    private ClientInfo clientInfo;

    private Label lab = new Label();
    private Label lab1 = new Label();
    private Label lab2 = new Label();
    private Label lab3 = new Label();

    private String newPlayer = null;

    @FXML
    private AnchorPane clockArea;

    @FXML
    private AnchorPane privateObjArea;

    @FXML
    private AnchorPane selectionArea;

    @FXML
    private Button createGameButton;

    @FXML
    private Button disconnectionButton;

    @FXML
    private Button personalAreaButton;

     @FXML
     private Button reconnectionButton;

    @FXML
    private Circle fourthWpcCircle4;

    @FXML
    private Circle fourthWpcCircle5;

    @FXML
    private Circle fourthWpcCircle6;

    @FXML
    private Circle thirdWpcCircle4;

    @FXML
    private Circle thirdWpcCircle5;

    @FXML
    private Circle thirdWpcCircle6;

    @FXML
    private Circle secondWpcCircle4;

    @FXML
    private Circle secondWpcCircle5;

    @FXML
    private Circle secondWpcCircle6;

    @FXML
    private Circle firstWpcCircle4;

    @FXML
    private Circle firstWpcCircle5;

    @FXML
    private Circle firstWpcCircle6;

    @FXML
    private Label clockLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label findPlayersLabel;

    @FXML
    private Label numPlayersInGameLabel;

    @FXML
    private Label numOfPlayersLabel;

    @FXML
    private Label startGameLabel;

    @FXML
    private Label wpcInfoLabel;

    @FXML
    private RadioButton twoPlayersBox;

    @FXML
    private RadioButton soloPlayerBox;

    @FXML
    private RadioButton threePlayersBox;

    @FXML
    private RadioButton fourPlayersBox;

    @FXML
    private Label wpc0Name;

    @FXML
    private Label wpc1Name;

    @FXML
    private Label wpc2Name;

    @FXML
    private Label wpc3Name;

    @FXML
    private GridPane fourthWPC;

    @FXML
    private GridPane firstWPC;

    @FXML
    private GridPane thirdWPC;

    @FXML
    private GridPane secondWPC;


    /**
     * Initializes the beginning anchor pane of this controller, contains all the lambdas related to the action that the
     * player can do by clicking on buttons or other elements in the scene.
     */
    @FXML
    void initialize() {

        networkClient = NetworkClient.getInstance();
        clientInfo = ClientInfo.getInstance();
        clientInfo.addObserver(this);


        if(clientInfo.getGame()!= null && clientInfo.getMyWpc() == null){
            reconnected = true;
            disableAll();
            String username = clientInfo.getUsername();
            colors[0] = clientInfo.getPrivateObjectives()[0];
            userWpcs.addAll(clientInfo.getWpcsProposedByUsername().get(username));
            initializeWpc();

        }

        ToggleGroup group = new ToggleGroup();
        soloPlayerBox.setToggleGroup(group);
        twoPlayersBox.setToggleGroup(group);
        threePlayersBox.setToggleGroup(group);
        fourPlayersBox.setToggleGroup(group);
        selectionArea.getChildren().addAll(lab, lab1, lab2, lab3);

        createGameButton.setOnAction(event -> {
            if (soloPlayerBox.isSelected()) findGame(event, 1);
            else if (twoPlayersBox.isSelected()) findGame(event, 2);
            else if (threePlayersBox.isSelected()) findGame(event, 3);
            else if (fourPlayersBox.isSelected()) findGame(event, 4);
            else {
                Platform.runLater(()->{
                    errorLabel.setText("Seleziona un numero");
                    errorLabel.setVisible(true);
                });
            }
        });

        soloPlayerBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        twoPlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        threePlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        fourPlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        personalAreaButton.setOnAction(event -> changeSceneHandle(event,
                "/client/client/view/gui/fxml/fxml/PersonalAreaScene.fxml"));

        firstWPC.setOnMouseClicked(event -> pickWpc(event, wpc0ID));

        secondWPC.setOnMouseClicked(event -> pickWpc(event, wpc1ID));

        thirdWPC.setOnMouseClicked(event -> pickWpc(event, wpc2ID));

        fourthWPC.setOnMouseClicked(event -> pickWpc(event, wpc3ID));

        reconnectionButton.setOnAction(this::waitPlayGameAfterDisconnection);

        disconnectionButton.setOnAction(event -> changeSceneHandle(event, "/client/view/gui/fxml/StartingScene.fxml"));
    }


    /**
     * This method set few javafx elements invisible
     */
    private void disableAll() {
        numPlayersInGameLabel.setVisible(false);
        createGameButton.setVisible(false);
        personalAreaButton.setVisible(false);
        soloPlayerBox.setVisible(false);
        twoPlayersBox.setVisible(false);
        threePlayersBox.setVisible(false);
        fourPlayersBox.setVisible(false);
        findPlayersLabel.setVisible(true);
    }


    /**
     * Calls the network method that sets a new available game or put the new player inside an already exixting game with
     * some available places.
     *
     * @param event  is the event generated by the action of the user while he/she has chosen the wpc
     * @param numPlayers is the number of players that the user wants inside the game that is going to play
     */
    private void findGame(Event event, int numPlayers) {
        disableAll();
        Platform.runLater(() -> {
            try {
                networkClient.findGame(clientInfo.getUserToken(), numPlayers, 0);
            } catch (CannotFindUserInDBException | InvalidNumOfPlayersException | CannotCreatePlayerException e) {
                errorLabel.setText(e.getMessage());
                errorLabel.setVisible(true);
                createGameButton.setDisable(false);
                personalAreaButton.setDisable(false);
            }

            String gameID = clientInfo.getGameID();

            if (gameID != null) {
                startGameLabel.setVisible(false);
                lab.setText("Entrato nella partita: " + gameID);
                lab.setLayoutX(100);
                lab.setLayoutY(300);
                lab1.setText("Giocatori presenti: " + clientInfo.getGameActualPlayers() +
                        " di " + clientInfo.getGameNumPlayers() + " necessari.");
                lab1.setLayoutX(100);
                lab1.setLayoutY(340);

                SettingOfGame(event);
            }
        });
    }

    /**
     * calls the methods that wait all the elements that compose a game
     */
    private void SettingOfGame(Event event) {
        waitPlayers();
        waitWPC();
        playGame(event);
    }


    /**
     * Waits the other player to start the game, every time a new player enters the game a label with his/her name appears.
     * The same happen every time somebody is disconnecting.
     */
    private void waitPlayers() {
        TranslateTransition transition = new TranslateTransition();
        ImageView image = new ImageView();
        diceAnimation(image, transition);
        Thread waitPlayers = new Thread(() -> {
            if (clientInfo.getGameActualPlayers() != clientInfo.getGameNumPlayers()) {
                findPlayersLabel.setVisible(false);
                numOfPlayersLabel.setVisible(true);
            }
            while (!gameStarted) {
                synchronized (playerWaiter){
                    try {
                        playerWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Platform.runLater(()->{
                transition.stop();
                selectionArea.getChildren().remove(image);
            });
        });
        waitPlayers.start();
    }


    /**
     * Makes a small animation while the player is waiting the beginning of the game. it has been done to show to the
     * user that something is working and that the scene doens't have problems.
     *
     * @param image is the smal element that will move
     * @param transition is the type of movement that the small dice does
     */
    private void diceAnimation(ImageView image, TranslateTransition transition) {
        Platform.runLater(()->{
            image.getStyleClass().add("violet5");
            image.setFitHeight(30);
            image.setFitWidth(30);
            selectionArea.getChildren().add(image);
            image.setLayoutX(100);
            image.setLayoutY(450);

            transition.setDuration(Duration.seconds(4));
            transition.setToX(300);
            transition.setAutoReverse(true);
            transition.setCycleCount(Animation.INDEFINITE);
            transition.setNode(image);
            transition.play();
        });
    }


    /**
     * waits that the four randoms schemas arrive; when the thread takes the lock it starts the setting machine of the
     * schemas.
     */
    private void waitWPC() {
        Thread waitingWPC = new Thread(() -> {
            while (!areWpcExtracted) {
                synchronized (wpcWaiter) {
                    try {
                        wpcWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Platform.runLater(this::initializeWpc);
        });
        waitingWPC.start();
    }

    private void initializeWpc(){
        selectionArea.setVisible(false);
        for (int i = 0; i < userWpcs.size(); i++) {
            ClientWpc wpc = userWpcs.get(i);
            switch (i) {
                case 0:
                    wpc0ID = wpc.getWpcID();
                    wpc0Name.setText(ClientWpcConstants.setWpcName(wpc.getWpcID()));
                    setWpc(firstWPC, wpc);
                    setFirstWpcFavours(wpc.getFavours());
                    break;
                case 1:
                    wpc1ID = wpc.getWpcID();
                    wpc1Name.setText(ClientWpcConstants.setWpcName(wpc.getWpcID()));
                    setWpc(secondWPC, wpc);
                    setSecondWpcFavours(wpc.getFavours());
                    break;
                case 2:
                    wpc2ID = wpc.getWpcID();
                    wpc2Name.setText(ClientWpcConstants.setWpcName(wpc.getWpcID()));
                    setWpc(thirdWPC, wpc);
                    setThirdWpcFavours(wpc.getFavours());
                    break;
                case 3:
                    wpc3ID = wpc.getWpcID();
                    wpc3Name.setText(ClientWpcConstants.setWpcName(wpc.getWpcID()));
                    setWpc(fourthWPC, wpc);
                    setFourthWpcFavours(wpc.getFavours());
                    break;
                default:
                    break;
            }
        }
        setVisibleWpcSelection();
    }

    /**
     * Sets all favafx elements related to the selection of the user's personal schema visible.
     */
    private void setVisibleWpcSelection(){
        Platform.runLater(()->{
            clockArea.setVisible(true);
            firstWPC.setVisible(true);
            secondWPC.setVisible(true);
            thirdWPC.setVisible(true);
            fourthWPC.setVisible(true);
            for (ClientColor color : colors) {
                String style = "prOC".concat(String.valueOf(color).toLowerCase());
                privateObjArea.setId(style);
            }
        });
        startClockAnimation();
    }

    /**
     * Animates the clock label to show time passing.
     */
    private void startClockAnimation(){
        Timer timer = new Timer();
        int time;

        if (!reconnected)time = 60;
        else time = clientInfo.getTimeLeftToCompleteTask()/1000;

        timer.scheduleAtFixedRate(new TimerTask() {
            int clock = time;
            public void run() {
                if (clock > 0) {
                    Platform.runLater(() -> clockLabel.setText("00:" + clock));
                    clock--;
                } else {
                    Platform.runLater(() -> clockLabel.setText("00:00"));
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }

    /**
     * Sets visible as many circles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 4th schema.
     */
    private void setFourthWpcFavours(int favours) {
        switch (favours) {
            case 3:
                fourthWpcCircle6.setVisible(false);
                fourthWpcCircle5.setVisible(false);
                fourthWpcCircle4.setVisible(false);
                break;
            case 4:
                fourthWpcCircle6.setVisible(false);
                fourthWpcCircle5.setVisible(false);
                break;
            case 5:
                fourthWpcCircle6.setVisible(false);
                break;
            default:
                break;
        }
    }


    /**
     * Sets visible as many cyrcles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 3rd schema.
     */
    private void setThirdWpcFavours(int favours) {
        switch (favours) {
            case 3:
                thirdWpcCircle6.setVisible(false);
                thirdWpcCircle5.setVisible(false);
                thirdWpcCircle4.setVisible(false);
                break;
            case 4:
                thirdWpcCircle6.setVisible(false);
                thirdWpcCircle5.setVisible(false);
                break;
            case 5:
                thirdWpcCircle6.setVisible(false);
                break;
            default:
                break;
        }
    }


    /**
     * Sets visible as many cyrcles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 2nd schema.
     */
    private void setSecondWpcFavours(int favours) {
        switch (favours) {
            case 3:
                secondWpcCircle6.setVisible(false);
                secondWpcCircle5.setVisible(false);
                secondWpcCircle4.setVisible(false);
                break;
            case 4:
                secondWpcCircle6.setVisible(false);
                secondWpcCircle5.setVisible(false);
                break;
            case 5:
                secondWpcCircle6.setVisible(false);
                break;
            default:
                break;
        }
    }

    /**
     * Sets visible as many cyrcles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 1st schema.
     */
    private void setFirstWpcFavours(int favours) {
        switch (favours) {
            case 3:
                firstWpcCircle6.setVisible(false);
                firstWpcCircle5.setVisible(false);
                firstWpcCircle4.setVisible(false);
                break;
            case 4:
                firstWpcCircle6.setVisible(false);
                firstWpcCircle5.setVisible(false);
                break;
            case 5:
                firstWpcCircle6.setVisible(false);
                break;
            default:
                break;

        }
    }


    /**
     * Fills the grid with AnchorPanes filled with restrictions by calling the two methods fillNumber and fillColor.
     *
     * @param gridPane is the schema tat will be fill
     * @param wpc  is the schema which is going to tell how to fill the gridPane
     */
    private void setWpc(GridPane gridPane, ClientWpc wpc) {
        Platform.runLater(()-> privateObjArea.setVisible(true));
        for (ClientCell cell : wpc.getSchema()) {
            int row = cell.getCellPosition().getRow();
            int column = cell.getCellPosition().getColumn();

            AnchorPane cellXY = new AnchorPane();
            gridPane.add(cellXY, column, row);

            ClientColor color = cell.getCellColor();
            fillColor(cellXY, color);

            int number = cell.getCellNumber();
            fillNumber(cellXY, number);
        }
    }


    /**
     * @param cell is the anchorPane that i have to fill
     * @param number is the number restriction
     */
    private void fillNumber(AnchorPane cell, int number) {
        switch (number) {
            case 0:
                cell.getStyleClass().add("white");
                break;
            default:
                String style = "num" + valueOf(number);
                cell.getStyleClass().add(style);
                break;
        }
    }


    /**
     * @param cell is a cell
     * @param color is the filling color of a schema's cell
     */
    private void fillColor(AnchorPane cell, ClientColor color) {
        if (color != null)
            cell.getStyleClass().add(color.toString().toLowerCase());
        else cell.getStyleClass().add("white");
    }


    /**
     * Calls the network method that will assign the schema chosen to the player.
     *
     * @param wpcID  is the ID of the schema chosen by the player
     */
    public void pickWpc(Event event, String wpcID) {
        Platform.runLater(()-> {
            try {
                networkClient.pickWpc(clientInfo.getUserToken(), wpcID);
            } catch (NotYourWpcException e) {
                wpcInfoLabel.setText(e.getMessage());
            } catch (CannotFindPlayerInDatabaseException e) {
                wpcInfoLabel.setText(e.getMessage());
            }
            firstWPC.setDisable(true);
            secondWPC.setDisable(true);
            thirdWPC.setDisable(true);
            fourthWPC.setDisable(true);
        });
        if(reconnected) waitPlayGameAfterDisconnection(event);
    }


    /**
     * when all game parameters have been set the window changes scene; the scene that will raise up depends by
     * the players number.
     *
     * @param event is the event necessary to change scene
     */
    private void playGame(Event event) {
        Thread startGame = new Thread(() -> {
            while (!pocExtracted || !toolExtracted || !allWpcsPicked || !privateObjExtractd) {
                synchronized (cardWaiter){
                    try {
                        cardWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Platform.runLater(()-> chooseScene(event));
        });
        startGame.start();
    }

    /**
     *when all schemas have been chosen the window changes scene; the scene that will raise up depends by
     *the players number.
     *
     * @param event is the event necessary to change scene
     */
    private void waitPlayGameAfterDisconnection(Event event){
        System.out.println("bu");
        Thread playGame = new Thread(() -> {
            System.out.println("ehi");
            while (!allWpcsPicked) {
                synchronized (cardWaiter){
                    try {
                        cardWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Platform.runLater(()-> chooseScene(event));
        });
        playGame.start();
    }

    /**
     * Selects which scene must me raised up.
     *
     * @param event is the event necessary to change scene
     */
    private void chooseScene(Event event){
        switch (clientInfo.getGameNumPlayers()){
            case 1:
                changeSceneHandle(event, "/client/view/gui/fxml/SoloPlayerGameScene.fxml");
                break;
            case 2:
                changeSceneHandle(event, "/client/view/gui/fxml/TwoPlayersGameScene.fxml");
                break;
            case 3:
                changeSceneHandle(event, "/client/view/gui/fxml/ThreePlayersGameScene.fxml");
                break;
            case 4:
                changeSceneHandle(event, "/client/view/gui/fxml/FourPlayersGameScene.fxml");
                break;
        }
    }


    /**
     * Changes the scene in the window.
     *
     * @param event the event related to the desire of the player to change scene
     * @param path is the path of the next scene
     */
    private void changeSceneHandle(Event event, String path) {

        AnchorPane nextNode = new AnchorPane();
        try {
            nextNode = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) { ((Notification) arg).handle(this); }


    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
        findPlayersLabel.setVisible(false);
        numOfPlayersLabel.setVisible(false);
        startGameLabel.setVisible(true);
        gameStarted = true;
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        Platform.runLater(() -> {
            lab2.setText(notification.username + " é entrato in partita!");
            lab2.setLayoutX(100);
            lab2.setLayoutY(360);
        });
        synchronized (playerWaiter){ playerWaiter.notify(); }
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        userWpcs = notification.wpcsByUser.get(clientInfo.getUsername());
        areWpcExtracted = true;
        synchronized (wpcWaiter){ wpcWaiter.notify();}
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        colors = notification.colorsByUser.get(clientInfo.getUsername());
        privateObjExtractd = true;
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        String id = notification.wpc.getWpcID();
        String user = notification.username;
        if(user.equals(clientInfo.getUsername()))
            Platform.runLater(()-> wpcInfoLabel.setText("Hai scelta lo schema numero "+ id));
        else
            Platform.runLater(()-> wpcInfoLabel.setText(user + " ha scelto lo schema numero" + id));
        allWpcsPicked = clientInfo.allPlayersChooseWpc();
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        toolExtracted = true;
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        pocExtracted = true;
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(NewRoundNotification notification) {
    }

    @Override
    public void handle(NextTurnNotification notification) {
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {
    }

    @Override
    public void handle(DicePlacedNotification notification) {

    }


    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }

    @Override
    public void handle(ScoreNotification notification) {
    }

    @Override
    public void handle(ToolCardDicePlacedNotification toolCardDicePlacedNotification) {

    }

    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification toolCardExtractedDicesModifiedNotification) {

    }

    @Override
    public void handle(PlayerDisconnectedNotification playerDisconnectedNotification) {
        String user = playerDisconnectedNotification.username;
        if (!gameStarted) Platform.runLater(() -> wpcInfoLabel.setText(user + " é uscito dalla partita!"));
        else Platform.runLater(() -> wpcInfoLabel.setText(user + " si è disconnesso!"));
    }

    @Override
    public void handle(PlayerReconnectedNotification playerReconnectedNotification) {
        String user = playerReconnectedNotification.username;
        Platform.runLater(() -> wpcInfoLabel.setText(user + " si è riconnesso!"));

    }

    @Override
    public void handle(ForceDisconnectionNotification notification) {
        Platform.runLater(disconnectionButton::fire);
    }

    @Override
    public void handle(ForceStartGameNotification notification) {
        Platform.runLater(disconnectionButton::fire);
    }

}