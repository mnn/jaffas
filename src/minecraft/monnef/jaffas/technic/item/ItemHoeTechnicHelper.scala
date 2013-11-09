/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item

import monnef.jaffas.food.block.BlockSwitchgrass
import monnef.jaffas.food.common.ContentHolder
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import monnef.core.utils.scalautils._
import monnef.core.utils.{WorldHelper, BlockHelper}
import net.minecraft.block.Block
import net.minecraftforge.common.IPlantable
import net.minecraft.enchantment.EnchantmentHelper
import monnef.core.utils.gameclassespimps._
import monnef.jaffas.trees.item.ItemBagCollecting

object ItemHoeTechnicHelper {
  private final val SWITCHGRASS_PLANT_RADIUS_LIMIT: Int = 6

  def doSwitchgrassPlanting(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, hoe: ItemHoeTechnic): Boolean = {
    val switchgrass: BlockSwitchgrass = ContentHolder.blockSwitchgrass
    var dmg: Int = 1
    val solidSwitchgrassRadiusOpt = computeSwitchgrassBlocksRadius(world, x, y, z)
    if (solidSwitchgrassRadiusOpt.nonEmpty) {
      val solidSwitchgrassRadius = solidSwitchgrassRadiusOpt.get
      for {
        xx <- x - solidSwitchgrassRadius to x + solidSwitchgrassRadius
        zz <- z - solidSwitchgrassRadius to z + solidSwitchgrassRadius
      } {
        if (!world.isRemote) world.setBlockToAir(xx, y, zz)
        dmg += 1
      }

      val plantRadius = solidSwitchgrassRadius * 3 + 1
      for {
        xx <- x - plantRadius to x + plantRadius
        zz <- z - plantRadius to z + plantRadius
      } {
        if (switchgrass.canPlaceBlockAt(world, xx, y, zz)) {
          if (!world.isRemote)
            BlockHelper.setBlock(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP)
        }
        else {
          WorldHelper.dropBlockAsItemDo(world, xx, y, zz, switchgrass.blockID, BlockSwitchgrass.VALUE_TOP, 1)
        }
      }
      hoe.damageTool(dmg, player, stack)
      true
    } else false
  }

  private def computeSwitchgrassBlocksRadius(w: World, x: Int, y: Int, z: Int): Option[Int] = {
    def isSwitchgrassBlock(x: Int, z: Int): Boolean = w.getBlockId(x, y, z) == ContentHolder.blockSwitchgrassSolid.blockID
    def loop(rad: Int): Option[Int] = {
      if (rad < 0) throw new IllegalArgumentException
      if (rad >= SWITCHGRASS_PLANT_RADIUS_LIMIT) None
      val circle = centeredSquares(rad)
      val largerCircle = centeredSquares(rad + 1)
      val isSwitchgrassCircle: Boolean = circle.forall {case (sx, sz) => isSwitchgrassBlock(x + sx, z + sz)}
      val switchgrassInLargerCircle: Int = largerCircle.count {case (sx, sz) => isSwitchgrassBlock(x + sx, z + sz)}
      if (isSwitchgrassCircle) {
        if (switchgrassInLargerCircle == 0) Some(rad)
        else loop(rad + 1)
      } else None
    }

    loop(0)
  }

  private final val HOE_HARVEST_RADIUS: Int = 3

  private def tryFillCollectingBags(player: EntityPlayer, loot: Seq[ItemStack]): Seq[ItemStack] = {
    val collectingBags = player.hotBarStacks.zipWithIndexFirst.filter {
      case (i, s: ItemStack) => s != null && s.getItem.isInstanceOf[ItemBagCollecting]
      case _ => false
    }
    var rest = loot
    for {
      (slotIndex, bagStack: ItemStack) <- collectingBags
      bagItem = bagStack.getItem.asInstanceOf[ItemBagCollecting]
      if rest.nonEmpty
    } {
      rest = bagItem.tryFillBag(bagStack, rest)
    }
    player.inventoryContainer.detectAndSendChanges()
    rest
  }

  def doHarvesting(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, blockId: Int, hoe: ItemHoeTechnic): Boolean = {
    var skipBag = false
    for {
      xx <- x - HOE_HARVEST_RADIUS to x + HOE_HARVEST_RADIUS
      zz <- z - HOE_HARVEST_RADIUS to z + HOE_HARVEST_RADIUS
    } {
      val currBlockId: Int = world.getBlockId(xx, y, zz)
      var harvest: Boolean = currBlockId == blockId
      val blockToHarvest = Block.blocksList(currBlockId)
      if (!harvest && player.isSneaking) {
        harvest = canBeMassHarvested(blockToHarvest)
      }
      if (harvest) {
        if (!world.isRemote) {
          if (!skipBag) {
            val loot = blockToHarvest.getBlockDropped(world, xx, y, zz, world.getBlockMetadata(xx, y, zz), EnchantmentHelper.getFortuneModifier(player)).toArray.toVector.asInstanceOf[Vector[ItemStack]]
            if (loot.nonEmpty) {
              val leftovers = tryFillCollectingBags(player, loot)
              if (leftovers.nonEmpty) {
                skipBag = true
                leftovers.foreach {WorldHelper.dropItem(world, xx, y, zz, _)}
              }
            }
          }
          val dropItems = skipBag
          world.destroyBlock(xx, y, zz, dropItems)
        }
      }
    }
    hoe.damageTool(5, player, stack)
    true
  }

  def canBeMassHarvested(block: Block): Boolean = block.isInstanceOf[IPlantable]
}
