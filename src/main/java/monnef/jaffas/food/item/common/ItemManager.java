/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.common;

import cpw.mods.fml.common.registry.LanguageRegistry;
import monnef.core.api.ICustomIcon;
import monnef.core.item.ItemMonnefCore;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemInfo;
import monnef.jaffas.food.item.JaffaItemType;
import net.minecraft.item.Item;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class ItemManager {
    protected static LinkedHashMap<JaffaItem, JaffaItemInfo> itemsInfo;
    protected static HashMap<Item, JaffaItem> itemToJaffaItem;
    protected static HashMap<Integer, JaffaItem> itemIdToJaffaItem;

    public static JaffaItem[] mallets;
    public static JaffaItem[] malletHeads;

    private static Hashtable<ModulesEnum, Hashtable<JaffaItemType, Class<? extends ItemMonnefCore>>> ClassMapping;

    static {
        itemsInfo = new LinkedHashMap<JaffaItem, JaffaItemInfo>();
        ClassMapping = new Hashtable<ModulesEnum, Hashtable<JaffaItemType, Class<? extends ItemMonnefCore>>>();
        itemToJaffaItem = new HashMap<Item, JaffaItem>();
        itemIdToJaffaItem = new HashMap<Integer, JaffaItem>();
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

        if (type == JaffaItemType.tool && !IItemTool.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("tool has to implement expected interface");
        }
    }

    public static Item getItem(JaffaItem item) {
        return getItemInfo(item).getItem();
    }

    public static JaffaItemInfo getItemInfo(JaffaItem item) {
        return itemsInfo.get(item);
    }

    public static JaffaItem getJaffaItem(Item item) {
        return itemToJaffaItem.get(item);
    }

    public static JaffaItem getJaffaItem(int itemId) {
        return itemIdToJaffaItem.get(itemId);
    }

    public static void addItemInfo(JaffaItem item, String name, int iconIndex, String title, ModulesEnum module, int sheetNumber) {
        String newTitle = title;
        String newName = name;
        if (title.isEmpty()) {
            newTitle = name;
            newName = item.toString();
        }
        JaffaItemInfo newItem = new JaffaItemInfo(newName);
        newItem.setIconIndex(iconIndex);
        newItem.setTitle(newTitle);
        newItem.setModule(module);
        newItem.setSheetNumber(sheetNumber);
        itemsInfo.put(item, newItem);
    }

    private static void finalizeItemSetup(JaffaItemInfo info, Item item, JaffaItem ji) {
        item.setUnlocalizedName(info.getTitle());
        if (ICustomIcon.class.isAssignableFrom(item.getClass())) {
            ICustomIcon itemWithIcon = (ICustomIcon) item;
            itemWithIcon.setCustomIconIndex(info.getIconIndex());
            itemWithIcon.setSheetNumber(info.getSheetNumber());
        }
        info.setItem(item);
        LanguageRegistry.addName(item, info.getTitle());
        itemToJaffaItem.put(item, ji);
        itemIdToJaffaItem.put(Item.getIdFromItem(item), ji);
    }

    public static Item createJaffaItem(JaffaItem ji, JaffaItemType type, ModulesEnum module) {
        JaffaItemInfo info = itemsInfo.get(ji);

        Item newJaffaItem;
        try {
            Class<? extends Item> clazz = ClassMapping.get(module).get(type);
            Constructor<? extends Item> constructor = clazz.getConstructor();
            newJaffaItem = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        finalizeItemSetup(info, newJaffaItem, ji);
        return newJaffaItem;
    }

    public static IItemFood createJaffaFood(JaffaItem ji, ModulesEnum module) {
        return (IItemFood) createJaffaItem(ji, JaffaItemType.food, module);
    }

    public static IItemTool createJaffaTool(JaffaItem ji, ModulesEnum module) {
        return (IItemTool) createJaffaItem(ji, JaffaItemType.tool, module);
    }

    public static <T extends Item> T createJaffaItemManual(JaffaItem ji, Class<T> item) {
        JaffaItemInfo info = itemsInfo.get(ji);
        T newJaffaItem;
        try {
            newJaffaItem = item.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        finalizeItemSetup(info, newJaffaItem, ji);
        return newJaffaItem;
    }

    public static <T extends Item> T createJaffaItemManual(JaffaItem ji, T item) {
        JaffaItemInfo info = itemsInfo.get(ji);
        finalizeItemSetup(info, item, ji);
        return item;
    }
}
