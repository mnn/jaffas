/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.entity;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityDuckEgg extends EntityFlyingEgg {

    public EntityDuckEgg(World par1World) {
        super(par1World);
    }

    public EntityDuckEgg(World par1World, EntityLivingBase par2EntityLiving) {
        super(par1World, par2EntityLiving);
    }

    public EntityDuckEgg(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    @Override
    protected Entity createEntity() {
        EntityDuck duck = new EntityDuck(this.worldObj);
        duck.setGrowingAge(-24000);
        return duck;
    }

    @Override
    protected Item itemForParticleEffect() {
        return JaffasFood.getItem(JaffaItem.duckEgg);
    }
}
