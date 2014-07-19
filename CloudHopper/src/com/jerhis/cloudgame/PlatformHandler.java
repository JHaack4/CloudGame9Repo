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

    public int lastAdMessenger = 0;
    public final int ad(int k) {
        int r = -2;
        if (k != lastAdMessenger) {
            r = platformInterface.adMessenger(k);
            lastAdMessenger = k;
        }
        return r;
    }

}
