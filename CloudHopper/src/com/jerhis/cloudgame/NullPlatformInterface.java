package com.jerhis.cloudgame;

public class NullPlatformInterface implements PlatformInterface {

    @Override
    public void wakeLockMessenger(int k) {
        //do nothing
    }

    @Override
    public int adMessenger(int k, int last) {
        //do nothing
        return -1;
    }

    @Override
    public int googleLogin(int k) {
        return -13;
    }

    @Override
    public int leaderboard(int score) {
        return -1;
    }

    @Override
    public int achievement(int k, int howMany) {
        return -1;
    }

    @Override
    public int achievement(int k) {
        return -1;
    }
}
