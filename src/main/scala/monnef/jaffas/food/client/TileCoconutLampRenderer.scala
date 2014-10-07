package monnef.jaffas.food.client

import net.minecraft.tileentity.TileEntity
import monnef.core.client.{ResourcePathHelper, ModelObj}
import monnef.core.client.ResourcePathHelper.ResourceTextureType
import org.lwjgl.opengl.{GL12, GL11}

class TileCoconutLampRenderer extends TileSpecialJaffaRenderer {

  import TileCoconutLampRenderer._

  val model = new ModelObj(ResourcePathHelper.assemble(s"$NAME.obj", ResourceTextureType.MODELS), 0, ResourcePathHelper.assemble(s"$NAME.png", ResourceTextureType.TILE), 1f / 4)

  override protected def getTexturePaths: Array[String] = Array()

  override def renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, renderTick: Float) {
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    GL11.glTranslatef(x.asInstanceOf[Float], y.asInstanceOf[Float] + 1.0F, z.asInstanceOf[Float] + 1.0F)
    GL11.glScalef(1.0F, -1.0F, -1.0F)
    GL11.glTranslatef(0.5F, 1f, 0.5F)
    //GL11.glRotatef(angle, 0, 1.0f, 0)
    GL11.glRotatef(180, 1f, 0, 0)
    model.renderWithTexture()
    GL11.glPopMatrix()
  }
}

object TileCoconutLampRenderer {
  final val NAME = "cocoLamp"
}