/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

public enum ModulesEnum {
    food(true), technic(true), xmas(true), trees(true), power(true);

    private final boolean enabledByDefault;

    ModulesEnum(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }

    public boolean getEnabledByDefault() {
        return enabledByDefault;
    }
}
