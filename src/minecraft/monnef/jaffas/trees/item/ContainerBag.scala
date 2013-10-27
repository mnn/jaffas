/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item

import net.minecraft.inventory.{Slot, Container}
import net.minecraft.entity.player.{InventoryPlayer, EntityPlayer}
import monnef.core.client.SlotLocked
import net.minecraft.item.ItemStack
import monnef.core.MonnefCorePlugin
import monnef.core.utils.PlayerHelper

class ContainerBag(player: EntityPlayer) extends Container {
  val stack = player.getCurrentEquippedItem
  val bagSlot = player.inventory.currentItem
  val bag: ItemBagBase =
    if (stack != null) stack.getItem match {
      case bag: ItemBagBase => bag
      case _ => null
    } else null
  var inv: InventoryBag = _
  createInv()
  bindSlots()
  bindPlayerInventory(player.inventory)
  player.setCurrentItemOrArmor(0, null)

  private def doIfFine(f: => Unit) = f

  def canInteractWith(entityplayer: EntityPlayer): Boolean = true

  protected def bindPlayerInventory(inventoryPlayer: InventoryPlayer) {
    val yShift = 84
    val xShift = 8

    for (i <- 0 until 3;j <- 0 until 9) addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xShift + j * 18, yShift + i * 18))
    for (i <- 0 until 9) addSlotToContainer(
      if (i == bagSlot) new SlotLocked(inventoryPlayer, i, xShift + i * 18, 142 - 84 + yShift)
      else new Slot(inventoryPlayer, i, xShift + i * 18, 142 - 84 + yShift)
    )
  }

  protected def createInv() = {
    inv = bag.createInventory(stack)
  }

  protected def bindSlots() = {
    for (i <- 0 until inv.getSizeInventory) addSlotToContainer(new Slot(inv, i, 12 + i * 18, 12))
  }

  override def onContainerClosed(player: EntityPlayer) {
    inv.saveToNBT()
    //player.setCurrentItemOrArmor(0, stack)
    PlayerHelper.giveItemToPlayer(player, stack)
  }

  override def transferStackInSlot(player: EntityPlayer, slot: Int): ItemStack = {
    var stack: ItemStack = null
    val slotObject = inventorySlots.get(slot).asInstanceOf[Slot]
    val slots = inv.getSizeInventory
    val inputSlotsCount = inv.getSizeInventory
    val PRINT_DEBUG_TRANSFER_MESSAGES = false

    if (PRINT_DEBUG_TRANSFER_MESSAGES && MonnefCorePlugin.debugEnv) {
      MonnefCorePlugin.Log.printDebug(this.getClass.getSimpleName + ": transferStackInSlot - slot#=" + slot)
    }
    if (slotObject != null && slotObject.getHasStack) {
      val stackInSlot: ItemStack = slotObject.getStack
      stack = stackInSlot.copy
      if (slot < slots) {
        if (!this.mergeItemStack(stackInSlot, slots, 36 + slots, true)) {
          return null
        }
      }
      else {
        if (!this.mergeItemStack(stackInSlot, 0, inputSlotsCount, false)) {
          return null
        }
      }
      if (stackInSlot.stackSize == 0) {
        slotObject.putStack(null)
      }
      else {
        slotObject.onSlotChanged()
      }
      if (stackInSlot.stackSize == stack.stackSize) {
        return null
      }
      slotObject.onPickupFromSlot(player, stackInSlot)
    }
    stack
  }
}
