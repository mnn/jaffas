package monnef.jaffas.power;

import monnef.core.IntegerCoordinates;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerNode;
import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PowerNodeCoordinates extends IntegerCoordinates implements IPowerNodeCoordinates {

    public PowerNodeCoordinates(int x, int y, int z, World world) {
        super(x, y, z, world);
    }

    public PowerNodeCoordinates(World world, NBTTagCompound tag, String compoundTagName) {
        super(world, tag, compoundTagName);
    }

    public PowerNodeCoordinates(TileEntity tile) {
        super(tile);
    }

    @Override
    public IPowerNode asNode() {
        return (IPowerNode) getTile();
    }

    @Override
    public IPowerProvider asProvider() {
        return (IPowerProvider) getTile();
    }

    @Override
    public IPowerConsumer asConsumer() {
        return (IPowerConsumer) getTile();
    }

}
