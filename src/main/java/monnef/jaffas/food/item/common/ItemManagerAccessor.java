/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.common;

import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import net.minecraft.item.Item;

public abstract class ItemManagerAccessor {
    protected int currentSheetNumber = 1;

    public abstract ModulesEnum getMyModule();

    protected abstract void InitializeItemInfos();

    protected abstract void CreateItems();

    protected void addItemInfo(JaffaItem item, int iconIndex) {
        ItemManager.addItemInfo(item, iconIndex, this.getMyModule(), currentSheetNumber);
    }

    protected Item createJaffaItem(JaffaItem ji) {
        return ItemManager.createJaffaItem(ji, JaffaItemType.basic, this.getMyModule());
    }

    protected IItemTool createJaffaTool(JaffaItem ji) {
        return ItemManager.createJaffaTool(ji, this.getMyModule());
    }

    protected IItemFood createJaffaFood(JaffaItem ji) {
        return ItemManager.createJaffaFood(ji, this.getMyModule());
    }

    protected <T extends Item> T createJaffaItemManual(JaffaItem ji, Class<T> itemClass) {
        return ItemManager.createJaffaItemManual(ji, itemClass);
    }

    protected <T extends Item> T createJaffaItemManual(JaffaItem ji, T item) {
        return ItemManager.createJaffaItemManual(ji, item);
    }

    public void RegisterItemType(JaffaItemType type, Class<? extends ItemJaffaBase> clazz) {
        ItemManager.registerItemTypeForModule(this.getMyModule(), type, clazz);
    }

    public int getCurrentSheetNumber() {
        return currentSheetNumber;
    }

    public void setCurrentSheetNumber(int currentSheetNumber) {
        this.currentSheetNumber = currentSheetNumber;
    }

    public JaffaItem getJaffaItem(int itemId) {
        return ItemManager.getJaffaItem(itemId);
    }
}
