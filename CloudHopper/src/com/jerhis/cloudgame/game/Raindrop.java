package com.jerhis.cloudgame.game;

public class Raindrop extends GameObject{
	
	public float x, y, yVel, xVel, rotation;
	
	public Raindrop(float xx, float yy) {
		super(GameObject.ObjectType.Raindrop);
		x=xx;
		y=yy;
		yVel=0;
        xVel=0;
	}

    public Raindrop(float xx, float yy, int q) {
        super(GameObject.ObjectType.Raindrop);
        x=xx;
        y=yy;
        yVel = (float) Math.random() + 0.01f;
        xVel = (float) Math.random() - 0.5f + q;
    }
	
	public void update(float delta) {
		yVel += delta * 10;
		y -= yVel;
        x += xVel;
        rotation = (float)Math.atan2(xVel, yVel);
		if (y<-40) gone = true;
	}
	
}
