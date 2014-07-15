/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client

import net.minecraft.client.gui.inventory.GuiContainer
import monnef.jaffas.trees.item.ContainerBag
import monnef.core.utils.GuiHelper._
import net.minecraft.util.StatCollector
import monnef.core.client.ResourcePathHelper

class GuiBag(container: ContainerBag) extends GuiContainer(container) {

  import GuiBag._

  var x, y: Int = _

  def drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) = {
    refreshXY()
    this.mc.renderEngine.bindTexture(backgroundTextureResource)
    drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
  }

  protected override def drawGuiContainerForegroundLayer(par1: Int, par2: Int) {
    super.drawGuiContainerForegroundLayer(par1, par2)
    fontRendererObj.drawString(container.stack.getDisplayName, 8, 4, COLOR_DARK_GRAY)
    fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 4, COLOR_DARK_GRAY)
  }

  protected def refreshXY() {
    x = (width - xSize) / 2
    y = (height - ySize) / 2
  }
}

object GuiBag {
  val backgroundTextureResource = ResourcePathHelper.assembleAndCreate("bag9.png", ResourcePathHelper.ResourceTextureType.GUI)
}