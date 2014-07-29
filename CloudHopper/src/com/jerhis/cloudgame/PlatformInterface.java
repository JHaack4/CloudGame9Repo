package com.jerhis.cloudgame;

public interface PlatformInterface {

    //This deals with the android's wakelock
    //0: normal
    //1: keep screen on
    void wakeLockMessenger(int k);

    int adMessenger(int k, int last);

    int googleLogin(int in1out2);

    int leaderboard(int score);

    int achievement(int k);


}
