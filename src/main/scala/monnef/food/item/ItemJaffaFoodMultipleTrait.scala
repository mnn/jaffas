/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item

import monnef.core.item.ItemMonnefCore
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.util.{MathHelper, Icon}
import cpw.mods.fml.common.registry.LanguageRegistry
import net.minecraft.item.ItemStack
import net.minecraft.creativetab.CreativeTabs

trait ItemJaffaFoodMultipleTrait[Self <: ItemMonnefCore] extends ItemJaffaFoodTrait[Self] {
  this: ItemMonnefCore with ItemJaffaFoodTrait[Self] =>

  init()

  private def init() {
    setHasSubtypes(true)
    setMaxDamage(0)
    setIconsCount(subNames.size)
  }

  def subNames: Seq[String]

  def subTitles: Seq[String]

  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(dmg: Int): Icon = {
    val idx = MathHelper.clamp_int(dmg, 0, subNames.length)
    getCustomIcon(idx)
  }

  override def getUnlocalizedName(par1ItemStack: ItemStack): String = {
    val idx: Int = MathHelper.clamp_int(par1ItemStack.getItemDamage, 0, subNames.length)
    getUnlocalizedName + "." + subNames(idx)
  }

  def registerNames() {
    for (i <- 0 until subNames.size)
      LanguageRegistry.instance.addStringLocalization(this.getUnlocalizedName + "." + subNames(i) + ".name", subTitles(i))
  }

  @SideOnly(Side.CLIENT)
  override def getSubItems(itemId: Int, tab: CreativeTabs, list: java.util.List[_]) {
    val l = list.asInstanceOf[java.util.List[ItemStack]]
    for (i <- 0 until subNames.size) l.add(new ItemStack(itemId, 1, i))
  }
}
