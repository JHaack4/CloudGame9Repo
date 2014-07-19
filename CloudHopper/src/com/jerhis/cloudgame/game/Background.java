package com.jerhis.cloudgame.game;

public class Background {

    public int imageIndex;
    public int y, targetY;
    public boolean gone;

    public Background(int targetY, int imageIndex, float height) {
        gone = false;
       // this.targetX = targetX;
        this.targetY = targetY;
       // x = targetX - (int)height;
        y = targetY - (int)height;
        this.imageIndex = imageIndex;
    }

    public void update(float delta, float height) {
       // x = targetX - (int)height;
        y = targetY - (int)height;
        if (y < -260) gone = true;
    }
}
