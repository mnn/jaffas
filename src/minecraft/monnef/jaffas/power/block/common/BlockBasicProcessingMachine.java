package monnef.jaffas.power.block.common;

import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBasicProcessingMachine extends BlockMachineWithInventory {
    private final Class<? extends TileEntityBasicProcessingMachine> tileClass;
    private final GuiHandler.GuiId guiId;

    public BlockBasicProcessingMachine(int id, int index, Class<? extends TileEntityBasicProcessingMachine> tileClass, GuiHandler.GuiId guiId) {
        super(id, index, JaffasTechnic.breakableIronMaterial, false);
        this.tileClass = tileClass;
        this.guiId = guiId;
    }

    @Override
    public boolean supportRotation() {
        return true;
    }

    public TileEntity createTileEntity(World world, int meta) {
        return createBasicProcessingMachineTileEntity(world, meta);
    }

    public TileEntityBasicProcessingMachine createBasicProcessingMachineTileEntity(World world, int meta) {
        try {
            return tileClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GuiHandler.GuiId getGuiId() {
        return guiId;
    }
}
