package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class TemplateClasses {

}


//boolean available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
// float accelX = Gdx.input.getAccelerometerX();
//float accelY = Gdx.input.getAccelerometerY();
//float accelZ = Gdx.input.getAccelerometerZ();

/*if (t.arrayY == arrayY + 1) {
if (t.arrayX == arrayX - 2) t.bit += 8; 
else if (t.arrayX == arrayX - 1) t.bit += 14; 
else if (t.arrayX == arrayX + 0) t.bit += 20; 
else if (t.arrayX == arrayX + 1) t.bit += 14; 
else if (t.arrayX == arrayX + 2) t.bit += 8; 
}
else if (t.arrayY == arrayY) {
if (t.arrayX == arrayX - 3) t.bit += 8; 
else if (t.arrayX == arrayX - 2) t.bit += 14; 
else if (t.arrayX == arrayX - 1) t.bit += 24; 
else if (t.arrayX == arrayX + 1) t.bit += 24; 
else if (t.arrayX == arrayX + 2) t.bit += 14; 
else if (t.arrayX == arrayX + 3) t.bit += 8; 
}
else if (t.arrayY == arrayY - 1) {
if (t.arrayX == arrayX - 2) t.bit += 12; 
else if (t.arrayX == arrayX - 1) t.bit += 20; 
else if (t.arrayX == arrayX + 0) t.bit += 30; 
else if (t.arrayX == arrayX + 1) t.bit += 20; 
else if (t.arrayX == arrayX + 2) t.bit += 12; 
}
else if (t.arrayY == arrayY - 2) {
if (t.arrayX == arrayX - 1) t.bit += 12;  
else if (t.arrayX == arrayX + 0) t.bit += 20; 
else if (t.arrayX == arrayX + 1) t.bit += 12;  
}
else if (t.arrayX == arrayX + 0 && t.arrayY == arrayY + 2) t.bit += 14; 
else if (t.arrayX == arrayX + 0 && t.arrayY == arrayY - 3) t.bit += 10; */

/*private float oS(float x) {
if (game.g.doOffSet)
	return ((x + game.g.offSet)%840) - 40;
else return x;
}*/


/*if (isGuyOn) 
timeLeft -= delta;
else timeLeft += delta;

timeLeft = timeLeft < 0 ? 0 : timeLeft;
timeLeft = timeLeft > 1 ? 1 : timeLeft;

if (timeLeft < 1)
frame = 1 + timeLeft;
else frame = 0;

if (timeLeft == 0) frame = -2;

isGuyOn = false;*/
/*if (type == CollisionType.TOP) { 
super.basicTopCollision(chaser);
isGuyOn = true;
}
if (type == CollisionType.UR) {
super.basicURCollision(chaser);
isGuyOn = true;
}
if (type == CollisionType.UL) {
super.basicULCollision(chaser);
isGuyOn = true;
}*/

/*public void calculateNeighborScore() {
for (Tile t: tilesDraw)
	calculateSingleNeighborScore(t.arrayX,t.arrayY);
}

private void calculateReigonNeighborScore(int x1, int y1) {
try { calculateSingleNeighborScore(x1+1,y1); } catch (Exception e) {}
try { calculateSingleNeighborScore(x1-1,y1); } catch (Exception e) {}
try { calculateSingleNeighborScore(x1,y1+1); } catch (Exception e) {}
try { calculateSingleNeighborScore(x1,y1-1); } catch (Exception e) {}
try { calculateSingleNeighborScore(x1,y1); } catch (Exception e) {}
}

private void calculateSingleNeighborScore(int x, int y) {
int score = 0;
//  1
// 8X2
//  4
try {
	int k = tiles2D[x][y+1].neighborScore;
	if (!(tiles2D[x][y+1] instanceof TileScenery))
		score += 1;
} catch (Exception e) {}
try {
	int k = tiles2D[x+1][y].neighborScore;
	if (!(tiles2D[x+1][y] instanceof TileScenery))
		score += 2;
} catch (Exception e) {}
try {
	int k = tiles2D[x][y-1].neighborScore;
	if (!(tiles2D[x][y-1] instanceof TileScenery))
		score += 4;
} catch (Exception e) {}
try {
	int k = tiles2D[x-1][y].neighborScore;
	if (!(tiles2D[x-1][y] instanceof TileScenery))
		score += 8;
} catch (Exception e) {}
tiles2D[x][y].setNeighborScore(score);
}*/

