package monnef.core;

import monnef.core.api.IIntegerCoordinates;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class IntegerCoordinates implements IIntegerCoordinates {
    public final int x;
    public final int y;
    public final int z;
    private final World world;

    public IntegerCoordinates(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public TileEntity getTile() {
        return world.getBlockTileEntity(x, y, z);
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void saveTo(NBTTagCompound tag) {
        throw new RuntimeException("Not implemented yet.");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void LoadFrom(NBTTagCompound tag) {
        throw new RuntimeException("Not implemented yet.");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerCoordinates that = (IntegerCoordinates) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
