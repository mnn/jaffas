package monnef.jaffas.food.common;

public enum ModulesEnum {
    food(true), ores(true), xmas(true), trees(true), power(false), carts(false);

    private final boolean enabledByDefault;

    ModulesEnum(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }

    public boolean getEnabledByDefault() {
        return enabledByDefault;
    }
}
