/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block

import monnef.core.block.CustomIconDescriptor
import monnef.jaffas.food.common.Reference

trait BlockDescriptorJaffas extends CustomIconDescriptor {
  def getBlockDescriptorModName: String = Reference.ModName

  def getBlockDescriptorSheetNumber: Int = 1
}
