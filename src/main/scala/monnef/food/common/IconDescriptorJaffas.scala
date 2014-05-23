/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common

import monnef.core.block.CustomIconDescriptor
import monnef.jaffas.food.common.Reference

trait IconDescriptorJaffas extends CustomIconDescriptor {
  def getDescriptorModName: String = Reference.ModName

  def getDescriptorSheetNumber: Int = 1
}
