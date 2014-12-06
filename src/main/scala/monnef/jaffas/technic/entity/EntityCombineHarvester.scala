package monnef.jaffas.technic.entity

import java.util.List

import cpw.mods.fml.relauncher.{Side, SideOnly}
import monnef.core.api.IIntegerCoordinates
import monnef.core.common.MutableFloatVec
import monnef.core.utils.scalautils._
import monnef.core.utils.{BlockHelper, IntegerCoordinates, WorldHelper}
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.item.EntityBoat
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLiving, EntityLivingBase, SharedMonsterAttributes}
import net.minecraft.init.{Items, Blocks}
import net.minecraft.item.Item
import net.minecraft.util.{MathHelper, AxisAlignedBB}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class EntityCombineHarvester(world: World) extends EntityLiving(world) with EntityTurnable with IRelaxedAccess {

  import monnef.jaffas.technic.common.CombineHarvesterRegistry._
  import monnef.jaffas.technic.entity.EntityCombineHarvester._

  var wheelsRotation = 0f
  var reelRotation = 0f
  private var velocityX, velocityY, velocityZ: Double = 0
  private var clientPosX, clientPosY, clientPosZ: Double = 0

  setSize(SIZE_X * SIZE_COEF, SIZE_Y * SIZE_COEF) // half because of occasional problems with reel not able to harvest solid blocks like melon
  setCombineBoundingBox()
  randomYawVelocity = 0

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
    /*
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
    */
  }

  override def applyEntityAttributes() {
    super.applyEntityAttributes()
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2)
  }

  override def updateRiderPosition() {
    if (riddenByEntity != null) {
      val (xDiff, zDiff) = computeXZDiffFromAngleAndDistance(rotationYaw, CHAIR_FROM_CENTER_DISTANCE)
      riddenByEntity.setPosition(
        posX + xDiff - chairPoint.x,
        posY + 1 / SIZE_COEF * getMountedYOffset + riddenByEntity.getYOffset - chairPoint.y,
        posZ + zDiff - chairPoint.z
      )
    }
  }

  @SideOnly(Side.CLIENT) override def setPositionAndRotation2(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, newPosRotationIncrements: Int) {
    super.setPositionAndRotation2(x, y, z, yaw, pitch, newPosRotationIncrements)
  }

  // disabling looking at other entities and random rotation
  override def updateEntityActionState() {
    entityAge += 1
  }

  private def calculateCenterReelBlockPosition(distance: Float): IIntegerCoordinates = {
    val blockY = posY.asInstanceOf[Float]
    val (xDiff, zDiff) = computeXZDiffFromAngleAndDistance(rotationYaw, distance)
    val (blockX, blockZ): (Float, Float) = (posX.asInstanceOf[Float] + xDiff, posZ.asInstanceOf[Float] + zDiff /*- 1.5f*/ )

    new IntegerCoordinates(
      Math.round(blockX),
      Math.round(blockY),
      Math.round(blockZ),
      world
    )
  }

  def tryHarvestBlock(pos: IIntegerCoordinates): Boolean = {
    blockHarvestableInstructions(pos) match {
      case Some(inst) =>
        val drops = inst.harvest(pos, 0)
        if (!world.isRemote) drops.foreach(WorldHelper.dropItem(pos.getWorld, pos.getX, pos.getY, pos.getZ, _))
        true

      case None => false
    }
  }

  def computeRadiusFromSection(section: Int): Float = section match {
    case _ if section == 0 || section == 1 => REEL_FROM_CENTER_DISTANCE + 1
    case _ => REEL_FROM_CENTER_DISTANCE
  }

  private def doHarvesting() {
    var otherBlocksConfigurationIndex = Math.round((rotationYaw + 180 + 90) / 45f) % 4
    val centerBlockPosition = calculateCenterReelBlockPosition(computeRadiusFromSection(otherBlocksConfigurationIndex))
    if (otherBlocksConfigurationIndex < 0) otherBlocksConfigurationIndex += 4
    val otherBlocksOffsets = OTHER_BLOCKS_CONFIGURATIONS(otherBlocksConfigurationIndex)
    val blocksToHarvest = centerBlockPosition +: otherBlocksOffsets.map { case (ox, oz) => centerBlockPosition.move(ox, 0, oz)}

    if (debugHarvestingAreaComputation) {
      val clearRadius = 5
      for {
        x <- centerBlockPosition.getX - clearRadius to centerBlockPosition.getX + clearRadius
        y <- centerBlockPosition.getY - clearRadius to centerBlockPosition.getY + clearRadius
        z <- centerBlockPosition.getZ - clearRadius to centerBlockPosition.getZ + clearRadius
        if world.getBlock(x, y, z) == debugBlock
      } BlockHelper.setAir(world, x, y, z)
    }

    blocksToHarvest.flatMap(b => Seq(b, b.shiftInDirectionBy(ForgeDirection.UP, 1))).foreach {
      case pos =>
        tryHarvestBlock(pos)
        if (debugHarvestingAreaComputation && pos.isAir) pos.setBlock(debugBlock)
    }
  }

  override def onLivingUpdate() {
    super.onLivingUpdate()
    if (canHarvest && world != null) {
      if (!world.isRemote) doHarvesting()
    }
  }

  def canHarvest: Boolean = riddenByEntity != null

  def blockHarvestableInstructions(pos: IIntegerCoordinates): Option[BlockHarvestingInstruction] = {
    harvestingTable.get(pos.getBlock) match {
      case oInst@Some(inst) =>
        if (inst.canBeHarvested(pos)) oInst
        else None
      case None => None
    }
  }

  override def setRotationPublic(yaw: Float, pitch: Float) {
    setRotation(yaw, pitch)
  }
}

object EntityCombineHarvester {
  final val SIZE_X = 5f
  final val SIZE_Z = 9f
  final val SIZE_Y = 4f

  final val SIZE_COEF = 1f

  var CHAIR_FROM_CENTER_DISTANCE = 3.5f
  var REEL_FROM_CENTER_DISTANCE = 3.5f

  final val WHEELS_DEGREES_PER_TICK = 360f / (5 * 20)
  final val REEL_DEGREES_PER_TICK = 360f / (3 * 20)

  var chairPoint = new MutableFloatVec(0f, .5f, 0f)

  var debugHarvestingAreaComputation = false
  val debugBlock = Blocks.deadbush

  def computeXZDiffFromAngleAndDistance(angleInDeg: Float, distance: Float): (Float, Float) = {
    val angleInRad = (angleInDeg + 90) * Math.PI.asInstanceOf[Float] / 180.0F
    val xDiff = net.minecraft.util.MathHelper.cos(angleInRad) * distance
    val zDiff = net.minecraft.util.MathHelper.sin(angleInRad) * distance
    (xDiff, zDiff)
  }

  private final val REEL_HARVEST_RADIUS = 2

  private final val OTHER_BLOCKS_CONFIGURATIONS_TEMPLATE: Seq[Seq[(Int, Int)]] = Seq(
    Seq((0, 1), (0, -1)),
    Seq((-1, 1), (1, -1)),
    Seq((-1, 0), (1, 0)),
    Seq((-1, -1), (1, 1))
  )

  private def dupMul(items: Seq[(Int, Int)], radius: Int): Seq[(Int, Int)] =
    for {
      r <- 1 to radius
      item <- items
    } yield (item._1 * r, item._2 * r)

  final val OTHER_BLOCKS_CONFIGURATIONS: Seq[Seq[(Int, Int)]] = OTHER_BLOCKS_CONFIGURATIONS_TEMPLATE.map {dupMul(_, REEL_HARVEST_RADIUS)}
}

