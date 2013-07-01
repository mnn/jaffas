/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.BlockBasicProcessingMachine;
import monnef.jaffas.power.client.GuiHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGrinder extends BlockBasicProcessingMachine {

    public BlockGrinder(int id, int index) {
        super(id, index);
    }

    @Override
    public int getGuiId() {
        return GuiHandler.GuiId.GRINDER.ordinal();
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityGrinder();
    }
}
