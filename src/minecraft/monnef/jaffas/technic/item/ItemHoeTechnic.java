/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ItemHoeTechnic extends ItemTechnicTool {
    public ItemHoeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int direction, float par8, float par9, float par10) {
        if (!player.canPlayerEdit(x, y, z, direction, stack)) {
            return false;
        } else {
            UseHoeEvent event = new UseHoeEvent(player, stack, world, x, y, z);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                damageTool(1, player, stack);
                return true;
            }

            int blockId = world.getBlockId(x, y, z);
            int blockAboveId = world.getBlockId(x, y + 1, z);

            if ((direction == 0 || blockAboveId != 0 || blockId != Block.grass.blockID) && blockId != Block.dirt.blockID) {
                return false;
            } else {
                Block newBlock = Block.tilledField;
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), newBlock.stepSound.getStepSound(), (newBlock.stepSound.getVolume() + 1.0F) / 2.0F, newBlock.stepSound.getPitch() * 0.8F);

                if (world.isRemote) {
                    return true;
                } else {
                    world.setBlock(x, y, z, newBlock.blockID);
                    damageTool(1, player, stack);
                    return true;
                }
            }
        }
    }
}
