package monnef.jaffas.xmas.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.xmas.JaffasXmas
import monnef.jaffas.xmas.common.IconDescriptorXmas

class ItemXmas(_texture: Int) extends ItemJaffaBase(_texture) with IconDescriptorXmas {
  def this() { this(0) }

  setCreativeTab(JaffasXmas.instance.creativeTab)
}