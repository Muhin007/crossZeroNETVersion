package model;

import controller.game.GameController;
import net.LocalClient;
import net.RemoteClient;
import startGame.Game;

import static net.Connection.isServer;

public class NetPlayer extends Player {
    private LocalClient localClient = LocalClient.getInstance();
    private RemoteClient remoteClient = RemoteClient.getInstance();
    private char dot;

    public NetPlayer(char dot) {
        this.dot = dot;
    }

    public char getDot() {
        return dot;
    }

    public void step() {
        Game game = GameController.getGame();
        if (!isServer) {
                lastStepX = remoteClient.getX();
                lastStepY = remoteClient.getY();

        } else {
                lastStepX = localClient.getX();
                lastStepY = localClient.getY();
        }
        game.getMapGame().setDotMap(lastStepX, lastStepY, dot);
    }
}





