package monnef.jaffas.xmas.item

import monnef.jaffas.food.item.ItemJaffaFood
import monnef.jaffas.xmas.common.IconDescriptorXmas
import monnef.jaffas.xmas.JaffasXmas

class ItemXmasFood extends ItemJaffaFood with IconDescriptorXmas {
  setCreativeTab(JaffasXmas.instance.creativeTab)
}
