package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.jerhis.cloudgame.game.*;

public class GameScreen implements Screen, InputProcessor {
	
	final MyGdxGame game;
	OrthographicCamera camera;

	TextureAtlas textures;// textures2, textures3;
	AtlasRegion ready, pauseIcon, music[] = new AtlasRegion[2], blackOverlay, yellowOverlay, rain, rain15, lighthalf, lightline,
		basic0, super0, redChaser, guyright, guyleft, sceneryClouds[] = new AtlasRegion[16], lightningani,
		blue[] = new AtlasRegion[31], buttonImages[][] = new AtlasRegion[10][2];
        //backgrounds[] = new AtlasRegion[10]; //yellow[] = new AtlasRegion[10];
    Texture bg1;
	State state;
    ButtonSet pauseButtons, finishButtons;
    boolean leavingToMainMenu;
    static boolean soundOn;
    public static Sound soundDrop;
    Music musicGame;
	//Texture splash;
    /*buttons list
    0 - resume
    1 - mainmenu
    2 - retry

    BACKGROUND LIST
    0 - sunriseT copy - 2
    1 - sunriseT2 - 2
    2 - morning copy - 2
    3 - morningT copy - 2
    4 - afternoon copy - 3
    5 - sunsetT copy - 3
    6 - sunsetT2 copy - 3
    7 - nightT copy - 3
    8 - night copy - 3
     */
	 
	public GameScreen(final MyGdxGame gam, Music music1) {
		game = gam;
		Gdx.input.setInputProcessor(this);
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		textures = new TextureAtlas("gameimages.txt");
		pauseIcon = textures.findRegion("pauseicon");
		music[0] = textures.findRegion("musicon");
        music[1] = textures.findRegion("musicoff");
		ready = textures.findRegion("ready");
		blackOverlay = textures.findRegion("blackoverlay");
        yellowOverlay = textures.findRegion("yellow7");
		rain = textures.findRegion("rain");
        rain15 = textures.findRegion("rain15");
		basic0 = textures.findRegion("basic0");
		super0 = textures.findRegion("super");
		redChaser = textures.findRegion("bug");
        guyright = textures.findRegion("rightbug");
        guyleft = textures.findRegion("leftbug");
        lighthalf = textures.findRegion("lightninghalf");
        lightline = textures.findRegion("lightningline");
        lightningani = textures.findRegion("lightningani3");
		for (int k = 1; k < 16; k++)
			sceneryClouds[k] = textures.findRegion("clsc" + k);
		sceneryClouds[0] = pauseIcon;
		for (int k = 1; k < 31; k++)
			blue[k] = textures.findRegion("blue" + k + " copy");
        buttonImages[0][0] = textures.findRegion("buttonresume");
        buttonImages[0][1] = textures.findRegion("pushedresume");
        buttonImages[1][0] = textures.findRegion("buttonmainmenu");
        buttonImages[1][1] = textures.findRegion("pushedmainmenu");
        buttonImages[2][0] = textures.findRegion("buttonretry");
        buttonImages[2][1] = textures.findRegion("pushedretry");

		//textures2 = new TextureAtlas("gameimages2.txt");
        //backgrounds[0] = textures2.findRegion("sunriseT copy");
        //backgrounds[1] = textures2.findRegion("sunriseT2");
        //backgrounds[2] = textures2.findRegion("morning copy");
       // backgrounds[3] = textures2.findRegion("morningT copy");

        //textures3 = new TextureAtlas("gameimages3.txt");
       // backgrounds[4] = textures3.findRegion("afternoon copy");
        //backgrounds[5] = textures3.findRegion("sunsetT copy");
        //backgrounds[6] = textures3.findRegion("sunsetT2 copy");
       // backgrounds[7] = textures3.findRegion("nightT copy");
       // backgrounds[8] = textures3.findRegion("night copy");

        bg1 = new Texture(Gdx.files.internal("bg1.png"));

        pauseButtons = new ButtonSet();
        pauseButtons.addButton(buttonImages[0][0],buttonImages[0][1],400,270);
        pauseButtons.addButton(buttonImages[1][0],buttonImages[1][1],400,150);

        finishButtons = new ButtonSet();
        finishButtons.addButton(buttonImages[2][0],buttonImages[2][1],400,270);
        finishButtons.addButton(buttonImages[1][0],buttonImages[1][1],400,150);

        soundOn = game.sound;
        soundDrop = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        musicGame = music1;

        game.ad(6);
		
		game.g.clear();
		state = State.Ready;
        leavingToMainMenu = false;

        //splash = new Texture(Gdx.files.internal("splash.png"));
		//highScore = game.prefs.getInteger("best", 0);
		//game.prefs.putInteger("best", 100);
		//game.prefs.flush(); <--- IMPORTANT!
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.86f, 0.86f, 0.86f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

        //long startNanoTime = System.nanoTime();
		stateRender(delta, true);
		stateDraw(delta, false);
		//game.setScreen(new GenericScreen(game));

        //long endNanoTime = System.nanoTime();
        //Log.d("jerhis", "delta: " + ((int)(delta*100000))/100000.0 + "\tr: " + (int)(((startNanoTime2 - startNanoTime) / 1000000000.0 / delta)*100.0) + "\td: " + (int)(((endNanoTime - startNanoTime2) / 1000000000.0 / delta)*100.0));
        //System.out.println("delta: " + delta + " nanotime: " + (endNanoTime - startNanoTime)/1000.0);

        if (leavingToMainMenu) {
            game.setScreen(new MainMenuScreen(game, musicGame));
            dispose();
        }
	}
 
