/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.common.FungiCatalog;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMushroomKnife extends ItemTechnic {
    public ItemMushroomKnife(int textureIndex) {
        super(textureIndex);
        setMaxStackSize(1);
        setMaxDamage(1024);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        Block block = world.getBlock(x, y, z);
        ItemStack mushroom = FungiCatalog.getRandomDropFromBlock(block);
        if (mushroom == null) return false;

        if (!world.isRemote) {
            EntityItem toSpawn = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, mushroom);
            toSpawn.motionY = 0.2;
            world.spawnEntityInWorld(toSpawn);
            BlockHelper.setAir(world, x, y, z);
        }

        return true;
    }
}
