package startGame;

import controller.MenuController;
import model.*;
import net.Connection;

public class Game {
    private Player player1;
    private Player player2;
    private Map mapGame;


    public Game() {
        mapGame = new Map();
        if (MenuController.gameWithAI) {
            player1 = new Human('X');
            player2 = new AI('O', 'X');
        }
        if (Connection.modeReady){
            player1 = new Human('X');
            player2 = new NetPlayer('O');
        }
        if(Connection.isServer){
            player1 = new Human('X');
            player2 = new NetPlayer('O');
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Map getMapGame() {
        return mapGame;
    }
}

