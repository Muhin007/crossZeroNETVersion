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
import java.net.Socket;

public class LocalClient extends Thread {
    private GameController gameController = new GameController();
    private Game game;
    private volatile int x = -1, y = -1;
    private static volatile LocalClient instance = null;

    private LocalClient() {
    }

    public static LocalClient getInstance() {
        if (instance == null) {
            synchronized (LocalClient.class) {
                if (instance == null) {
                    instance = new LocalClient();
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

    public void transferDataLocalClient() {
        try (Socket clientSocket = MenuController.serverSocket.accept();
             PrintWriter fromServerToClient = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader toServerFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            Platform.runLater(() -> gameController.goGame("Локальный клиент"));
            System.out.println("локальный клиент запустил игру");
            Platform.runLater(() -> Alert.showAlert("Ждем хода противника."));

            while (true) {
                String inputLine;
                int tempX, tempY;
                while (!gameController.flag) {
                    game = GameController.getGame();
                    inputLine = toServerFromClient.readLine();
                    if (inputLine == null) {
                        System.exit(0);
                    } else {
                        String[] coordinates = inputLine.split(" ");
                        tempX = Integer.parseInt(coordinates[0]);
                        setX(tempX);
                        tempY = Integer.parseInt(coordinates[1]);
                        setY(tempY);
                        System.out.println();
                        Platform.runLater(() -> gameController.stepPlayer(game.getPlayer2(), GameController.imageO));
//                    Connection.localClientCanGo = true;
                        break;
                    }
                }
                while (true) {
                    if (gameController.localClientDataReady && !gameController.flag) {
                        String outputLine;
                        outputLine = "";
                        outputLine = outputLine.concat(TransferStep.getX());
                        outputLine = outputLine.concat(" ");
                        outputLine = outputLine.concat(TransferStep.getY());
                        fromServerToClient.println(outputLine);
                        break;
                    } else {
                        Thread.sleep(5);
                    }
                }

            }
        } catch (InterruptedException ex) {
            Platform.runLater(() -> Alert.showAlert("Произошел сбой"));
        } catch (IOException ioe) {
            Platform.runLater(() -> Alert.showAlert("Клиент отключился"));
            System.exit(1);
        }
    }
}
