package com.jerhis.cloudgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.jerhis.cloudgame.game.*;
import com.jerhis.cloudgame.game.Tile.CollisionType;

public class CloudGame {
	
	final MyGdxGame game;
	int gameType = 0; // there are no unique types yet
	float score;
	//float bgOffSet = 0, bg1 = 0, bg2 = 0;
	float totalTime, height, lastDelta = 1.0f/60;
	boolean right, left;
    final float BG_SPEED = 0.5f;

	ArrayList<Tile> tiles = new ArrayList<Tile>();
	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    ArrayList<Background> backgrounds = new ArrayList<Background>();
	Guy guy;
	ControllerOfTiles controllerOfTiles = new ControllerOfTiles(tiles,gameType,gameObjects,backgrounds);
	
	public CloudGame(MyGdxGame gam) {
		game = gam;
		clear();
	}
	
	public GameScreen.State update(float delta) {
        lastDelta = delta;
        delta = 1/60.0f;

		if (game.tiltControls) 
			game.accelY = Gdx.input.getAccelerometerY();
		
		totalTime += delta;
		//bgOffSet = ((totalTime * guy.OFFSETSPEED) % 840) - 40;
		//bg1 = (int)((totalTime * 2 * 10) % 1600 - 800);
		//bg2 = (int)((bg1 + 1600) % 1600 - 800);//
		
		if (game.tiltControls)
			guy.update(delta, game.accelX, game.accelY, game.accelZ, height, game.slider);
		else 
			guy.update(delta, right, left, height, game.slider);
		int buffer = 300;
		score = guy.y > height + buffer ? score + guy.y - height - buffer : score;
		height = guy.y > height + buffer ? guy.y - buffer : height;
		
		controllerOfTiles.updateCollisions(delta, guy);
		
		int cloudsRemoved = controllerOfTiles.updateTiles(delta, height);
		score += (cloudsRemoved)*(cloudsRemoved + 1) * 25; //combos are scored better
        if (cloudsRemoved >= 7) game.achievement(-7);
		controllerOfTiles.spawn(totalTime, height);
		
		controllerOfTiles.updateGameObjects(delta);
        controllerOfTiles.spawnBackground(height*BG_SPEED);
        controllerOfTiles.updateBackground(delta,height*BG_SPEED);

		if (!guy.dead)
			return GameScreen.State.Running;
		else {
            game.increaseGamesPlayed();
            return GameScreen.State.Finished;
        }
	}
	
	public void touchDown(int x, int y, int pointer) {
		right = false;
		left = false;
		if (x > 400)
			right = true;
		if (x < 400)
			left = true;
	}
	
	public void touchDragged(int x, int y, int pointer) {
		right = false;
		left = false;
		if (x >= 400) 
			right = true;
		if (x < 400)
			left = true;	
	}

	public void touchUp(int x, int y, int pointer) {
		right = false;
		left = false;
	}
	
	public void setMode(int type) {
		gameType = type;
	}
	
	public void clear() {
		game.highScore((int)score);
		score = 0;
		totalTime = 0;
		height = 0;
		right = false;
		left = false;
		guy = new Guy();
		controllerOfTiles.reset(gameType);
	}

    public void clearTutorial() {
        game.highScore((int)score);
        score = 0;
        totalTime = 0;
        height = 0;
        right = false;
        left = false;
        guy = new Guy();
        controllerOfTiles.reset(-1);
    }

}
