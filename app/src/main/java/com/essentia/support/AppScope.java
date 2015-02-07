package com.essentia.support;

/**
 * Created by kyawzinlatt94 on 2/6/15.
 */
public class AppScope {
    public static boolean isStarted = false;
    public boolean isActivityStarted(){
        return isStarted;
    }
    public void startActivity(){
        isStarted = true;
    }
}
