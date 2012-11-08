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

    public static final double turnChance = 0.022;
    public static int turnChanceMultiplier = 1;

    private int myMaxAge;
    private int age;
    private int timer;
    private static Random rand = new Random();
    private boolean checked = false;
    private fruitType fruit;
    private int leavesID;
    private int leavesMeta;

    public TileEntityFruitLeaves() {
        myMaxAge = (int) (maxAge + Math.round((maxAge / 4) * rand.nextGaussian()));
        timer = Math.abs(rand.nextInt()) % timerMax;
    }

    public TileEntityFruitLeaves(int leavesID, int leavesMeta) {
        this();
        this.leavesID = leavesID;
        this.leavesMeta = leavesMeta;
    }

    public TileEntityFruitLeaves(TileEntityFruitLeaves tileEntityFruitLeaves) {
        this(tileEntityFruitLeaves.leavesID, tileEntityFruitLeaves.leavesMeta);
        this.timer = 0;
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
            fruit = getActualLeavesType(Block.blocksList[this.leavesID], this.leavesMeta);
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
            if (this.rand.nextDouble() < this.turnChance * this.turnChanceMultiplier) {

                if (this.getBlockType().blockID == mod_jaffas_trees.leavesList.get(0).leavesID || this.getBlockType().blockID == this.leavesID) {
                    ChangeBlockAndRespawnMe(this.leavesID, this.leavesMeta);
                } else {
                    // no leaves block on my position => something went wrong, kill myself
                    this.invalidate();
                    if (mod_jaffas_trees.debug)
                        System.err.println(this.xCoord + "," + this.yCoord + "," + this.zCoord + " - wrong block => ending my function");
                    return;
                }
            }
        }

    }

    private void ChangeBlockAndRespawnMe(int newBlock, int newMeta) {
        worldObj.setBlockAndMetadata(this.xCoord, this.yCoord, this.zCoord, newBlock, BlockFruitLeaves.getChangedTypeInMeta(newMeta, this.getBlockMetadata()));
        this.invalidate();
        TileEntityFruitLeaves te = new TileEntityFruitLeaves(this);
        worldObj.setBlockTileEntity(this.xCoord, this.yCoord, this.zCoord, te);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("age", this.age);
        par1NBTTagCompound.setInteger("myMaxAge", this.myMaxAge);
        par1NBTTagCompound.setInteger("timer", this.timer);
        par1NBTTagCompound.setInteger("leavesID", this.leavesID);
        par1NBTTagCompound.setInteger("leavesMeta", this.leavesMeta);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        this.age = par1NBTTagCompound.getInteger("age");
        this.timer = par1NBTTagCompound.getInteger("timer");
        this.myMaxAge = par1NBTTagCompound.getInteger("myMaxAge");
        this.leavesID = par1NBTTagCompound.getInteger("leavesID");
        this.leavesMeta = par1NBTTagCompound.getInteger("leavesMeta");
    }

    public boolean generateFruitAndDecay() {
        if (this.getBlockType().blockID != this.leavesID || BlockFruitLeaves.getLeavesType(this.getBlockMetadata()) != this.leavesMeta) {
            if (mod_jaffas_trees.debug) System.err.println("not fruit block, no fruit generated");
            return false;
        }

        this.age = 0; // obsolete
        this.generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand, this.leavesMeta);
        //worldObj.setBlockAndMetadata(this.xCoord, this.yCoord, this.zCoord, mod_jaffas_trees.leavesList.get(0).leavesID, 0);
        //worldObj.setBlockAndMetadata(this.xCoord, this.yCoord, this.zCoord, mod_jaffas_trees.leavesList.get(0).leavesID, BlockFruitLeaves.getChangedTypeInMeta(0, this.getBlockMetadata()));
        ChangeBlockAndRespawnMe(mod_jaffas_trees.leavesList.get(0).leavesID, 0);

        return true;
    }

    private void generateFruit(World world, int x, int y, int z, Random rand, int metadata) {
//        if (rand.nextDouble() < dropChance * dropChanceMultiplier) {
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
        //  }
    }

    public static ItemFromFruitResult getItemFromFruit(fruitType fruit) {
        ItemFromFruitResult res = new ItemFromFruitResult();

        switch (fruit) {
            case Normal:
                if (debug) {
                    //System.err.println("normal tree! - " + metadata + ", t:" + leavesMetadataType);
                    //debugPrintPos();
                    res.setMessage("normal tree!");
                    res.setStack(new ItemStack(Item.porkRaw));
                } else
                    res.exception = new RuntimeException("normal tree leaves cannot drop stuff!");
                break;

            case Apple:
                res.setStack(new ItemStack(Item.appleRed));
                break;

            case Cocoa:
                res.setStack(new ItemStack(Item.dyePowder, 1, 3));
                break;

            case Vanilla:
                res.setStack(new ItemStack(mod_jaffas.getJaffaItem(mod_jaffas.JaffaItem.vanillaBeans)));
                break;

            case Lemon:
                res.setStack(new ItemStack(itemLemon));
                break;

            case Orange:
                res.setStack(new ItemStack(itemOrange));
                break;

            case Plum:
                res.setStack(new ItemStack(itemPlum));
                break;

            default:
                if (debug) {
                    //System.err.println("unknown type of tree - " + metadata + ", t:" + leavesMetadataType + ", T:" + fruit);
                    // debugPrintPos();
                    res.setMessage("unknown type of tree");
                    res.setStack(new ItemStack(Item.stick));
                } else
                    res.exception = new RuntimeException("unknown type of tree, don't have information about fruits: " + fruit);
        }

        return res;
    }

    public ItemStack getItemFromMetadataAndBlockID(fruitType fruit) {
        int metadata = -1, leavesMetadataType = -1;
        if (debug) {
            metadata = this.getBlockMetadata();
            leavesMetadataType = BlockFruitLeaves.getLeavesType(metadata);
        }

        ItemFromFruitResult info = getItemFromFruit(fruit);
        if (info.exception != null) {
            throw (RuntimeException) info.exception;
        }

        if (info.getMessage() != null) {
            System.err.println(info.getMessage() + metadata + ", t:" + leavesMetadataType + ", T:" + fruit);
            debugPrintPos();
        }

        return info.getStack();
    }

    private void debugPrintPos() {
        System.err.println(this.xCoord + " " + this.yCoord + " " + this.zCoord);
    }
}