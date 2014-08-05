package com.jerhis.cloudgame.game;

public class TileSuper extends Tile {

	public TileSuper(int x, int y) {
		super(x, y, 0, Tile.Type.Super);
	}

	public float dropStage = 0.08f;

	@Override
	public void update(float delta) {
		
		if (bit > 0) {
			dropStage -= delta;
			if (dropStage <= 0) {
				dropStage = 0.08f;
				bit--;
			}
		}

	}

	@Override
	public boolean collision(Guy guy, CollisionType Ctype) {
		
		if (guy.velY < 0 && (Ctype == CollisionType.TOP ||
				Ctype == CollisionType.UL && (leftTile == null || leftTile instanceof TileScenery) ||
				Ctype == CollisionType.UR && (rightTile == null || rightTile instanceof TileScenery))) {
			super.continuousJumpCollision(guy, guy.SUPER_JUMP);
            guy.lightning = guy.LIGHTNING_MAX*1.5f;
			type = Tile.Type.becomeScenery;
			return true;
		}
		return false;
	}

	@Override
	public void setNeighborScore(int score) {
		neighborScore = score;
	}

}