/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.achievement.AchievementsHandler;
import net.minecraftforge.common.Configuration;

public class ConfigurationManager {
    public static String jaffasTitle;
    public static String jaffaTitle;
    public static boolean showUpdateMessages;
    public static String lastVersionShown;
    public static boolean spawnStonesEnabled = true;
    public static int spawnStoneLittleCD;
    public static int spawnStoneMediumCD;
    public static int spawnStoneBigCD;
    public static boolean spawnStoneMultidimensional;
    public static boolean transferItemsFromCraftingMatrix;
    public static boolean genDisabled;
    public static boolean genDisabledForNonStandardDimensions;
    public static boolean achievementsDisabled;
    public static int duckSpawnProbabilityHigh;
    public static int duckSpawnProbabilityMed;
    public static int duckSpawnProbabilityLow;
    public static boolean slimeSpawningEnabled;
    public static boolean vanillaRecipesEnabled;
    public static boolean dungeonLootEnabled;
    public static boolean disableAutoUnEquip;
    public static boolean useOldConfigItemNames;

    public static void loadSettings(Configuration config) {
        showUpdateMessages = config.get(Configuration.CATEGORY_GENERAL, "showUpdateMessages", true).getBoolean(true);
        lastVersionShown = config.get(Configuration.CATEGORY_GENERAL, JaffasFood.LAST_VERSION_SHOWN, "").getString();
        spawnStonesEnabled = config.get(Configuration.CATEGORY_GENERAL, "spawnStonesEnable", true).getBoolean(true);
        spawnStoneLittleCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneLittleCD", 27).getInt();
        spawnStoneMediumCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMediumCD", 17).getInt();
        spawnStoneBigCD = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneBigCD", 7).getInt();
        spawnStoneMultidimensional = config.get(Configuration.CATEGORY_GENERAL, "spawnStoneMultidimensional", true).getBoolean(true);
        transferItemsFromCraftingMatrix = config.get(Configuration.CATEGORY_GENERAL, "transferItemsFromCraftingMatrix", true, "Transfers items created after craft directly to a player (e.g. crumpled paper), without this enabled there can be problems with BC crafters").getBoolean(true);
        genDisabled = config.get(Configuration.CATEGORY_GENERAL, "genDisabled", false, "This option applies to all modules").getBoolean(false);
        genDisabledForNonStandardDimensions = config.get(Configuration.CATEGORY_GENERAL, "genDisabledForNonStandardDimensions", false, "This option applies to all modules").getBoolean(false);
        achievementsDisabled = config.get(Configuration.CATEGORY_GENERAL, "achievementsDisabled", false).getBoolean(false);
        duckSpawnProbabilityLow = config.get(Configuration.CATEGORY_GENERAL, "duckSpawnProbabilityLow", 10).getInt();
        duckSpawnProbabilityMed = config.get(Configuration.CATEGORY_GENERAL, "duckSpawnProbabilityMed", 12).getInt();
        duckSpawnProbabilityHigh = config.get(Configuration.CATEGORY_GENERAL, "duckSpawnProbabilityHigh", 16).getInt();
        FuelHandler.switchgrassBurnValue = config.get(Configuration.CATEGORY_GENERAL, "switchgrassBurnValue", 100).getInt();
        slimeSpawningEnabled = config.get(Configuration.CATEGORY_GENERAL, "slimeSpawningEnabled", true).getBoolean(true);
        JaffasRegistryHelper.compatibilityMode = config.get(Configuration.CATEGORY_GENERAL, "dontPrefixTileEntityIDs", false, "Set to true if you're playing map created with 0.4.20 or older. Do not use in new worlds, because it will be eventually removed.").getBoolean(false);
        vanillaRecipesEnabled = config.get(Configuration.CATEGORY_GENERAL, "vanillaRecipesEnabled", true, "These are recipes producing vanilla items/blocks from vanilla items/blocks - e.g. grass block").getBoolean(true);
        dungeonLootEnabled = config.get(Configuration.CATEGORY_GENERAL, "dungeonLootEnabled", true).getBoolean(true);
        disableAutoUnEquip = config.get(Configuration.CATEGORY_GENERAL, "disableAutoUnEquip", false).getBoolean(false);
        AchievementsHandler.setStartingId(config.get(Configuration.CATEGORY_GENERAL, "achievementOffset", 9790).getInt());
        useOldConfigItemNames = config.get(Configuration.CATEGORY_GENERAL, "useOldConfigItemNames", false, "If you're updating and you want to use your world save then set this option to true.").getBoolean(false);
    }
}
