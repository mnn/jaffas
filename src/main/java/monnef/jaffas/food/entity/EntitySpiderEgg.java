package monnef.jaffas.food.entity;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntitySpiderEgg extends EntityFlyingEgg {
    public EntitySpiderEgg(World par1World) {
        super(par1World);
    }

    public EntitySpiderEgg(World par1World, EntityLivingBase par2EntityLiving) {
        super(par1World, par2EntityLiving);
    }

    public EntitySpiderEgg(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    @Override
    protected Entity createEntity() {
        return new EntityLittleSpider(worldObj);
    }

    protected Item itemForParticleEffect() {
        return JaffasFood.getItem(JaffaItem.littleSpiderEggs);
    }
}