	public void stateRender(float delta, boolean showAds) {
		switch (state) {
		case Finished:
			break;
		case Paused:
			break;
		case Ready:
			break;
		case Running:
			state = game.g.update(delta);
            if (state == state.Running && game.g.score > 10000) {
                game.ad(2);
            }
            if (state != state.Running) {
                game.wakeLock(0);
            }
            if (state == state.Finished && showAds) {
                game.highScore((int)game.g.score);
                game.leaderboard((int)game.g.score);
                game.achievement((int)game.g.score);
                game.ad(6);
                game.ad(3);
            }
			break;
		default:
			break;
		}
	}
	
	public void stateDraw(float delta, boolean tutorial) {
		game.batch.begin();

       // long s1 = System.nanoTime();

        for (Background bg: game.g.backgrounds) {
            //game.batch.draw(bg1,0,bg.y,0,0,800,200,1,4,0);
            game.batch.draw(bg1,-112,bg.y);
            game.batch.draw(bg1,144,bg.y);
            game.batch.draw(bg1,400,bg.y);
            game.batch.draw(bg1,656,bg.y);
        }
		
		//public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
		//float scaleX, float scaleY, float rotation) {
		//game.batch.draw(i, x, y, 0,0, height,width, scX,scY,rotation);

        //long s2 = System.nanoTime();

		for (Tile tile: game.g.tiles) {
			switch (tile.type) {
			case Scenery: 
			case becomeScenery:	
				if (tile.bit > 0)
                    d(sceneryClouds[(int) tile.bit], tile.x - 20, tile.y - 20 - game.g.height);
                break;

			case Default: 
				d(basic0, tile.x - 32, tile.y - 32 - game.g.height);
				if (tile.bit > 0 && tile.bit <= 30)
                    d(blue[(int) (tile.bit)], tile.x - 20, tile.y - 20 - game.g.height);
                break;

			case Super: 
				d(super0, tile.x - 32, tile.y - 32 - game.g.height);
				if (tile.bit > 0 && tile.bit <= 30)
                    game.batch.draw(blue[(int) (tile.bit)], tile.x - 20, tile.y - 20 - game.g.height);
                break;

			case Removable: break;
			}
		}

        //long s3 = System.nanoTime();
		
		for (GameObject go: game.g.gameObjects) {
			switch (go.objectType) {
			case Raindrop: 
				Raindrop r = (Raindrop)go;
				//d(rain, r.x-20, r.y-20 - game.g.height);
                game.batch.draw(rain15, r.x-7.5f, r.y-7.5f - game.g.height, 0,0, 15,15, 1,1,(float)(r.rotation / 3.14159 * 180));
				break;

            case Lightning:
                Lightning lightning = (Lightning)go;
                lightning.draw(game.batch, lightline, lighthalf, game.g.height);
			}
			
		}

        /*d(blackOverlay, 100,100);
        d(blackOverlay, 200,100);
        d(blackOverlay, 100,200);
        d(blackOverlay, 200,200);
        d(blackOverlay, 0,0);
        game.batch.draw(yellowOverlay, 100, 100, 0,0, 10,10, 1,10,0);*/

        //float scX = game.g.guy.velY > 8.5f ? 1/(game.g.guy.velY/8.5f) : 1;
		//game.batch.draw(redChaser, game.g.guy.x - redChaser.originalWidth/2, game.g.guy.y - 15- game.g.height, 0,0,redChaser.originalWidth,redChaser.originalHeight,1,scX,0);

        //long s4 = System.nanoTime();
        if (!(game.g.left ^ game.g.right))
            game.batch.draw(redChaser, game.g.guy.x - redChaser.originalWidth/2, game.g.guy.y - 15- game.g.height);
        else if (game.g.left && !game.g.right)
            game.batch.draw(guyleft, game.g.guy.x - redChaser.originalWidth/2, game.g.guy.y - 15- game.g.height);
        else if (!game.g.left && game.g.right)
            game.batch.draw(guyright, game.g.guy.x - redChaser.originalWidth/2, game.g.guy.y - 15- game.g.height);

        if (game.g.guy.lightning > 0) {
            float stretch = (game.g.guy.LIGHTNING_MAX - Math.abs(game.g.guy.lightning - game.g.guy.LIGHTNING_MAX))*game.g.guy.LIGHTNING_SCALE;
            game.batch.draw(lightningani,game.g.guy.x- 192/2*stretch,game.g.guy.y - game.g.height - 192/2*stretch,0,0,192,192,stretch, stretch,0);
        }

        if (tutorial) {
            d(music[game.sound ? 0 : 1],800-50,480-50);
            game.batch.end();
            return;
        }

		d("Score: " + (int)game.g.score, 10, 490);
		d("Best: " + game.highScore, 10, 460);
		if (game.debug) {
            int fps = (int)(1/game.g.lastDelta);
            if (fps > 55 && fps < 64) fps = 60;
			d("FPS: " + fps, 10, 430);
			//d(" Y: " + game.accelY, 10, 380);
		}
					
		
		switch (state) {
		case Running:
			d(pauseIcon,800-50,480-50);
            d(music[game.sound ? 0 : 1],800-100,480-50);
			break;
		case Finished:
            game.batch.draw(blackOverlay,-100,-100,0,0,10,10,100,68,0);
			finishButtons.draw(game.batch);
			break;
		case Paused:
            game.batch.draw(blackOverlay,-100,-100,0,0,10,10,100,68,0);
            pauseButtons.draw(game.batch);
			break;
		case Ready:
            game.batch.draw(blackOverlay,-100,-100,0,0,10,10,100,68,0);
			game.batch.draw(ready, 400-250, 250);
            if (game.tiltControls) {
                game.font.draw(game.batch, "Tilt the phone", 265, 180);
                game.font.draw(game.batch, "     to move!", 260, 140);
            }
            else {
                game.font.draw(game.batch, "Tap the edges", 265, 180);
                game.font.draw(game.batch, "     to move!", 260, 140);
            }
			break;
		default:
			break;
		}
       // long s5 = System.nanoTime();
		game.batch.end();



        //Log.d("jerhis", "bgs: " + pT(s1,s2,delta) + " tiles: " + pT(s2,s3,delta) + " gos: " + pT(s3,s4,delta) + " rest: " + pT(s4,s5,delta));
	}

