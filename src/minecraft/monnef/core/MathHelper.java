package monnef.core;

import java.text.DecimalFormat;

public class MathHelper {
    public static final DecimalFormat oneDecimalPlace = new DecimalFormat("#.#");

    public static int Square(int number) {
        return number * number;
    }
}
