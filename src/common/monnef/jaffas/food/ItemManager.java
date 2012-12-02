package monnef.jaffas.food;

import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.IDProvider;
import net.minecraft.src.Item;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class ItemManager {
    public static LinkedHashMap<JaffaItem, JaffaItemInfo> ItemsInfo;
    public static JaffaItem[] mallets;
    public static JaffaItem[] malletHeads;

    private static Hashtable<ModulesEnum, Hashtable<JaffaItemType, Class<? extends Item>>> ClassMapping;

    static {
        ItemsInfo = new LinkedHashMap<JaffaItem, JaffaItemInfo>();
        ClassMapping = new Hashtable<ModulesEnum, Hashtable<JaffaItemType, Class<? extends Item>>>();
    }

    public static void LoadItemsFromConfig(ModulesEnum module, IDProvider idProvider) {
        for (JaffaItem item : JaffaItem.values()) {
            JaffaItemInfo info = ItemManager.ItemsInfo.get(item);
            if (info == null) {
                throw new RuntimeException("got null in item list - " + item);
            }

            if (info.getModule() == module) {
                String configName = info.getConfigName();
                int id = idProvider.getItemIDFromConfig(configName);
                info.setId(id);
            }
        }
    }

    public static void RegisterItemTypeForModule(ModulesEnum module, JaffaItemType type, Class<? extends Item> clazz) {
        if (!ClassMapping.containsKey(module)) {
            ClassMapping.put(module, new Hashtable<JaffaItemType, Class<? extends Item>>());
        }

        Hashtable<JaffaItemType, Class<? extends Item>> typeToClass = ClassMapping.get(module);
        typeToClass.put(type, clazz);

        if (type == JaffaItemType.food && !IItemFood.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("food has to implement expected interface");
        }

        if (type == JaffaItemType.pack && !IItemPack.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("pack has to implement expected interface");
        }

        if (type == JaffaItemType.tool && !IItemTool.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("tool has to implement expected interface");
        }
    }

    private static boolean implementsInterface(Class<?> clazz, Class<?> testedInterface) {
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getName().equals(testedInterface.getName()))
                return true;
        }
        return false;
    }

    public static Item getItem(JaffaItem item) {
        JaffaItemInfo info = ItemsInfo.get(item);
        return info.getItem();
    }

    public static void AddItemInfo(JaffaItem item, String name, int iconIndex, String title, ModulesEnum module) {
        JaffaItemInfo newItem = new JaffaItemInfo(name);
        newItem.setIconIndex(iconIndex);
        newItem.setTitle(title);
        newItem.setModule(module);
        ItemsInfo.put(item, newItem);
    }

    private static void finilizeItemSetup(JaffaItemInfo info, Item item) {
        item.setItemName(info.getTitle());
        item.setIconIndex(info.getIconIndex());
        info.setItem(item);
        LanguageRegistry.addName(item, info.getTitle());
    }

    public static Item createJaffaItem(JaffaItem ji, JaffaItemType type, ModulesEnum module) {
        JaffaItemInfo info = ItemsInfo.get(ji);

        Item newJaffaItem = null;
        try {
            Class<? extends Item> clazz = ClassMapping.get(module).get(type);
            Constructor<? extends Item> constructor = clazz.getConstructor(int.class);
            newJaffaItem = constructor.newInstance(info.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    public static IItemFood createJaffaFood(JaffaItem ji, ModulesEnum module) {
        return (IItemFood) createJaffaItem(ji, JaffaItemType.food, module);
    }

    public static IItemTool createJaffaTool(JaffaItem ji, ModulesEnum module) {
        return (IItemTool) createJaffaItem(ji, JaffaItemType.tool, module);
    }

    public static IItemPack createJaffaPack(JaffaItem ji, ModulesEnum module) {
        return (IItemPack) createJaffaItem(ji, JaffaItemType.pack, module);
    }

    public static Item createJaffaItemManual(JaffaItem ji, Class<? extends Item> item) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        Item newJaffaItem = null;
        try {
            newJaffaItem = item.getConstructor(int.class).newInstance(info.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    /*
    private ItemJaffaFood createJaffaFood(JaffaItem ji, int healAmount, float saturation) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaFood newJaffaItem = new ItemJaffaFood(info.getId(), healAmount, saturation);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaBase createJaffaItem(JaffaItem ji) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaBase newJaffaItem = new ItemJaffaBase(info.getId());
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaTool createJaffaTool(JaffaItem ji, int usageCount) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaTool newJaffaItem = new ItemJaffaTool(info.getId(), usageCount);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemJaffaPack createJaffaPack(JaffaItem ji, ItemStack content) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemJaffaPack newJaffaItem = new ItemJaffaPack(info.getId(), content);
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    private ItemBagOfSeeds createBagOfSeed(JaffaItem ji) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        ItemBagOfSeeds newJaffaItem = new ItemBagOfSeeds(info.getId());
        finilizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }
    */
}
