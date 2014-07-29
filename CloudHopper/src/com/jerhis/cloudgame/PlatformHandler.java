package com.jerhis.cloudgame;


public class PlatformHandler {

    public PlatformInterface platformInterface;

    public PlatformHandler(PlatformInterface platformInterface) {
        this.platformInterface = platformInterface;
    }


    public int lastWakeLockMessenger = 0;
    public final void wakeLock(int k) {
        if (k != lastWakeLockMessenger) {
            platformInterface.wakeLockMessenger(k);
            lastWakeLockMessenger = k;
        }
    }

    //1: show banner
    //2: hide banner
    //3: show interstitial
    //4: load next interstitial
    //5: menu banner
    //6: game banner
    public int lastAdMessenger = 0;
    public final int ad(int k) {
        int r = -2;
        if (k == 3)
            r = platformInterface.adMessenger(3,-1);
        else if (k == 4)
            r = platformInterface.adMessenger(4,-1);
        else if (lastAdMessenger != k) {
            r = platformInterface.adMessenger(k, lastAdMessenger);
            lastAdMessenger = k;
        }
        return r;
    }

    //1: attempt login
    //2: attempt logout
    public final int login1out2(int in1out2) {
        int r = platformInterface.googleLogin(in1out2);
        return r;
    }

    //-1: show leaderboard
    // n>0 add score
    public final int leaderboard(int score) {
        int r = platformInterface.leaderboard(score);
        return r;
    }

    //-1 show achievements
    //>0 call for achievement
    public final int achievement(int k) {
        int r = platformInterface.achievement(k);
        return r;
    }

}
