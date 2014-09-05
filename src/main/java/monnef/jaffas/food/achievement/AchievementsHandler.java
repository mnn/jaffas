/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.utils.CallerFinder;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.JaffasException;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.common.ContentHolder.getItem;
import static monnef.jaffas.food.item.JaffaItem.butter;
import static monnef.jaffas.food.item.JaffaItem.flour;
import static monnef.jaffas.food.item.JaffaItem.fryingPan;
import static monnef.jaffas.food.item.JaffaItem.knifeKitchen;
import static monnef.jaffas.food.item.JaffaItem.mallet;
import static monnef.jaffas.food.item.JaffaItem.malletDiamond;
import static monnef.jaffas.food.item.JaffaItem.malletHead;
import static monnef.jaffas.food.item.JaffaItem.malletHeadDiamond;
import static monnef.jaffas.food.item.JaffaItem.malletHeadIron;
import static monnef.jaffas.food.item.JaffaItem.malletHeadStone;
import static monnef.jaffas.food.item.JaffaItem.malletIron;
import static monnef.jaffas.food.item.JaffaItem.malletStone;
import static monnef.jaffas.food.item.JaffaItem.meatCleaver;
import static monnef.jaffas.food.item.JaffaItem.milkBoxEmpty;
import static monnef.jaffas.food.item.JaffaItem.milkBoxFull;

public class AchievementsHandler {
    private static AchievementPage page;

    // item id -> achiev
    private static HashMap<Item, Achievement> craftAchievement = new HashMap<Item, Achievement>();

    // achiev ID -> achiev
    private static HashMap<String, Achievement> allAchievements = new HashMap<String, Achievement>();

    // one of needed achievements -> combined achievement
    private static HashMultimap<String, CombinedAchievement> combinedAchievementLookupMap = HashMultimap.create();

    private static int idCounter = -1;
    private static boolean initialized = false;

    public static void init() {
        if (ConfigurationManager.achievementsDisabled) {
            Log.printInfo("Achievements disabled in config, skipping registrations.");
            return;
        }
        if (initialized) {
            Log.printSevere("AchievementsHandler reinitialization by " + CallerFinder.getCallerClassName(1));
        }
        if (idCounter == -1) {
            throw new RuntimeException("Counter not initialized.");
        }

        FMLCommonHandler.instance().bus().register(new AchievementHooksHandler());

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

        addAchievement(milkBoxEmpty, "MilkBoxEmptyAch", 0, 3, getItem(milkBoxEmpty), null, false, "Empty Milk Box", "Grab some paper and place it in a rectangular shape.");
        addAchievement(milkBoxFull, "MilkBoxAch", 2, 3, getItem(milkBoxFull), getAchievementFromJaffaItem(milkBoxEmpty), false, "Milk", "Fill empty milk boxes with a bucket with milk.");

        addAchievement(butter, "ButterAch", 0, 4, getItem(butter), null, false, "Cheap Gold", "Use mallet and milk to churn some butter.");

        addAchievement(knifeKitchen, "KnifeAch", 3, 10, getItem(knifeKitchen), null, false, "Going Sharp", "Combine some iron and a stick.");
        addAchievement(fryingPan, "PanAch", 3, 11, getItem(fryingPan), null, false, "Frying!", "From one jaffarrol ingot and four iron ones create a frying pan!");
        addAchievement(meatCleaver, "CleaverAch", 3, 12, getItem(meatCleaver), null, false, "Choppin'", "From one jaffarrol ingot and four iron ones create a meat cleaver.");

        addCombinedAchievement("LordOfToolsAch", 5, 11, getItem(knifeKitchen), null, true, "Lord Of Tools", "", getCraftingAchievementIdsFromJaffaItems(knifeKitchen, fryingPan, meatCleaver));
    }

    private static String[] getCraftingAchievementIdsFromJaffaItems(JaffaItem... items) {
        String[] res = new String[items.length];
        for (int i = 0; i < items.length; i++) res[i] = getAchievementFromJaffaItem(items[i]).statId;
        return res;
    }

    private static Achievement getAchievementFromJaffaItem(JaffaItem item) {
        return craftAchievement.get(getItem(item));
    }

