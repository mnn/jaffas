/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.BlockPowerMachine;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockKitchenUnit extends BlockPowerMachine {

    public static final int ICONS_PER_TYPE = 3;
    public static final String KITCHEN_UNIT_TITLE = "Kitchen Unit";
    public static final String UNLOCALIZED_NAME = "kitchenUnit";
    private final int typesCount;

    public BlockKitchenUnit(int textureID, int typesCount) {
        super(textureID, Material.wood, false, false);
        this.typesCount = typesCount;
        setHardness(0.5f);
        setIconsCount(ICONS_PER_TYPE * typesCount);
        setBlockName(UNLOCALIZED_NAME);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileKitchenUnit();
    }

    @Override
    public boolean supportRotation() {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        int base = meta * ICONS_PER_TYPE;
        int shift = 2;
        if (side == ForgeDirection.UP.ordinal()) shift = 1;
        else if (side == ForgeDirection.DOWN.ordinal()) shift = 0;
        return getCustomIcon(base + shift);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < typesCount; i++) {
            ItemStack stack = new ItemStack(item, 1, i);
            list.add(stack);
        }
    }

    public String[] generateSubNames() {
        String[] ret = new String[typesCount];
        for (int i = 0; i < typesCount; i++) {
            ret[i] = UNLOCALIZED_NAME + i;
        }
        return ret;
    }

    public String[] generateTitles() {
        return new String[]{
                KITCHEN_UNIT_TITLE + " 1",
                KITCHEN_UNIT_TITLE + " 2",
                KITCHEN_UNIT_TITLE + " 3",
        };
    }
}
