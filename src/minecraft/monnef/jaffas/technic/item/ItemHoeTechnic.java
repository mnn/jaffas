/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.BlockHelper;
import monnef.core.utils.WorldHelper;
import monnef.jaffas.food.ContentHolder;
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
    private static final int HOE_SWITCHGRASS_PLANT_RADIUS_BIG = 4;

    public ItemHoeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
    }

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
            } else if (canBeMassHarvested(block)) {
                for (int xx = x - HOE_HARVEST_RADIUS; xx <= x + HOE_HARVEST_RADIUS; xx++) {
                    for (int zz = z - HOE_HARVEST_RADIUS; zz <= z + HOE_HARVEST_RADIUS; zz++) {
                        int currBlockId = world.getBlockId(xx, y, zz);
                        boolean harvest = currBlockId == blockId;
                        if (!harvest && player.isSneaking()) {
                            harvest = canBeMassHarvested(Block.blocksList[currBlockId]);
                        }
                        if (harvest) {
                            if (!world.isRemote) {
                                world.destroyBlock(xx, y, zz, true);
                            }
                        }
                    }
                }
                damageTool(5, player, stack);
                return true;
            } else if (blockId == ContentHolder.blockSwitchgrassSolid.blockID) {
                if (!world.isRemote) world.setBlockToAir(x, y, z);
                BlockSwitchgrass switchgrass = ContentHolder.blockSwitchgrass;
                int dmgCoef = 1;

                int plantSize = HOE_SWITCHGRASS_PLANT_RADIUS;
                if (allNeighboursAreSwitchgrass(world, x, y, z)) {
                    plantSize = HOE_SWITCHGRASS_PLANT_RADIUS_BIG;
                    for (int xx = x - 1; xx <= x + 1; xx++) {
                        for (int zz = z - 1; zz <= z + 1; zz++) {
                            if (!world.isRemote) world.setBlockToAir(xx, y, zz);
                        }
                    }
                    dmgCoef = 3;
                }

                for (int xx = x - plantSize; xx <= x + plantSize; xx++) {
                    for (int zz = z - plantSize; zz <= z + plantSize; zz++) {
                        if (switchgrass.canPlaceBlockAt(world, xx, y, zz)) {
                            if (!world.isRemote) {
                                BlockHelper.setBlock(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP);
                            }
                        } else {
                            WorldHelper.dropBlockAsItemDo(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP, 1);
                        }
                    }
                }
                damageTool(2 * dmgCoef, player, stack);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean canBeTilled(int direction, int blockAboveId, int blockId) {
        boolean tillableBlock = blockId == Block.dirt.blockID || blockId == Block.grass.blockID;
        return (direction != 0 && blockAboveId == 0 && tillableBlock);
    }

    private boolean allNeighboursAreSwitchgrass(World world, int x, int y, int z) {
        for (int xx = x - 1; xx <= x + 1; xx++) {
            for (int zz = z - 1; zz <= z + 1; zz++) {
                if (xx == x && zz == z) continue; // skip already destroyed block
                if (world.getBlockId(xx, y, zz) != ContentHolder.blockSwitchgrassSolid.blockID) return false;
            }
        }
        return true;
    }

    private boolean canBeMassHarvested(Block block) {
        return block instanceof IPlantable;
    }
}
