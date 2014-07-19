package com.jerhis.cloudgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CloudGame";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new MyGdxGame(new NullPlatformInterface()), cfg);
	}
}
