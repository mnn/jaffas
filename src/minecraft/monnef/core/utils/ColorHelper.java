package monnef.core.utils;

public class ColorHelper {
    public static int getInt(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }
}
