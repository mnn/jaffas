package monnef.core.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public abstract class BlockMonnefCore extends Block implements ICustomIcon {
    protected int iconIndex;
    protected int sheetNumber;

    public BlockMonnefCore(int id, Material material) {
        super(id, material);
        this.sheetNumber = getDefaultSheetNumber();
    }

    public BlockMonnefCore(int id, int index, Material material) {
        super(id, material);
        this.iconIndex = index;
    }

    @Override
    public void setCustomIconIndex(int index) {
        this.iconIndex = index;
    }

    @Override
    public int getSheetNumber() {
        return sheetNumber;
    }

    @Override
    public int getCustomIconIndex() {
        return iconIndex;
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
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this));
    }
}
