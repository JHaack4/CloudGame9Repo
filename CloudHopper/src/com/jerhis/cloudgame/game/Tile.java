package com.jerhis.cloudgame.game;

import java.util.ArrayList;

public abstract class Tile {
	
	public int neighborScore;
	public int x, y;
	public int arrayX, arrayY;
	public Tile aboveTile, belowTile, rightTile, leftTile;
	public Type type;
	public final float DROP_TIME = 0.11f;
	public float bit; //bit stores other information
	//default: how much hits are left
	//scenery: orientation (based on neighbor score)
	
	public enum Type {
		Default, Removable, Scenery, becomeScenery, Super
	}
	
	public Tile(int x, int y, int bit, Type type) {
		arrayX = x;
		arrayY = y;
		this.x = x * 40 + 20;
		this.y = y * 40 + 20;
		this.bit = bit;
		neighborScore = 0;
		this.type = type;
	}
	
	public void initializeNeighbors(Tile at, Tile bt, Tile rt, Tile lt) {
		aboveTile = at;
		belowTile = bt;
		rightTile = rt;
		leftTile = lt;
	}
	
	public void continuousJumpCollision(Guy guy, float newVelocity)
    {
        if (guy.velY < 0) {
            guy.velY = newVelocity;
            guy.jumping = true;
        }
    }

    public CollisionType checkForCollision(Guy guy)
    {
        int x = (int) guy.x - this.x;
        int y = (int) guy.y - this.y;

        if (Math.abs(y) >= 30 || Math.abs(x) >= 30)
            return CollisionType.NONE;
        int grid = (x + 40) / 20 + 4 * ((y + 40) / 20);
        //System.out.println(grid + " x " + x + " y " + y);
        switch (grid) {
        case 0: return CollisionType.DL;
        case 3: return CollisionType.DR;
        case 12: return CollisionType.UL;
        case 15: return CollisionType.UR;
        case 1: case 2: return CollisionType.BOTTOM;
        case 8: case 4: return CollisionType.LEFT;
        case 7: case 11: return CollisionType.RIGHT;
        case 13: case 14: return CollisionType.TOP;
        case 9: case 10: case 5: case 6: return CollisionType.IN;
        }

        return CollisionType.NONE;
    }
	
    public abstract void update(float delta);
    public abstract void setNeighborScore(int score);
    public abstract boolean collision(Guy guy, CollisionType type);
    
	public enum CollisionType {
        TOP, LEFT, RIGHT, BOTTOM, UR, UL, DR, DL, IN, NONE
    }

}
