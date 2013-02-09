package monnef.jaffas.power.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.BitHelper;
import monnef.jaffas.power.block.common.BlockMachineWithInventory;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGenerator extends BlockMachineWithInventory {
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

    @Override
    public int getGuiId() {
        return 0;
    }

    @Override
    public boolean useOwnRenderId() {
        return true;
    }


    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        int meta = world.getBlockMetadata(x, y, z);
        if (isBurning(meta)) {
            for (int i = 0; i < 2; i++) {
                double px = x + 0.1 + rand.nextDouble() * 0.9;
                double py = y + 0.8;
                double pz = z + 0.1 + rand.nextDouble() * 0.9;

                world.spawnParticle("smoke", px, py, pz, 0.0D, 0.005D, 0.0D);
                world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (isBurning(meta)) {
            return 12;
        } else {
            return 0;
        }
    }
}
