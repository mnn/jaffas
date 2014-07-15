/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client

import monnef.jaffas.food.client.TileSpecialJaffaRenderer
import net.minecraft.tileentity.TileEntity
import monnef.core.client.{ResourcePathHelper, ModelObj}
import monnef.core.client.ResourcePathHelper.ResourceTextureType
import org.lwjgl.opengl.{GL12, GL11}
import monnef.jaffas.power.block.TileJuiceMaker

class TileJuiceMakerRenderer extends TileSpecialJaffaRenderer {
  val model = new ModelObj("/juiceMaker.obj", 0, ResourcePathHelper.assemble("juiceMaker.png", ResourceTextureType.TILE))

  protected def getTexturePaths: Array[String] = Array()

  def renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, f: Float) {
    var rotation: Int = tile.asInstanceOf[TileJuiceMaker].getRotation.ordinal
    // fix for inventory rendering
    if (tile.getWorldObj == null) rotation = 0

    val angle: Float =
      rotation match {
        case 0 => 0
        case 1 => 90
        case 2 => 180
        case 3 => -90
        case _ => 45
      }

    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    GL11.glTranslatef(x.asInstanceOf[Float], y.asInstanceOf[Float] + 1.0F, z.asInstanceOf[Float] + 1.0F)
    GL11.glScalef(1.0F, -1.0F, -1.0F)
    GL11.glTranslatef(0.5F, 0.5F + 0.5F, 0.5F)
    GL11.glRotatef(angle, 0, 1.0f, 0)
    model.renderWithTexture(1 / 5f /*0.0625F*/)
    GL11.glPopMatrix()
  }
}
