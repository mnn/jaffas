/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.api.ICustomIcon;
import monnef.core.common.CustomIconHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockTable extends BlockJaffas {
    private static final int NUMBER_OF_TABLES = 3;
    private static final int SIDES_COUNT = 4;
    private IIcon[][] icons;

    public static final String[] multiBlockNames = new String[]{"red", "green", "blue"};

    public BlockTable(int texture, Material material) {
        super(texture, material);
        setHardness(2f);
        setBlockName("blockJTable");
        setBurnProperties(5, 5);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[NUMBER_OF_TABLES][];
        for (int color = 0; color < NUMBER_OF_TABLES; color++) {
            icons[color] = new IIcon[SIDES_COUNT];
            int index = this.getCustomIconIndex() + SIDES_COUNT * color;
            for (int side = 0; side < SIDES_COUNT; side++) {
                int idx = index + side;
                icons[color][side] = iconRegister.registerIcon(CustomIconHelper.generateId((ICustomIcon) this, idx));
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        ForgeDirection s = ForgeDirection.getOrientation(side);

        switch (s) {
            case UP:
                return icons[meta][0];

            case NORTH:
            case SOUTH:
                return icons[meta][1];

            case EAST:
            case WEST:
                return icons[meta][2];

            case DOWN:
                return icons[meta][3];

            default:
                return icons[meta][0];
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List result) {
        for (int i = 0; i < NUMBER_OF_TABLES; i++) {
            result.add(new ItemStack(this, 1, i));
        }
    }
}
