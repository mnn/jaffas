package monnef.jaffas.food.item;

import monnef.jaffas.food.entity.EntitySpiderEgg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemSpiderEgg extends ItemCustomEgg {
    @Override
    protected Entity createFlyingEggEntity(World world, EntityPlayer player) {
        return new EntitySpiderEgg(world, player);
    }
}
