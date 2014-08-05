/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.entity.EntityDuckEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDuckEgg extends ItemJaffaBase {
    public ItemDuckEgg() {
        super();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            stack.stackSize--;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityDuckEgg(world, player));
        }

        return stack;
    }
}
