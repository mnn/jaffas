/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item

import monnef.jaffas.food.block.BlockSwitchgrass
import monnef.jaffas.food.common.ContentHolder
import monnef.core.utils.{WorldHelper, BlockHelper}
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object ItemHoeTechnicHelper {
  private final val HOE_SWITCHGRASS_PLANT_RADIUS: Int = 1
  private final val HOE_SWITCHGRASS_PLANT_RADIUS_BIG: Int = 4

  def doSwitchgrassPlanting(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, hoe: ItemHoeTechnic): Boolean = {
    if (!world.isRemote) world.setBlockToAir(x, y, z)
    val switchgrass: BlockSwitchgrass = ContentHolder.blockSwitchgrass
    var dmgCoef: Int = 1
    var plantSize: Int = HOE_SWITCHGRASS_PLANT_RADIUS
    if (allNeighboursAreSwitchgrass(world, x, y, z)) {
      plantSize = HOE_SWITCHGRASS_PLANT_RADIUS_BIG
      var xx: Int = x - 1
      while (xx <= x + 1) {
        var zz: Int = z - 1
        while (zz <= z + 1) {
          if (!world.isRemote) world.setBlockToAir(xx, y, zz)
          zz += 1
        }
        xx += 1
      }
      dmgCoef = 3
    }
    var xx: Int = x - plantSize
    while (xx <= x + plantSize) {
      var zz: Int = z - plantSize
      while (zz <= z + plantSize) {
        if (switchgrass.canPlaceBlockAt(world, xx, y, zz)) {
          if (!world.isRemote)
            BlockHelper.setBlock(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP)
        }
        else {
          WorldHelper.dropBlockAsItemDo(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP, 1)
        }
        zz += 1
      }
      xx += 1
    }
    hoe.damageTool(2 * dmgCoef, player, stack)
    true
  }

  private def allNeighboursAreSwitchgrass(world: World, x: Int, y: Int, z: Int): Boolean = {
    var xx: Int = x - 1
    while (xx <= x + 1) {
      var zz: Int = z - 1
      while (zz <= z + 1) {
        if (xx == x && zz == z) {
          // continue
        } else {
          if (world.getBlockId(xx, y, zz) != ContentHolder.blockSwitchgrassSolid.blockID) return false
        }
        zz += 1
      }
      xx += 1
    }
    true
  }
}
