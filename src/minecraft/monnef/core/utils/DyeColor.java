package monnef.core.utils;

public enum DyeColor {
    // "black", "red", "green", "brown",
    // "blue", "purple", "cyan", "silver",
    // "gray", "pink", "lime", "yellow",
    // "lightBlue", "magenta", "orange", "white"

    BLACK("black"), RED("red"), GREEN("green"), BROWN("brown"), BLUE("blue"), PURPLE("purple"), CYAN("cyan"), SILVER("silver"),
    GRAY("gray"), PINK("pink"), LIME("lime"), YELLOW("yellow"), L_BLUE("lightBlue"), MAGENTA("magenta"), ORANGE("orange"), WHITE("white");

    private final String color;

    DyeColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
