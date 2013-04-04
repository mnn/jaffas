package monnef.core.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public abstract class BlockMonnefCore extends Block implements ICustomIcon {
    protected int customIconIndex;
    protected int sheetNumber;
    protected int iconsCount = 1;
    protected Icon[] icons;

    public BlockMonnefCore(int id, Material material) {
        super(id, material);
        this.sheetNumber = getDefaultSheetNumber();
    }

    public BlockMonnefCore(int id, int index, Material material) {
        this(id, material);
        this.customIconIndex = index;
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
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this));
        if (iconsCount > 1) {
            icons[0] = this.blockIcon;
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
