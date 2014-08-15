package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;

public class TutorialScreen implements Screen, InputProcessor {

    final MyGdxGame game;
    OrthographicCamera camera;

    GameScreen gameScreen;
    boolean leaving = false, leavingMenu = false;
    int frame;
    int tutorialState;
    boolean pastControls;
    float pastSlider;
    Music music;


    public TutorialScreen(final MyGdxGame gam, boolean menu, Music music) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        pastControls = game.tiltControls;
        pastSlider = game.slider;

        this.music = music;
        leavingMenu = menu;
        gameScreen = new GameScreen(game, music);
        gameScreen.state = GameScreen.State.Running;
        game.g.clearTutorial();
        frame = 0;
        tutorialState = 0;
        game.setSlider(0.7f);
        game.setControls(false);

        game.ad(2);

        Gdx.input.setInputProcessor(this);

        //textures = new TextureAtlas("gameimages.txt");
        //extractedTexture = textures.findRegion("MyTexture");

        //splash = new Texture(Gdx.files.internal("splash.png"));

        //sound = Gdx.audio.newSound(Gdx.files.internal("sound.mp3"));
        //music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        //sound.play();
        //music.play();

        //int stored = game.prefs.getInteger("best", -1); //stored = -1 if there is nothing stored at "best"
       // game.prefs.putInteger("best", stored);
        //game.prefs.flush();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0f, 0.86f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stateRender(delta);
        stateDraw(delta);


        //game.setScreen(new GenericScreen(game));
        //dispose();

        if (leaving)
            if (leavingMenu) {
                gameScreen.dispose();
                game.setSlider(pastSlider);
                game.setControls(pastControls);
                dispose();
                game.ad(5);
                game.setScreen(new MainMenuScreen(game, music));
            }
            else {
                Gdx.input.setInputProcessor(gameScreen);
                game.g.clear();
                game.setSlider(pastSlider);
                game.setControls(pastControls);
                gameScreen.state = GameScreen.State.Ready;
                game.ad(6);
                dispose();
                game.setScreen(gameScreen);
            }

    }

    private void stateRender(float delta) {
        //update everything here
        //access game with game.g.whatever
        //remember to dispose()

        if (tutorialState %2 == 0) {
            gameScreen.stateRender(0.017f, false);
            frame++;
        }

        switch(frame) {
                   case 85: move(-1);
            break; case 102: move(0);
            break; case 143: tutorialState = 1;
            break; case 255: tutorialState = 3;
            break; case 280: move(1);
            break; case 284: move(0);
            break; case 290: tutorialState = 5;
            break; case 400: move(1);
            break; case 430: move(0);
            break; case 500: move(-1);
            break; case 507: move(0);
            break; case 530: move(1);
            break; case 539: move(0);
            break; case 620: tutorialState = 7;
            break; case 820: move(-1);
            break; case 845: move(1);
            break; case 847: move(1);
            break; case 900: tutorialState = 9;
        }

    }

    private void move(int k) {
        game.g.touchDown(400 + k, 100, 0);
    }

    private void stateDraw(float delta) {

        gameScreen.stateDraw(0.017f, true);

        game.batch.begin();
        game.font.draw(game.batch, "Tutorial", 10, 490);
        //game.font.draw(game.batch, " f" + frame, 200, 490);
        if (tutorialState == 1) {
            game.font.draw(game.batch, "Hit the lightning to move up!", 100, 270);
        }
        if (tutorialState == 3) {
            game.font.draw(game.batch, "Hitting the cloud damages nearby tiles.", 10, 340);
        }
        if (tutorialState == 5) {
            game.font.draw(game.batch, "Dark blue tiles will be destroyed", 80, 350);
            game.font.draw(game.batch, "when nearby tiles are hit.", 128, 320);
        }
        if (tutorialState == 7) {
            game.font.draw(game.batch, "Don't destroy all of the lightning!", 65, 300);
        }
        if (tutorialState == 9) {
            game.font.draw(game.batch, "The game ends when you fall off.", 50, 300);
        }
        if (tutorialState %2 == 1)
            game.font.draw(game.batch, "Tap to Continue", 10, 50);
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
       // textures.dispose();
      //  splash.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;


        if (x > 750 && y> 430) {
            gameScreen.sound();
        }
        else {
            if (tutorialState %2 == 1)
                tutorialState++;
            if (tutorialState == 10) {
                leaving = true;
            }
        }
        //leaving = true;
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
