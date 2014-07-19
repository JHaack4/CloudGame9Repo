package com.jerhis.cloudgame.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.jerhis.cloudgame.GameScreen;
import com.jerhis.cloudgame.game.Tile.CollisionType;
import com.jerhis.cloudgame.game.Tile.Type;

public class ControllerOfTiles {
	
	public ArrayList<Tile> tiles;
	public int gameType;
    Random random;
	ArrayList<GameObject> gameObjects;
    ArrayList<Background> backgrounds;
	public int spawnHeight, backgroundSpawnHeight; //height where clouds first spawn
	
	public ControllerOfTiles(ArrayList<Tile> tilesDraw, int gT, ArrayList<GameObject> gos, ArrayList<Background> bgss) {
		tiles = tilesDraw;
		gameObjects = gos;
        backgrounds = bgss;
		reset(gT);
	}
	
	public void reset(int gT) {
		spawnHeight = 40*12;
        backgroundSpawnHeight = 0;
		gameObjects.clear();
		tiles.clear();
        backgrounds.clear();
		gameType = gT;
        if (gT == -1)
            random = new Random(15994);
        else
            random = new Random((int)(Math.random()*1000000000));
		spawnStart();
        startSpawnBackground();
	}
	
	public int updateTiles(float delta, float height) {
		int cloudsRemoved = 0;
		for (int k = 0; k < tiles.size(); k++) {       
			Tile t = tiles.get(k);                         //for each tile t
			if (t.y < height - 40) {                       //if it falls too low, remove it
				removeTile(t);
				k--;
			}
			else if (t.type == Tile.Type.Removable) {      //if it is removable (scenery), remove it
				removeTile(t);
				k--;
			}
			else if (t.type == Tile.Type.becomeScenery) {  //if it is becoming scenery
				int x1 = t.arrayX, y1 = t.arrayY;          
				removeTile(t);                                   //remove it
				addTile(x1,y1,scen);                             //add a scenery there
				spawnRaindrop(t.x, t.y);       //add a raindrop there
				k--;
				cloudsRemoved += 1;
			}
			else {                                          //otherwise, update it
				t.update(delta); 
			}
		}
		return cloudsRemoved;
	}

    public void spawnRaindrop(int x, int y) {
        int d = 17;
        for (int q = -1; q <= 1; q += 1)
            for (int qq = -1; qq <= 1; qq += 1)
                gameObjects.add(new Raindrop(x + q*d, y + qq*d, q));
    }
	
	public void updateCollisions(float delta, Guy guy) {
		for (Tile t: getAdjacentTiles((int)guy.x, (int)guy.y)) {
			CollisionType type = t.checkForCollision(guy);
			if (type != CollisionType.NONE) {
				boolean didHitNeighbors = t.collision(guy, type);
				if (didHitNeighbors) {
					hitNeighbors(t.arrayX,t.arrayY, t instanceof TileSuper);
				}
			}
		}
	}
	
