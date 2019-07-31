package controller.game;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Alert {
    public static void showAlert(String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("ВНИМАНИЕ!");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resource/logo_XO.png"));
        alert.show();
    }
}
