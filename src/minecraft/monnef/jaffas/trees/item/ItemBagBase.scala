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
import net.minecraft.block.Block
import net.minecraftforge.common.{ForgeDirection, IPlantable}

abstract class ItemBagBase(_id: Int, _texture: Int) extends ItemTrees(_id, _texture) {

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

  override def addInformation(stack: ItemStack, player: EntityPlayer, result: util.List[_], par4: Boolean) {
    super.addInformation(stack, player, result, par4)
    val l = result.asInstanceOf[util.List[String]]
    ItemMonnefCore.initNBT(stack)
    var c = -1
    val t = stack.getTagCompound
    if (t.hasKey("Inventory")) c = t.getTagList("Inventory").tagCount()
    l.add(s"$c / ${ItemBagBase.farmerBagsSlotsCount}")
    if (MonnefCorePlugin.debugEnv) l.add(getDebugString(stack))
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
