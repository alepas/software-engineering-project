package it.polimi.ingsw.control.guicontroller;


import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.constants.NetworkConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;


public class StartController {

    @FXML
    private Button rmiButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Button socketButton;

    @FXML
    private Label errorLabel;

    public void initialize() {
        //if the socket button is selected the event tries to open a socket connection
        rmiButton.setOnAction(event -> {
            try {
                NetworkClient.getNewRmiInstance();
            } catch (Exception e) {
                errorLabel.setText("Impossibile stabilire connessione RMI con il Server.");
            }
            changeButton();
        });

        //if the socket button is selected the event tries to open a socket connection
        socketButton.setOnAction(event -> {
            try {
                startSocketClient();
            } catch (ConnectException e) {
                errorLabel.setText("Impossibile stabilire connessione Socket con il Server.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeButton();
        });

        signUpButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/SignUpScene.fxml"));

        signInButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/SignInScene.fxml"));

    }

    /**
     * Starts a socket connection.
     *
     * @throws IOException in case it is not possible to start a socket connection
     */
    private static void startSocketClient() throws IOException {
        NetworkClient networkClient = NetworkClient.getNewSocketInstance(NetworkConstants.SERVER_ADDRESS,
                NetworkConstants.SOCKET_SERVER_PORT);

        ((SocketClient) networkClient).init();

    }

    /**
     * Sets visible the sign in/up button and invisible the rmi/socket button
     */
    private void changeButton(){
        rmiButton.setVisible(false);
        socketButton.setVisible(false);
        signInButton.setVisible(true);
        signUpButton.setVisible(true);
    }


    /**
     * Changes the scene in the window.
     *
     * @param event the event related to the desire of the player to change scene
     * @param path is the path of the next scene
     */
    private void changeSceneHandle(ActionEvent event, String path) {
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
}
