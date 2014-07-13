package monnef.jaffas.power.common

import monnef.core.block.CustomIconDescriptor

trait IconDescriptorPower extends CustomIconDescriptor {
  override def getDescriptorModName: String = Reference.ModName

  override def getDescriptorSheetNumber: Int = 5
}
