package monnef.jaffas.carts.entity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLocomotive extends EntityMinecart {
    public static final String REVERSE_TAG_NAME = "isInReverse";

    public EntityLocomotive(World par1World, double par2, double par4, double par6, int type) {
        super(par1World, par2, par4, par6, type);
    }

    public EntityLocomotive(World w) {
        super(w);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean(REVERSE_TAG_NAME, field_70499_f);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        field_70499_f = tag.getBoolean(REVERSE_TAG_NAME);
    }
}
