package monnef.jaffas.technic.entity

import net.minecraft.entity.{EntityLivingBase, SharedMonsterAttributes, Entity, EntityLiving}
import net.minecraft.world.{IBlockAccess, IWorldAccess, World}
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer
import monnef.core.utils.scalautils._
import net.minecraft.item.{ItemStack, Item}
import monnef.jaffas.technic.JaffasTechnic
import cpw.mods.fml.relauncher.{SideOnly, Side}
import monnef.core.api.IIntegerCoordinates
import monnef.core.utils.{WorldHelper, PlayerHelper, BlockHelper, IntegerCoordinates}
import net.minecraft.init.Blocks
import net.minecraft.block.Block

class EntityCombineHarvester(world: World) extends EntityLiving(world) {

  import EntityCombineHarvester._

  var wheelsRotation = 0f
  var reelRotation = 0f
  private var velocityX, velocityY, velocityZ: Double = 0
  private var clientPosX, clientPosY, clientPosZ: Double = 0

  setSize(SIZE_X, SIZE_Y)
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
      val (xDiff, zDiff) = computeXZDiffFromAngleAndDistance(rotationYaw, CHAIR_FROM_CENTER_DISTANCE)
      val cp = ChairPoint
      riddenByEntity.setPosition(
        posX + xDiff - cp.x,
        posY + getMountedYOffset + riddenByEntity.getYOffset - cp.y,
        posZ + zDiff - cp.z
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

  private def calculateCenterReelBlockPosition(): IIntegerCoordinates = {
    val blockY = posY.asInstanceOf[Float]
    val (xDiff, zDiff) = computeXZDiffFromAngleAndDistance(rotationYaw, REEL_FROM_CENTER_DISTANCE)
    val (blockX, blockZ): (Float, Float) = (posX.asInstanceOf[Float] + xDiff, posZ.asInstanceOf[Float] + zDiff)

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

  private def doHarvesting() {
    val centerBlockPosition = calculateCenterReelBlockPosition()
    var otherBlocksConfigurationIndex = Math.round(rotationYaw + 360 - 90) / 30 % 4 // TODO: rotation is wrong, shifted or mirrored?
    if (otherBlocksConfigurationIndex < 0) otherBlocksConfigurationIndex += 4
    val otherBlocksOffsets = OTHER_BLOCKS_CONFIGURATIONS(otherBlocksConfigurationIndex)
    val blocksToHarvest = centerBlockPosition +: otherBlocksOffsets.map { case (ox, oz) => centerBlockPosition.move(ox, 0, oz)}
    blocksToHarvest.foreach {
      case pos =>
        tryHarvestBlock(pos)
        if (pos.isAir) pos.setBlock(Blocks.deadbush)
    }
  }

  override def onLivingUpdate() {
    super.onLivingUpdate()
    if (canHarvest && world != null) {
      doHarvesting()
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
}

object EntityCombineHarvester {
  final val SIZE_X = 5f
  final val SIZE_Z = 9f
  final val SIZE_Y = 4f

  var CHAIR_FROM_CENTER_DISTANCE = 2f
  var REEL_FROM_CENTER_DISTANCE = 2.5f

  final val WHEELS_DEGREES_PER_TICK = 360f / (5 * 20)
  final val REEL_DEGREES_PER_TICK = 360f / (3 * 20)

  object ChairPoint {
    var (x, y, z) = (0f, .5f, 1.5f)
  }

  def computeXZDiffFromAngleAndDistance(angleInDeg: Float, distance: Float): (Float, Float) = {
    val angleInRad = (angleInDeg + 90) * Math.PI.asInstanceOf[Float] / 180.0F
    val xDiff = net.minecraft.util.MathHelper.cos(angleInRad) * distance
    val zDiff = net.minecraft.util.MathHelper.sin(angleInRad) * distance
    (xDiff, zDiff)
  }

  final val OTHER_BLOCKS_CONFIGURATIONS: Seq[Seq[(Int, Int)]] = Seq(
    Seq((0, 1), (0, -1)),
    Seq((-1, -1), (1, 1)),
    Seq((-1, 0), (1, 0)),
    Seq((-1, 1), (1, -1))
  )

  abstract class BlockHarvestingInstruction() {
    def canBeHarvested(pos: IIntegerCoordinates): Boolean = canBeHarvested(pos.getWorld, pos.getX, pos.getY, pos.getZ)

    def canBeHarvested(world: World, x: Int, y: Int, z: Int): Boolean

    def harvest(pos: IIntegerCoordinates, fortune: Int): Seq[ItemStack] = harvest(pos.getWorld, pos.getX, pos.getY, pos.getZ, fortune)

    def harvest(world: World, x: Int, y: Int, z: Int, fortune: Int): Seq[ItemStack]
  }

  var harvestingTable: Map[Block, BlockHarvestingInstruction] = Map()

  class SimpleDestroyBlockHarvestingInstruction extends BlockHarvestingInstruction {
    override def canBeHarvested(world: World, x: Int, y: Int, z: Int): Boolean = true

    override def harvest(world: World, x: Int, y: Int, z: Int, fortune: Int): Seq[ItemStack] = {
      val b = world.getBlock(x, y, z)
      val jDrops = b.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), fortune)
      BlockHelper.setAir(world, x, y, z)
      import scala.collection.JavaConverters._
      jDrops.asScala
    }
  }

  def registerBlockHarvestingInstruction(block: Block, instruction: BlockHarvestingInstruction) {
    harvestingTable += block -> instruction
  }
}
