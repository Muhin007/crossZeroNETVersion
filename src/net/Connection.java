package net;

import controller.game.Alert;
import javafx.application.Platform;

import static net.RemoteClient.isRemoteClient;

public class Connection extends Thread {
    private LocalClient localClient = LocalClient.getInstance();
    private RemoteClient remoteClient = RemoteClient.getInstance();
    public static boolean modeReady = false;
    public static boolean isServer = false;

    @Override

    public void run() {

        while (!modeReady) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Platform.runLater(() -> Alert.showAlert("нет соединения с сетью"));
            }
        }
        if (!isServer) {
            while (!isRemoteClient) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Platform.runLater(() -> Alert.showAlert("нет соединения с сервером"));
                }
            }

            while (true) {
                remoteClient.transferDataRemoteClient();
            }
        } else {
            localClient.transferDataLocalClient();
        }
    }
}
