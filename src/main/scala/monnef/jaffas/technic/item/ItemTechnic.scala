package monnef.jaffas.technic.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.technic.JaffasTechnic
import monnef.jaffas.technic.common.IconDescriptorTechnic

class ItemTechnic(_tex: Int) extends ItemJaffaBase(_tex) with IconDescriptorTechnic {
  setCreativeTab(JaffasTechnic.instance.creativeTab)
}