package monnef.jaffas.trees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockFruitLeaves extends BlockLeavesBase {
    // Old:    .. D N T T
    // D - decay, N - never decay, T - type
    public static final int bitMarkedForDecay = 8;
    public static final int bitNeverDecay = 4;
    public static final int bitMaskLeavesType = 3;

    public int serialNumber = -1;

    /**
     * The base index in terrain.png corresponding to the fancy version of the leaf texture. This is stored so we can
     * switch the displayed version between fancy and fast graphics (fast is this index + 1).
     */
    int[] adjacentTreeBlocks;
    private int subCount;
    private static Random rand = new Random();

    public BlockFruitLeaves(int par1, int par2, int subCount) {
        super(par1, par2, Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        mod_jaffas_trees.proxy.setFancyGraphicsLevel(this, true);
        this.subCount = subCount;
        //this.setGraphicsLevel(true);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    public BlockFruitLeaves setLeavesRequiresSelfNotify() {
        this.setRequiresSelfNotify();
        return this;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (player.isSneaking()) return false;

        ItemStack handItem = player.getCurrentEquippedItem();
        if (handItem != null) {
            int itemId = handItem.getItem().shiftedIndex;
            if (itemId == mod_jaffas_trees.itemDebug.shiftedIndex) {
                int bid = world.getBlockId(x, y, z);
                int bmeta = world.getBlockMetadata(x, y, z);

                TileEntity e = world.getBlockTileEntity(x, y, z);
                player.addChatMessage(x + "," + y + "," + z + "~" + bid + ":" + bmeta);
                String msg = "E~";
                msg += e == null ? "NULL" : e.getClass();
                player.addChatMessage(msg);

                return false;
            }
        }

        if (world.isRemote) return true;

        if (handItem != null) {
            if (handItem.getItem().shiftedIndex == mod_jaffas_trees.itemRod.shiftedIndex) {
                boolean harvested;
                harvested = harvestArea(world, x, y, z, 0.10, null, 3);
                if (harvested || rand.nextInt(3) == 0) PlayerHelper.damageCurrentItem(player);
                return harvested;
            } else if (handItem.getItem().shiftedIndex == mod_jaffas_trees.itemFruitPicker.shiftedIndex) {
                boolean harvested;
                harvested = harvestArea(world, x, y, z, 0.50, player, 5);
                if (harvested || rand.nextInt(3) == 0) PlayerHelper.damageCurrentItem(player);
                return harvested;
            }
        }

        if (this.haveFruit(world, x, y, z)) {
            return harvest(world, x, y, z, 0, null);
        }

        return false;
    }

    private static final int[][] eightNeighbourWithMeTable = {{0, 0}, {0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    private boolean harvestArea(World world, int x, int y, int z, double critChance, EntityPlayer player, int height) {
        ArrayList<Boolean> allowedPillar = new ArrayList<Boolean>();
        for (int i = 0; i < eightNeighbourWithMeTable.length; i++)
            allowedPillar.add(true);

        int myLevel = 0;
        ArrayList<Integer> blocksToInspect = new ArrayList<Integer>();
        boolean found = false;
        int bx = -1000, by = -1000, bz = -1000;
        while (height > myLevel) {
            // prepare current level
            blocksToInspect.clear();
            for (int i = 0; i < allowedPillar.size(); i++)
                if (allowedPillar.get(i)) blocksToInspect.add(i);

            while (!blocksToInspect.isEmpty()) {
                int toPop = rand.nextInt(blocksToInspect.size());
                int blockNum = blocksToInspect.get(toPop); // get block's number in neighbour list
                blocksToInspect.remove(toPop);
                // compute block's coordinates
                by = y + myLevel;
                bx = x + eightNeighbourWithMeTable[blockNum][0];
                bz = z + eightNeighbourWithMeTable[blockNum][1];

                int currentBlockID = world.getBlockId(bx, by, bz);
                if (haveFruit(world, bx, by, bz))
                    found = true;

                // on a solid block we make a mark to not test blocks above current block
                if (!TileEntityFruitLeaves.isThisBlockTransparentForFruit(currentBlockID) && currentBlockID != 0)
                    allowedPillar.set(blockNum, false);

                if (found) break;
            }

            if (found) break;
            myLevel++;
        }

        if (!found) return false;

        return harvest(world, bx, by, bz, critChance, player);

        //return harvest(world, x, y, z, critChance, player);
    }

    public static boolean harvest(World world, int x, int y, int z, double critChance, EntityPlayer player) {
        TileEntity e = world.getBlockTileEntity(x, y, z);
        if (e == null || !(e instanceof TileEntityFruitLeaves)) {
            //if (mod_jaffas_trees.debug) System.err.println("null in TE, where are my leaves?");
            return false;
        }

        TileEntityFruitLeaves te = (TileEntityFruitLeaves) e;
        return te.generateFruitAndDecay(critChance, player);
    }

    public static boolean haveFruit(World world, int x, int y, int z) {
        int blockId = world.getBlockId(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        Block b = Block.blocksList[blockId];
        if (!(b instanceof BlockFruitLeaves)) return false;

        BlockFruitLeaves leaves = (BlockFruitLeaves) b;
        mod_jaffas_trees.fruitType fruit = mod_jaffas_trees.getActualLeavesType(leaves, BlockFruitLeaves.getLeavesType(meta));
        return fruit != mod_jaffas_trees.fruitType.Normal;
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        if (hasTileEntity(par1World.getBlockMetadata(par2, par3, par4))) {
            //par1World.setBlockTileEntity(par2, par3, par4, this.createNewTileEntity(par1World));
        }
    }

    public TileEntity createNewTileEntity(World par1World) {
        return new TileEntityFruitLeaves();
    }

    public boolean hasTileEntity(int metadata) {
        //return getLeavesType(metadata) != 0;
        return true;
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        byte var7 = 1;
        int var8 = var7 + 1;

        par1World.removeBlockTileEntity(par2, par3, par4);

        if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
            for (int var9 = -var7; var9 <= var7; ++var9) {
                for (int var10 = -var7; var10 <= var7; ++var10) {
                    for (int var11 = -var7; var11 <= var7; ++var11) {
                        int var12 = par1World.getBlockId(par2 + var9, par3 + var10, par4 + var11);

                        if (Block.blocksList[var12] != null) {
                            Block.blocksList[var12].beginLeavesDecay(par1World, par2 + var9, par3 + var10, par4 + var11);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            int metadata = world.getBlockMetadata(x, y, z);

            if (areLeavesMarkedForDecay(metadata) && !areLeavesNeverDecaying(metadata)) {
                byte var7 = 4;
                int var8 = var7 + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;

                if (this.adjacentTreeBlocks == null) {
                    this.adjacentTreeBlocks = new int[var9 * var9 * var9];
                }

                int var12;

                if (world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                    int var13;
                    int var14;
                    int var15;

                    for (var12 = -var7; var12 <= var7; ++var12) {
                        for (var13 = -var7; var13 <= var7; ++var13) {
                            for (var14 = -var7; var14 <= var7; ++var14) {
                                var15 = world.getBlockId(x + var12, y + var13, z + var14);

                                Block block = Block.blocksList[var15];

                                if (block != null && block.canSustainLeaves(world, x + var12, y + var13, z + var14)) {
                                    this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
                                } else if (block != null && block.isLeaves(world, x + var12, y + var13, z + var14)) {
                                    this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
                                } else {
                                    this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
                                }
                            }
                        }
                    }

                    for (var12 = 1; var12 <= 4; ++var12) {
                        for (var13 = -var7; var13 <= var7; ++var13) {
                            for (var14 = -var7; var14 <= var7; ++var14) {
                                for (var15 = -var7; var15 <= var7; ++var15) {
                                    if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11] == var12 - 1) {
                                        if (this.adjacentTreeBlocks[(var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15 + var11] = var12;
                                        }

                                        if (this.adjacentTreeBlocks[(var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15 + var11] = var12;
                                        }

                                        if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15 + var11] = var12;
                                        }

                                        if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15 + var11] = var12;
                                        }

                                        if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + (var15 + var11 - 1)] == -2) {
                                            this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + (var15 + var11 - 1)] = var12;
                                        }

                                        if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11 + 1] == -2) {
                                            this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11 + 1] = var12;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                var12 = this.adjacentTreeBlocks[var11 * var10 + var11 * var9 + var11];

                if (var12 >= 0) {
                    // 9 -> 0..001001 -> 1..110110 +1 -> 1..110111
                    // => masking out 0x8 bit
                    world.setBlockMetadata(x, y, z, unsetDecayBit(metadata));
                } else {
                    this.removeLeaves(world, x, y, z);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (par1World.canLightningStrikeAt(par2, par3 + 1, par4) && !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && par5Random.nextInt(15) == 1) {
            double var6 = (double) ((float) par2 + par5Random.nextFloat());
            double var8 = (double) par3 - 0.05D;
            double var10 = (double) ((float) par4 + par5Random.nextFloat());
            par1World.spawnParticle("dripWater", var6, var8, var10, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World par1World, int par2, int par3, int par4) {
        this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
        par1World.setBlockWithNotify(par2, par3, par4, 0);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random) {
        return 0;
        //return par1Random.nextInt(20) == 0 ? 1 : 0;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    /*
    public int idDropped(int par1, Random par2Random, int par3) {
        return mod_jaffas_trees.blockFruitSapling.blockID;
    } */

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int metadata, float par6, int par7) {
        if (!par1World.isRemote) {
            /*
            if (par1World.rand.nextInt(20) == 0) {
                this.dropBlockAsItem_do(par1World, par2, par3, par4, TileEntityFruitLeaves.getItemFromMetadata(metadata));
            }                */
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1) {
        return getLeavesType(par1);
    }

    // Bit stuff
    public static int getLeavesType(int par1) {
        return par1 & bitMaskLeavesType;
    }

    public static int setLeavesDecay(int par4) {
        return par4 | bitMarkedForDecay;
        //return BitHelper.setBit(par4, bitMarkedForDecayN);
    }

    private static int unsetDecayBit(int metadata) {
        return metadata & -9;
        //return BitHelper.unsetBit(metadata, bitMarkedForDecayN);
    }

    public static boolean areLeavesMarkedForDecay(int metadata) {
        return (metadata & bitMarkedForDecay) != 0;
        //return BitHelper.isBitSet(metadata, bitMarkedForDecayN);
    }

    private static boolean areLeavesNeverDecaying(int metadata) {
        return (metadata & bitNeverDecay) == 1;
        //return BitHelper.isBitSet(metadata, bitNeverDecayN);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
        //return (par2 & 3) == 1 ? this.blockIndexInTexture + 80 : ((par2 & 3) == 3 ? this.blockIndexInTexture + 144 : this.blockIndexInTexture);
        //return (getLeavesType(par2)) == 1 ? this.blockIndexInTexture + 80 : ((getLeavesType(par2)) == 3 ? this.blockIndexInTexture + 144 : this.blockIndexInTexture);
        return this.blockIndexInTexture + getLeavesType(par2);
        //return this.blockIndexInTexture + par2;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    public void setGraphicsLevel(boolean par1) {
        this.graphicsLevel = par1;
        //this.blockIndexInTexture = this.baseIndexInPNG + (par1 ? 0 : 1);
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
        return !this.graphicsLevel && var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    @SideOnly(Side.CLIENT)
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subCount; i++)
            par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z) {
        world.setBlockMetadata(x, y, z, setLeavesDecay(world.getBlockMetadata(x, y, z)));
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z) {
        return true;
    }

    public static int getChangedTypeInMeta(int leavesMeta, int oldMeta) {
        return (oldMeta & 12) | (getLeavesType(leavesMeta));
    }
}
