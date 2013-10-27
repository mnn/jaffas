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

abstract class ItemBagBase(_id: Int, _texture: Int) extends ItemTrees(_id, _texture) {

  import ItemBagBase._

  setMaxStackSize(1)

  override def onItemRightClick(stack: ItemStack, world: World, p: EntityPlayer): ItemStack = {
    if (!world.isRemote) {
      p.openGui(JaffasTrees.instance, getGuiId, world, p.posX.toInt, p.posY.toInt, p.posZ.toInt)
    }
    stack
  }

  override def onItemUse(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3World: World, x: Int, y: Int, z: Int, par7: Int, par8: Float, par9: Float, par10: Float): Boolean = {
    if (par3World.getBlockId(x, y, z) == Block.grass.blockID) {
      par3World.setBlock(x, y, z, Block.sand.blockID)
      true
    } else
      super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, x, y, z, par7, par8, par9, par10)
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
