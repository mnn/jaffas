/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.MonnefCorePlugin;
import monnef.core.api.ICustomIcon;
import monnef.core.common.CustomIconHelper;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.block.BlockLeavesBaseJaffas;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.core.utils.BlockHelper.setBlockMetadata;

public class BlockFruitLeaves extends BlockLeavesBaseJaffas {
    // D N T T
    // D - decay, N - never decay, T - type
    public static final int bitMarkedForDecay = 8;
    public static final int bitNeverDecay = 4;
    public static final int bitMaskLeavesType = 3;

    public int serialNumber = -1;

    int[] adjacentTreeBlocks;
    private int subCount;
    private static Random rand = new Random();
    private Icon[] icons;
    private static Icon plainLeavesIcon;

    public BlockFruitLeaves(int par1, int index, int subCount) {
        super(par1, index, Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        JaffasTrees.proxy.setFancyGraphicsLevel(this, true);
        this.subCount = subCount;
        //this.setGraphicsLevel(true);
        setCreativeTab(JaffasTrees.instance.creativeTab);
        setSheetNumber(2);
        setBurnProperties(blockID, 30, 60);
        removeFromCreativeTab();
    }

    public Icon getFruitIcon(int meta) {
        return icons[getLeavesType(meta)];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (player.isSneaking()) return false;

        ItemStack handItem = player.getCurrentEquippedItem();
        if (handItem != null) {
            int itemId = handItem.getItem().itemID;
            if (itemId == JaffasTrees.itemDebug.itemID) {
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
            if (handItem.getItem().itemID == JaffasTrees.itemRod.itemID) {
                boolean harvested;
                harvested = harvestArea(world, x, y, z, 0.10, null, 3);
                if (harvested || rand.nextInt(3) == 0) PlayerHelper.damageCurrentItem(player);
                return harvested;
            } else if (handItem.getItem().itemID == JaffasTrees.itemFruitPicker.itemID) {
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
                if (!TileFruitLeaves.isThisBlockTransparentForFruit(currentBlockID) && currentBlockID != 0)
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
        if (e == null || !(e instanceof TileFruitLeaves)) {
            //if (JaffasTrees.debug) System.err.println("null in TE, where are my leaves?");
            return false;
        }

        TileFruitLeaves te = (TileFruitLeaves) e;
        return te.generateFruitAndDecay(critChance, player);
    }

    public static boolean haveFruit(World world, int x, int y, int z) {
        int blockId = world.getBlockId(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        Block b = Block.blocksList[blockId];
        if (!(b instanceof BlockFruitLeaves)) return false;

        BlockFruitLeaves leaves = (BlockFruitLeaves) b;
        JaffasTrees.FruitType fruit = JaffasTrees.getActualLeavesType(leaves, BlockFruitLeaves.getLeavesType(meta));
        return fruit.doesGenerateFruitAndSeeds();
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        if (hasTileEntity(par1World.getBlockMetadata(par2, par3, par4))) {
            //par1World.setBlockTileEntity(par2, par3, par4, this.createNewTileEntity(par1World));
        }
    }

    public TileEntity createNewTileEntity(World par1World) {
        return new TileFruitLeaves();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        //return getLeavesType(metadata) != 0;
        return true;
    }

    @Override
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

    @Override
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
                    setBlockMetadata(world, x, y, z, unsetDecayBit(metadata));
                } else {
                    this.removeLeaves(world, x, y, z);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
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
        setBlock(par1World, par2, par3, par4, 0);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int metadata, float par6, int par7) {
        if (!par1World.isRemote) {
            /*
            if (par1World.rand.nextInt(20) == 0) {
                this.dropBlockAsItem_do(par1World, par2, par3, par4, TileFruitLeaves.getItemFromMetadata(metadata));
            }                */
        }
    }

    @Override
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    @Override
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

    @Override
    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return plainLeavesIcon;
        //return icons[getLeavesType(meta)];
    }

    @Override
    public String getDefaultModName() {
        return Reference.ModName;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[subCount];
        for (int i = 0; i < subCount; i++) {
            icons[i] = iconRegister.registerIcon(CustomIconHelper.generateShiftedId((ICustomIcon) this, i));
        }
        if (serialNumber == 0) plainLeavesIcon = icons[0];
    }

    @SideOnly(Side.CLIENT)
    public void setGraphicsLevel(boolean par1) {
        this.graphicsLevel = par1;
        //this.blockIndexInTexture = this.baseIndexInPNG + (par1 ? 0 : 1);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
        return !this.graphicsLevel && var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subCount; i++)
            par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z) {
        setBlockMetadata(world, x, y, z, setLeavesDecay(world.getBlockMetadata(x, y, z)));
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z) {
        return true;
    }

    public static int getChangedTypeInMeta(int leavesMeta, int oldMeta) {
        return (oldMeta & 12) | (getLeavesType(leavesMeta));
    }

    // mostly taken from BlockLeaves
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
        int red = 0;
        int green = 0;
        int blue = 0;

        ColorHelper.IntColor weightedColor = new ColorHelper.IntColor();
        for (int zShift = -1; zShift <= 1; ++zShift) {
            for (int xShift = -1; xShift <= 1; ++xShift) {
                int foliageColor = access.getBiomeGenForCoords(x + xShift, z + zShift).getBiomeFoliageColor();
                ColorHelper.IntColor currentFoliageColor = ColorHelper.getColor(foliageColor);
                red += currentFoliageColor.getRed();
                green += currentFoliageColor.getGreen();
                blue += currentFoliageColor.getBlue();
            }
        }

        int div = 9;
        int shift = 10;
        //return (red / 9 & 255) << 16 | (green / 9 & 255) << 8 | blue / 9 & 255;
        return ColorHelper.getInt(red / div + shift, green / div + shift, blue / div + shift);
    }

    @Override
    public int getRenderType() {
        return JaffasTrees.leavesRenderID;
    }

    public boolean doFruitRendering(int meta) {
        return !(serialNumber == 0 && getLeavesType(meta) == 0);
    }
}
