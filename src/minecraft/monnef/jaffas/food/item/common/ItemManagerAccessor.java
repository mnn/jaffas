/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.item.common;

import monnef.core.utils.IDProvider;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.JaffaItemType;
import net.minecraft.item.Item;

public abstract class ItemManagerAccessor<IT extends ItemJaffaBase> {
    protected int currentSheetNumber = 1;

    public abstract ModulesEnum getMyModule();

    protected abstract void InitializeItemInfos();

    protected abstract void CreateItems();

    protected void AddItemInfo(JaffaItem item, String name, int iconIndex, String title) {
        ItemManager.AddItemInfo(item, name, iconIndex, title, this.getMyModule(), currentSheetNumber);
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

    @Deprecated
    protected IItemPack createJaffaPack(JaffaItem ji) {
        return ItemManager.createJaffaPack(ji, this.getMyModule());
    }

    protected <T extends Item> T createJaffaItemManual(JaffaItem ji, Class<T> itemClass) {
        return ItemManager.createJaffaItemManual(ji, itemClass);
    }

    protected <T extends Item> T createJaffaItemManual(JaffaItem ji, T item) {
        return ItemManager.createJaffaItemManual(ji, item);
    }

    public void LoadItemsFromConfig(IDProvider idProvider) {
        ItemManager.LoadItemsFromConfig(this.getMyModule(), idProvider);
    }

    public void RegisterItemType(JaffaItemType type, Class<? extends IT> clazz) {
        ItemManager.RegisterItemTypeForModule(this.getMyModule(), type, clazz);
    }

    public int getCurrentSheetNumber() {
        return currentSheetNumber;
    }

    public void setCurrentSheetNumber(int currentSheetNumber) {
        this.currentSheetNumber = currentSheetNumber;
    }
}
