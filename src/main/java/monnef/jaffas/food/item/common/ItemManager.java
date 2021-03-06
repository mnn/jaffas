/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.common;

import monnef.core.api.ICustomIcon;
import monnef.core.item.ItemMonnefCore;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemInfo;
import monnef.jaffas.food.item.JaffaItemType;
import net.minecraft.item.Item;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public static void registerItemTypeForModule(ModulesEnum module, JaffaItemType type, Class<? extends ItemMonnefCore> clazz) {
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

    public static void addItemInfo(JaffaItem item, int iconIndex, ModulesEnum module, int sheetNumber) {
        String newName = item.toString();
        JaffaItemInfo newItem = new JaffaItemInfo(newName);
        newItem.setIconIndex(iconIndex);
        newItem.setModule(module);
        newItem.setSheetNumber(sheetNumber);
        itemsInfo.put(item, newItem);
    }

    private static void finalizeItemSetup(JaffaItemInfo info, Item item, JaffaItem ji) {
        if (info == null)
            throw new RuntimeException("Missing info for item " + ji.toString() + ", did you forget to call addItemInfo in Items?");
        item.setUnlocalizedName(info.getName());
        if (ICustomIcon.class.isAssignableFrom(item.getClass())) {
            ICustomIcon itemWithIcon = (ICustomIcon) item;
            itemWithIcon.setCustomIconIndex(info.getIconIndex());
            itemWithIcon.setSheetNumber(info.getSheetNumber());
        }
        info.setItem(item);
        RegistryUtils.registerItem(item, info.getName());
        itemToJaffaItem.put(item, ji);
    }

    public static void constructItemIdToJaffaItemMappings() {
        itemIdToJaffaItem.clear();
        for (Map.Entry<Item, JaffaItem> e : itemToJaffaItem.entrySet()) {
            int id = Item.getIdFromItem(e.getKey());
            if (id == -1) throw new RuntimeException("Unable to get proper item id.");
            itemIdToJaffaItem.put(id, e.getValue());
        }
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
