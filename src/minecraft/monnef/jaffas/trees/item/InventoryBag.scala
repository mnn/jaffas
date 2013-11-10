/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.entity.player.EntityPlayer
import monnef.core.MonnefCorePlugin
import cpw.mods.fml.common.FMLCommonHandler
import monnef.core.utils.scalagameutils._

class InventoryBag(name: String, isLocalized: Boolean, slotsCount: Int, stack: ItemStack) extends IInventory {
  final val INV_TAG_NAME = "Inventory"
  final val SLOT_TAG_NAME = "Slot"

  val inv = new Array[ItemStack](slotsCount)
  loadFromNBT()

  override def onInventoryChanged() {
    for (i <- 0 until getSizeInventory)
      if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize <= 0)
        this.setInventorySlotContents(i, null)

    saveToNBT()
  }

  def saveToNBT() {
    val tagCompound = stack.getValidTagCompound()
    val itemList = new NBTTagList

    for {
      i <- 0 until getSizeInventory
      stack = getStackInSlot(i)
      if stack != null
    } {
      val tag = new NBTTagCompound
      tag.setByte(SLOT_TAG_NAME, i.asInstanceOf[Byte])
      stack.writeToNBT(tag)
      itemList.appendTag(tag)
    }
    tagCompound.setTag(INV_TAG_NAME, itemList)
    if (MonnefCorePlugin.debugEnv) {
      tagCompound.setString("DebugString", "xx")
      MonnefCorePlugin.Log.printInfo(s"Saving on ${FMLCommonHandler.instance().getEffectiveSide}. stack: $stack, debugStr: ${ItemBagBase.getDebugString(stack)}")
    }
  }

  def loadFromNBT() {
    val tagCompound = stack.getTagCompound

    if (tagCompound != null) {
      val tagList = tagCompound.getTagList(INV_TAG_NAME)
      for {
        i <- 0 until tagList.tagCount()
        tag = tagList.tagAt(i).asInstanceOf[NBTTagCompound]
        slot = tag.getByte(SLOT_TAG_NAME)
        if slot >= 0 && slot < getSizeInventory
      } {
        setInventorySlotContentsInternal(slot, ItemStack.loadItemStackFromNBT(tag))
      }
    }
  }

  def closeChest() {}

  def openChest() {}

  def setInventorySlotContents(slot: Int, itemstack: ItemStack) {
    setInventorySlotContentsInternal(slot, itemstack)
    onInventoryChanged()
  }

  protected def setInventorySlotContentsInternal(slot: Int, itemstack: ItemStack) {
    inv(slot) = itemstack
    if (itemstack != null) {
      if (itemstack.stackSize > getInventoryStackLimit) itemstack.stackSize = getInventoryStackLimit
      if (itemstack.stackSize <= 0) inv(slot) = null
    }
  }

  def getSizeInventory: Int = inv.size

  def getStackInSlot(i: Int): ItemStack = inv(i)

  def decrStackSize(slot: Int, amount: Int): ItemStack = {
    var stack = getStackInSlot(slot)
    if (stack != null) {
      if (stack.stackSize > amount) {
        stack = stack.splitStack(amount)
        this.onInventoryChanged()
      }
      else {
        setInventorySlotContents(slot, null)
      }
    }
    stack
  }

  def getStackInSlotOnClosing(slot: Int): ItemStack = {
    setInventorySlotContents(slot, null)
    getStackInSlot(slot)
  }

  def getInvName: String = name

  def isInvNameLocalized: Boolean = false

  def getInventoryStackLimit: Int = 64

  def isUseableByPlayer(entityplayer: EntityPlayer): Boolean = true

  def isItemValidForSlot(i: Int, itemstack: ItemStack): Boolean = true
}
