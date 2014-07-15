/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import cpw.mods.fml.common.eventhandler.Event;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import static monnef.jaffas.technic.item.ItemHoeTechnicHelper.canBeMassHarvested;
import static monnef.jaffas.technic.item.ItemHoeTechnicHelper.doHarvesting;
import static monnef.jaffas.technic.item.ItemHoeTechnicHelper.doSwitchgrassPlanting;

public class ItemHoeTechnic extends ItemTechnicTool {
    public static boolean falloutScanAllowed = true;
    public static int falloutScanRadius = 7;

    public ItemHoeTechnic(int textureOffset, ToolMaterial material) {
        super(textureOffset, material);
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

            int aboveY = y + 1;
            Block blockAbove = world.getBlock(x, aboveY, z);
            Block block = world.getBlock(x, y, z);
            if (canBeTilled(world, direction, blockAbove, x, aboveY, z, block)) {
                return doTilling(stack, player, world, x, y, z, direction);
            } else if (canBeMassHarvested(block)) {
                return doHarvesting(stack, player, world, x, y, z, block, this);
            } else if (block == ContentHolder.blockSwitchgrassSolid) {
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
        Block newBlock = Blocks.farmland;

        if (world.isRemote) {
            return true;
        } else {
            world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), newBlock.stepSound.getStepResourcePath(), (newBlock.stepSound.getVolume() + 1.0F) / 2.0F, newBlock.stepSound.getPitch() * 0.8F);
            BlockHelper.setBlock(world, x, y, z, newBlock);
            damageTool(1, player, stack);
            if (player.isSneaking() && !nearlyDestroyed(stack)) {
                damageTool(1, player, stack);
                for (int xx = x - 1; xx <= x + 1; xx++) {
                    for (int zz = z - 1; zz <= z + 1; zz++) {
                        Block b = world.getBlock(xx, y, zz);
                        Block bAbove = world.getBlock(xx, y + 1, zz);
                        if (canBeTilled(world, direction, bAbove, xx, y + 1, zz, b) && !nearlyDestroyed(stack)) {
                            BlockHelper.setBlock(world, xx, y, zz, newBlock);
                            damageTool(1, player, stack);
                        }
                    }
                }
            }
            return true;
        }
    }

    private boolean canBeTilled(IBlockAccess world, int direction, Block blockAbove, int aboveX, int aboveY, int aboveZ, Block block) {
        boolean tillableBlock = block == Blocks.dirt || block == Blocks.grass;
        return (direction != 0 && blockAbove.isAir(world, aboveX, aboveY, aboveZ) && tillableBlock);
    }
}
