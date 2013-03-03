package monnef.jaffas.carts.entity;

import monnef.jaffas.carts.mod_jaffas_carts;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static monnef.core.utils.MathHelper.degToRad;

public class EntityLocomotive extends EntityMinecart {
    public static final String REVERSE_TAG_NAME = "isInReverse";

    public EntityLocomotive(World par1World, double par2, double par4, double par6, int type) {
        super(par1World, par2, par4, par6, type);
        this.setSize(0.98F, 1.7F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityLocomotive(World w) {
        super(w);
    }

    @Override
    public List<ItemStack> getItemsDropped() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(mod_jaffas_carts.itemLocomotive));
        return ret;
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

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer) {
        fillFuel(par1EntityPlayer);
        return true;
    }

    private void fillFuel(EntityPlayer par1EntityPlayer) {
        fuel += 1000;
        float rot = degToRad(rotationYaw);
        this.pushX = this.posX - -MathHelper.cos(rot);
        this.pushZ = this.posZ - -MathHelper.sin(rot);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (isMinecartPowered() && rand.nextInt(5) == 0 && getClass() == EntityLocomotive.class) {
            float x, z;
            float rot = degToRad(rotationYaw);

            x = -MathHelper.cos(rot);
            z = -MathHelper.sin(rot);
            float r = 0.6f;

            for (int i = 0; i < 2; i++) {
                worldObj.spawnParticle("largesmoke", posX + x * r, posY + 1.2D, posZ + z * r, 0.0D, rand.nextInt(2) == 0 ? 0.001D : 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isPoweredCart() {
        return getClass() == EntityLocomotive.class;
    }

    public boolean isOnRails() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);

        if (BlockRail.isRailBlockAt(this.worldObj, x, y - 1, z)) {
            return true;
            //--y;
        }

        return false;
    }
}
