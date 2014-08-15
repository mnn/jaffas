/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.common.ModulesEnum;
import net.minecraft.item.Item;

public class JaffaItemInfo {
    private String name;
    private Item item;
    private String title;
    private int iconIndex;
    private ModulesEnum module;
    private int sheetNumber;

    public JaffaItemInfo(String name) {
        this.setName(name);
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Deprecated
    public String getTitle() {
        return title;
    }

    @Deprecated
    public void setTitle(String title) {
        this.title = title;
    }

    public void setIconIndex(int iconIndex) {
        this.iconIndex = iconIndex;
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public ModulesEnum getModule() {
        return module;
    }

    public void setModule(ModulesEnum module) {
        this.module = module;
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }
}
