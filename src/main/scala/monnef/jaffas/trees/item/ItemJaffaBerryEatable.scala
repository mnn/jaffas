package monnef.jaffas.trees.item

import monnef.jaffas.food.item.ItemJaffaFood
import monnef.jaffas.trees.JaffasTrees
import monnef.jaffas.trees.common.Reference


class ItemJaffaBerryEatable extends ItemJaffaFood {
  setCreativeTab(JaffasTrees.instance.creativeTab)
  setupFromOldDefaultProperties()

  override def getDefaultModName(): String = Reference.ModName

  override def getDefaultSheetNumber(): Int = 2
}
