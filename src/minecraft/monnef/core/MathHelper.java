package monnef.core;

import monnef.jaffas.power.api.IPowerNodeCoordinates;

import java.text.DecimalFormat;

import static net.minecraft.util.MathHelper.ceiling_float_int;
import static net.minecraft.util.MathHelper.sqrt_float;

public class MathHelper {
    public static final DecimalFormat oneDecimalPlace = new DecimalFormat("#.#");

    public static int Square(int number) {
        return number * number;
    }

    public static float exactDistance(IPowerNodeCoordinates a, IPowerNodeCoordinates b) {
        return sqrt_float(Square(a.getX() - b.getX()) + Square(a.getY() - b.getY()) + Square(a.getZ() - b.getZ()));
    }

    public static int exactDistanceInt(IPowerNodeCoordinates a, IPowerNodeCoordinates b) {
        return ceiling_float_int(exactDistance(a, b));
    }

    public static float degToRad(float deg) {
        return (float) (deg * Math.PI / 180f);
    }
}
