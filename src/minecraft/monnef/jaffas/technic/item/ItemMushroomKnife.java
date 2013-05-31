/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.common.FungiCatalog;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMushroomKnife extends ItemTechnic {
    public ItemMushroomKnife(int id, int textureIndex) {
        super(id, textureIndex);
        setMaxStackSize(1);
        setMaxDamage(1024);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        int blockId = world.getBlockId(x, y, z);
        ItemStack mushroom = FungiCatalog.getRandomDropFromBlock(blockId);
        if (mushroom == null) return false;

        if (!world.isRemote) {
            EntityItem toSpawn = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, mushroom);
            toSpawn.motionY = 0.2;
            world.spawnEntityInWorld(toSpawn);
            BlockHelper.setBlock(world, x, y, z, 0);
        }

        return true;
    }
}
