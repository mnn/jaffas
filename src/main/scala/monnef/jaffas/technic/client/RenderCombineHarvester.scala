package monnef.jaffas.technic.client

import monnef.core.common.MutableFloatVec
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import monnef.core.client.{ResourcePathHelper, ModelObj}
import monnef.core.client.ResourcePathHelper.ResourceTextureType
import org.lwjgl.opengl.GL11
import monnef.jaffas.technic.entity.EntityCombineHarvester
import monnef.core.utils.RenderUtils
import monnef.core.MonnefCorePlugin

class RenderCombineHarvester extends Render {

  import RenderCombineHarvester._

  val modelBody = new ModelObj(ResourcePathHelper.assemble(s"$COMBINE_NAME.obj", ResourceTextureType.MODELS), 0, ResourcePathHelper.assemble(s"$COMBINE_NAME.png", ResourceTextureType.ENTITY), SCALE)

  val modelWheels = new ModelObj(ResourcePathHelper.assemble(s"$WHEELS_NAME.obj", ResourceTextureType.MODELS), 0, ResourcePathHelper.assemble(s"$WHEELS_NAME.png", ResourceTextureType.ENTITY), SCALE)
  modelWheels.setRotationPoint(0, -1.2f, -0.67f)

  val modelReel = new ModelObj(ResourcePathHelper.assemble(s"$REEL_NAME.obj", ResourceTextureType.MODELS), 0, ResourcePathHelper.assemble(s"$REEL_NAME.png", ResourceTextureType.ENTITY), SCALE)
  modelReel.setRotationPoint(0, -.85f, -2.7f)

  override def doRender(genEntity: Entity, x: Double, y: Double, z: Double, scale: Float, renderTick: Float) {
    val entity = genEntity.asInstanceOf[EntityCombineHarvester]
    GL11.glPushMatrix()
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    GL11.glTranslatef(x.asInstanceOf[Float], y.asInstanceOf[Float], z.asInstanceOf[Float] + 1.0F)
    GL11.glScalef(1.0F, -1.0F, 1.0F)
    //GL11.glTranslatef(0.5F, 1f, 0.5F)
    GL11.glTranslatef(renderShiftPre.x, renderShiftPre.y, renderShiftPre.z)
    //GL11.glRotatef(angle, 0, 1.0f, 0)
    GL11.glRotatef(180, 1f, 0, 0)
    GL11.glRotatef(180, 0, 1f, 0)
    GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
    GL11.glDisable(GL11.GL_CULL_FACE)

    val baseYRotation = entity.rotationYaw
    RenderUtils.glRotateAroundPoint(baseYRotation, 0, 1, 0, centerPoint.x, centerPoint.y, centerPoint.z)

    GL11.glTranslatef(renderShiftPost.x, renderShiftPost.y, renderShiftPost.z)

    modelBody.renderWithTexture()

    modelWheels.setRotation(entity.wheelsRotation + renderTick * EntityCombineHarvester.WHEELS_DEGREES_PER_TICK, 0, 0)
    modelWheels.renderWithTexture()

    modelReel.setRotation(entity.reelRotation + renderTick * EntityCombineHarvester.REEL_DEGREES_PER_TICK, 0, 0)
    modelReel.renderWithTexture()

    GL11.glPopAttrib()
    GL11.glPopMatrix()
    if (MonnefCorePlugin.debugEnv) renderDebugHelperLines()
  }

  override def getEntityTexture(entity: Entity): ResourceLocation = modelBody.getTexture

  private def renderDebugHelperLines() {
    RenderUtils.glRenderLine(1, 0, 1, 2, 0, 0, 0, 0, 2, 0)
  }
}

object RenderCombineHarvester {
  final val COMBINE_NAME = "combine"
  final val WHEELS_NAME = "combine_wheels"
  final val REEL_NAME = "combine_reel"
  final val SCALE = 1f

  val centerPoint = new MutableFloatVec(0, 0, .5f)
  val renderShiftPre = new MutableFloatVec(0, 0, -1.5f)
  val renderShiftPost = new MutableFloatVec(0, 0, 1.5f)
}