package monnef.jaffas.power.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.power.JaffasPower
import monnef.jaffas.power.common.IconDescriptorPower

class ItemPower(_tex: Int) extends ItemJaffaBase(_tex) with IconDescriptorPower {
  setCreativeTab(JaffasPower.instance.creativeTab)
}

