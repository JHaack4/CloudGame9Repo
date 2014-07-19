package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class SplashScreen implements Screen {
	
	final MyGdxGame game;	 
	OrthographicCamera camera;
	
	long startTime;
	Texture splash;
 
	public SplashScreen(final MyGdxGame gam) {
		game = gam;
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		splash = new Texture(Gdx.files.internal("splash.png"));
 
		startTime = System.currentTimeMillis();
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		game.batch.begin();
		game.batch.draw(splash, 0, -(512-480));
		game.batch.end();
		
		//waits for 2 seconds to move on
		if (System.currentTimeMillis() > 2000 + startTime)
		{
			game.setScreen(new MainMenuScreen(game, null));
			dispose();
		}
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
		splash.dispose();
	}
	
}
	