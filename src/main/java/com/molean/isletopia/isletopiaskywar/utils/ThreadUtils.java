package com.molean.isletopia.isletopiaskywar.utils;

public class ThreadUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSecond() {
        sleep(1000);
    }

    public static void sleepSeconds(int seconds) {
        sleep(seconds * 1000L);
    }
}
