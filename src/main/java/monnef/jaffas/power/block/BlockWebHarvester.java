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
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWebHarvester extends BlockMachineWithInventory {
    public BlockWebHarvester(int index, Material material, boolean customRenderer, boolean useCustomRenderingId) {
        super(index, material, customRenderer, useCustomRenderingId);
        setIconsCount(2);
        setHardness(2f);
        setResistance(10f);
    }

    @Override
    public GuiHandler.GuiId getGuiId() {
        return GuiHandler.GuiId.WEB_HARVESTER;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileWebHarvester();
    }

    @Override
    public boolean supportRotation() {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (DirectionHelper.isYAxis(side)) return getCustomIcon(0);
        return getCustomIcon(1);
    }
}