    //private void d(Texture t, int x, int y) { game.batch.draw(t, x, y); }
    private void d(AtlasRegion at, float x, float y) { game.batch.draw(at, x, y); }
    private void d(String text, float x, float y) { game.font.draw(game.batch, text, x, y); }

    public static int pT(long s, long e, float d) {
        return (int)(((e - s) / 1000000000.0 / d)*100.0);
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
		if (state == State.Running)
			state = State.Paused;
        if (game.sound) {
            musicGame.pause();
        }
        game.wakeLock(0);
	}
 
	@Override
	public void resume() {
        if (game.sound) {
            musicGame.play();
        }
	}
 
	@Override
	public void dispose() {
		textures.dispose();
		//textures2.dispose();
        //textures3.dispose();
        bg1.dispose();
        //musicGame.stop();
        //musicGame.dispose();
        soundDrop.stop();
        soundDrop.dispose();
        game.wakeLock(0);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		switch (state) {
		case Finished:
            finishButtons.touchDown(x,y);
			break;
		case Paused:
            pauseButtons.touchDown(x,y);
			break;
		case Ready:
			state = State.Running;
            if (game.tiltControls) game.wakeLock(1);
			break;
		case Running:
			if (x > 800-50 && y > 480 - 50) {
				state = State.Paused;
                game.wakeLock(0);
            }
            else if (x > 800-100 && y > 480 - 50)
                sound();
			else game.g.touchDown(x, y, pointer);
			break;
		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		switch (state) {
		case Finished:
            finishButtons.touchUp(x,y);
            switch (finishButtons.pollClick()) {
                case 1: leavingToMainMenu = true;
                    game.g.clear(); break;
                case 0: game.g.clear();
                    //game.ad(4);
                    state = State.Running;
                    if (game.tiltControls) game.wakeLock(1); break;
                case -1:
            }
			break;
		case Paused:
			if (x < 50 && y > 430) game.debug = !game.debug;
            pauseButtons.touchUp(x,y);
            switch (pauseButtons.pollClick()) {
                case 1: game.increaseGamesPlayed();
                    leavingToMainMenu = true; break;
                case 0: state = State.Running;
                    if (game.tiltControls) game.wakeLock(1); break;
                case -1:
            }
			break;
		case Ready:
			break;
		case Running:
			game.g.touchUp(x, y, pointer);
			break;
		default:
			break;
		}
				
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		switch (state) {
		case Finished:
            finishButtons.touchDragged(x,y);
			break;
		case Paused:
            pauseButtons.touchDragged(x,y);
			break;
		case Ready:
			break;
		case Running:
			game.g.touchDragged(x, y, pointer);
			break;
		default:
			break;
		}
		
		return false;
	}

    protected void sound() {
        game.toggleSound();
        soundOn = game.sound;
        if (game.sound) {
            musicGame.play();
        }
        else {
            musicGame.stop();
        }
    }

    public static void playSound(Sound sound) {
        if (soundOn) {
            sound.play();
        }
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
	
	public enum State {
		Running, Paused, Finished, Ready
	}
	
}
