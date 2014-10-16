package monnef.jaffas.technic.entity

import net.minecraft.entity.{Entity, EntityLiving}
import net.minecraft.world.World
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer

class EntityCombineHarvester(world: World) extends EntityLiving(world) {

  import EntityCombineHarvester._

  setSize(SIZE_X, SIZE_Y)
  setCombineBoundingBox()

  def setCombineBoundingBox() = {
    boundingBox.setBounds(
      posX - SIZE_X / 2,
      posY,
      posZ - SIZE_Z / 2,
      posX + SIZE_X / 2,
      posY + SIZE_Y,
      posZ + SIZE_Z / 2
    )
  }

  override def setPosition(x: Double, y: Double, z: Double) {
    super.setPosition(x, y, z)
    setCombineBoundingBox() // because setPosition also recalculates BB
  }

  override def getBoundingBox: AxisAlignedBB = boundingBox

  override def getCollisionBox(entity: Entity): AxisAlignedBB = entity.getBoundingBox

  override def interact(player: EntityPlayer): Boolean = {
    addVelocity(0, 0, .1f)
    true
  }
}

object EntityCombineHarvester {
  final val SIZE_X = 5f
  final val SIZE_Z = 10f
  final val SIZE_Y = 5f
}
