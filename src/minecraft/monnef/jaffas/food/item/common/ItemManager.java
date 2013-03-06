package monnef.jaffas.food.item.common;

import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.base.ItemMonnefCore;
import monnef.core.utils.IDProvider;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemInfo;
import monnef.jaffas.food.item.JaffaItemType;
import net.minecraft.item.Item;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class ItemManager {
    public static LinkedHashMap<JaffaItem, JaffaItemInfo> ItemsInfo;
    public static JaffaItem[] mallets;
    public static JaffaItem[] malletHeads;

    private static Hashtable<ModulesEnum, Hashtable<JaffaItemType, Class<? extends ItemMonnefCore>>> ClassMapping;

    static {
        ItemsInfo = new LinkedHashMap<JaffaItem, JaffaItemInfo>();
        ClassMapping = new Hashtable<ModulesEnum, Hashtable<JaffaItemType, Class<? extends ItemMonnefCore>>>();
    }

    public static void LoadItemsFromConfig(ModulesEnum module, IDProvider idProvider) {
        for (JaffaItem item : JaffaItem.values()) {
            if (item == JaffaItem._last) continue;

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

    public static void RegisterItemTypeForModule(ModulesEnum module, JaffaItemType type, Class<? extends ItemMonnefCore> clazz) {
        if (!ClassMapping.containsKey(module)) {
            ClassMapping.put(module, new Hashtable<JaffaItemType, Class<? extends ItemMonnefCore>>());
        }

        Hashtable<JaffaItemType, Class<? extends ItemMonnefCore>> typeToClass = ClassMapping.get(module);
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

    public static Item getItem(JaffaItem item) {
        return getItemInfo(item).getItem();
    }

    public static JaffaItemInfo getItemInfo(JaffaItem item) {
        return ItemsInfo.get(item);
    }

    public static void AddItemInfo(JaffaItem item, String name, int iconIndex, String title, ModulesEnum module) {
        JaffaItemInfo newItem = new JaffaItemInfo(name);
        newItem.setIconIndex(iconIndex);
        if (title.isEmpty()) title = name;
        newItem.setTitle(title);
        newItem.setModule(module);
        ItemsInfo.put(item, newItem);
    }

    private static void finalizeItemSetup(JaffaItemInfo info, Item item) {
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

        finalizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    public static IItemFood createJaffaFood(JaffaItem ji, ModulesEnum module) {
        return (IItemFood) createJaffaItem(ji, JaffaItemType.food, module);
    }

    public static IItemTool createJaffaTool(JaffaItem ji, ModulesEnum module) {
        return (IItemTool) createJaffaItem(ji, JaffaItemType.tool, module);
    }

    @Deprecated
    public static IItemPack createJaffaPack(JaffaItem ji, ModulesEnum module) {
        return (IItemPack) createJaffaItem(ji, JaffaItemType.pack, module);
    }

    public static <T extends Item> T createJaffaItemManual(JaffaItem ji, Class<T> item) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        T newJaffaItem = null;
        try {
            newJaffaItem = item.getConstructor(int.class).newInstance(info.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        finalizeItemSetup(info, newJaffaItem);
        return newJaffaItem;
    }

    public static <T extends Item> T createJaffaItemManual(JaffaItem ji, T item) {
        JaffaItemInfo info = ItemsInfo.get(ji);
        finalizeItemSetup(info, item);
        return item;
    }
}
