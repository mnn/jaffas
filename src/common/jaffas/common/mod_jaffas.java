package jaffas.common;

import java.util.Hashtable;
import java.util.logging.Level;

import net.minecraft.src.*;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "moen-jaffas", name = "Jaffas", version = "0.3.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_jaffas {
    public static Hashtable<JaffaItem, JaffaItemInfo> ItemsInfo;

    public enum JaffaItem {
        pastry, cake, jamO, jamR, jaffaO, jaffaR, jaffa, chocolate, apples, beans, sweetBeans, butter, mallet
    }

    public static final int startID = 3753;
    private int actualID = startID;

    private int getID() {
        return this.actualID++;
    }

    private void AddItemInfo(JaffaItem item, String name, int iconIndex, String title) {
        JaffaItemInfo newItem = new JaffaItemInfo(name);
        newItem.setIconIndex(iconIndex);
        newItem.setTitle(title);
        ItemsInfo.put(item, newItem);
    }

    private void InitializeItemInfos() {
        ItemsInfo = new Hashtable<JaffaItem, JaffaItemInfo>();
        AddItemInfo(JaffaItem.pastry, "Pastry", 0, "Pastry");
        AddItemInfo(JaffaItem.cake, "Cake", 1, "Sponge Cake");
        AddItemInfo(JaffaItem.jamO, "Jam Orange", 2, "Orange Jam");
        AddItemInfo(JaffaItem.jamR, "Jam Red", 3, "Jam");
        AddItemInfo(JaffaItem.jaffaO, "Jaffa Orange", 4, "Orange Jaffa Cake");
        AddItemInfo(JaffaItem.jaffaR, "Jaffa Red", 5, "Jaffa Cake");
        AddItemInfo(JaffaItem.jaffa, "Jaffa", 6, "Jaffa Cake");
        AddItemInfo(JaffaItem.chocolate, "Chocolate", 7, "Chocolate");
        AddItemInfo(JaffaItem.apples, "Apples", 10, "Apples");
        AddItemInfo(JaffaItem.beans, "Beans", 8, "Cocoa Powder");
        AddItemInfo(JaffaItem.sweetBeans, "Sweet Beans", 9, "Sweet Cocoa Powder");
        AddItemInfo(JaffaItem.butter, "Butter", 12, "Butter");
        AddItemInfo(JaffaItem.mallet, "Mallet", 11, "Little Mallet");
    }

    public mod_jaffas() {
        InitializeItemInfos();
    }

    @SidedProxy(clientSide = "jaffas.common.ClientProxyTutorial", serverSide = "jaffas.common.CommonProxyTutorial")
    public static CommonProxyTutorial proxy;

    @PreInit
    public void PreLoad(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());

        try {
            config.load();


            for (JaffaItem item : JaffaItem.values()) {
                JaffaItemInfo info = ItemsInfo.get(item);
                int id = config.getOrCreateIntProperty(info.getConfigName(), Configuration.CATEGORY_ITEM, getID()).getInt();
                info.setId(id);
            }

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Mod Jaffas can't read config file.");
        } finally {
            config.save();
        }
    }

    private void finilizeItemSetup(JaffaItemInfo info, Item item) {
        item.setItemName(info.getTitle());
        item.setIconIndex(info.getIconIndex());
        info.setItem(item);
        LanguageRegistry.addName(item, info.getTitle());
    }

    private ItemJaffaBase createJaffaItem(JaffaItem ji) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaBase newJaffaItem = new ItemJaffaBase(info.getId());
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaFood createJaffaFood(JaffaItem ji, int healAmount, float saturation) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaFood newJaffaItem = new ItemJaffaFood(info.getId(), healAmount, saturation);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    @Init
    public void load(FMLInitializationEvent event) {

        createJaffaItem(JaffaItem.pastry);
        createJaffaItem(JaffaItem.jamO);
        createJaffaItem(JaffaItem.jamR);

        createJaffaFood(JaffaItem.cake, 1, 0.2F);
        createJaffaFood(JaffaItem.jaffaO, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 5, 1, 0.2F);
        createJaffaFood(JaffaItem.jaffaR, 3, 0.7F).setPotionEffect(Potion.regeneration.id, 5, 1, 0.2F);
        createJaffaFood(JaffaItem.jaffa, 2, 0.5F);

        createJaffaItem(JaffaItem.chocolate);
        createJaffaItem(JaffaItem.apples);
        createJaffaItem(JaffaItem.beans);
        createJaffaItem(JaffaItem.sweetBeans);
        createJaffaItem(JaffaItem.butter);
        createJaffaItem(JaffaItem.mallet);

        installRecipes();

        // texture stuff
        proxy.registerRenderThings();

        GameRegistry.registerCraftingHandler(new JaffaCraftingHandler());
    }

    private void installRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.pastry)),
                new Object[]{new ItemStack(Item.sugar),
                        new ItemStack(Item.egg), new ItemStack(getItem(JaffaItem.butter)),
                        new ItemStack(Item.wheat), new ItemStack(Item.wheat),
                        new ItemStack(Item.wheat)});

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.mallet)), new Object[]{"X",
                "Y", 'X', Block.planks, 'Y', Item.stick});

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.beans), 8),
                new Object[]{new ItemStack(getItem(JaffaItem.mallet)),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),
                        new ItemStack(Item.dyePowder, 1, 3),});

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.sweetBeans)),
                new Object[]{new ItemStack(getItem(JaffaItem.beans)),
                        new ItemStack(Item.sugar)});

        GameRegistry.addSmelting(getItem(JaffaItem.sweetBeans).shiftedIndex, new ItemStack(getItem(JaffaItem.chocolate)), 0.1F);

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.apples)),
                new Object[]{new ItemStack(Item.appleRed),
                        new ItemStack(Item.appleRed),
                        new ItemStack(Item.appleRed),
                        new ItemStack(Item.appleRed)});

        GameRegistry.addShapelessRecipe(new ItemStack(Item.appleRed, 4),
                new Object[]{new ItemStack(getItem(JaffaItem.apples))});

        GameRegistry.addSmelting(getItem(JaffaItem.apples).shiftedIndex, new ItemStack(
                getItem(JaffaItem.jamR)), 0.5F);

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jamO)), new Object[]{
                "X", "Y", 'X', new ItemStack(Item.dyePowder, 1, 14), 'Y',
                new ItemStack(getItem(JaffaItem.jamR))});

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffa), 12), new Object[]{
                "X", "Y", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.cake))});

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaR), 12), new Object[]{
                "X", "Y", "Z", 'X', new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                new ItemStack(getItem(JaffaItem.jamR)), 'Z', new ItemStack(getItem(JaffaItem.cake))});

        GameRegistry.addRecipe(new ItemStack(getItem(JaffaItem.jaffaO), 12),
                new Object[]{"X", "Y", "Z", 'X',
                        new ItemStack(getItem(JaffaItem.chocolate)), 'Y',
                        new ItemStack(getItem(JaffaItem.jamO)), 'Z',
                        new ItemStack(getItem(JaffaItem.cake))});

        GameRegistry.addShapelessRecipe(new ItemStack(getItem(JaffaItem.butter), 8),
                new Object[]{new ItemStack(getItem(JaffaItem.mallet)),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk),
                        new ItemStack(Item.bucketMilk)});

        GameRegistry.addSmelting(getItem(JaffaItem.pastry).shiftedIndex, new ItemStack(
                getItem(JaffaItem.cake)), 0.1F);
    }

    private Item getItem(JaffaItem item) {
        return ItemsInfo.get(item).getItem();
    }
}
