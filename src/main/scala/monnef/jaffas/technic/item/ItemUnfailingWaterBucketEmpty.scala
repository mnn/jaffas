package monnef.jaffas.technic.item

import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.entity.Entity
import monnef.core.utils.scalagameutils._
import net.minecraft.entity.player.EntityPlayer
import monnef.jaffas.technic.JaffasTechnic

class ItemUnfailingWaterBucketEmpty(_tex: Int) extends ItemTechnic(_tex) {

  import ItemUnfailingWaterBucketEmpty._

  setMaxStackSize(1)

  override def onUpdate(stack: ItemStack, world: World, entity: Entity, inventorySlot: Int, isCurrentItem: Boolean) {
    super.onUpdate(stack, world, entity, inventorySlot, isCurrentItem)
    stack.initTag()
    val mainTag = stack.getTagCompound
    if (!mainTag.hasKey(FILLING_TICKS_SPENT_TAG)) setFillingTicksSpent(stack, 0)
    val ticks = mainTag.getByte(FILLING_TICKS_SPENT_TAG)
    if (ticks >= TICKS_TO_FILL) {
      if (!world.isRemote && entity.isInstanceOf[EntityPlayer]) {
        val player = entity.asInstanceOf[EntityPlayer]
        player.inventory.setInventorySlotContents(inventorySlot, new ItemStack(JaffasTechnic.unfailingWaterBucketFull))
      }
    } else {
      setFillingTicksSpent(stack, (ticks + 1).asInstanceOf[Byte])
    }
  }

  def setFillingTicksSpent(stack: ItemStack, value: Byte) {
    stack.initTag()
    stack.getTagCompound.setByte(FILLING_TICKS_SPENT_TAG, value)
  }
}

object ItemUnfailingWaterBucketEmpty {
  final val FILLING_TICKS_SPENT_TAG = "fillingTicks"
  final val TICKS_TO_FILL = 20 * 2
}
