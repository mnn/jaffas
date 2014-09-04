package monnef.jaffas.trees;

import java.util.HashMap;
import java.util.HashSet;

public enum FruitType {
    Normal(0), Apple(1), Cocoa(2), Vanilla(3), Lemon(4), Orange(5), Plum(6), Coconut(7), Banana(8);
    private int value;
    private int blockNumber;
    private int metaNumber;

    static {
        helper.noSeedsAndFruit.add(Normal);
    }

    FruitType(int value) {
        this.value = value;
        this.blockNumber = value % 4;
        this.metaNumber = value / 4;

        helper.indexToFruitMap.put(value, this);
    }

    public int getValue() {
        return value;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public static FruitType indexToFruitType(int index) {
        return helper.indexToFruitMap.get(index);
    }

    public static boolean doesGenerateFruitAndSeeds(FruitType fruit) {
        return !helper.noSeedsAndFruit.contains(fruit);
    }

    public boolean doesGenerateFruitAndSeeds() {
        return doesGenerateFruitAndSeeds(this);
    }

    private static class helper {
        private static HashMap<Integer, FruitType> indexToFruitMap;
        private static HashSet<FruitType> noSeedsAndFruit;

        static {
            indexToFruitMap = new HashMap<Integer, FruitType>();
            noSeedsAndFruit = new HashSet<FruitType>();
        }
    }
}
