package model;

import controller.game.GameController;
import startGame.Game;

public class Human extends Player {
    private char dot;

    public Human(char dot) {
        this.dot = dot;
    }

    @Override
    public char getDot() {
        return dot;
    }

    public void step() {
        Game game = GameController.getGame();
        int stepTempX = -1, stepTempY = -1;
        lastStepX = -1;
        lastStepY = -1;

        char x = (GameController.getButtonClick().charAt(10));
        switch ( x ) {
            case '0':
                stepTempX = 0;
                break;
            case '1':
                stepTempX = 1;
                break;
            case '2':
                stepTempX = 2;
                break;
            case '3':
                stepTempX = 3;
                break;
            case '4':
                stepTempX = 4;
                break;
            case '5':
                stepTempX = 5;
                break;
            case '6':
                stepTempX = 6;
                break;
        }

        char y = GameController.getButtonClick().charAt(12);
        switch ( y ) {
            case '0':
                stepTempY = 0;
                break;
            case '1':
                stepTempY = 1;
                break;
            case '2':
                stepTempY = 2;
                break;
            case '3':
                stepTempY = 3;
                break;
            case '4':
                stepTempY = 4;
                break;
            case '5':
                stepTempY = 5;
                break;
            case '6':
                stepTempY = 6;
                break;
        }

        if (game.getMapGame().isDotChar(stepTempX, stepTempY, '-')) {
            lastStepX = stepTempX;
            lastStepY = stepTempY;
            game.getMapGame().setDotMap(lastStepX, lastStepY, dot);
        }
    }
}
