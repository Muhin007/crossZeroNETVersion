package controller.game;

import controller.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Player;
import startGame.Game;

import java.io.IOException;

import static net.Connection.isServer;

public class GameController {

    public AnchorPane anchorPane;
    private GridPane gridPane;
    private Stage stage = new Stage();
    private static String buttonClick;
    private static Image imageX;
    public static Image imageO;
    private static Player player1;
    private static Player player2;
    private static MouseEvent mouseEvent;

    public boolean remoteClientDataReady = false;
    public boolean localClientDataReady = false;
    public static boolean remoteClientCanGo = false;

    private static int winPlayer = 0;
    private Button[][] buttons;
    public boolean flag = false;

    private static Game game;

    public static void newGame() {
        game = new Game();
    }

    public static Game getGame() {
        return game;
    }

    public static String getButtonClick() {
        return buttonClick;
    }

    public static int getWinPlayer() {
        return winPlayer;
    }

    public void goGame(String gameName) {
        newGame();
        game = getGame();
        winPlayer = 0;
        int size = MenuController.getSize();
        String x = "/resource/x.png";
        imageX = new Image(getClass().getResourceAsStream(x));
        String y = "/resource/o.png";
        imageO = new Image(getClass().getResourceAsStream(y));
        buttons = new Button[size][size];
        gridPane = new GridPane();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new Button("");
                buttons[i][j].setId(i + " " + j);
                buttons[i][j].setFocusTraversable(false);
                buttons[i][j].setPrefSize(700 / size, 700 / size);
                buttons[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::btnClick);
                gridPane.add(buttons[i][j], i, j);
            }
        }
        Scene scene = new Scene(gridPane, 700, 700);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(gameName);
        stage.getIcons().add(new Image("/resource/logo_XO.png"));
        stage.setResizable(false);
        stage.show();
    }

    private void btnClick(MouseEvent mouseEvent) {
        GameController.mouseEvent = mouseEvent;
        game = getGame();
        player1 = game.getPlayer1();
        player2 = game.getPlayer2();
        buttonClick = mouseEvent.getSource().toString();
        flag = false;
        if (MenuController.gameWithAI) {
            gameHumanVSAi();
        }

        if (!isServer) {
            gameRemoteClientVSLocalClient();
        }
        if (isServer) {
            gameLocalClientVSRemoteClient();
        }
    }

    public void stepPlayer(Player player, Image image) {

        if (!game.getMapGame().isEmptyMap()) {
            showWin(mouseEvent);
            flag = true;
            return;
        }
        player.step();
        if (player.getLastStepX() == -1) return;
        buttonDrow(player, image);
        if (game.getMapGame().isWin(player.getDot())) {
            if (player == player1) {
                winPlayer = 1;
            }
            if (player == player2) {
                winPlayer = 2;
            }
            showWin(mouseEvent);
            flag = true;
        }
    }

    private void buttonDrow(Player player, Image image) {
        buttons[player.getLastStepX()][player.getLastStepY()].graphicProperty().setValue(new ImageView(image));
    }

    private void showWin(MouseEvent mouseEvent) {
        try {
            gridPane.getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent load = FXMLLoader.load(getClass().getResource("/view/EndGame.fxml"));
            stage.setTitle("Крестики-нолики");
            stage.getIcons().add(new Image("/resource/logo_XO.png"));
            stage.setResizable(false);
            stage.setScene(new Scene(load));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gameHumanVSAi() {
        stepPlayer(player1, imageX);
        if (!flag) {
            stepPlayer(player2, imageO);
        }
    }

    private void gameRemoteClientVSLocalClient() {
        remoteClientDataReady = true;
        stepPlayer(player1, imageX);
        remoteClientDataReady = false;
        localClientDataReady = true;
        remoteClientCanGo = false;
        if (!flag) {
            remoteClientDataReady = true;
            remoteClientCanGo = true;
            localClientDataReady = false;
        }
    }

    private void gameLocalClientVSRemoteClient() {
        localClientDataReady = true;
        if (!flag) {
            stepPlayer(player1, imageX);
            localClientDataReady = true;
            remoteClientDataReady = false;
            remoteClientCanGo = false;
        }
    }
}
