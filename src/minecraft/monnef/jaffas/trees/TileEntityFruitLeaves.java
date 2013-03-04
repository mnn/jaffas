package monnef.jaffas.trees;

import monnef.jaffas.food.item.ItemManager;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.item.ItemFromFruitResult;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;

import static monnef.jaffas.food.mod_jaffas_food.Log;
import static monnef.jaffas.trees.mod_jaffas_trees.*;


public class TileEntityFruitLeaves extends TileEntity {

    // tree has 60-110 blocks /3
    // from 1 tree 1 fruit per 3minutes

    public static final int maxAge = 240;
    public static int timerMax = 20 * 60;

    public static final double turnChance = 0.0165; // 0.022
    public static int turnChanceMultiplier = 1;

    private int myMaxAge;
    private int age;
    private int timer;
    private static Random rand = new Random();
    private boolean checked = false;
    private fruitType fruit;
    private int leavesID;
    private int leavesMeta;

    private static final HashSet<Integer> fruitFallThroughBlocks = new HashSet<Integer>();

    static {
        for (LeavesInfo info : mod_jaffas_trees.leavesList) {
            fruitFallThroughBlocks.add(info.leavesID);
        }

        fruitFallThroughBlocks.add(Block.wood.blockID);
    }

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
            try {
                fruit = getActualLeavesType(Block.blocksList[this.leavesID], this.leavesMeta);
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                this.invalidate();
                return;
            }

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
                if (this.fruit != fruitType.Vanilla || rand.nextInt(3) == 0) {
                    if (this.getBlockType().blockID == mod_jaffas_trees.leavesList.get(0).leavesID || this.getBlockType().blockID == this.leavesID) {
                        ChangeBlockAndRespawnMe(this.leavesID, this.leavesMeta);
                    } else {
                        // no leaves block on my position => something went wrong, kill myself
                        this.invalidate();
                        if (mod_jaffas_trees.debug)
                            Log.printInfo(this.xCoord + "," + this.yCoord + "," + this.zCoord + " - wrong block => ending my function");
                        return;
                    }
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

    public static boolean isThisBlockTransparentForFruit(int blockID) {
        return fruitFallThroughBlocks.contains(blockID);
    }

    public boolean generateFruitAndDecay(double chanceForSecondFruit, EntityPlayer player) {
        if (this.getBlockType().blockID != this.leavesID || BlockFruitLeaves.getLeavesType(this.getBlockMetadata()) != this.leavesMeta) {
            if (mod_jaffas_trees.debug) Log.printInfo("not fruit block, no fruit generated");
            return false;
        }

        this.age = 0; // obsolete
        int fruits = 1;
        if (rand.nextDouble() < chanceForSecondFruit) fruits++;

        for (int i = 0; i < fruits; i++)
            this.generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand, this.leavesMeta, player);

        ChangeBlockAndRespawnMe(mod_jaffas_trees.leavesList.get(0).leavesID, 0);

        return true;
    }

    private static final int[][] fourNeighbourTable = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    private void generateFruit(World world, int x, int y, int z, Random rand, int metadata, EntityPlayer player) {
        boolean found = false;
        int newX = x, newY = y, newZ = z;

        if (player == null) {
            for (int passNum = 0; passNum < 2 && !found; passNum++) {
                int tries = 0;
                newX = x;
                newY = y;
                newZ = z;

                // looking for a way through leaves
                int currentBlockID;
                do {
                    tries++;
                    newY--;
                    currentBlockID = world.getBlockId(newX, newY, newZ);

                    if (currentBlockID == 0) {
                        found = true;
                    } else if (passNum == 1) {
                        // on a second pass we try harder
                        for (int i = 0; i < fourNeighbourTable.length && !found; i++) {
                            if (world.getBlockId(newX + fourNeighbourTable[i][0], newY, newZ + fourNeighbourTable[i][1]) == 0) {
                                newX += fourNeighbourTable[i][0];
                                newZ += fourNeighbourTable[i][1];
                                found = true;
                            }
                        }
                    }
                } while (tries <= 5 && !found && isThisBlockTransparentForFruit(currentBlockID));
            }
        } else {
            newX = (int) Math.round(player.posX);
            newY = (int) Math.round(player.posY + .5);
            newZ = (int) Math.round(player.posZ);
            found = true;
        }

        if (found) {
            if (fruit != null) {
                ItemStack stack = getItemFromMetadataAndBlockID(this.fruit);
                if (stack != null) {
                    EntityItem ent = new EntityItem(world, newX, newY, newZ, stack);
                    ent.setPosition(newX + 0.5, newY + 0.9, newZ + 0.5);
                    ent.motionX = (rand.nextDouble() - 0.5) / 3;
                    ent.motionY = 0;
                    ent.motionZ = (rand.nextDouble() - 0.5) / 3;
                    world.spawnEntityInWorld(ent);
                } else {
                    if (debug) {
                        Log.printWarning("got null in stack: m~" + metadata + " b~" + this.getBlockType().blockID);
                        debugPrintPos();
                    }
                }
            } else {
                if (debug) {
                    Log.printWarning("got null in fruit: m~" + metadata + " b~" + this.getBlockType().blockID);
                    debugPrintPos();
                }
            }
        } else {
            if (debug) Log.printWarning("tree: not found");
        }
    }

    public static ItemFromFruitResult getItemFromFruit(fruitType fruit) {
        ItemFromFruitResult res = new ItemFromFruitResult();

        switch (fruit) {
            case Normal:
                if (debug) {
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
                res.setStack(new ItemStack(ItemManager.getItem(JaffaItem.vanillaBeans)));
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

            case Coconut:
                res.setStack(new ItemStack(itemCoconut));
                break;

            default:
                if (debug) {
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
            Log.printWarning(info.getMessage() + metadata + ", t:" + leavesMetadataType + ", T:" + fruit);
            debugPrintPos();
        }

        return info.getStack();
    }

    private void debugPrintPos() {
        Log.printInfo(this.xCoord + " " + this.yCoord + " " + this.zCoord);
    }
}