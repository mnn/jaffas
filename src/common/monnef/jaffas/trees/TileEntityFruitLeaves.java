package monnef.jaffas.trees;

import net.minecraft.src.*;

import java.util.Random;


public class TileEntityFruitLeaves extends TileEntity {

    // tree has 60-110 blocks
    // from 1 tree 1 fruit per 3minutes

    public static int maxAge = 20;
    public static int timerMax = 20 * 60;

    private int age;
    private int metadata = -1;
    private int timer;
    private static Random rand = new Random();

    public TileEntityFruitLeaves() {
    }

    public void updateEntity() {
        timer++;
        if (timer >= timerMax) {
            timer = 0;
            generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand, blockMetadata);
            age++;
            if (age > maxAge) {
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
        par1NBTTagCompound.setInteger("timer", this.timer);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        this.age = par1NBTTagCompound.getInteger("age");
        this.timer = par1NBTTagCompound.getInteger("timer");
    }

    private void generateFruit(World world, int x, int y, int z, Random rand, int metadata) {
        if (rand.nextDouble() < 0.05) {
            // TODO: more fruits
            // TODO: not spawn IN leaves
            EntityItem ent = new EntityItem(world, x, y, z, new ItemStack(getItemFromMetadata(metadata)));
            world.spawnEntityInWorld(ent);
        }
    }

    private Item getItemFromMetadata(int metadata) {
        switch (metadata) {
            case 1:
                return Item.appleRed;
            default:
                return Item.stick;
        }
    }
}