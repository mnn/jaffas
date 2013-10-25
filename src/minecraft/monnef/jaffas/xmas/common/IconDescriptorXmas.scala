package monnef.jaffas.xmas.common

import monnef.core.block.CustomIconDescriptor

trait IconDescriptorXmas extends CustomIconDescriptor {
  override def getDescriptorModName: String = Reference.ModName

  override def getDescriptorSheetNumber: Int = 4
}
