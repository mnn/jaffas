/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item

import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import monnef.jaffas.trees.JaffasTrees
import java.util
import monnef.core.item.ItemMonnefCore
import monnef.core.MonnefCorePlugin
import monnef.core.utils.{NBTHelper, InventoryUtils}
import scala.collection.mutable.ArrayBuffer

abstract class ItemBagBase(_texture: Int) extends ItemTrees(_texture) {

  import ItemBagBase._

  setMaxStackSize(1)

  override def onItemRightClick(stack: ItemStack, world: World, p: EntityPlayer): ItemStack = {
    if (!world.isRemote) {
      p.openGui(JaffasTrees.instance, getGuiId, world, p.posX.toInt, p.posY.toInt, p.posZ.toInt)
    }
    stack
  }

  def getGuiId: Int

  def createInventory(stack: ItemStack): InventoryBag = new InventoryBag(getUnlocalizedName, false, farmerBagsSlotsCount, stack)

  override def addInformationCustom(stack: ItemStack, player: EntityPlayer, result: util.List[String], par4: Boolean) {
    super.addInformationCustom(stack, player, result, par4)
    val l = result.asInstanceOf[util.List[String]]
    ItemMonnefCore.initNBT(stack)
    var c = -1
    val t = stack.getTagCompound
    if (t.hasKey("Inventory")) c = t.getTagList("Inventory", NBTHelper.TagTypes.TAG_Compound).tagCount()
    val cText = if (c == -1) "?" else c
    l.add(s"$cText / ${ItemBagBase.farmerBagsSlotsCount}")
    if (MonnefCorePlugin.debugEnv) l.add(getDebugString(stack))
  }

  def tryFillBag(stack: ItemStack, toInsert: Seq[ItemStack]): Seq[ItemStack] = {
    val inv = createInventory(stack)
    var leftovers = ArrayBuffer[ItemStack]()
    for {a <- toInsert} {
      val left = InventoryUtils.insertStackToExternalInventory(inv, a, 0)
      if (left != null) leftovers += left
    }
    leftovers
  }
}

object ItemBagBase {
  val farmerBagsSlotsCount = 9

  def getDebugString(stack: ItemStack): String = {
    var s = "<null>"
    val t = stack.getTagCompound
    if (t != null) s = if (t.hasKey("DebugString")) t.getString("DebugString") else "<no-key>"
    s
  }
}
