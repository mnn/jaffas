package monnef.core;

import monnef.core.api.IIntegerCoordinates;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class IntegerCoordinates implements IIntegerCoordinates {
    // do not ever change after construction!
    protected int x;
    protected int y;
    protected int z;

    protected String compoundTagName;
    private final World world;

    public IntegerCoordinates(TileEntity tile) {
        this.world = tile.worldObj;
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;

        postInitChecks();
    }

    public IntegerCoordinates(int x, int y, int z, World world) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        postInitChecks();
    }

    public IntegerCoordinates(World world, NBTTagCompound tag, String compoundTagName) {
        this.world = world;
        this.compoundTagName = compoundTagName;
        loadFrom(tag);

        postInitChecks();
    }

    private void postInitChecks() {
        if (world == null) {
            throw new RuntimeException("null in world!");
        }
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
        NBTTagCompound save = new NBTTagCompound();
        save.setInteger("x", getX());
        save.setInteger("y", getY());
        save.setInteger("z", getZ());
        tag.setCompoundTag(getCompoundTagName(), save);
    }

    @Override
    public void loadFrom(NBTTagCompound tag) {
        NBTTagCompound save = tag.getCompoundTag(getCompoundTagName());
        x = save.getInteger("x");
        y = save.getInteger("y");
        z = save.getInteger("z");
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

    @Override
    public String getCompoundTagName() {
        return this.compoundTagName;
    }

    @Override
    public void setCompoundTagName(String name) {
        this.compoundTagName = name;
    }
}
