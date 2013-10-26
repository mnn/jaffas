package monnef.jaffas.power.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.power.JaffasPower
import monnef.jaffas.power.common.IconDescriptorPower

class ItemPower(_id: Int, _tex: Int) extends ItemJaffaBase(_id, _tex) with IconDescriptorPower {
  setCreativeTab(JaffasPower.instance.creativeTab)
}

