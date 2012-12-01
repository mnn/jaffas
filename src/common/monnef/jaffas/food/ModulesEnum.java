package monnef.jaffas.food;

public enum ModulesEnum {
    ores(true), xmas(true), trees(true);

    private final boolean enabledByDefault;

    ModulesEnum(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }

    public boolean getEnabledByDefault() {
        return enabledByDefault;
    }
}
