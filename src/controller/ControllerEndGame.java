package controller;

import controller.game.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ControllerEndGame {
    @FXML
    private Button btnWin;

    @FXML
    public void initialize() {
        if (GameController.getWinPlayer() == 0)
            btnWin.setText("Ничья");
        if (GameController.getWinPlayer() == 1)
            btnWin.setText("X WIN");
        if (GameController.getWinPlayer() == 2)
            btnWin.setText("O WIN");
    }

    private MenuController menuController = new MenuController();

    public void clickOnWin(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        GameController.newGame();
        menuController.gameSettingForm();
        stage.close();
    }


}

