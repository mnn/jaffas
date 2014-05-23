package monnef.jaffas.technic.common

import monnef.core.block.CustomIconDescriptor
import monnef.jaffas.technic.Reference

trait IconDescriptorTechnic extends CustomIconDescriptor{
  override def getDescriptorModName: String = Reference.ModName

  override def getDescriptorSheetNumber: Int = 3
}
