package monnef.core.base;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public abstract class ItemMonnefCore extends Item implements ICustomIcon {
    protected int customIconIndex;
    protected int sheetNumber;
    protected int iconsCount = 1;
    protected Icon[] icons;

    public ItemMonnefCore(int id) {
        super(id);
        this.sheetNumber = getDefaultSheetNumber();
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
    public String getCustomIconName() {
        return null;
    }

    @Override
    public int getIconsCount() {
        return iconsCount;
    }

    @Override
    public void setIconsCount(int iconsCount) {
        this.iconsCount = iconsCount;
    }

    @Override
    public void updateIcons(IconRegister iconRegister) {
        this.iconIndex = iconRegister.registerIcon(CustomIconHelper.generateId(this));
        if (iconsCount > 1) {
            icons = new Icon[iconsCount];
            icons[0] = this.iconIndex;
            for (int i = 1; i < iconsCount; i++) {
                icons[i] = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, i));
            }
        }
    }

    @Override
    public Icon getCustomIcon(int index) {
        return icons[index];
    }
}
