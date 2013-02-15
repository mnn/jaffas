package monnef.jaffas.food.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class BlockTable extends BlockJaffas {
    private static final int NUMBER_OF_TABLES = 3;
    public static final String[] multiBlockNames = new String[]{"Table with Red Tablecloth", "Table with Green Tablecloth", "Table with Blue Tablecloth"};

    public BlockTable(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
    }

    public int getTextureFileIndex() {
        return 1;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        ForgeDirection s = ForgeDirection.getOrientation(side);
        int index = this.blockIndexInTexture + 4 * meta;

        switch (s) {
            case UP:
                return index;

            case NORTH:
            case SOUTH:
                return index + 1;

            case EAST:
            case WEST:
                return index + 2;

            case DOWN:
                return index + 3;

            default:
                return index;
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
