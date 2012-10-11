package monnef.jaffas.trees;

import net.minecraft.src.*;

import java.util.Random;


public class TileEntityFruitLeaves extends TileEntity {

    // tree has 60-110 blocks
    // from 1 tree 1 fruit per 3minutes

    public static final int maxAge = 240;
    public static final int timerMax = 20 * 60;

    private int myMaxAge;
    private int age;
    private int timer;
    private static Random rand = new Random();

    public TileEntityFruitLeaves() {
        myMaxAge = (int) (maxAge + Math.round((maxAge / 4) * rand.nextGaussian()));
        timer = rand.nextInt() % timerMax;
    }

    public void updateEntity() {
        timer++;
        if (timer >= timerMax) {
            timer = 0;
            generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand, this.getBlockMetadata());
            age++;
            if (age > myMaxAge) {
                worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, 0);
                this.invalidate();
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("age", this.age);
        par1NBTTagCompound.setInteger("myMaxAge", this.myMaxAge);
        par1NBTTagCompound.setInteger("timer", this.timer);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        this.age = par1NBTTagCompound.getInteger("age");
        this.timer = par1NBTTagCompound.getInteger("timer");
        this.myMaxAge = par1NBTTagCompound.getInteger("myMaxAge");
    }

    private void generateFruit(World world, int x, int y, int z, Random rand, int metadata) {
        if (rand.nextDouble() < 0.0039215) {
            // TODO: more fruits
            // TODO: not spawn IN leaves

            boolean found = false;
            int tries = 0;
            int newY = y;

            while (tries <= 5 && !found && world.getBlockId(x, newY, z) == mod_jaffas_trees.blockFruitLeaves.blockID) {
                tries++;
                newY--;
                if (world.getBlockId(x, newY, z) == 0) {
                    found = true;
                }
            }

            if (found) {
                EntityItem ent = new EntityItem(world, x, newY, z, new ItemStack(getItemFromMetadata(metadata)));
                world.spawnEntityInWorld(ent);
            } else {
                System.out.println("tree: not found - tries~" + tries);
            }
        }
    }

    private Item getItemFromMetadata(int metadata) {
        switch (BlockFruitLeaves.getLeavesType(metadata)) {
            case 0:
                return Item.wheat;
            case 1:
                return Item.appleRed;
            case 2:
                return Item.clay;
            case 3:
                return Item.arrow;
            default:
                return Item.stick;
        }
    }
}