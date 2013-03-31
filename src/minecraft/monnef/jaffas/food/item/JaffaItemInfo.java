package monnef.jaffas.food.item;

import monnef.jaffas.food.common.ModulesEnum;
import net.minecraft.item.Item;

public class JaffaItemInfo {
    private int id;
    private String configName;
    private Item item;
    private String title;
    private int iconIndex;
    private ModulesEnum module;
    private int sheetNumber;

    public JaffaItemInfo(String configName) {
        this.setConfigName(configName);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigName() {
        return this.configName;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

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
