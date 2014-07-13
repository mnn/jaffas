package monnef.jaffas.xmas.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.xmas.JaffasXmas
import monnef.jaffas.xmas.common.IconDescriptorXmas

class ItemXmas(_id: Int, _texture: Int) extends ItemJaffaBase(_id, _texture) with IconDescriptorXmas {
  def this(id: Int) { this(id, 0) }

  setCreativeTab(JaffasXmas.instance.creativeTab)
}