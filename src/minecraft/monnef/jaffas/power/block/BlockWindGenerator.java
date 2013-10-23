/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.utils.DirectionHelper;
import monnef.jaffas.power.block.common.BlockMachineWithInventory;
import monnef.jaffas.power.client.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWindGenerator extends BlockMachineWithInventory {
    public BlockWindGenerator(int id, int index, Material material, boolean customRenderer, boolean useCustomRenderingId) {
        super(id, index, material, customRenderer, useCustomRenderingId);
        useForgeDirectionsInPlacingComputation = true;
        setIconsCount(3);
    }

    @Override
    public GuiHandler.GuiId getGuiId() {
        return GuiHandler.GuiId.WIND_GENERATOR;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileWindGenerator();
    }

    @Override
    public boolean supportRotation() {
        return false;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileWindGenerator tile = (TileWindGenerator) getTile(world, x, y, z);
        return getIconFromRotation(side, world.getBlockMetadata(x, y, z), tile.getRotation().ordinal());
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return getIconFromRotation(side, meta, 0);
    }

    private Icon getIconFromRotation(int side, int meta, int rotation) {
        if (DirectionHelper.isYAxis(side)) return getCustomIcon(2);
        return rotation == side ? getCustomIcon(0) : getCustomIcon(1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        ((TileWindGenerator) getTile(world, x, y, z)).killAllTurbinesInFront();
        super.breakBlock(world, x, y, z, par5, par6);
    }
}
