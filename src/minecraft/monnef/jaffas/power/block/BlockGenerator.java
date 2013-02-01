package monnef.jaffas.power.block;

import monnef.core.BitHelper;
import monnef.jaffas.power.block.common.BlockMachine;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGenerator extends BlockMachine {
    public static final int BURN_BIT = 3;

    public BlockGenerator(int id, int texture) {
        super(id, texture, Material.iron, true);
        setCreativeTab(mod_jaffas_power.CreativeTab);
        setBlockName("generator");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityGenerator();
    }

    @Override
    public boolean supportRotation() {
        return false;
    }

    public static boolean isBurning(int meta) {
        return BitHelper.isBitSet(meta, BURN_BIT);
    }

    public static int setBurning(int meta, boolean burningValue) {
        return BitHelper.setBitToValue(meta, BURN_BIT, burningValue);
    }
}
