package com.jerhis.cloudgame;

public class NullPlatformInterface implements PlatformInterface {

    @Override
    public void wakeLockMessenger(int k) {
        //do nothing
    }

    @Override
    public int adMessenger(int k) {
        //do nothing
        return -1;
    }
}