    public static Achievement addAchievement(Item item, String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc) {
        Achievement ach = generateAndRegisterAchievement(name, xCoord, yCoord, icon, requiredInTree, special, title, desc);

        if (craftAchievement.containsKey(item)) {
            throw new RuntimeException("overriding crafting achievement with item #" + item.getUnlocalizedName() + ", name \"" + title + "\"");
        }
        craftAchievement.put(item, ach);
        return ach;
    }

    private static Achievement addAchievement(JaffaItem item, String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc) {
        return addAchievement(getItem(item), name, xCoord, yCoord, icon, requiredInTree, special, title, desc);
    }

    private static Achievement generateAndRegisterAchievement(String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc) {
        Achievement ach = null;
        String newName = "jaffas." + name;
        String newId = "achievement." + newName;

        if (icon instanceof Item) {
            ach = new Achievement(newId, newName, xCoord, yCoord, (Item) icon, requiredInTree);
        } else if (icon instanceof Block) {
            ach = new Achievement(newId, newName, xCoord, yCoord, (Block) icon, requiredInTree);
        }
        if (ach == null) throw new JaffasException("wrong icon object");

        if (special) ach.setSpecial();
        ach.registerStat();

        allAchievements.put(ach.statId, ach);
        return ach;
    }

    private static int generateAchievementId() {
        return idCounter++;
    }

    public static void addCombinedAchievement(String name, int xCoord, int yCoord, Object icon, Achievement requiredInTree, boolean special, String title, String desc, String[] achievementsNeeded) {
        Achievement ach = generateAndRegisterAchievement(name, xCoord, yCoord, icon, requiredInTree, special, title, desc);
        CombinedAchievement ca = new CombinedAchievement(ach.statId, achievementsNeeded);
        for (String num : achievementsNeeded) {
            combinedAchievementLookupMap.put(num, ca);
        }
    }

    private static void createPage() {
        Set<Achievement> achievs = new HashSet<Achievement>();

        achievs.addAll(allAchievements.values());

        Achievement[] achievements = achievs.toArray(new Achievement[0]);
        page = new AchievementPage("Jaffas and more!", achievements);
        AchievementPage.registerAchievementPage(page);
    }

    public static boolean hasPlayerAchievement(EntityPlayer player, String achievId) {
        return hasPlayerAchievement(player, getAchievement(achievId));
    }

    public static boolean hasPlayerAchievement(EntityPlayer player, Achievement achievement) {
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).func_147099_x().hasAchievementUnlocked(achievement);
        } else if (player instanceof EntityPlayerSP) {
            return ((EntityClientPlayerMP) player).getStatFileWriter().hasAchievementUnlocked(achievement);
        }
        throw new RuntimeException("Can only check EntityPlayerMP and EntityPlayerSP.");
    }

    public static boolean craftAchievementExists(Item item) {
        return getCraftAchievement(item) != null;
    }

    public static Achievement getCraftAchievement(Item item) {
        return craftAchievement.get(item);
    }

    public static void craftAchievementCompleted(Item item, EntityPlayer player) {
        Achievement achievement = getCraftAchievement(item);
        if (achievement == null) {
            Log.printSevere(String.format("Achievement for item %s not found!", item.getUnlocalizedName()));
            return;
        }

        completeAchievement(achievement, player);
    }

    public static void completeAchievement(String id, EntityPlayer player) {
        Achievement ach = getAchievement(id);
        if (ach == null) {
            throw new NullPointerException("achievement not found, server sent corrupted information?");
        }

        completeAchievement(ach, player);
    }

    public static void completeAchievement(Achievement ach, EntityPlayer player) {
        player.addStat(ach, 1);

        Set<CombinedAchievement> suspects = combinedAchievementLookupMap.get(ach.statId);
        for (CombinedAchievement suspect : suspects) {
            if (suspect.checkPlayer(player)) {
                completeAchievement(getAchievement(suspect.getAchievementId()), player);
            }
        }
    }

    public static Achievement getAchievement(String id) {
        return allAchievements.get(id);
    }

    public static void setStartingId(int offset) {
        idCounter = offset;
    }
}
