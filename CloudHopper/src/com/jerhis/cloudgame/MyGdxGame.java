package com.jerhis.cloudgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MyGdxGame extends Game {

    private PlatformHandler platformHandeler;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
	BitmapFont font;
	Preferences prefs;
	CloudGame g;
	public boolean debug = false;
    public int highScore = 0;
	public boolean tiltControls = false;
    public int gamesPlayed = 0;
    boolean sound = true;
    public float slider = 1;
    public boolean seenTutorial = false;
	String fileNameHighScore = "bestscore";
	String fileNameTiltControls = "tiltcontrols";
    String fileNameGamesPlayed = "gamesplayed";
    String fileNameSound = "sound";
    String fileNameSlider = "slider";
    String fileNameTutorial = "tutorial";
	float accelX = 0, accelY = 0, accelZ = 0;
    final boolean showAds = true;
    public boolean loggedInToGoogle= false;
    private String fileNameUnsavedBest= "unbest";
    private String fileNameUnsavedAchievements = "unach";

    public MyGdxGame(PlatformInterface platformInterface) {
        platformHandeler = new PlatformHandler(platformInterface);
    }

	public void create() {
		
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
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
        gamesPlayed = prefs.getInteger(fileNameGamesPlayed, 0);
        sound = prefs.getBoolean(fileNameSound, true);
        slider = prefs.getFloat(fileNameSlider, 0.8f);
        seenTutorial = prefs.getBoolean(fileNameTutorial, false);

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

    public void wakeLock(int k) {
       platformHandeler.wakeLock(k);
    }

    boolean adOrLoad = true;
    public int ad(int k) {
        if (k==6) k=5;
        if (adOrLoad && k == 3) {
            adOrLoad = false;
        }
        else if (!adOrLoad && k == 3) {
            adOrLoad = true;
            k = 4;
        }
        return platformHandeler.ad(k);
    }

    public int login1out2(int k) {
        int r = platformHandeler.login1out2(k);
        if (r==-13) loggedInToGoogle = !loggedInToGoogle;

        return r;
    }

    public int leaderboard(int k) {
        return platformHandeler.leaderboard(k);
    }

    public int achievement(int k) {
        if (k == -1) {
            platformHandeler.achievement(-1);
            return 0;
        }

        if (k == -7) {
            platformHandeler.achievement(7);
            return 0;
        }

        //call for an achievement here tp platformHandler when attained
        if (k >= 10000) platformHandeler.achievement(0);
        if (k >= 25000) platformHandeler.achievement(1);
        if (k >= 50000) platformHandeler.achievement(2);
        if (k >= 100000) platformHandeler.achievement(3);
        if (k >= 250000) platformHandeler.achievement(4);

        platformHandeler.achievement(5);
        platformHandeler.achievement(6);

        return 0;
    }

    public void failedLeaderboard(int score) {
        int unsaved = prefs.getInteger(fileNameUnsavedBest, -1);
        if (score > unsaved) {
            prefs.putInteger(fileNameUnsavedBest, score);
            prefs.flush();
        }
    }

    public void failedAchievement(int achievement) {
        String unsaved = prefs.getString(fileNameUnsavedAchievements, "");
        char ach = (char)(achievement + 'A');
        prefs.putString(fileNameUnsavedAchievements, unsaved + "" + ach);
        prefs.flush();
    }

    public void tryToSave() {
        if (prefs == null) prefs = Gdx.app.getPreferences(".cloudgame");
        int unsaved = prefs.getInteger(fileNameUnsavedBest, -1);
        if (unsaved > 0) {
            prefs.putInteger(fileNameUnsavedBest, -1);
            prefs.flush();
            platformHandeler.leaderboard(unsaved);
        }
        String unach = prefs.getString(fileNameUnsavedAchievements, "");
        if (! unach.equals("")) {
            for (int q = 0; q < unach.length(); q++) {
                int c = unach.charAt(q) - 'A';
                platformHandeler.achievement(c);
            }
            prefs.putString(fileNameUnsavedAchievements, "");
            prefs.flush();
        }
    }

 
	public void render() {
		super.render(); // important!
	}
 
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

    public void onBackPressed() {
        if (getScreen() instanceof MainMenuScreen) {
            ((MainMenuScreen)(getScreen())).onBackPressed();
        }
    }
}
