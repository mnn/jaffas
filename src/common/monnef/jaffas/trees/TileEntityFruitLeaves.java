package monnef.jaffas.trees;

import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.*;

import java.util.Random;


public class TileEntityFruitLeaves extends TileEntity {

    // tree has 60-110 blocks
    // from 1 tree 1 fruit per 3minutes

    public static final int maxAge = 240;
    public static int timerMax = 20 * 60;
    public static final double dropChance = 0.017;
    public static int dropChanceMultiplier = 1;

    private int myMaxAge;
    private int age;
    private int timer;
    private static Random rand = new Random();
    private boolean checked = false;

    public TileEntityFruitLeaves() {
        myMaxAge = (int) (maxAge + Math.round((maxAge / 4) * rand.nextGaussian()));
        timer = Math.abs(rand.nextInt()) % timerMax;
    }

    public void updateEntity() {
        if (worldObj.isRemote) {
            return;
        }

        timer++;
        if (timer >= timerMax) {
            if (!checked) {
                checked = true;
                if (this.getBlockMetadata() == 0) {
                    this.invalidate();
                    return;
                }
            }

            timer = 0;
            generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand, this.getBlockMetadata());
            age++;
            if (age > myMaxAge) {
                worldObj.setBlockMetadata(this.xCoord, this.yCoord, this.zCoord, 0);
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
        if (rand.nextDouble() < dropChance * dropChanceMultiplier) {
            // Vanilla
            if (BlockFruitLeaves.getLeavesType(metadata) == 3 && rand.nextDouble() > 0.25) {
                return;
            }

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
                EntityItem ent = new EntityItem(world, x, newY, z, getItemFromMetadata(metadata));
                ent.setPosition(x + 0.5, newY + 0.9, z + 0.5);
                ent.setVelocity((rand.nextDouble() - 0.5) / 3, 0, (rand.nextDouble() - 0.5) / 3);
                world.spawnEntityInWorld(ent);
            } else {
                if (mod_jaffas_trees.debug) System.out.println("tree: not found - tries~" + tries);
            }
        }
    }

    public static ItemStack getItemFromMetadata(int metadata) {
        switch (BlockFruitLeaves.getLeavesType(metadata)) {
            case 0:
                throw new RuntimeException("normal tree leaves cannot drop stuff!");
            case 1:
                return new ItemStack(Item.appleRed);
            case 2:
                return new ItemStack(Item.dyePowder, 1, 3);
            case 3:
                return new ItemStack(mod_jaffas.getJaffaItem(mod_jaffas.JaffaItem.vanillaBeans));
            case 4:
                return new ItemStack(mod_jaffas_trees.itemLemon);
            case 5:
                return new ItemStack(mod_jaffas_trees.itemOrange);
            case 6:
                return new ItemStack(mod_jaffas_trees.itemPlum);
            default:
                throw new RuntimeException("unknown type of tree, don't have information about fruits");
        }
    }
}