/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.CallerClassNameFinder;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.JaffasException;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.JaffasFood.getItem;
import static monnef.jaffas.food.achievement.AchievementDataHolder.ACHIEVEMENT_DATA_HOLDER;
import static monnef.jaffas.food.item.JaffaItem.flour;
import static monnef.jaffas.food.item.JaffaItem.mallet;
import static monnef.jaffas.food.item.JaffaItem.malletDiamond;
import static monnef.jaffas.food.item.JaffaItem.malletHead;
import static monnef.jaffas.food.item.JaffaItem.malletHeadDiamond;
import static monnef.jaffas.food.item.JaffaItem.malletHeadIron;
import static monnef.jaffas.food.item.JaffaItem.malletHeadStone;
import static monnef.jaffas.food.item.JaffaItem.malletIron;
import static monnef.jaffas.food.item.JaffaItem.malletStone;

public class AchievementsHandler {
    private static AchievementPage page;

    // item id -> achiev
    private static HashMap<Integer, Achievement> craftAchievement = new HashMap<Integer, Achievement>();

    // achiev ID -> achiev
    private static HashMap<Integer, Achievement> allAchievements = new HashMap<Integer, Achievement>();

    // one of needed achievements -> combined achievement
    private static HashMultimap<Integer, CombinedAchievement> combinedAchievementLookupMap = HashMultimap.create();

    private static int idCounter = 9790;
    private static boolean initialized = false;

    public static void init() {
        if (JaffasFood.achievementsDisabled) {
            Log.printInfo("Achievements disabled in config, skipping registrations.");
            return;
        }
        if (initialized) {
            Log.printSevere("AchievementsHandler reinitialization by " + CallerClassNameFinder.getCallerClassName(1));
        }

        AchievementHooksHandler handler = new AchievementHooksHandler();
        GameRegistry.registerCraftingHandler(handler);
        MinecraftForge.EVENT_BUS.register(handler);

        createAchievements();
        createPage();
        initialized = true;
    }

    private static void createAchievements() {
        addAchievement(flour, "FlourAch", 0, 2, getItem(flour), null, false, "Flour", "Make a pack of flour from wheat and paper.");

        addAchievement(malletHead, "MalletHeadWoodAch", 1, 0, getItem(malletHead), null, false, "Wooden Mallet Head", "Use a string, a piece of wood and planks to make a mallet head.");
        addAchievement(mallet, "MalletWoodAch", 3, 0, getItem(mallet), getAchievementFromJaffaItem(malletHead), false, "Wooden Mallet", "Use sticks and a mallet head to make a mallet.");

        addAchievement(malletHeadStone, "MalletHeadStoneAch", 1, -1, getItem(malletHeadStone), null, false, "Stone Mallet Head", "Use strings, a piece of stone and a bunch of cobblestone to make a mallet head.");
        addAchievement(malletStone, "MalletStoneAch", 3, -1, getItem(malletStone), getAchievementFromJaffaItem(malletHeadStone), false, "Stone Mallet", "Use sticks and a mallet head to make a mallet.");

        addAchievement(malletHeadIron, "MalletHeadIronAch", 1, -2, getItem(malletHeadIron), null, false, "Iron Mallet Head", "Use strings, a block of iron and a bunch of stone to make a mallet head.");
        addAchievement(malletIron, "MalletIronAch", 3, -2, getItem(malletIron), getAchievementFromJaffaItem(malletHeadIron), false, "Iron Mallet", "Use sticks and a mallet head to make a mallet.");

        addAchievement(malletHeadDiamond, "MalletHeadDiaAch", 1, -3, getItem(malletHeadDiamond), null, false, "Diamond Mallet Head", "Use strings, slimeballs, iron ingots and a block of diamond to make a mallet head.");
        addAchievement(malletDiamond, "MalletDiaAch", 3, -3, getItem(malletDiamond), getAchievementFromJaffaItem(malletHeadDiamond), false, "Diamond Mallet", "Use sticks and a mallet head to make a mallet.");

        addCombinedAchievement("MalletMasterAch", 5, 0, getItem(malletDiamond), null, true, "Mallet Master", "", getCraftingAchievementIdsFromJaffaItems(mallet, malletStone, malletIron, malletDiamond));

        if (MonnefCorePlugin.debugEnv)
            addCombinedAchievement("MalletMaster2Ach", 5, -2, getItem(mallet), null, true, "Mallet Master 2", "", getCraftingAchievementIdsFromJaffaItems(malletHead));
    }

    private static Integer[] getCraftingAchievementIdsFromJaffaItems(JaffaItem... items) {
        Integer[] res = new Integer[items.length];
        for (int i = 0; i < items.length; i++) res[i] = getAchievementFromJaffaItem(items[i]).statId;
        return res;
    }

