package model;

import controller.MenuController;
import controller.game.GameController;
import startGame.Game;

import java.util.Random;

public class AI extends Player {
    private Random rand = new Random();
    private char X_DOT;
    private char O_DOT;
    private int DOTSWIN;
    private int SIZE;
    private Game game;

    public AI(char dot, char dotAnotherPlayer) {
        this.O_DOT = dot;
        this.X_DOT = dotAnotherPlayer;
        DOTSWIN = MenuController.getDotsToWin();
    }

    public char getDot() {
        return O_DOT;
    }

    public void step() {
        this.game = GameController.getGame();
        SIZE = game.getMapGame().getSIZE();
        aiBrain();
        game.getMapGame().setDotMap(lastStepX, lastStepY, O_DOT);
    }

    private void aiBrain() {
        if (game.getMapGame().isDotChar(SIZE / 2, SIZE / 2, '-')) {
            lastStepX = SIZE / 2;
            lastStepY = SIZE / 2;
            return;
        }
        if (isBlock(O_DOT)) {
            return;
        }

        if (!isBlock(X_DOT)) {
            do {
                lastStepX = rand.nextInt(SIZE);
                lastStepY = rand.nextInt(SIZE);
            } while (!game.getMapGame().isDotChar(lastStepX, lastStepY, '-'));
        }
    }

    private boolean isBlock(char dot) {
        int countDot = 0;
        for (int x = (DOTSWIN - 1) * -1; x < DOTSWIN; x++) {
            if (lastStepX + x < 0 || lastStepX + x > SIZE - 2) continue;
            if (game.getMapGame().isDotChar(lastStepX + x, lastStepY, dot)) {
                countDot++;
                if (countDot == DOTSWIN - 1 && game.getMapGame().isDotChar(lastStepX + x + 1, lastStepY, '-')) {
                    lastStepX = lastStepX + x + 1;
                    return true;
                }
            } else countDot = 0;
        }
        countDot = 0;

        for (int y = (DOTSWIN - 1) * -1; y < DOTSWIN; y++) {
            if (lastStepY + y < 0 || lastStepY + y > SIZE - 2) continue;
            if (game.getMapGame().isDotChar(lastStepX, lastStepY + y, dot)) {
                countDot++;
                if (countDot == DOTSWIN - 1 && game.getMapGame().isDotChar(lastStepX, lastStepY + y + 1, '-')) {
                    lastStepY = lastStepY + y + 1;
                    return true;
                }
            } else countDot = 0;
        }
        countDot = 0;

        for (int xy = (DOTSWIN - 1) * -1; xy < DOTSWIN; xy++) {
            if (lastStepX + xy < 0 || lastStepX + xy > SIZE - 2 || lastStepY + xy < 0 || lastStepY + xy > SIZE - 2)
                continue;
            if (game.getMapGame().isDotChar(lastStepX + xy, lastStepY + xy, dot)) {
                countDot++;
                if (countDot == DOTSWIN - 1 && game.getMapGame().isDotChar(lastStepX + xy + 1, lastStepY + xy + 1, '-')) {
                    lastStepX += xy + 1;
                    lastStepY += xy + 1;
                    return true;
                }
            } else countDot = 0;
        }
        countDot = 0;

        for (int y = (DOTSWIN - 1) * -1, x = (DOTSWIN - 1); y < DOTSWIN; y++, x--) {
            if (lastStepX + x < 0 || lastStepX + x > SIZE - 2 || lastStepY + y < 0 || lastStepY + y > SIZE - 2)
                continue;
            if (game.getMapGame().isDotChar(lastStepX + x, lastStepY + y, dot)) {
                countDot++;
                if (countDot == DOTSWIN - 1 && game.getMapGame().isDotChar(lastStepX + x + 1, lastStepY + y + 1, '-')) {
                    lastStepX += x + 1;
                    lastStepY += y + 1;
                    return true;
                }
            } else countDot = 0;
        }
        return false;
    }
}
