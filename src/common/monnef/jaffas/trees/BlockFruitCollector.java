package monnef.jaffas.trees;

import net.minecraft.src.*;

import java.util.Random;

public class BlockFruitCollector extends BlockContainer {

    protected BlockFruitCollector(int id) {
        super(id, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setBlockName("blockFruitCollector");
        setCreativeTab(CreativeTabs.tabMisc);
        setRequiresSelfNotify();
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(mod_jaffas_trees.instance, 0, world, x, y, z);
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
                    entityItem.item.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
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

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityFruitCollector();
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public int getBlockTexture(IBlockAccess access, int x, int y, int z, int side) {
        return getBlockTextureFromSide(side);
    }

    /**
     * Returns the block texture based on the side being looked at. Args: side
     */
    public int getBlockTextureFromSide(int side) {
        return getTextureFromSide(side);
    }

    private int getTextureFromSide(int side) {
        if (side == 0) {
            return 1; // bottom
        } else if (side == 1) {
            return 2; // top
        } else {
            return 3;
        }
    }
}