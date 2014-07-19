package com.jerhis.cloudgame;

public interface PlatformInterface {

    //This deals with the android's wakelock
    //0: normal
    //1: keep screen on
    void wakeLockMessenger(int k);

    //for the ads
    //-1 no response
    int adMessenger(int k);


}
