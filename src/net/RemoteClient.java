package net;

import controller.MenuController;
import controller.game.Alert;
import controller.game.GameController;
import javafx.application.Platform;
import startGame.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RemoteClient extends Thread {
    private GameController gameController = new GameController();
    private Game game;
    private static PrintWriter fromClientToServer = null;
    private static BufferedReader toClientFromServer = null;
    static boolean isRemoteClient = false;
    private volatile int x = -1, y = -1;

    private static volatile RemoteClient instance = null;

    private RemoteClient() {
    }

    public static RemoteClient getInstance() {
        if (instance == null) {
            synchronized (RemoteClient.class) {
                if (instance == null) {
                    instance = new RemoteClient();
                }
            }
        }
        return instance;
    }

    private void setX(int x) {
        this.x = x;
    }

    private void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void connectionRemoteClient(InetAddress ia) {
        try {
            Socket remoteClientSocket = new Socket(ia, MenuController.PORT);
            fromClientToServer = new PrintWriter(remoteClientSocket.getOutputStream(), true);
            toClientFromServer = new BufferedReader(new InputStreamReader(remoteClientSocket.getInputStream()));
            isRemoteClient = true;
            Platform.runLater(() -> gameController.goGame("Удаленный клиент"));
            System.out.println("Удаленный клиент запустил игру");
            Platform.runLater(() -> Alert.showAlert("Ваш ход"));

        } catch (UnknownHostException e) {
            Platform.runLater(() -> Alert.showAlert("Хост с таким адресом не доступен: " + ia.getHostAddress()));
            System.exit(1);
        } catch (SocketException e) {
            Platform.runLater(() -> Alert.showAlert("Сбой связи с сервером"));
            System.exit(1);
        } catch (IOException e) {
            Platform.runLater(() -> Alert.showAlert("Не могу подключится к хосту: " + ia.getHostAddress()));
            System.exit(1);
        }
    }

    public void transferDataRemoteClient() {
        try {
            while (true) {
                if (gameController.remoteClientDataReady && !gameController.flag) {
                    String outputLine;
                    outputLine = "";
                    outputLine = outputLine.concat(TransferStep.getX());
                    outputLine = outputLine.concat(" ");
                    outputLine = outputLine.concat(TransferStep.getY());
                    RemoteClient.fromClientToServer.println(outputLine);
                    break;
                } else {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Platform.runLater(() -> Alert.showAlert("Произошел сбой связи со вторым игроком"));
                    }
                }
            }

            String inputLine;
            int tempX, tempY;
            while (toClientFromServer != null && !gameController.flag) {
                game = GameController.getGame();
                inputLine = toClientFromServer.readLine();
                if (inputLine == null) {
                    System.exit(0);
                } else {
                    String[] coordinates = inputLine.split(" ");
                    tempX = Integer.parseInt(coordinates[0]);
                    setX(tempX);
                    tempY = Integer.parseInt(coordinates[1]);
                    setY(tempY);
                    System.out.println(" ");
                    Platform.runLater(() -> gameController.stepPlayer(game.getPlayer2(), GameController.imageO));
//                Connection.remoteClientCanGo = true;
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.runLater(() -> Alert.showAlert("Произошел сбой связи со вторым игроком"));
        }
    }
}
