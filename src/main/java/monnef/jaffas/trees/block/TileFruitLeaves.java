/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.ItemFromFruitResult;
import monnef.jaffas.trees.common.LeavesInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.trees.JaffasTrees.debug;
import static monnef.jaffas.trees.JaffasTrees.getActualLeavesType;
import static monnef.jaffas.trees.JaffasTrees.itemBanana;
import static monnef.jaffas.trees.JaffasTrees.itemCoconut;
import static monnef.jaffas.trees.JaffasTrees.itemLemon;
import static monnef.jaffas.trees.JaffasTrees.itemOrange;
import static monnef.jaffas.trees.JaffasTrees.itemPlum;


public class TileFruitLeaves extends TileEntity {

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
    private JaffasTrees.FruitType fruit;
    private Block leavesBlock;
    private int leavesMeta;

    private static final HashSet<Block> fruitFallThroughBlocks = new HashSet<Block>();

    static {
        for (LeavesInfo info : JaffasTrees.leavesList) {
            fruitFallThroughBlocks.add(info.leavesBlock);
        }

        fruitFallThroughBlocks.add(Blocks.log);
        fruitFallThroughBlocks.add(Blocks.log2);
    }

    public TileFruitLeaves() {
        myMaxAge = (int) (maxAge + Math.round((maxAge / 4) * rand.nextGaussian()));
        timer = Math.abs(rand.nextInt()) % timerMax;
    }

    public TileFruitLeaves(Block leavesBlock, int leavesMeta) {
        this();
        this.leavesBlock = leavesBlock;
        this.leavesMeta = leavesMeta;
    }

    public TileFruitLeaves(TileFruitLeaves tileFruitLeaves) {
        this(tileFruitLeaves.leavesBlock, tileFruitLeaves.leavesMeta);
        this.timer = 0;
    }

    @Override
    public void updateEntity() {
        if (!checked) {
            try {
                fruit = getActualLeavesType(this.leavesBlock, this.leavesMeta);
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                this.invalidate();
                return;
            }

            checked = true;
            if (!fruit.doesGenerateFruitAndSeeds()) {
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
            if (rand.nextDouble() < turnChance * turnChanceMultiplier) {
                if (this.fruit != JaffasTrees.FruitType.Vanilla || rand.nextInt(3) == 0) {
                    if (this.getBlockType() == JaffasTrees.leavesList.get(0).leavesBlock || this.getBlockType() == this.leavesBlock) {
                        ChangeBlockAndRespawnMe(this.leavesBlock, this.leavesMeta);
                    } else {
                        // no leaves block on my position => something went wrong, kill myself
                        this.invalidate();
                        if (JaffasTrees.debug)
                            Log.printInfo(this.xCoord + "," + this.yCoord + "," + this.zCoord + " - wrong block => ending my function");
                        return;
                    }
                }
            }
        }

    }

    private void ChangeBlockAndRespawnMe(Block newBlock, int newMeta) {
        BlockHelper.setBlock(worldObj, this.xCoord, this.yCoord, this.zCoord, newBlock, BlockFruitLeaves.getChangedTypeInMeta(newMeta, this.getBlockMetadata()));
        this.invalidate();
        TileFruitLeaves te = new TileFruitLeaves(this);
        worldObj.setTileEntity(this.xCoord, this.yCoord, this.zCoord, te);
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("age", this.age);
        par1NBTTagCompound.setInteger("myMaxAge", this.myMaxAge);
        par1NBTTagCompound.setInteger("timer", this.timer);
        par1NBTTagCompound.setInteger("leavesID", Block.getIdFromBlock(this.leavesBlock));
        par1NBTTagCompound.setInteger("leavesMeta", this.leavesMeta);
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        this.age = par1NBTTagCompound.getInteger("age");
        this.timer = par1NBTTagCompound.getInteger("timer");
        this.myMaxAge = par1NBTTagCompound.getInteger("myMaxAge");
        this.leavesBlock = Block.getBlockById(par1NBTTagCompound.getInteger("leavesID"));
        this.leavesMeta = par1NBTTagCompound.getInteger("leavesMeta");
    }

    public static boolean isThisBlockTransparentForFruit(Block block) {
        return fruitFallThroughBlocks.contains(block);
    }

    public boolean generateFruitAndDecay(double chanceForSecondFruit, EntityPlayer player) {
        if (this.getBlockType() != this.leavesBlock || BlockFruitLeaves.getLeavesType(this.getBlockMetadata()) != this.leavesMeta) {
            if (JaffasTrees.debug) Log.printInfo("not fruit block, no fruit generated");
            return false;
        }

        this.age = 0; // obsolete
        int fruits = 1;
        if (rand.nextDouble() < chanceForSecondFruit) fruits++;

        for (int i = 0; i < fruits; i++)
            this.generateFruit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, rand, this.leavesMeta, player);

        ChangeBlockAndRespawnMe(JaffasTrees.leavesList.get(0).leavesBlock, 0);

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
                Block currentBlock;
                do {
                    tries++;
                    newY--;
                    currentBlock = world.getBlock(newX, newY, newZ);

                    if (world.isAirBlock(newX, newY, newZ)) {
                        found = true;
                    } else if (passNum == 1) {
                        // on a second pass we try harder
                        for (int i = 0; i < fourNeighbourTable.length && !found; i++) {
                            if (world.isAirBlock(newX + fourNeighbourTable[i][0], newY, newZ + fourNeighbourTable[i][1])) {
                                newX += fourNeighbourTable[i][0];
                                newZ += fourNeighbourTable[i][1];
                                found = true;
                            }
                        }
                    }
                } while (tries <= 5 && !found && isThisBlockTransparentForFruit(currentBlock));
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
                    ent.motionX = (rand.nextDouble() - 0.5D) / 3D;
                    ent.motionY = 0;
                    ent.motionZ = (rand.nextDouble() - 0.5D) / 3D;
                    world.spawnEntityInWorld(ent);
                } else {
                    if (debug) {
                        Log.printWarning("got null in stack: m~" + metadata + " b~" + this.getBlockType().getUnlocalizedName());
                        debugPrintPos();
                    }
                }
            } else {
                if (debug) {
                    Log.printWarning("got null in fruit: m~" + metadata + " b~" + this.getBlockType().getUnlocalizedName());
                    debugPrintPos();
                }
            }
        } else {
            if (debug) Log.printWarning("tree: not found");
        }
    }

    public static ItemFromFruitResult getItemFromFruit(JaffasTrees.FruitType fruit) {
        ItemFromFruitResult res = new ItemFromFruitResult();

        switch (fruit) {
            case Apple:
                res.setStack(new ItemStack(Items.apple));
                break;

            case Cocoa:
                res.setStack(new ItemStack(Items.dye, 1, 3));
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

            case Banana:
                res.setStack(new ItemStack(itemBanana));
                break;

            default:
                if (debug) {
                    res.setMessage("unknown type of tree");
                    res.setStack(new ItemStack(Items.stick));
                } else
                    res.exception = new RuntimeException("unknown type of tree, don't have information about fruits: " + fruit);
        }

        return res;
    }

    public ItemStack getItemFromMetadataAndBlockID(JaffasTrees.FruitType fruit) {
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