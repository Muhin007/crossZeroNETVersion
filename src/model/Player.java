package model;

abstract public class Player {
    static int lastStepX = -1, lastStepY = -1;

    public int getLastStepX() {
        return lastStepX;
    }

    public int getLastStepY() {
        return lastStepY;
    }

    abstract public char getDot();

    abstract public  void step();
}
