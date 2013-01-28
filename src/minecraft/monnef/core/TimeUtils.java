package monnef.core;

public class TimeUtils {
    public static long getCurrentTimeInSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}
