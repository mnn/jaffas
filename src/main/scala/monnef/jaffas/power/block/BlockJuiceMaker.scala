/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block

import monnef.jaffas.power.block.common.{TileEntityBasicProcessingMachine, BlockBasicProcessingMachine}
import monnef.jaffas.power.client.GuiHandler
import cpw.mods.fml.client.registry.RenderingRegistry

class BlockJuiceMaker(_idx: Int, _tc: Class[_ <: TileEntityBasicProcessingMachine], _guiId: GuiHandler.GuiId) extends BlockBasicProcessingMachine(_idx, _tc, _guiId, true, true) {
  private final val U: Float = 1f / 16f
  private final val BORDER_LEFT: Float = 5 * U
  private final val BORDER_BACK: Float = 2 * U
  private final val BORDER_RIGHT: Float = 4 * U
  private final val BORDER_FRONT: Float = 4 * U
  private final val TOP: Float = 1 * U

  setCustomRotationSensitiveBoundingBox(BORDER_LEFT, 0, BORDER_BACK, 1 - BORDER_RIGHT, 1 - TOP, 1 - BORDER_FRONT)
}