/*try {
if (tiles2D[x][y] != null) {
	if (tiles2D[x][y] instanceof TileScenery) {
		removeTile(tiles2D[x][y]);
	}
	else return;
}
} catch (Exception e) {
return;
}*/
/*int[] bits = {1,1,2,5};
if (left == 0) {
	bit += bits[left];
}
else {
	bit += bits[left];
	if (leftTile != null && !(leftTile instanceof TileScenery)) {
		leftTile.hitNeighbors(left-1);
	}
	if (rightTile != null && !(rightTile instanceof TileScenery)) {
		rightTile.hitNeighbors(left-1);
	}
	if (aboveTile != null && !(aboveTile instanceof TileScenery)) {
		aboveTile.hitNeighbors(left-1);
	}
	if (belowTile != null && !(belowTile instanceof TileScenery)) {
		belowTile.hitNeighbors(left-1);
	}
}*/


/*public int compareTo(Object t) {
		int me = 16*y + x;
		int they = 16*(((Tile)t).y) + (((Tile)t).x);
		return they - me;
	}*/

/*public void basicTopCollision(Guy guy)
    {
        if (guy.velY < 0) {
            guy.velY = guy.velY < -3 ? -guy.velY / 3: 0;
            guy.y = this.y + 40 - 1;
            guy.jumping = false;
        }
    }
	public void basicURCollision(Guy guy)
    {
		int rad = 18;
        if (guy.velY < 0 && (neighborScore & 2) != 2) {
        	if ( ((y + 20) - guy.y)*((y + 20) - guy.y) +
        			((x + 20) - guy.x)*((x + 20) - guy.x) < rad*rad ) {
        		guy.x = (int)Math.sqrt(rad*rad - (guy.y-(this.y+20))*(guy.y-(this.y+20))) + this.x + 20;
        		guy.velX = guy.velX <= 0 ? 0.3f - guy.velY/2 : guy.velX + 1f - guy.velY/2;
        		guy.velY = guy.velY < -1 ? -guy.velY / 5: 0;
                //chaser.y = this.y + 40 - 1;
                guy.jumping = false; 
        	}  
        }
    }
	public void basicULCollision(Guy guy)
    {
		int rad = 18;
		if (guy.velY < 0 && (neighborScore & 8) != 8) {
        	if ( ((y + 20) - guy.y)*((y + 20) - guy.y) +
        			((x - 20) - guy.x)*((x - 20) - guy.x) < rad*rad ) {
        		guy.x = -(int)Math.sqrt(rad*rad - (guy.y-(this.y+20))*(guy.y-(this.y+20))) + this.x - 20;
        		guy.velX = guy.velX >= 0 ? -0.3f + guy.velY/2 : guy.velX - 1f + guy.velY/2;
        		guy.velY = guy.velY < -1 ? -guy.velY / 5: 0;
                //chaser.y = this.y + 40 - 1;
                guy.jumping = false;
        	}  
        }
    }
	
    public void basicLeftCollision(Chaser chaser)
    {
        if (chaser.sideVelocity > 0) {
            chaser.sideVelocity = 0;
            chaser.coord.x = coord.x - C.blocksSize;
        }
    }

    public void basicRightCollision(Chaser chaser)
    {
        if (chaser.sideVelocity < 0) {
            chaser.sideVelocity = 0;
            chaser.coord.x = coord.x + C.blocksSize;
        }
    }

    public void basicBottomCollision(Chaser chaser)
    {
        if (chaser.upwardVelocity > 0) {
            chaser.upwardVelocity = -chaser.upwardVelocity/2;
            chaser.coord.y = coord.y + C.blocksSize - 1;
        }
    }*/

