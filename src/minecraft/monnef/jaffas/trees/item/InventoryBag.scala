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

class InventoryBag(name: String, isLocalized: Boolean, slotsCount: Int, stack: ItemStack) extends IInventory {
  val inv = new Array[ItemStack](slotsCount)
  loadFromNBT()

  override def onInventoryChanged() {
    for (i <- 0 until getSizeInventory)
      if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize <= 0)
        this.setInventorySlotContents(i, null)

    saveToNBT()
  }

  def saveToNBT() {
    if (!stack.hasTagCompound) stack.setTagCompound(new NBTTagCompound())
    val tagCompound = stack.getTagCompound
    val itemList = new NBTTagList

    for {
      i <- 0 until getSizeInventory
      stack = getStackInSlot(i)
      if stack != null
    } {
      val tag = new NBTTagCompound
      tag.setByte("Slot", i.asInstanceOf[Byte])
      stack.writeToNBT(tag)
      itemList.appendTag(tag)
    }
    tagCompound.setTag("Inventory", itemList)
    if (MonnefCorePlugin.debugEnv) {
      tagCompound.setString("DebugString", "xx")
      MonnefCorePlugin.Log.printInfo(s"Saving on ${FMLCommonHandler.instance().getEffectiveSide}. stack: $stack, debugStr: ${ItemBagBase.getDebugString(stack)}")
    }
  }

  def loadFromNBT() {
    val tagCompound = stack.getTagCompound

    if (tagCompound != null) {
      val tagList = tagCompound.getTagList("Inventory")
      //for (i <- 0 until getSizeInventory) setInventorySlotContents(i, null)
      for {
        i <- 0 until tagList.tagCount()
        tag = tagList.tagAt(i).asInstanceOf[NBTTagCompound]
        slot = tag.getByte("Slot")
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
    if (itemstack != null && itemstack.stackSize > getInventoryStackLimit) itemstack.stackSize = getInventoryStackLimit
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
