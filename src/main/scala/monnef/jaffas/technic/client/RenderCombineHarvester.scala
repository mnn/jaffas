package monnef.jaffas.technic.client

import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import monnef.core.client.{ResourcePathHelper, ModelObj}
import monnef.core.client.ResourcePathHelper.ResourceTextureType
import org.lwjgl.opengl.GL11

class RenderCombineHarvester extends Render {

  import RenderCombineHarvester._

  val modelBody = new ModelObj(ResourcePathHelper.assemble(s"$NAME.obj", ResourceTextureType.MODELS), 0, ResourcePathHelper.assemble(s"$NAME.png", ResourceTextureType.ENTITY), SCALE)

  override def doRender(entity: Entity, x: Double, y: Double, z: Double, scale: Float, renderTick: Float) {
    GL11.glPushMatrix()
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    GL11.glTranslatef(x.asInstanceOf[Float], y.asInstanceOf[Float] + 1.0F, z.asInstanceOf[Float] + 1.0F)
    GL11.glScalef(1.0F, -1.0F, 1.0F)
    //GL11.glTranslatef(0.5F, 1f, 0.5F)
    GL11.glTranslatef(0, 0, -3f)
    //GL11.glRotatef(angle, 0, 1.0f, 0)
    GL11.glRotatef(180, 1f, 0, 0)
    GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
    GL11.glDisable(GL11.GL_CULL_FACE)
    modelBody.renderWithTexture()
    GL11.glPopAttrib()
    GL11.glPopMatrix()
  }

  override def getEntityTexture(entity: Entity): ResourceLocation = modelBody.getTexture
}

object RenderCombineHarvester {
  final val NAME = "combine"
  final val SCALE = 1f
}