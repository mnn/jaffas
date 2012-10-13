package monnef.jaffas.trees;

import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.*;

import java.util.Random;

import static monnef.jaffas.trees.mod_jaffas_trees.*;


public class TileEntityFruitLeaves extends TileEntity {

    // tree has 60-110 blocks /3
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
    private fruitType fruit;

    public TileEntityFruitLeaves() {
        myMaxAge = (int) (maxAge + Math.round((maxAge / 4) * rand.nextGaussian()));
        timer = Math.abs(rand.nextInt()) % timerMax;
    }

    /**
     * Gets the block type at the location of this entity (client-only).
     */
    public Block getBlockType() {
        if (this.blockType == null) {
            this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
        }

        return this.blockType;
    }

    public void updateEntity() {
        if (!checked) {
            fruit = getActualLeavesType(this.getBlockType(), BlockFruitLeaves.getLeavesType(this.getBlockMetadata()));
            checked = true;
            if (fruit == fruitType.Normal) {
                this.invalidate();
                return;
            }
        }

        if (worldObj.isRemote) {
            return;
        }

        timer++;
        if (timer >= timerMax) {

            timer = 0;
            generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand, this.getBlockMetadata());
            age++;
            if (age > myMaxAge) {
                worldObj.setBlockAndMetadata(this.xCoord, this.yCoord, this.zCoord, mod_jaffas_trees.leavesList.get(0).leavesID, 0);
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
            if (this.fruit == fruitType.Vanilla && rand.nextInt(3) != 0) {
                return;
            }

            boolean found = false;
            int tries = 0;
            int newY = y;

            int thisBlockID = world.getBlockId(x, y, z);

            //TODO optimize
            while (tries <= 5 && !found && (world.getBlockId(x, newY, z) == thisBlockID || world.getBlockId(x, newY, z) == mod_jaffas_trees.leavesList.get(0).leavesID)) {
                tries++;
                newY--;
                if (world.getBlockId(x, newY, z) == 0) {
                    found = true;
                }
            }

            if (found) {
                if (fruit != null) {
                    ItemStack stack = getItemFromMetadataAndBlockID(this.fruit);
                    if (stack != null) {
                        EntityItem ent = new EntityItem(world, x, newY, z, stack);
                        ent.setPosition(x + 0.5, newY + 0.9, z + 0.5);
                        ent.motionX = (rand.nextDouble() - 0.5) / 3;
                        ent.motionY = 0;
                        ent.motionZ = (rand.nextDouble() - 0.5) / 3;
                        world.spawnEntityInWorld(ent);
                    } else {
                        if (debug) {
                            System.err.println("got null in stack: m~" + metadata + " b~" + this.getBlockType().blockID);
                            debugPrintPos();
                        }
                    }
                } else {
                    if (debug) {
                        System.err.println("got null in fruit: m~" + metadata + " b~" + this.getBlockType().blockID);
                        debugPrintPos();
                    }
                }
            } else {
                if (debug) System.out.println("tree: not found - tries~" + tries);
            }
        }
    }

    public ItemStack getItemFromMetadataAndBlockID(fruitType fruit) {
        //public ItemStack getItemFromMetadataAndBlockID(int metadata, Block block) {

        int metadata = -1, leavesMetadataType = -1, blockID = -1;
        if (debug) {
            metadata = this.getBlockMetadata();
            leavesMetadataType = BlockFruitLeaves.getLeavesType(metadata);
            blockID = this.getBlockType().blockID;
        }

        //fruitType fruit = mod_jaffas_trees.getActualLeavesType(block, metadata);

        //TODO!

        switch (fruit) {
            case Normal:
                if (debug) {
                    System.err.println("normal tree! - " + metadata + ", t:" + leavesMetadataType);
                    debugPrintPos();
                    return new ItemStack(Item.porkRaw);
                } else
                    throw new RuntimeException("normal tree leaves cannot drop stuff!");
            case Apple:
                return new ItemStack(Item.appleRed);
            case Cocoa:
                return new ItemStack(Item.dyePowder, 1, 3);
            case Vanilla:
                return new ItemStack(mod_jaffas.getJaffaItem(mod_jaffas.JaffaItem.vanillaBeans));
            case Lemon:
                return new ItemStack(itemLemon);
            case Orange:
                return new ItemStack(itemOrange);
            case Plum:
                return new ItemStack(itemPlum);
            default:
                if (debug) {
                    System.err.println("unknown type of tree - " + metadata + ", t:" + leavesMetadataType + ", T:" + fruit);
                    debugPrintPos();
                    return new ItemStack(Item.stick);
                } else
                    throw new RuntimeException("unknown type of tree, don't have information about fruits: " + metadata);
        }

    }

    private void debugPrintPos() {
        System.err.println(this.xCoord + " " + this.yCoord + " " + this.zCoord);
    }
}