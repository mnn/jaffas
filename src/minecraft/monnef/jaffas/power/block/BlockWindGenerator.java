/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

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
        setIconsCount(2);
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
        return tile.getRotation().ordinal() == side ? icons[0] : icons[1];
    }
}
