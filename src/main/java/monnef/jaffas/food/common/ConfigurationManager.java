/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import com.google.common.base.Joiner;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.achievement.AchievementsHandler;
import net.minecraftforge.common.config.Configuration;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

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
    public static String[] craftingTablesWithBrokenCraftingHandlerSupport;
    public static boolean villagerTrades;
    public static boolean disableThermalExpansionIntegration;

    private static final String[] craftingTablesWithBrokenCraftingHandlerSupportDefault;

    static {
        craftingTablesWithBrokenCraftingHandlerSupportDefault = new String[]{
                "thaumcraft.common.tiles.TileArcaneWorkbench"
        };
    }

    public static void loadSettings(Configuration config) {
        showUpdateMessages = config.get(CATEGORY_GENERAL, "showUpdateMessages", true).getBoolean(true);
        lastVersionShown = config.get(CATEGORY_GENERAL, JaffasFood.LAST_VERSION_SHOWN, "").getString();
        spawnStonesEnabled = config.get(CATEGORY_GENERAL, "spawnStonesEnable", true).getBoolean(true);
        spawnStoneLittleCD = config.get(CATEGORY_GENERAL, "spawnStoneLittleCD", 27).getInt();
        spawnStoneMediumCD = config.get(CATEGORY_GENERAL, "spawnStoneMediumCD", 17).getInt();
        spawnStoneBigCD = config.get(CATEGORY_GENERAL, "spawnStoneBigCD", 7).getInt();
        spawnStoneMultidimensional = config.get(CATEGORY_GENERAL, "spawnStoneMultidimensional", true).getBoolean(true);
        transferItemsFromCraftingMatrix = config.get(CATEGORY_GENERAL, "transferItemsFromCraftingMatrix", true, "Transfers items created after craft directly to a player (e.g. crumpled paper), without this enabled there can be problems with BC crafters").getBoolean(true);
        genDisabled = config.get(CATEGORY_GENERAL, "genDisabled", false, "This option applies to all modules").getBoolean(false);
        genDisabledForNonStandardDimensions = config.get(CATEGORY_GENERAL, "genDisabledForNonStandardDimensions", false, "This option applies to all modules").getBoolean(false);
        achievementsDisabled = config.get(CATEGORY_GENERAL, "achievementsDisabled", false).getBoolean(false);
        duckSpawnProbabilityLow = config.get(CATEGORY_GENERAL, "duckSpawnProbabilityLow", 10).getInt();
        duckSpawnProbabilityMed = config.get(CATEGORY_GENERAL, "duckSpawnProbabilityMed", 12).getInt();
        duckSpawnProbabilityHigh = config.get(CATEGORY_GENERAL, "duckSpawnProbabilityHigh", 16).getInt();
        FuelHandler.switchgrassBurnValue = config.get(CATEGORY_GENERAL, "switchgrassBurnValue", 100).getInt();
        slimeSpawningEnabled = config.get(CATEGORY_GENERAL, "slimeSpawningEnabled", true).getBoolean(true);
        vanillaRecipesEnabled = config.get(CATEGORY_GENERAL, "vanillaRecipesEnabled", true, "These are recipes producing vanilla items/blocks from vanilla items/blocks - e.g. grass block").getBoolean(true);
        dungeonLootEnabled = config.get(CATEGORY_GENERAL, "dungeonLootEnabled", true).getBoolean(true);
        disableAutoUnEquip = config.get(CATEGORY_GENERAL, "disableAutoUnEquip", false).getBoolean(false);
        AchievementsHandler.setStartingId(config.get(CATEGORY_GENERAL, "achievementOffset", 9790).getInt());
        craftingTablesWithBrokenCraftingHandlerSupport = config.get(CATEGORY_GENERAL, "craftingTablesWithBrokenCraftingHandlerSupport", craftingTablesWithBrokenCraftingHandlerSupportDefault,
                "Custom crafting tables (classes implementing IInventory) with broken or none support for crafting handlers. Default values: " + Joiner.on(", ").join(craftingTablesWithBrokenCraftingHandlerSupportDefault)).getStringList();
        villagerTrades = config.get(CATEGORY_GENERAL, "Custom villager trades enabled", true).getBoolean(true);
        disableThermalExpansionIntegration = config.get(CATEGORY_GENERAL, "disableThermalExpansionIntegration", false).getBoolean(false);
    }
}
