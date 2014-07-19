package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen, InputProcessor {
	
	final MyGdxGame game;
	OrthographicCamera camera;

	TextureAtlas textures;
	AtlasRegion bg, controlTilt, controlTouch, sliderBG, sliderBar, blackOverlay, loading, tutImage, tutImagePushed, musicIm[] = new AtlasRegion[2];

    float slider;
    boolean sliderTouch = false;
    boolean soundOn;
	boolean leaving = false, readyToLeave = false, shouldPlayTutorial = false, shouldReturnToMenu = false;
    Music gameMusic;
 
	public MainMenuScreen(final MyGdxGame gam, Music music) {
		game = gam;
		Gdx.input.setInputProcessor(this);
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		textures = new TextureAtlas("menuimages.txt");
		bg = textures.findRegion("menubg");
		controlTilt = textures.findRegion("controltilt");
		controlTouch = textures.findRegion("controltouch");
        sliderBar = textures.findRegion("sliderbar");
        sliderBG = textures.findRegion("slider");
        loading = textures.findRegion("loading");
        blackOverlay = textures.findRegion("blackoverlay");
        tutImage = textures.findRegion("tutorial");
        tutImagePushed = textures.findRegion("pushedtutorial");
        musicIm[0] = textures.findRegion("musicon");
        musicIm[1] = textures.findRegion("musicoff");

        slider = game.slider;
        soundOn = game.sound;
        if (music == null) {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
            gameMusic.setVolume(0.2f);
            gameMusic.setLooping(true);
            if (soundOn) {
                gameMusic.play();
            }
        }
        else {
            gameMusic = music;
        }

		
		//highScore = game.prefs.getInteger("best", 0);
		//game.prefs.putInteger("best", (int)GameDisplay.guy.currentScore);
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		game.batch.begin();
		//game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
		game.batch.draw(bg, 0, 0);
		if (game.tiltControls)
			game.batch.draw(controlTilt, 800-185, 0);
		else game.batch.draw(controlTouch, 800-185, 0);
        game.batch.draw(sliderBG, 50, 0);
        game.batch.draw(sliderBar, 50 + 500*(slider - 0.5f) - 25, 0);

        game.font.draw(game.batch, "Games Played: " + game.gamesPlayed, 10, 490);
        game.font.draw(game.batch, "Best: " + game.highScore, 10, 460);

        game.batch.draw(tutImage, 800-185,380-25);
        game.batch.draw(musicIm[game.sound ? 0 : 1],800-50-185-10,480-50);

        if (readyToLeave) {
            game.batch.draw(blackOverlay,-100,-100,0,0,10,10,100,68,0);
            game.batch.draw(loading, 400 - loading.originalWidth/2, 240 - loading.originalHeight/2);
        }

		game.batch.end();
		
		if (leaving)
            if (shouldPlayTutorial) {
                game.setScreen(new TutorialScreen(game, shouldReturnToMenu, gameMusic));
                dispose();
            }
            else {
                game.setScreen(new GameScreen(game, gameMusic));
                dispose();
            }



        leaving = readyToLeave;
		
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
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;

        //game.batch.draw(tutImage, 800-185,380-25);
        //game.batch.draw(musicIm[game.sound ? 0 : 1],800-50-185-10,480-50);

        if (x > 800-50-185-10-10 && x <= 800-185 && y> 430) {
            sound();
        }
		else if (x > 800-175 && y < 125) {
			game.setControls(!game.tiltControls);
		}
        else if (x>800-10-175 && y > 480-125) {
            shouldPlayTutorial = true;
            shouldReturnToMenu = true;
            game.setSeenTutorial(true);
            readyToLeave = true;
        }
        else if (y < 125 && (x >= 25 || x <= 575) ) {
            slider = x;
            slider = slider < 50 ? 50 : slider;
            slider = slider > 550 ? 550 : slider;
            slider = (slider - 50.0f) / 500.0f + 0.5f;
            slider = slider < 1.05f && slider > 0.95f ? 1 : slider;
            sliderTouch = true;
        }
		else {
			readyToLeave = true;
            if (!game.seenTutorial) {
                shouldPlayTutorial = true;
                shouldReturnToMenu = false;
                game.setSeenTutorial(true);
            }
        }
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;

        if (sliderTouch && y < 125 && x < 625) {
            game.setSlider(slider);
            sliderTouch = false;
        }
				
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;

        if (sliderTouch && y < 125 && x < 625) {
            slider = x;
            slider = slider < 50 ? 50 : slider;
            slider = slider > 550 ? 550 : slider;
            slider = (slider - 50.0f) / 500.0f + 0.5f;
            slider = slider < 1.05f && slider > 0.95f ? 1 : slider;
        }
        else if (sliderTouch) {
            game.setSlider(slider);
            sliderTouch = false;
        }
		
		return false;
	}

    protected void sound() {
        game.toggleSound();
        soundOn = game.sound;
        if (game.sound) {
            gameMusic.play();
        }
        else {
            gameMusic.stop();
        }
    }


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
	