/*x = x / 40;
y = y / 40;

for (int offX = -1; offX <= 1; offX++)
    for (int offY = -1; offY <= 1; offY++)
        if (x+offX >= 0 && y+offY >= 0 && x+offX < 20 && y+offY < 12)
            if (tiles[x+offX][y+offY] != null)
            	if (tiles[x+offX][y+offY].type != Type.Scenery)
            		closeTiles.add(tiles[x+offX][y+offY]);*/





/*------------------------------COORD CLASS-------------
 * package com.jerhis.cloudgame.game;

//easy way to store coordinates
public class Coord {

	public double x, y;

	public Coord(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Coord() {
		x = 0;
		y = 0;
	}
	
	public Coord(String s) {
		int k = 2;
		while (s.charAt(k) != ',')
			k++;
		x = Double.parseDouble(s.substring(1, k));
		y = Double.parseDouble(s.substring(k + 1, s.length() - 1));
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public String toString(boolean doInt) {
		return "(" + (int) x + ", " + (int) y + ")";
	}

	public Coord clone() {
		return new Coord(x, y);
	}
	
	//UTIL STUFF
	public static double distance(Coord a, Coord b) {
	    return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
	}
		
	public static boolean inBounds(Coord a, Coord b, Coord c) {
	    return a.x>b.x && a.x<c.x && a.y>b.y && a.y < c.y;
	}
}
 */


/* ----------------------------------------SCREEN CLASS TEMPLATE--------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;

public class ExampleScreen implements Screen, InputProcessor {
	
	final MyGdxGame game;
	OrthographicCamera camera;

	TextureAtlas textures;
	AtlasRegion extractedTexture;
	Texture splash;
	static Sound sound;
	static Music music;
	 
	public GameScreen(final MyGdxGame gam) {
		game = gam;
		Gdx.input.setInputProcessor(this);
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		textures = new TextureAtlas("gameimages.txt");
		extractedTexture = textures.findRegion("MyTexture");
		
		splash = new Texture(Gdx.files.internal("splash.png"));
		
		sound = Gdx.audio.newSound(Gdx.files.internal("sound.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		//sound.play();
		//music.play();
		
		int stored = game.prefs.getInteger("best", -1); //stored = -1 if there is nothing stored at "best"
		game.prefs.putInteger("best", stored);
		game.prefs.flush();
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.86f, 0.86f, 0.86f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		stateRender(delta);
		stateDraw(delta);
		//game.setScreen(new GenericScreen(game));
		//dispose();
	}
 
	private void stateRender(float delta) {
		//update everything here
		//access game with game.g.whatever
		//remember to dispose()
	}
	
	private void stateDraw(float delta) {
		game.batch.begin();
		
		game.batch.draw(splash, 0, -(512-480));
		game.batch.draw(extractedTexture, 10, 20); //draws at lower left (10,20)
		
		//public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
		//					float scaleX, float scaleY, float rotation) {


		game.font.draw(game.batch, "Text", 10, 470);
		//if (game.g.debug) game.font.draw(game.batch, "fps: " + (int)(1/game.g.lastDelta), 10, 410);

		game.batch.end();
	}

	
	
	@Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void show() {
	}
 
	@Override
	public void hide() {
	}
 
	@Override
	public void pause() {
	
	}
 
	@Override
	public void resume() {
	}
 
	@Override
	public void dispose() {
		textures.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		//touch down input processed here
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;

		//touch up input processed here

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		//touch drag input processed here
		
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {return false;}
	@Override
	public boolean keyUp(int keycode) {return false;}
	@Override
	public boolean keyTyped(char character) {return false;}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	@Override
	public boolean scrolled(int amount) {return false;}

}
*/

/*------------------------------------template MyGdxGame Class--------------
package com.jerhis.cloudgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	 
    SpriteBatch batch;
	BitmapFont font;
	Preferences prefs;
	ExampleGame g;
	boolean debug = true;
 
	public void create() {
		
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		font.setColor(new Color(0,0,0,1));
		font.setScale(2f);
		this.prefs = Gdx.app.getPreferences(".examplegame");
		g = new ExampleGame(this);
		this.setScreen(new SplashScreen(this));
	}
 
	public void render() {
		super.render(); // important!
	}
 
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
 
}
*/