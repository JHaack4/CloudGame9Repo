package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen, InputProcessor {
	
	final MyGdxGame game;
	OrthographicCamera camera;

	TextureAtlas textures;
	AtlasRegion bg, controlTilt, controlTouch, sliderBG, sliderBar, blackOverlay, loading, tutImage, tutImagePushed, musicIm[] = new AtlasRegion[2];

    float slider;
    boolean sliderTouch = false, soundOn;
	boolean leaving, readyToLeave, shouldPlayTutorial , shouldReturnToMenu;
    Music gameMusic;
    boolean stateSettings, stateGPS;
    ButtonSet mainButtons, gpsButtons, settingsButtons;
 
	public MainMenuScreen(final MyGdxGame gam, Music music) {
		game = gam;
		Gdx.input.setInputProcessor(this);
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
        stateGPS = false;
        stateSettings = false;
        leaving = false;
        readyToLeave = false;
        shouldPlayTutorial = false;
        shouldReturnToMenu = false;

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

        mainButtons = new ButtonSet();
        mainButtons.addButton(tutImage, tutImagePushed, 725, 415);
        mainButtons.addButton(tutImage, tutImagePushed, 575, 415);

        settingsButtons = new ButtonSet();
        settingsButtons.addButton(tutImage, tutImagePushed, 700, 480-125/2);

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
        if (stateGPS) {
            game.font.draw(game.batch, "Back Arrow", 100, 150);
            game.font.draw(game.batch, "LEaderboard", 100, 150);
            game.font.draw(game.batch, "Achievements", 100, 150);
            game.font.draw(game.batch, "Sign In/ Sign Out", 100, 150);
        }
        else if (stateSettings) {
            game.font.draw(game.batch, "Settings", 300, 430);
            game.font.draw(game.batch, "Back Arrow, 50x50", 20, 460);
            //game.batch.draw(backArrow, 20,410);
            game.font.draw(game.batch, "Music:", 100, 350);
            game.batch.draw(musicIm[game.sound ? 0 : 1],250,300);
            //game.font.draw(game.batch, "tutorial", 100, 150);
            settingsButtons.draw(game.batch);
            game.font.draw(game.batch, "Controls:", 100, 250);
            if (game.tiltControls) game.batch.draw(controlTilt, 300, 150);
            else game.batch.draw(controlTouch, 300, 150);
            //game.font.draw(game.batch, "sensitivity", 100, 150);
            game.batch.draw(sliderBG, 400 - sliderBG.getRegionWidth()/2, 0);
            game.batch.draw(sliderBar, 400 - sliderBG.getRegionWidth()/2 + 500*(slider - 0.5f) - 25, 0);
        }
        else {
            game.font.draw(game.batch, "tap to play", 100, 120);
            //game.font.draw(game.batch, "high score/ games playaed", 100, 150);
            game.font.draw(game.batch, "Games Played: " + game.gamesPlayed, 10, 490);
            game.font.draw(game.batch, "Best: " + game.highScore, 10, 460);
            //game.font.draw(game.batch, "gps", 100, 150);
            //game.font.draw(game.batch, "settings", 100, 150);
            mainButtons.draw(game.batch);
            game.font.draw(game.batch, "remove ads?", 100, 150);
        }

		//if (game.tiltControls)
		//	game.batch.draw(controlTilt, 800-185, 0);
		//else game.batch.draw(controlTouch, 800-185, 0);
        //game.batch.draw(sliderBG, 50, 0);
        //game.batch.draw(sliderBar, 50 + 500*(slider - 0.5f) - 25, 0);



        //game.batch.draw(tutImage, 800-185,380-25);
        //game.batch.draw(musicIm[game.sound ? 0 : 1],800-50-185-10,480-50);

        if (readyToLeave) {
            game.batch.draw(blackOverlay,-100,-100,0,0,10,10,100,68,0);
            game.batch.draw(loading, 400 - loading.originalWidth/2, 240 - loading.originalHeight/2);
        }

		game.batch.end();
		
		if (leaving) {
            if (shouldPlayTutorial) {
                game.setScreen(new TutorialScreen(game, shouldReturnToMenu, gameMusic));
                dispose();
            }
            else {
                game.setScreen(new GameScreen(game, gameMusic));
                dispose();
            }
        }
        if (readyToLeave) leaving = true;


        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(1,0.5f,0,1);
        if (stateGPS) {

        }
        else if (stateSettings) {
            game.shapeRenderer.rect(20,410,50,50);
        }
        else {

        }
        game.shapeRenderer.setColor(0,1,0,1);
        int xw = 50, yw = 50;
        for (int xx = 0; xx < 800; xx+=xw)
            for (int yy = 0; yy < 480; yy+= yw)
                game.shapeRenderer.rect(xx,yy,xw,yw);
        game.shapeRenderer.end();
		
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
        if (game.sound) {
            gameMusic.pause();
        }
	}
 
	@Override
	public void resume() {
        if (game.sound)
            gameMusic.play();
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

        if (stateGPS) {

        }
        else if (stateSettings) {
            settingsButtons.touchDown(x,y);
            if (x < 90 && y > 390) stateSettings = false;
            if (x > 230 && x < 320 && y > 280 && y < 370) sound();
            if (x > 300 && x < 470 && y > 150 && y < 280) game.setControls(!game.tiltControls);
            if (y < 125 && (x >= 100 && x <= 700) ) {
                slider = x;
                int min = 400 - sliderBG.getRegionWidth()/2;
                slider = slider < min ? min : slider;
                slider = slider > min + 500 ? min + 500 : slider;
                slider = (slider - min) / 500.0f + 0.5f;
                slider = slider < 1.05f && slider > 0.95f ? 1 : slider;
                sliderTouch = true;
            }
        }
        else {
            if (y < 350 || x < 500) {
                readyToLeave = true;
                if (!game.seenTutorial) {
                    shouldPlayTutorial = true;
                    shouldReturnToMenu = false;
                    game.setSeenTutorial(true);
                }
            }
            mainButtons.touchDown(x,y);
        }
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;

        if (stateGPS) {

        }
        else if (stateSettings) {
            settingsButtons.touchUp(x,y);
            switch (settingsButtons.pollClick()) {
                case 0: shouldPlayTutorial = true;
                    shouldReturnToMenu = true;
                    game.setSeenTutorial(true);
                    readyToLeave = true; break;
                case -1: break;
            }
            if (sliderTouch && y <= 130) {
                game.setSlider(slider);
                sliderTouch = false;
            }
        }
        else {
            mainButtons.touchUp(x,y);
            switch (mainButtons.pollClick()) {
                case 0: stateSettings = true; break;
                case 1: stateGPS = true; break;
                case -1: break;
            }

        }
				
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;

        if (stateGPS) {

        }
        else if (stateSettings) {
            settingsButtons.touchDragged(x,y);
            if (sliderTouch && y < 125) {
                slider = x;
                int min = 400 - sliderBG.getRegionWidth()/2;
                slider = slider < min ? min : slider;
                slider = slider > 500 + min ? 500 + min : slider;
                slider = (slider - min) / 500.0f + 0.5f;
                slider = slider < 1.05f && slider > 0.95f ? 1 : slider;
            }
            else if (sliderTouch) {
                game.setSlider(slider);
                sliderTouch = false;
            }
        }
        else {
            mainButtons.touchDragged(x,y);

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
	