package monnef.jaffas.trees;

public enum BushType {
    Coffee, Strawberry, Onion, Paprika, Raspberry, Tomato, Mustard, Peanuts, Pea, Bean;

    public static boolean isFruit(BushType type) {
        switch (type) {
            case Strawberry:
            case Raspberry:
                return true;
        }
        return false;
    }
}
