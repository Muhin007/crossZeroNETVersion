package model;

import controller.MenuController;

public class Map {
    private char[][] dotsMap;
    private final char E_DOT = '-';
    private int SIZE;
    private int DOTSWIN;

    public Map() {
        SIZE = MenuController.getSize();
        DOTSWIN = MenuController.getDotsToWin();
        createMap();
    }

    int getSIZE() {
        return SIZE;
    }

    private void createMap() {
        dotsMap = new char[SIZE][SIZE];
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                dotsMap[x][y] = E_DOT;
            }
        }
    }

    public boolean isDotChar(int x, int y, char ch) {
        return (y >= 0) && (y <= SIZE) && (x >= 0) && (x <= SIZE) && dotsMap[x][y] == ch;
    }

    public boolean isEmptyMap() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (dotsMap[x][y] == E_DOT) return true;
            }
        }
        return false;
    }

    public void setDotMap(int x, int y, char dot) {
        if ((y >= 0) && (y <= SIZE) && (x >= 0) && (x <= SIZE)) {
            dotsMap[x][y] = dot;
        }
    }

    public boolean isWin(char dot) {
        int xStep = Player.lastStepX;
        int yStep = Player.lastStepY;
        int countDot = 0;
        for (int x = (DOTSWIN - 1) * -1; x < DOTSWIN; x++) {
            if (xStep + x < 0 || xStep + x > SIZE - 1) continue;
            if (isDotChar(xStep + x, yStep, dot)) countDot++;
            else countDot = 0;
        }
        if (countDot >= DOTSWIN) return true;
        else countDot = 0;

        for (int y = (DOTSWIN - 1) * -1; y < DOTSWIN; y++) {
            if (yStep + y < 0 || yStep + y > SIZE - 1) continue;
            if (isDotChar(xStep, yStep + y, dot)) countDot++;
            else countDot = 0;
        }
        if (countDot >= DOTSWIN) return true;
        else countDot = 0;

        for (int xy = (DOTSWIN - 1) * -1; xy < DOTSWIN; xy++) {
            if (xStep + xy < 0 || xStep + xy > SIZE - 1 || yStep + xy < 0 || yStep + xy > SIZE - 1)
                continue;
            if (isDotChar(xStep + xy, yStep + xy, dot)) countDot++;
            else countDot = 0;
        }
        if (countDot >= DOTSWIN) return true;
        else countDot = 0;

        for (int y = (DOTSWIN - 1) * -1, x = (DOTSWIN - 1); y < DOTSWIN; y++, x--) {
            if (xStep + x < 0 || xStep + x > SIZE - 1 || yStep + y < 0 || yStep + y > SIZE - 1)
                continue; //
            if (isDotChar(xStep + x, yStep + y, dot)) countDot++;
            else countDot = 0;
        }
        return countDot >= DOTSWIN;
    }
}

