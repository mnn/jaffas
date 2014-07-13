/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item

import monnef.core.item.ItemMonnefCore
import monnef.jaffas.food.item.common.IItemJaffa
import monnef.jaffas.food.JaffasFood
import net.minecraft.item.Item
import monnef.jaffas.food.common.IconDescriptorJaffas

class ItemJaffaBase extends ItemMonnefCore with IItemJaffa with IconDescriptorJaffas {
  maxStackSize = 64
  this.setCreativeTab(JaffasFood.instance.creativeTab)
  setCustomIconIndex(-1)

  def this(textureIndex: Int) {
    this()
    setCustomIconIndex(textureIndex)
  }

  override def getCustomIconName: String = if (getCustomIconIndex < 0) "todo" else null

  override def setUnlocalizedName(par1Str: String): Item = super.setUnlocalizedName("jaffas." + par1Str)
}