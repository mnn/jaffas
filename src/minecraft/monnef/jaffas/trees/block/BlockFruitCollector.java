package monnef.jaffas.trees.block;

import monnef.jaffas.food.jaffasFood;
import monnef.jaffas.trees.TileEntityFruitCollector;
import monnef.jaffas.trees.jaffasTrees;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFruitCollector extends BlockContainer {
    public BlockFruitCollector(int id) {
        super(id, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setUnlocalizedName("blockFruitCollector");
        setCreativeTab(CreativeTabs.tabMisc);
        //setRequiresSelfNotify();
        setCreativeTab(jaffasTrees.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasFood.textureFile[0];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(jaffasTrees.instance, 0, world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z) {
        Random rand = new Random();

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        spawnParticlesOfTargetedItem(world, random, i, j, k, false);

        /*
        for (int a = 0; a < 3; a++) {
            jaffasTrees.proxy.addEffect("sucking", world, i + randomShift(random), j + randomShift(random), k + randomShift(random), 0D, 0.02D,0D);
        }
        */
    }

    public void spawnParticlesOfTargetedItem(World world, Random random, int i, int j, int k, boolean force) {
        TileEntityFruitCollector et = (TileEntityFruitCollector) world.getBlockTileEntity(i, j, k);
        if (et.getState() == TileEntityFruitCollector.CollectorStates.targeted || force) {
            for (int a = 0; a < 15; a++) {
                jaffasTrees.proxy.addEffect("sucking", world, et.getIX() + randomShift(random), et.getIY() + randomShift(random), et.getIZ() + randomShift(random), (random.nextDouble() - 0.5) / 40D, (random.nextDouble()) / 200D, (random.nextDouble() - 0.5) / 40D);
            }

        }
    }

    private double randomShift(Random random) {
        return random.nextDouble() % 1D - 0.5D;
    }


    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityFruitCollector();
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess access, int x, int y, int z, int side) {
        return null;
        // TODO
        // return getTextureFromSide(side);
    }


    private int getTextureFromSide(int side) {
        if (side == 0) {
            return 96; // bottom
        } else if (side == 1) {
            return 96; // top
        } else {
            return 95;
        }
    }
}