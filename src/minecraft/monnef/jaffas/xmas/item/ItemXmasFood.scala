package monnef.jaffas.xmas.item

import monnef.jaffas.food.item.ItemJaffaFood
import monnef.jaffas.xmas.common.IconDescriptorXmas
import monnef.jaffas.xmas.JaffasXmas

class ItemXmasFood(_id: Int) extends ItemJaffaFood(_id) with IconDescriptorXmas {
  setCreativeTab(JaffasXmas.instance.creativeTab)
}
