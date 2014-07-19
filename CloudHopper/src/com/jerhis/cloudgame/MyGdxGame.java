package com.jerhis.cloudgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {

    PlatformHandler platformHandeler;
    SpriteBatch batch;
	BitmapFont font;
	Preferences prefs;
	CloudGame g;
	public boolean debug = false;
    public int highScore = 0;
	public boolean tiltControls = false;
    public boolean[] badges = new boolean[10];
    public int gamesPlayed = 0;
    boolean sound = true;
    public float slider = 1;
    public boolean seenTutorial = false;
	String fileNameHighScore = "bestscore";
	String fileNameTiltControls = "tiltcontrols";
    String fileNameBadges = "badge";
    String fileNameGamesPlayed = "gamesplayed";
    String fileNameSound = "sound";
    String fileNameSlider = "slider";
    String fileNameTutorial = "tutorial";
	float accelX = 0, accelY = 0, accelZ = 0;

    public MyGdxGame(PlatformInterface platformInterface) {
        platformHandeler = new PlatformHandler(platformInterface);
    }

	public void create() {
		
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
		//font.setColor(new Color(0,1,0,1));
		//font.setScale(0.5f);
        //Constantes.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        /*float densityIndependentSize = origFontSize * Gdx.graphics.getDensity();
        int fontSize = Math.round(densityIndependentSize );
        BitmapFont font = generator.generateFont(fontSize );*/
		
		this.prefs = Gdx.app.getPreferences(".cloudgame");
        highScore = decrypt(prefs.getString(fileNameHighScore, "0"));
        //highScore = prefs.getInteger("highscore0", 0);
		tiltControls = prefs.getBoolean(fileNameTiltControls, false);
        for (int k = 0; k < 10; k++)
            badges[k] = prefs.getBoolean(fileNameBadges + k, false);
        gamesPlayed = prefs.getInteger(fileNameGamesPlayed, 0);
        sound = prefs.getBoolean(fileNameSound, true);
        slider = prefs.getFloat(fileNameSlider, 1);
        seenTutorial = prefs.getBoolean(fileNameTutorial, false);
		//seenTutorial = false;

		g = new CloudGame(this);
		this.setScreen(new SplashScreen(this));
	}

    public void highScore(int score) {
        //sets high score to current highscore
        if (score > highScore) {
            prefs.putString(fileNameHighScore, encrypt(score));
            prefs.flush();
            highScore = score;
        }
    }
	
	public void setControls(boolean toTiltControls) {
		tiltControls = toTiltControls;
		prefs.putBoolean(fileNameTiltControls, toTiltControls);
		prefs.flush();
	}

    public void setBadge(int badgeIndex) {
        badges[badgeIndex] = true;
        prefs.putBoolean(fileNameBadges + badgeIndex, true);
        prefs.flush();
    }

    public void increaseGamesPlayed() {
        gamesPlayed++;
        prefs.putInteger(fileNameGamesPlayed, gamesPlayed);
        prefs.flush();
    }

    public void toggleSound() {
        sound = !sound;
        prefs.putBoolean(fileNameSound, sound);
        prefs.flush();
    }

    public void setSlider(float s) {
        this.slider = s;
        prefs.putFloat(fileNameSlider, slider);
        prefs.flush();
    }

    public void setSeenTutorial(boolean t) {
        this.seenTutorial = t;
        prefs.putBoolean(fileNameTutorial, seenTutorial);
        prefs.flush();
    }

    public String encrypt(int score) {
        String i = Integer.toBinaryString(score);
        String e = "";
        int amod2 = 0, amod10 = 0, amod23 = 0;
        for (int k = 0; k < i.length(); k++) {
            int c = i.charAt(k) - '0' + 'a' + 2*((int)(Math.random()*10));
            amod2 += c;
            amod10 += c;
            amod23 += c;
            e = e + (char)c;
        }
        e = e + (char)(amod2%2 + 'j') + (char)(amod10%10 + 'd') + (char)(amod23%23 + 'b');
        return e;
    }

    public int decrypt(String e) {
        if (e.length() < 4) return 0;
        int amod2 = 0, amod10 = 0, amod23 = 0;
        String i = "";
        for (int k = 0; k < e.length()-3; k++) {
            int c = (e.charAt(k) - 'a')%2 + '0';
            amod2 += e.charAt(k);
            amod10 += e.charAt(k);
            amod23 += e.charAt(k);
            i = i + (char)c;
        }
        boolean b = (char)(amod2%2 + 'j') == e.charAt(e.length()-3) && (char)(amod10%10 + 'd') == e.charAt(e.length()-2) && (char)(amod23%23 + 'b') == e.charAt(e.length()-1);
        if (b) return Integer.parseInt(i, 2);
        else return 0;
    }

    public int ad(int k) {
        return platformHandeler.ad(k);
    }
 
	public void render() {
		super.render(); // important!
	}
 
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
 
}