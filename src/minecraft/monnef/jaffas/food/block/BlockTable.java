/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.block;

import monnef.core.base.CustomIconHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class BlockTable extends BlockJaffas {
    private static final int NUMBER_OF_TABLES = 3;
    private static final int SIDES_COUNT = 4;
    private Icon[][] icons;

    public static final String[] multiBlockNames = new String[]{"Table with Red Tablecloth", "Table with Green Tablecloth", "Table with Blue Tablecloth"};


    public BlockTable(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        setHardness(2f);
        setUnlocalizedName("blockJTable");
        setBurnProperties(blockID, 5, 5);
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[NUMBER_OF_TABLES][];
        for (int color = 0; color < NUMBER_OF_TABLES; color++) {
            icons[color] = new Icon[SIDES_COUNT];
            int index = this.customIconIndex + SIDES_COUNT * color;
            for (int side = 0; side < SIDES_COUNT; side++) {
                int idx = index + side;
                icons[color][side] = iconRegister.registerIcon(CustomIconHelper.generateId(this, idx));
            }
        }
    }

    @Override
    public Icon getIcon(int side, int meta) {
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
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < NUMBER_OF_TABLES; i++) {
            par3List.add(new ItemStack(this, 1, i));
        }
    }
}
