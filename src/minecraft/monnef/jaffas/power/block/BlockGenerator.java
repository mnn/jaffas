/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.common.BlockMachineWithInventory;
import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGenerator extends BlockMachineWithInventory {

    public BlockGenerator(int id, int texture) {
        super(id, texture, JaffasTechnic.breakableIronMaterial, true, true);
        setCreativeTab(JaffasPower.instance.creativeTab);
        setUnlocalizedName("generator");
        setHardness(1.5f);
        setResistance(5);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileGenerator();
    }

    @Override
    public boolean supportRotation() {
        return false;
    }

    public boolean isBurning(IBlockAccess world, int x, int y, int z) {
        return ((TileGenerator) getTile(world, x, y, z)).isBurning();
    }

    @Override
    public GuiHandler.GuiId getGuiId() {
        return GuiHandler.GuiId.GENERATOR;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (isBurning(world, x, y, z)) {
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
        if (isBurning(world, x, y, z)) {
            return 12;
        } else {
            return 0;
        }
    }
}