    private static Achievement getAchievementFromJaffaItem(JaffaItem item) {
        return craftAchievement.get(getItemID(item));
    }

    private static Achievement addAchievement(JaffaItem item, String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc) {
        Achievement ach = generateAndRegisterAchievement(name, xCoord, yCoord, icon, requiredInTree, special, title, desc);

        craftAchievement.put(getItemID(item), ach);
        return ach;
    }

    private static Achievement generateAndRegisterAchievement(String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc) {
        Achievement ach = null;
        int newId = generateAchievementId();

        if (icon instanceof Item) {
            ach = new Achievement(newId, name, xCoord, yCoord, (Item) icon, requiredInTree);
        } else if (icon instanceof Block) {
            ach = new Achievement(newId, name, xCoord, yCoord, (Block) icon, requiredInTree);
        }
        if (ach == null) throw new JaffasException("wrong icon object");

        if (special) ach.setSpecial();
        ach.registerAchievement();

        addAchievementName(name, title);
        addAchievementDesc(name, desc);
        allAchievements.put(ach.statId, ach);
        return ach;
    }

    private static int generateAchievementId() {
        return idCounter++;
    }

    private static void addCombinedAchievement(String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc, Integer[] achievementsNeeded) {
        Achievement ach = generateAndRegisterAchievement(name, xCoord, yCoord, icon, requiredInTree, special, title, desc);
        CombinedAchievement ca = new CombinedAchievement(ach.statId, achievementsNeeded);
        for (int num : achievementsNeeded) {
            combinedAchievementLookupMap.put(num, ca);
        }
    }

    private static void addAchievementName(String ach, String name) {
        LanguageRegistry.instance().addStringLocalization("achievement." + ach, "en_US", name);
    }

    private static void addAchievementDesc(String ach, String desc) {
        LanguageRegistry.instance().addStringLocalization("achievement." + ach + ".desc", "en_US", desc);
    }

    private static void createPage() {
        Set<Achievement> achievs = new HashSet<Achievement>();

        achievs.addAll(allAchievements.values());

        Achievement[] achievements = achievs.toArray(new Achievement[0]);
        page = new AchievementPage("Jaffas and more!", achievements);
        AchievementPage.registerAchievementPage(page);
    }

    private static int getItemID(JaffaItem jItem) {
        return getItem(jItem).itemID;
    }

    public static boolean hasPlayerAchievement(EntityPlayer player, int achievId) {
        return getAchievementHolder(player).hasAchievement(achievId);
    }

    public static boolean craftAchievementExists(int itemId) {
        return getCraftAchievement(itemId) != null;
    }

    public static Achievement getCraftAchievement(int itemId) {
        return craftAchievement.get(itemId);
    }

    public static void craftAchievementCompleted(int itemId, EntityPlayer player) {
        Achievement achievement = getCraftAchievement(itemId);
        if (achievement == null) {
            Log.printSevere(String.format("Achievement for item #%d not found!", itemId));
            return;
        }

        completeAchievement(achievement, player);
    }

    private static AchievementDataHolder getAchievementHolder(EntityPlayer player) {
        if (player == null) {
            throw new NullPointerException("player");
        }

        IExtendedEntityProperties properties = player.getExtendedProperties(ACHIEVEMENT_DATA_HOLDER);
        if (properties == null) {
            throw new NullPointerException("properties");
        }

        return (AchievementDataHolder) properties;
    }

    public static Achievement getAchievement(int id) {
        return allAchievements.get(id);
    }

    public static void completeAchievement(int id, EntityPlayer player) {
        Achievement ach = getAchievement(id);
        if (ach == null) {
            throw new NullPointerException("achievement not found, server sent corrupted information?");
        }

        completeAchievement(ach, player);
    }

    public static void completeAchievement(Achievement ach, EntityPlayer player) {
        player.addStat(ach, 1);
        getAchievementHolder(player).markAchievementCompleted(ach.statId);

        Set<CombinedAchievement> suspects = combinedAchievementLookupMap.get(ach.statId);
        for (CombinedAchievement suspect : suspects) {
            if (suspect.checkPlayer(player)) {
                completeAchievement(getAchievement(suspect.getAchievementId()), player);
            }
        }
    }

    public static void removeAchievement(Achievement ach, EntityPlayer player) {
        player.addStat(ach, 0);
        getAchievementHolder(player).markAchievementNotCompleted(ach.statId);
    }

    public static void removeAllJaffasAchievements(EntityPlayer player) {
        for (Achievement ach : allAchievements.values()) {
            removeAchievement(ach, player);
        }
    }

    public static void synchronizeAchievements(EntityPlayer player) {
        //getAchievementHolder(player).sendSyncPackets();
        getAchievementHolder(player).recreateAchievements();
    }
}
