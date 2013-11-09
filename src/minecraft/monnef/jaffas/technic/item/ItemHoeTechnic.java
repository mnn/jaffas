/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import static monnef.jaffas.technic.item.ItemHoeTechnicHelper.canBeMassHarvested;
import static monnef.jaffas.technic.item.ItemHoeTechnicHelper.doHarvesting;
import static monnef.jaffas.technic.item.ItemHoeTechnicHelper.doSwitchgrassPlanting;

public class ItemHoeTechnic extends ItemTechnicTool {

    public ItemHoeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int direction, float par8, float par9, float par10) {
        if (!player.canPlayerEdit(x, y, z, direction, stack)) {
            return false;
        } else {
            if (nearlyDestroyed(stack)) {
                return false;
            }

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
            Block block = Block.blocksList[blockId];
            if (canBeTilled(direction, blockAboveId, blockId)) {
                return doTilling(stack, player, world, x, y, z, direction);
            } else if (canBeMassHarvested(block)) {
                return doHarvesting(stack, player, world, x, y, z, blockId, this);
            } else if (blockId == ContentHolder.blockSwitchgrassSolid.blockID) {
                return doSwitchgrassPlanting(stack, player, world, x, y, z, this);
            } else {
                return false;
            }
        }
    }

    @Override
    public void damageTool(int dmg, EntityLivingBase source, ItemStack stack) {
        super.damageTool(dmg, source, stack);
    }

    private boolean doTilling(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int direction) {
        Block newBlock = Block.tilledField;

        if (world.isRemote) {
            return true;
        } else {
            world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), newBlock.stepSound.getStepSound(), (newBlock.stepSound.getVolume() + 1.0F) / 2.0F, newBlock.stepSound.getPitch() * 0.8F);
            BlockHelper.setBlock(world, x, y, z, newBlock.blockID);
            damageTool(1, player, stack);
            if (player.isSneaking() && !nearlyDestroyed(stack)) {
                damageTool(1, player, stack);
                for (int xx = x - 1; xx <= x + 1; xx++) {
                    for (int zz = z - 1; zz <= z + 1; zz++) {
                        int bId = world.getBlockId(xx, y, zz);
                        int bAboveId = world.getBlockId(xx, y + 1, zz);
                        if (canBeTilled(direction, bAboveId, bId) && !nearlyDestroyed(stack)) {
                            BlockHelper.setBlock(world, xx, y, zz, newBlock.blockID);
                            damageTool(1, player, stack);
                        }
                    }
                }
            }
            return true;
        }
    }

    private boolean canBeTilled(int direction, int blockAboveId, int blockId) {
        boolean tillableBlock = blockId == Block.dirt.blockID || blockId == Block.grass.blockID;
        return (direction != 0 && blockAboveId == 0 && tillableBlock);
    }
}
