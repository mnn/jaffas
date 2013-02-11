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
    private World world;
    private boolean locked = false;

    public IntegerCoordinates(TileEntity tile) {
        this.world = tile.worldObj;
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;

        postInit();
    }

    public IntegerCoordinates(int x, int y, int z, World world) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        postInit();
    }

    public IntegerCoordinates(World world, NBTTagCompound tag, String compoundTagName) {
        this.world = world;
        this.compoundTagName = compoundTagName;
        loadFrom(tag, compoundTagName);

        postInit();
    }

    private void postInit() {
        locked = true;
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
    public void setWorld(World world) {
        if (world != null) {
            System.err.println("world cannot be changed");
        }

        this.world = world;
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
    public void saveTo(NBTTagCompound tag, String tagName) {
        NBTTagCompound save = new NBTTagCompound();
        save.setInteger("x", getX());
        save.setInteger("y", getY());
        save.setInteger("z", getZ());
        tag.setCompoundTag(tagName, save);
    }

    @Override
    public void loadFrom(NBTTagCompound tag, String tagName) {
        if (locked) {
            throw new RuntimeException("locked");
        }

        NBTTagCompound save = tag.getCompoundTag(tagName);
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
}
