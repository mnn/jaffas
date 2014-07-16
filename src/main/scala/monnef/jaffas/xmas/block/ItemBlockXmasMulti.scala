package monnef.jaffas.xmas.block

import monnef.jaffas.food.block.ItemBlockJaffas
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.registry.LanguageRegistry
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.util.{IIcon, MathHelper}
import monnef.core.common.CustomIconHelper
import monnef.core.api.ICustomIcon
import monnef.jaffas.xmas.common.IconDescriptorXmas
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.block.Block

abstract class ItemBlockXmasMulti(_block: Block) extends ItemBlockJaffas(_block) with IconDescriptorXmas {
  private var subTitles: Array[String] = null

  setHasSubtypes(true)
  this.subNames = this.getSubNames
  this.subTitles = this.getSubTitles
  this.registerNames(getParentBlock)

  override def getMetadata(damageValue: Int): Int = damageValue

  def getSubBlocksCount: Int = this.subNames.length

  def registerNames(block: BlockXmasMulti) {
    var i: Int = 0
    while (i < subNames.length) {
      val multiBlockStack: ItemStack = new ItemStack(block, 1, i)
      LanguageRegistry.addName(multiBlockStack, subTitles(multiBlockStack.getItemDamage))
      i += 1
    }
  }

  @SideOnly(Side.CLIENT) override def getIconFromDamage(dmg: Int): IIcon = {
    val idx: Int = MathHelper.clamp_int(dmg, 0, subNames.length)
    icons(idx)
  }

  def getSubNames: Array[String]

  def getSubTitles: Array[String]

  def getParentBlock: BlockXmasMulti

  override def registerIcons(register: IIconRegister) {
    icons = (
      for (i <- 0 until getSubBlocksCount) yield register.registerIcon(CustomIconHelper.generateShiftedId(this.asInstanceOf[ICustomIcon], i))
      ).toArray
  }
}
