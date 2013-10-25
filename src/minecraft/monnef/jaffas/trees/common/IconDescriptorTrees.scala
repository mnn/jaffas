package monnef.jaffas.trees.common

import monnef.jaffas.food.common.IconDescriptorJaffas

trait IconDescriptorTrees extends IconDescriptorJaffas {
  override def getDescriptorModName: String = Reference.ModName

  override def getDescriptorSheetNumber: Int = 2
}
