package monnef.core;

public class BitHelper {
    public static boolean isBitSet(int a, int bitNumber) {
        return (a & (1 << bitNumber)) != 0;
    }

    public static int setBit(int a, int bitNumber) {
        return a | (1 << bitNumber);
    }

    public static int unsetBit(int a, int bitNumber) {
        return a & ~(1 << bitNumber);
    }

    public static int toggleBit(int a, int bitNumber) {
        return a ^ (1 << bitNumber);
    }
}
