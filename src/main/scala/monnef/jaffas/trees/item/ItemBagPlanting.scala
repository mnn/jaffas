/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item

import monnef.jaffas.trees.client.GuiHandlerTrees
import net.minecraftforge.common.{ForgeDirection, IPlantable}
import net.minecraft.world.World
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.block.Block
import monnef.core.utils.ItemStackList

object ItemBagPlanting {
  val blackList = new ItemStackList("Planting Bag - Black-List")
}

class ItemBagPlanting(_id: Int, _texture: Int, radius: Int) extends ItemBagBase(_id, _texture) {

  import monnef.core.utils.scalautils._
  import ItemBagPlanting._

  def getGuiId: Int = GuiHandlerTrees.GuiId.BAG_PLANTING.ordinal()

  def getCropSeedSlots(inv: InventoryBag): Seq[Int] =
    for {
      i <- 0 until inv.getSizeInventory
      s = inv.getStackInSlot(i)
      if s != null
      item = s.getItem
      if item.isInstanceOf[IPlantable]
      if !blackList.contains(s)
    } yield i

  def plantCrop(w: World, x: Int, y: Int, z: Int, seed: ItemStack, p: EntityPlayer): Boolean = {
    assert(w.isAirBlock(x, y, z), "expecting air")
    val seedItem = seed.getItem
    val oldSize = seed.stackSize
    seedItem.onItemUse(seed, p, w, x, y - 1, z, ForgeDirection.UP.ordinal(), 0, 0, 0)
    if (!w.isAirBlock(x, y, z)) {
      assert(seed.stackSize != oldSize, "seeds count must be lowered after planting")
      true
    } else {
      false
    }
  }

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, par8: Float, par9: Float, par10: Float): Boolean = {
    if (side == ForgeDirection.UP.ordinal() && world.getBlockId(x, y, z) == Block.tilledField.blockID) {
      val (fx, fy, fz) = (x, y + 1, z)
      var seedsExhausted = false
      val bagItem = stack.getItem.asInstanceOf[ItemBagBase]
      val inv = bagItem.createInventory(stack)
      var plantedCount = 0
      //val radius = if (player.isSneaking) 2 else 1
      for {
        nx <- fx - radius to fx + radius
        nz <- fz - radius to fz + radius
        ny = fy
        if !seedsExhausted
        if world.isAirBlock(nx, ny, nz)
        if world.getBlockId(nx, ny - 1, nz) == Block.tilledField.blockID
      } {
        val seedStackSlots = getCropSeedSlots(inv)
        if (seedStackSlots.nonEmpty) {
          def loopPlanting(slots: List[Int]): Boolean = slots match {
            case s :: ss =>
              val seedStack = inv.getStackInSlot(s)
              if (plantCrop(world, nx, ny, nz, seedStack, player)) {
                //seedStack.stackSize -= 1
                inv.setInventorySlotContents(s, seedStack)
                plantedCount += 1
                true
              } else loopPlanting(ss)
            case _ => false
          }
          seedsExhausted = !loopPlanting(seedStackSlots.toList.shiftTrivial(plantedCount % seedStackSlots.size))
        } else seedsExhausted = true
      }
      if (plantedCount > 0) inv.saveToNBT()
      plantedCount > 0
    } else
      super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10)
  }
}
