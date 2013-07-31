/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.common;

import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.block.TileFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenFruitTrees extends WorldGenerator {
    /**
     * The minimum height of a generated tree.
     */
    private final int minTreeHeight;

    /**
     * True if this tree should grow Vines.
     */
    private final boolean vinesGrow;

    /**
     * The metadata value of the wood to use in tree generation.
     */
    private final int metaWood;

    /**
     * The metadata value of the leaves to use in tree generation.
     */
    private final int metaLeaves;

    private final int leavesID;

    public WorldGenFruitTrees(boolean par1) {
        this(par1, 4, 0, 0, false, JaffasTrees.leavesList.get(0).leavesID);
    }

    public WorldGenFruitTrees(boolean notify, int minHeight, int metaWood, int metaLeaves, boolean vines, int leavesID) {
        super(notify);
        this.minTreeHeight = minHeight;
        this.metaWood = metaWood;
        this.metaLeaves = metaLeaves;
        this.vinesGrow = vines;
        this.leavesID = leavesID;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        int treeHeight = random.nextInt(3) + this.minTreeHeight;
        boolean doGenerate = true;

        // check
        if (y >= 1 && y + treeHeight + 1 <= 256) {
            for (int stemY = y; stemY <= y + 1 + treeHeight; ++stemY) {
                byte radius = 1;

                if (stemY == y) {
                    radius = 0;
                }

                if (stemY >= y + 1 + treeHeight - 2) {
                    radius = 2;
                }

                for (int xx = x - radius; xx <= x + radius && doGenerate; ++xx) {
                    for (int zz = z - radius; zz <= z + radius && doGenerate; ++zz) {
                        if (stemY >= 0 && stemY < 256) {
                            int blockId = world.getBlockId(xx, stemY, zz);

                            Block block = Block.blocksList[blockId];

                            if (blockId != 0 &&
                                    !block.isLeaves(world, xx, stemY, zz) &&
                                    blockId != Block.grass.blockID &&
                                    blockId != Block.dirt.blockID &&
                                    !block.isWood(world, xx, stemY, zz)) {
                                doGenerate = false;
                            }
                        } else {
                            doGenerate = false;
                        }
                    }
                }
            }

            if (!doGenerate) {
                return false;
            } else {
                int blockUnder = world.getBlockId(x, y - 1, z);

                if ((blockUnder == Block.grass.blockID || blockUnder == Block.dirt.blockID) && y < 256 - treeHeight - 1) {
                    this.setBlock(world, x, y - 1, z, Block.dirt.blockID);
                    byte topHeight = 3;
                    byte var18 = 0;
                    int crownRadius;

                    for (int yy = y - topHeight + treeHeight; yy <= y + treeHeight; ++yy) {
                        int currentTopLevel = yy - (y + treeHeight);
                        crownRadius = var18 + 1 - currentTopLevel / 2;

                        for (int xx = x - crownRadius; xx <= x + crownRadius; ++xx) {
                            int xxShift = xx - x;

                            for (int zz = z - crownRadius; zz <= z + crownRadius; ++zz) {
                                int zzShift = zz - z;

                                Block block = Block.blocksList[world.getBlockId(xx, yy, zz)];

                                if ((Math.abs(xxShift) != crownRadius || Math.abs(zzShift) != crownRadius || random.nextInt(2) != 0 && currentTopLevel != 0) &&
                                        (block == null || block.canBeReplacedByLeaves(world, xx, yy, zz))) {

                                    int chosenLeavesMeta;
                                    int chosenLeavesID;

                                    if (random.nextInt(3) == 0) {
                                        chosenLeavesID = this.leavesID;
                                        chosenLeavesMeta = this.metaLeaves;
                                    } else {
                                        chosenLeavesID = JaffasTrees.leavesList.get(0).leavesID;
                                        chosenLeavesMeta = 0;
                                    }

                                    this.setBlockAndMetadata(world, xx, yy, zz, JaffasTrees.leavesList.get(0).leavesID, 0);
                                    TileFruitLeaves te = new TileFruitLeaves(chosenLeavesID, chosenLeavesMeta);
                                    world.setBlockTileEntity(xx, yy, zz, te);
                                }
                            }
                        }
                    }

                    for (int yy = 0; yy < treeHeight; ++yy) {
                        int blockId = world.getBlockId(x, y + yy, z);

                        Block block = Block.blocksList[blockId];

                        if (blockId == 0 || block == null || block.isLeaves(world, x, y + yy, z)) {
                            this.setBlockAndMetadata(world, x, y + yy, z, Block.wood.blockID, this.metaWood);

                            if (this.vinesGrow && yy > 0) {
                                if (random.nextInt(3) > 0 && world.isAirBlock(x - 1, y + yy, z)) {
                                    this.setBlockAndMetadata(world, x - 1, y + yy, z, Block.vine.blockID, 8);
                                }

                                if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + yy, z)) {
                                    this.setBlockAndMetadata(world, x + 1, y + yy, z, Block.vine.blockID, 2);
                                }

                                if (random.nextInt(3) > 0 && world.isAirBlock(x, y + yy, z - 1)) {
                                    this.setBlockAndMetadata(world, x, y + yy, z - 1, Block.vine.blockID, 1);
                                }

                                if (random.nextInt(3) > 0 && world.isAirBlock(x, y + yy, z + 1)) {
                                    this.setBlockAndMetadata(world, x, y + yy, z + 1, Block.vine.blockID, 4);
                                }
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Grows vines downward from the given block for a given length. Args: World, x, starty, z, vine-length
     */
    private void growVines(World par1World, int par2, int par3, int par4, int par5) {
        this.setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
        int var6 = 4;

        while (true) {
            --par3;

            if (par1World.getBlockId(par2, par3, par4) != 0 || var6 <= 0) {
                return;
            }

            this.setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
            --var6;
        }
    }
}
