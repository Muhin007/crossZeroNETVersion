package net;

import controller.game.GameController;

public class TransferStep {

    public static String getX() {
        return String.valueOf(GameController.getButtonClick().charAt(10));
    }

    public static String getY() {
        return String.valueOf(GameController.getButtonClick().charAt(12));
    }
}
