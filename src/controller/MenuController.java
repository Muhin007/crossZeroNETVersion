package controller;

import controller.game.Alert;
import controller.game.GameController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.Connection;
import net.RemoteClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class MenuController {


    public ToggleGroup net;
    public Button newGame;
    private RemoteClient remoteClient = RemoteClient.getInstance();
    private Connection connection = new Connection();
    private GameController gameController = new GameController();
    public static final int PORT = 8189;
    private static int size = 3;
    public static boolean gameWithAI = false;
    public static ServerSocket serverSocket = null;
    public BorderPane mainForm;
    public BorderPane menuScreen;
    public ToggleGroup enemy;
    public Button minusSize;
    public Button plusSize, selectSize, startGame, setIP;
    public Label counter, changeSize, serverIP;
    public Pane netPane, playerPane, sizePane;
    public RadioButton human, ai, serverButton, clientButton;
    public TextField insertIP;

    public static int getSize() {
        return size;
    }

    public static int getDotsToWin() {
        return getSize();
    }

    public void startMenu() {
        mainForm.getScene().getWindow().hide();
        gameSettingForm();
    }

    public void gameSettingForm() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settingForm.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Крестики-нолики");
        stage.getIcons().add(new Image("/resource/logo_XO.png"));
        assert root != null;
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void gameOver() {
        Platform.exit();
    }


    public void showNetSetting() {
        ai.setVisible(false);
        netPane.setVisible(true);
        sizePane.setVisible(false);
        startGame.setVisible(false);
        new Thread(connection).start();
    }

    public void getServerIP() {
        clientButton.setVisible(false);
        String thisIp = null;
        try {
            thisIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Connection.isServer = true;
        Connection.modeReady = true;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        menuScreen.getScene().getWindow().hide();
        String finalThisIp = thisIp;
        Platform.runLater(() -> Alert.showAlert("Ваш IP: " + finalThisIp));
    }

    public void getClientConnection() {
        serverButton.setVisible(false);
        insertIP.setVisible(true);
        insertIP.setEditable(true);
        setIP.setVisible(true);
        Connection.isServer = false;
        Connection.modeReady = true;
        GameController.remoteClientCanGo = true;
    }

    public void setIP() throws UnknownHostException {
        InetAddress ia = InetAddress.getByName(insertIP.getText());
        remoteClient.connectionRemoteClient(ia);
        menuScreen.getScene().getWindow().hide();
    }

    public void deleteTextInTextField() {
        if (insertIP.getText().equalsIgnoreCase("Введите IP")) {
            insertIP.setText("");
        }
    }

    public int plusSize() {
        if (size >= 7) {
            counter.setText("недопустимое значение");
        } else {
            size++;
            counter.setText(String.valueOf(size));
        }
        return size;
    }

    public int minusSize() {
        if (size <= 3) {
            counter.setText("недопустимое значение");
        } else {
            size--;
            counter.setText(String.valueOf(size));
        }
        return size;
    }

    public void showSize() {
        changeSize.setText("Размер поля = " + size);
        startGame.setDisable(false);
    }

    public void startGame() {
        gameWithAI = true;
        size = getSize();
        menuScreen.getScene().getWindow().hide();
        gameController.goGame("игра с компьютером");
    }
}

