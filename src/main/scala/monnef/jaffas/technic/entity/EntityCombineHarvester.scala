package monnef.jaffas.technic.entity

import net.minecraft.entity.{EntityLivingBase, SharedMonsterAttributes, Entity, EntityLiving}
import net.minecraft.world.World
import net.minecraft.util.{MathHelper, AxisAlignedBB}
import net.minecraft.entity.player.EntityPlayer
import monnef.core.utils.scalautils._
import net.minecraft.item.Item
import monnef.jaffas.technic.JaffasTechnic
import monnef.core.utils.MathHelper
import monnef.jaffas.technic.client.RenderCombineHarvester

class EntityCombineHarvester(world: World) extends EntityLiving(world) {

  import EntityCombineHarvester._

  var wheelsRotation = 0f
  var reelRotation = 0f

  setSize(SIZE_X, SIZE_Y)
  setCombineBoundingBox()

  def setCombineBoundingBox() {
    /* disabled
    boundingBox.setBounds(
      posX - SIZE_X / 2,
      posY,
      posZ - SIZE_Z / 2,
      posX + SIZE_X / 2,
      posY + SIZE_Y,
      posZ + SIZE_Z / 2
    )
    */
  }

  override def setPosition(x: Double, y: Double, z: Double) {
    super.setPosition(x, y, z)
    setCombineBoundingBox() // because setPosition also recalculates BB
  }

  override def getBoundingBox: AxisAlignedBB = boundingBox

  override def getCollisionBox(entity: Entity): AxisAlignedBB = entity.getBoundingBox

  override def interact(player: EntityPlayer): Boolean = {
    if (player.isSneaking) {
      addVelocity(0, 0, .5f)
    } else {
      mountHarvester(player)
    }
    true
  }

  override def onEntityUpdate() {
    super.onEntityUpdate()
    if (world.isRemote) {
      wheelsRotation = wheelsRotation.boundedAdd(WHEELS_DEGREES_PER_TICK, 360)
      reelRotation = reelRotation.boundedAdd(REEL_DEGREES_PER_TICK, 360)
    }
  }

  override def getDropItem: Item = JaffasTechnic.itemCombineHarvester

  private def mountHarvester(player: EntityPlayer) {
    player.rotationYaw = rotationYaw
    player.rotationPitch = rotationPitch
    if (!world.isRemote) player.mountEntity(this)
  }

  override def moveEntityWithHeading(strafe: Float, forward: Float) {
    if (this.riddenByEntity != null) {
      prevRotationYaw = riddenByEntity.rotationYaw
      rotationYaw = riddenByEntity.rotationYaw
      var newStrafe = this.riddenByEntity.asInstanceOf[EntityLivingBase].moveStrafing * 0.5F
      var newForward = this.riddenByEntity.asInstanceOf[EntityLivingBase].moveForward
      if (!this.worldObj.isRemote) {
        this.setAIMoveSpeed(this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue.asInstanceOf[Float])
        super.moveEntityWithHeading(newStrafe, newForward)
      }
    } else {
      super.moveEntityWithHeading(strafe, forward)
    }
  }

  override def applyEntityAttributes() {
    super.applyEntityAttributes()
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2)
  }

  override def updateRiderPosition() {
    if (riddenByEntity != null) {
      val angleInDeg = rotationYaw //renderYawOffset
      val angleInRad = (angleInDeg + 90) * Math.PI.asInstanceOf[Float] / 180.0F
      val xDiff = net.minecraft.util.MathHelper.cos(angleInRad) * CHAIR_FROM_CENTER_DISTANCE
      val zDiff = net.minecraft.util.MathHelper.sin(angleInRad) * CHAIR_FROM_CENTER_DISTANCE
      val cp = ChairPoint
      riddenByEntity.setPosition(
        posX + xDiff - cp.x,
        posY + getMountedYOffset + riddenByEntity.getYOffset - cp.y,
        posZ + zDiff - cp.z
      )
    }
  }
}

object EntityCombineHarvester {
  final val SIZE_X = 5f
  final val SIZE_Z = 9f
  final val SIZE_Y = 4f

  var CHAIR_FROM_CENTER_DISTANCE = 2f

  final val WHEELS_DEGREES_PER_TICK = 360f / (5 * 20)
  final val REEL_DEGREES_PER_TICK = 360f / (3 * 20)

  object ChairPoint {
    var (x, y, z) = (0f, .5f, 1.5f)
  }

}
