package monnef.jaffas.carts.entity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;

public class EntityLocomotive extends EntityMinecart {
    public EntityLocomotive(World par1World, double par2, double par4, double par6, int type) {
        super(par1World, par2, par4, par6, type);
    }

    public EntityLocomotive(World w) {
        super(w);
    }
}
