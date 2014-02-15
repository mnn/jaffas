/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block

import monnef.jaffas.power.block.common.{TileEntityBasicProcessingMachine, BlockBasicProcessingMachine}
import monnef.jaffas.power.client.GuiHandler
import cpw.mods.fml.client.registry.RenderingRegistry

class BlockJuiceMaker(_id: Int, _idx: Int, _tc: Class[_ <: TileEntityBasicProcessingMachine], _guiId: GuiHandler.GuiId) extends BlockBasicProcessingMachine(_id, _idx, _tc, _guiId) {
  private val renderId = RenderingRegistry.getNextAvailableRenderId

  override def getRenderType: Int = renderId
}
