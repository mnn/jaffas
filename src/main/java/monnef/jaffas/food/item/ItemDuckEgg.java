/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.entity.EntityDuckEgg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemDuckEgg extends ItemCustomEgg {
    @Override
    protected Entity createFlyingEggEntity(World world, EntityPlayer player) {
        return new EntityDuckEgg(world, player);
    }
}
