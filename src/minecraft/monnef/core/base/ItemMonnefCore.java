package monnef.core.base;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public abstract class ItemMonnefCore extends Item implements ICustomIcon {
    protected int customIconIndex;
    protected int sheetNumber;

    public ItemMonnefCore(int id) {
        super(id);
    }

    @Override
    public void setCustomIconIndex(int index) {
        this.customIconIndex = index;
    }

    @Override
    public int getSheetNumber() {
        return sheetNumber;
    }

    @Override
    public int getCustomIconIndex() {
        return customIconIndex;
    }

    @Override
    public void setSheetNumber(int index) {
        this.sheetNumber = index;
    }

    @Override
    public String customIconName() {
        return null;
    }

    @Override
    public void updateIcons(IconRegister iconRegister) {
        this.iconIndex = iconRegister.registerIcon(CustomIconHelper.generateId(this));
    }

}