	private void hitNeighbors(int arrayX, int arrayY, boolean lightning) {
        GameScreen.playSound(GameScreen.soundDrop);
        //if (lightning) gameObjects.add(new Lightning(new Vector2(arrayX * 40, arrayY * 40), new Vector2(arrayX * 40, arrayY * 40 - 300)));
		
		int[][] damageValues = new int[][] { //how blue do the neighbor tiles get?
		 	{0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		 	{0, 0, 0, 0, 8, 0, 0, 0, 0},
		 	{0, 0, 8,16,20,16, 8, 0, 0},
		 	{0, 8,16,24, 0,24,16, 8, 0},
		 	{0, 0,14,20,26,20,14, 0, 0},
		 	{0, 0, 0,12,20,12, 0, 0, 0},
		 	{0, 0, 0, 0,12, 0, 0, 0, 0},
		 	{0, 0, 0, 0, 0, 0, 0, 0, 0} };
		
		for (Tile t: tiles) {
			if (t instanceof TileScenery) continue;
			int difX = t.arrayX - arrayX + 4;
			int difY = arrayY - t.arrayY + 3;
			if (difX >= 0 && difY >= 0 && difX < 9 && difY < 8)
				t.bit += damageValues[difY][difX];
			if (t.bit > 30) t.type = Type.becomeScenery;
		}
	}

	private ArrayList<Tile> getAdjacentTiles(int x, int y)
    {
        ArrayList<Tile> closeTiles = new ArrayList<Tile>();
        
        for (Tile t: tiles) {
        	if (Math.sqrt((t.x - x)*(t.x-x) + (t.y-y)*(t.y-y)) < 75)
        		closeTiles.add(t);
        }

        return closeTiles;
    }
	
	public void updateGameObjects(float delta) {
		for (int k = 0; k < gameObjects.size(); k++) {
			GameObject go = gameObjects.get(k);
			go.update(delta);
			if (go.gone) {
				gameObjects.remove(k);
				k--;
			}
		}
	}

    public void startSpawnBackground() {
        spawnBackground(0);
        spawnBackground(0);
    }

    public void spawnBackground(float height) {
        if (height + 515 > backgroundSpawnHeight) {
            int bgGap = 256;
            //int[] bgs = new int[] {4};
            //int index = bgs[ Math.min(backgroundSpawnHeight/bgGap, bgs.length - 1) ];
            backgrounds.add(new Background(backgroundSpawnHeight,-1,height));
            backgroundSpawnHeight += bgGap;
        }
    }

    public void updateBackground(float delta, float height) {
        for (int k = 0; k < backgrounds.size(); k++) {
            Background bg = backgrounds.get(k);
            bg.update(delta,height);
            if (bg.gone) {
                backgrounds.remove(k);
                k--;
            }
        }
    }
	
	//  0 is end, 9-10 is middle, 19 is end
	private void spawnStart() {
		switch (gameType) {
		
		}
		spawnCloud(10,1,0,0); //spawns base cloud
		spawnCloud(rng(7,10),6,rng(1,2),rng(2,3)); //spawns first clouds
		spawnCloud(rng(9,12),6,rng(1,2),rng(2,3));
		//spawnCloud(10,6,16,0);
	}
	
	public void spawn(float totalTime, float height) {
		
		int section = 0;
		if (height > 1000) section = 1;
		if (height > 2000) section = 2;
		if (height > 3000) section = 3;
		if (height > 4000) section = 4;
		if (height > 5000) section = 5;
		if (height > 6000) section = 6;
		if (height > 7000) section = 7;
		
		if (height + 480 > spawnHeight) {
			int numClouds = rng(NUM_CLOUDS[section]);
			while (numClouds > 0) {
				int y = spawnHeight/40;
				int cloudType = rng(CLOUD_TYPE[section]);
				int supers = rng(SUPERS[section]);
				int x = rng(XLOCATION[section]);
				spawnCloud(x, y, cloudType, supers);
				numClouds--;
			}
			spawnHeight += 40 * rng(INTERVALS[section]);
		}
		
	}
	
	private void spawnCloud(int x, int y, int cloudShape, int supers) {
		float[][] cloud;
		boolean flip = cloudShape != 0 && random.nextDouble() < 0.5;
		cloud = getCloudShape(cloudShape);
		
		while (supers > 0) {
			boolean stillNeed = true;
			while (stillNeed) {
				int randA = rng(1,4);
				int randB = rng(1,7);
				if (cloud[randA][randB] > 0) 
					if (random.nextDouble() < cloud[randA][randB]) {
						cloud[randA][randB] = SUPE;
						supers--;
						stillNeed = false;
					}
			}
		}
		
		for (int qY = 0; qY < 6; qY++)
			for (int qX = 0; qX < 9; qX++) {
				int k = flip ? -qX + 4 : qX - 4;
				addTile(x + k, y + 4 - qY, (int)cloud[qY][qX]);
			}
	}
	
	private void addTile(int x, int y, int type) {
		if (type == b___) return;
		
		Tile aboveTile = null, belowTile = null, leftTile = null, rightTile = null, newTile;
		
		//make our new tile, nt
		switch (type) {
		case scen: newTile = new TileScenery(x, y); break;
		case SUPE: newTile = new TileSuper(x, y); break;
		default: case 0: case 1:
		case regu: newTile = new TileBasic(x, y); break;
		}
		
		for (int k = 0; k < tiles.size(); k++) {
			Tile t = tiles.get(k);
			if (t.arrayX == x && t.arrayY == y) {
				if (t.type == Tile.Type.Scenery || t.type == Tile.Type.Removable) {
					tiles.remove(t);
					k--;
				}
				else return;
			}
			else if (t.arrayX == x + 1 && t.arrayY == y) 
				rightTile = t;
			else if (t.arrayX == x - 1 && t.arrayY == y) 
				leftTile = t;
			else if (t.arrayX == x && t.arrayY == y + 1) 
				aboveTile = t;
			else if (t.arrayX == x && t.arrayY == y - 1) 
				belowTile = t;
		}
		
		if (rightTile != null) {
			rightTile.leftTile = newTile;
			onNeighborUpdate(rightTile);
		}
		if (leftTile != null) {
			leftTile.rightTile = newTile;
			onNeighborUpdate(leftTile);
		}
		if (aboveTile != null) {
			aboveTile.belowTile = newTile;
			onNeighborUpdate(aboveTile);
		}
		if (belowTile != null) {
			belowTile.aboveTile = newTile;
			onNeighborUpdate(belowTile);
		}
		newTile.initializeNeighbors(aboveTile, belowTile, rightTile, leftTile);
		onNeighborUpdate(newTile);
		tiles.add(newTile);
	}
	
	private void removeTile(Tile t) {
		if (t.aboveTile != null) {
			t.aboveTile.belowTile = null;
			onNeighborUpdate(t.aboveTile);
		}
		if (t.belowTile != null) {
			t.belowTile.aboveTile = null;
			onNeighborUpdate(t.belowTile);
		}
		if (t.leftTile != null) {
			t.leftTile.rightTile = null;
			onNeighborUpdate(t.leftTile);
		}
		if (t.rightTile != null) {
			t.rightTile.leftTile = null;
			onNeighborUpdate(t.rightTile);
		}
		tiles.remove(t);
	}
	
	public void onNeighborUpdate(Tile t) {
		int score = 0;
			//  1
			// 8X2
			//  4
		if (t.aboveTile != null && t.aboveTile.type != Tile.Type.Removable && t.aboveTile.type != Tile.Type.Scenery) score += 1;
		if (t.belowTile != null && t.belowTile.type != Tile.Type.Removable && t.belowTile.type != Tile.Type.Scenery) score += 4;
		if (t.leftTile != null  && t.leftTile.type != Tile.Type.Removable && t.leftTile.type  != Tile.Type.Scenery) score += 8;
		if (t.rightTile != null && t.rightTile.type != Tile.Type.Removable && t.rightTile.type != Tile.Type.Scenery) score += 2;
		t.setNeighborScore(score);
	}
	
	//inclusive!
	private int rng(int a, int b) {
		return a + (int)(random.nextDouble() * (b-a+1));
	}
	private int rng(int[] a) {
		return a[rng(0,a.length-1)];
	}
		
	
	private final int[][] INTERVALS = 
		  { {6}, //space between the clouds
			{6},
			{6,7},
			{6,7},
			{6,7},
			{6,7},
			{7},
			{7}} ;

	private final int[][] NUM_CLOUDS = 
		   {{1,1,1,1,1,2,2,2,2,2,3,3,3,3}, //a number > 0 
			{1,1,1,1,1,1,2,2,2,2,2,3,3,3},
			{1,1,1,1,1,1,1,2,2,2,2,3,3,3},
			{1,1,1,1,1,1,1,1,2,2,2,2,3,3},
			{1,1,1,1,1,1,1,1,1,2,2,2,3,3},
			{1,1,1,1,1,1,1,1,1,1,2,2,2,3},
			{1,1,1,1,1,1,1,1,1,1,1,2,2,2},
			{1,1,1,1,1,1,1,1,1,1,1,1,2,2}} ;

	private final int[][] SUPERS =    
		  { {2,3,3},  //4 is maximum!
			{1,2,3},
			{1,2,2},
			{1,2,2},
			{1,2},
			{1,1,2},
			{1,1,1,2},
			{1,1,1,1,2}} ;

	private final int[][] XLOCATION = 
		  { {7,8,9,9,10,10,11,12}, //3 min, 16 max
			{6,7,8,9,9,10,10,11,12,13},
			{6,7,8,9,10,11,12,13},
			{5,6,7,8,8,9,10,11,11,12,13,14},
			{5,6,7,7,8,9,10,11,12,12,13,14},
			{5,6,6,7,8,9,10,11,12,13,13,14},
			{4,5,5,6,6,7,8,9,10,11,12,13,13,14,14,15},
			{4,4,4,4,5,6,7,8,9,10,11,12,13,14,15,15,15,15}} ;
	
	private final int[][] CLOUD_TYPE = //0st 1e 2em 3mh 4em 5mh 6em 7m 8m 9h 10e 11m 12mh 13mh 14sh 15m							
		  { {1,10,2,4,6},           //st: 0 ||| e: 1,10 ||| em: 2,4,6 ||| m: 7,8,11,15 ||| mh: 3,5,12,13 ||| h: 9,14
			{1,10,2,4,6,7,8,11,15},
			{1,10,2,4,6,7,8,11,15},
			{1,10,2,4,6,7,8,11,15,3,5,12,13},
			{1,10,2,4,6,7,8,11,15,3,5,12,13,9,14},
			{2,4,6,7,8,11,15,3,5,12,13,9,14},
			{2,4,6,7,8,11,15,3,5,12,13,9,14},
			{2,4,6,7,8,11,15,3,5,12,13,9,14,9,14,7,8,11,15,3,5,12,13,9,14}} ;
	
	
	private float[][] getCloudShape(int cloudShape) {
		switch (cloudShape) {
		case 0: return new float[][] { //starter
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,scen,scen,scen,scen,b___,b___,b___},
				{b___,scen,1.0f,SUPE,SUPE,1.0f,scen,b___,b___},
				{b___,b___,scen,scen,scen,scen,b___,b___,b___}};
		case 1: return new float[][] { //easy *0
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.1f,scen,b___,b___,b___},
				{b___,b___,scen,0.5f,1.0f,0.5f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		case 2: return new float[][] { //easy med *1
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.1f,scen,b___,b___,b___},
				{b___,b___,scen,0.2f,0.4f,scen,b___,b___,b___},
				{b___,b___,scen,0.4f,1.0f,0.3f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		case 3: return new float[][] { //medium hard *3
				{b___,b___,b___,scen,b___,b___,b___,b___,b___},
				{b___,b___,scen,0.1f,scen,b___,b___,b___,b___},
				{b___,scen,0.2f,0.4f,0.2f,scen,b___,b___,b___},
				{b___,scen,0.4f,1.0f,0.4f,scen,b___,b___,b___},
				{b___,b___,scen,1.0f,1.0f,0.3f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		case 4: return new float[][] { //easy med *1
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,scen,scen,b___,b___},
				{b___,b___,scen,0.1f,scen,0.1f,0.1f,scen,b___},
				{b___,scen,0.3f,scen,0.2f,scen,0.7f,0.3f,scen},
				{b___,b___,scen,1.0f,0.6f,1.0f,1.0f,scen,b___},
				{b___,b___,b___,scen,scen,scen,scen,b___,b___}};
		case 5: return new float[][] { //med hard *3
				{b___,b___,b___,b___,b___,b___,scen,b___,b___},
				{b___,b___,b___,b___,scen,scen,0.1f,scen,b___},
				{b___,b___,b___,scen,0.1f,0.2f,0.6f,0.3f,scen},
				{b___,b___,scen,0.3f,0.6f,scen,1.0f,scen,b___},
				{b___,b___,scen,0.6f,1.0f,1.0f,1.0f,scen,b___},
				{b___,b___,b___,scen,scen,scen,scen,b___,b___}};
		case 6: return new float[][] { //easy med *1
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.1f,scen,b___,b___,b___},
				{b___,b___,scen,0.2f,0.6f,0.2f,scen,b___,b___},
				{b___,b___,scen,0.6f,1.0f,0.6f,0.2f,scen,b___},
				{b___,b___,b___,scen,scen,scen,scen,b___,b___}};
		case 7: return new float[][] { //med *2
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,scen,scen,b___,b___},
				{b___,b___,scen,0.1f,scen,0.1f,0.1f,scen,b___},
				{b___,scen,0.3f,0.4f,scen,0.4f,scen,b___,b___},
				{b___,scen,0.5f,1.0f,0.7f,1.0f,0.6f,scen,b___},
				{b___,b___,scen,scen,scen,scen,scen,b___,b___}};
		case 8: return new float[][] { //med *2
				{b___,b___,b___,b___,b___,b___,scen,b___,b___},
				{b___,b___,b___,scen,b___,scen,0.1f,scen,b___},
				{b___,b___,scen,0.1f,scen,scen,0.4f,scen,b___},
				{b___,b___,scen,0.4f,0.2f,0.2f,scen,b___,b___},
				{b___,scen,0.3f,1.0f,0.4f,0.4f,1.0f,scen,b___},
				{b___,b___,scen,scen,scen,scen,scen,b___,b___}};
		case 9: return new float[][] { //hard *4
				{b___,scen,scen,b___,b___,b___,b___,b___,b___},
				{scen,0.1f,0.1f,scen,scen,b___,b___,b___,b___},
				{scen,0.4f,scen,0.2f,0.1f,scen,b___,b___,b___},
				{scen,1.0f,1.0f,0.4f,0.4f,0.2f,scen,b___,b___},
				{b___,scen,1.0f,1.0f,1.0f,0.4f,0.2f,scen,b___},
				{b___,b___,scen,scen,scen,scen,scen,b___,b___}};
		case 10: return new float[][] { //easy *0
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,scen,b___,b___,b___},
				{b___,b___,scen,0.1f,scen,0.1f,scen,scen,b___},
				{b___,scen,0.4f,1.0f,0.4f,1.0f,0.4f,0.1f,scen},
				{b___,b___,scen,scen,scen,scen,scen,scen,b___}};
		case 11: return new float[][] { //medium *2
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,scen,b___,b___,b___},
				{b___,b___,scen,scen,0.1f,0.1f,scen,b___,b___},
				{b___,scen,0.1f,0.2f,0.4f,0.4f,scen,scen,b___},
				{b___,scen,0.4f,0.4f,1.0f,1.0f,0.3f,0.1f,scen},
				{b___,b___,scen,scen,scen,scen,scen,scen,b___}};
		case 12: return new float[][] { //med *3
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,scen,b___,scen,0.1f,scen,b___,b___,b___},
				{scen,0.1f,scen,0.2f,0.4f,scen,b___,b___,b___},
				{scen,0.4f,0.3f,0.4f,1.0f,0.3f,scen,scen,b___},
				{b___,scen,0.6f,1.0f,1.0f,0.4f,0.2f,0.1f,scen},
				{b___,b___,scen,scen,scen,scen,scen,scen,b___}};
		case 13: return new float[][] { //med *3
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,scen,scen,b___,scen,scen,scen,b___},
				{b___,scen,0.1f,0.1f,scen,0.1f,0.2f,0.1f,scen},
				{b___,scen,0.4f,0.4f,0.2f,scen,0.4f,scen,b___},
				{scen,0.3f,1.0f,1.0f,0.7f,0.7f,1.0f,0.7f,scen},
				{b___,scen,scen,scen,scen,scen,scen,scen,b___}};
		case 14: return new float[][] { //super hard *7
				{b___,b___,b___,scen,scen,scen,b___,b___,b___},
				{b___,b___,scen,0.1f,0.2f,0.1f,scen,b___,b___},
				{b___,scen,0.2f,0.4f,0.4f,0.4f,scen,scen,b___},
				{scen,0.2f,0.4f,1.0f,1.0f,1.0f,0.3f,0.1f,scen},
				{scen,0.4f,1.0f,1.0f,1.0f,1.0f,0.4f,0.4f,scen},
				{b___,scen,scen,scen,scen,scen,scen,scen,b___}};
		case 15: return new float[][] { //med *2
				{b___,b___,b___,b___,scen,scen,scen,b___,b___},
				{b___,b___,b___,scen,0.1f,0.2f,0.1f,scen,b___},
				{b___,b___,scen,0.2f,scen,0.4f,scen,b___,b___},
				{b___,b___,b___,scen,0.5f,scen,b___,b___,b___},
				{b___,b___,scen,0.6f,1.0f,1.0f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		default: return new float[][] {
				{scen,scen,scen,scen,scen,scen,scen,scen,scen},
				{scen,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,scen},
				{scen,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,scen},
				{scen,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,scen},
				{scen,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,scen},
				{scen,scen,scen,scen,scen,scen,scen,scen,scen}};
		}
	}
	
	private final int b___ = -1;
	private final int SUPE = -2;
	private final int scen = -3;
	private final int regu = -4;

}
