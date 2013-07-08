/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.BlockHelper;
import monnef.core.utils.WorldHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.BlockSwitchgrass;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ItemHoeTechnic extends ItemTechnicTool {

    private static final int HOE_HARVEST_RADIUS = 3;
    private static final int HOE_SWITCHGRASS_PLANT_RADIUS = 1;

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
            Block block = Block.blocksList[blockId];

            boolean tillableBlock = blockId == Block.dirt.blockID || blockId == Block.grass.blockID;
            if ((direction != 0 && blockAboveId == 0 && tillableBlock)) {
                Block newBlock = Block.tilledField;

                if (world.isRemote) {
                    return true;
                } else {
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), newBlock.stepSound.getStepSound(), (newBlock.stepSound.getVolume() + 1.0F) / 2.0F, newBlock.stepSound.getPitch() * 0.8F);
                    world.setBlock(x, y, z, newBlock.blockID);
                    damageTool(1, player, stack);
                    return true;
                }
            } else if (canBeMassHarvested(block)) {
                for (int xx = x - HOE_HARVEST_RADIUS; xx <= x + HOE_HARVEST_RADIUS; xx++) {
                    for (int zz = z - HOE_HARVEST_RADIUS; zz <= z + HOE_HARVEST_RADIUS; zz++) {
                        if (world.getBlockId(xx, y, zz) == blockId) {
                            if (!world.isRemote) {
                                world.destroyBlock(xx, y, zz, true);
                            }
                        }
                    }
                }
                damageTool(5, player, stack);
                return true;
            } else if (blockId == JaffasFood.blockSwitchgrassSolid.blockID) {
                if (!world.isRemote) world.setBlockToAir(x, y, z);
                BlockSwitchgrass switchgrass = JaffasFood.blockSwitchgrass;
                for (int xx = x - HOE_SWITCHGRASS_PLANT_RADIUS; xx <= x + HOE_SWITCHGRASS_PLANT_RADIUS; xx++) {
                    for (int zz = z - HOE_SWITCHGRASS_PLANT_RADIUS; zz <= z + HOE_SWITCHGRASS_PLANT_RADIUS; zz++) {
                        if (switchgrass.canPlaceBlockAt(world, xx, y, zz)) {
                            if (!world.isRemote) {
                                BlockHelper.setBlock(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP);
                            }
                        } else {
                            WorldHelper.dropBlockAsItemDo(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP, 1);
                        }
                    }
                }
                damageTool(2, player, stack);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean canBeMassHarvested(Block block) {
        return block instanceof IPlantable;
    }
}
