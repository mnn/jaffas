package monnef.jaffas.xmas.block

import monnef.jaffas.food.block.ItemBlockJaffas
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.registry.LanguageRegistry
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.util.{MathHelper, Icon}
import scala.Predef.String
import monnef.core.common.CustomIconHelper
import monnef.core.api.ICustomIcon
import net.minecraft.client.renderer.texture.IconRegister
import monnef.jaffas.xmas.common.IconDescriptorXmas

abstract class ItemBlockXmasMulti(_id: Int) extends ItemBlockJaffas(_id) with IconDescriptorXmas {
  private var subTitles: Array[String] = null

  setHasSubtypes(true)
  this.subNames = this.getSubNames
  this.subTitles = this.getSubTitles
  this.registerNames(getParentBlock)

  override def getMetadata(damageValue: Int): Int = damageValue

  def getSubBlocksCount: Int = this.subNames.length

  def registerNames(block: BlockXmasMulti) {
    {
      var i: Int = 0
      while (i < subNames.length) {
        {
          val multiBlockStack: ItemStack = new ItemStack(block, 1, i)
          LanguageRegistry.addName(multiBlockStack, subTitles(multiBlockStack.getItemDamage))
        }
        i += 1
      }
    }
  }

  @SideOnly(Side.CLIENT) override def getIconFromDamage(dmg: Int): Icon = {
    val idx: Int = MathHelper.clamp_int(dmg, 0, subNames.length)
    icons(idx)
  }

  def getSubNames: Array[String]

  def getSubTitles: Array[String]

  def getParentBlock: BlockXmasMulti

  // TODO: refactor to use parent's stuff?
  //override var icons: Array[Icon] = null

  override def registerIcons(register: IconRegister) {
    icons = new Array[Icon](getSubBlocksCount)
    var i: Int = 0
    while (i < icons.length) {
      icons(i) = register.registerIcon(CustomIconHelper.generateShiftedId(this.asInstanceOf[ICustomIcon], i))
      i += 1
    }
  }
}
