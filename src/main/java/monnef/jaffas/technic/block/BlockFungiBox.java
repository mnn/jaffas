/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockFungiBox extends BlockTechnic implements IFactoryHarvestable {
    public static final MaterialFungiBox material = new MaterialFungiBox();

    private static final float U = 1f / 16;

    public BlockFungiBox(int id, int textureID) {
        super(id, textureID, material);
        Block.setBurnProperties(id, 5, 20);
        setBlockBounds(0, 0, 0, 16 * U, 6 * U, 16 * U);
        setHardness(0.25f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        TileFungiBox te = (TileFungiBox) world.getBlockTileEntity(x, y, z);
        boolean res = te.playerActivatedBox(player);
        if (MonnefCorePlugin.debugEnv) {
            te.printDebugInfo(player);
        }

        return res;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileFungiBox();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderID;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y - 1, z).isSolid();
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
        boolean drop = false;
        if (!canBlockStay(world, x, y, z)) {
            drop = true;
        }

        if (drop) {
            dropBlockAsItem(world, x, y, z, 0, 0); // metadata, fortune
            BlockHelper.setBlock(world, x, y, z, 0);
        }
    }

    public static class MaterialFungiBox extends Material {
        public MaterialFungiBox() {
            super(MapColor.woodColor);
            setBurning();
        }

        @Override
        public boolean isOpaque() {
            return false;
        }

        @Override
        public boolean isSolid() {
            return false;
        }
    }

    @Override
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float par6) {
        if (!world.isRemote && world.rand.nextFloat() < par6 - 0.5F) {
            if (!(entity instanceof EntityPlayer) && !world.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                return;
            }

            TileFungiBox tile = (TileFungiBox) world.getBlockTileEntity(x, y, z);
            tile.onFallUpon();
        }
    }

    // MFR support
    @Override
    public int getPlantId() {
        return blockID;
    }

    @Override
    public HarvestType getHarvestType() {
        return HarvestType.Normal;
    }

    @Override
    public boolean breakBlock() {
        return false;
    }

    @Override
    public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
        TileFungiBox tile = (TileFungiBox) world.getBlockTileEntity(x, y, z);
        return tile.canBeHarvested();
    }

    @Override
    public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
        TileFungiBox tile = (TileFungiBox) world.getBlockTileEntity(x, y, z);
        tile.generateDrop();
        return Arrays.asList(tile.collectLastLoot());
    }

    @Override
    public void preHarvest(World world, int x, int y, int z) {
        TileFungiBox tile = (TileFungiBox) world.getBlockTileEntity(x, y, z);
        if (!tile.harvest(null)) {
            throw new RuntimeException("Error in MFR integration.");
        }
        tile.collectLastLoot();
    }

    @Override
    public void postHarvest(World world, int x, int y, int z) {
    }
}
