package monnef.jaffas.food.block

import net.minecraftforge.fluids.{FluidContainerRegistry, FluidStack, Fluid, BlockFluidFinite}
import net.minecraft.block.material.{MapColor, MaterialLiquid, Material}
import net.minecraft.util.{MathHelper, IIcon}
import monnef.jaffas.food.common.{IconDescriptorJaffas, JaffaFluid}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.world.{IBlockAccess, World}
import monnef.core.utils._
import monnef.jaffas.food.JaffasFood
import monnef.core.block.{CustomBlockIconTrait, GameObjectDescriptor}
import net.minecraft.init.Blocks
import net.minecraft.entity.{EntityLivingBase, Entity}
import net.minecraft.potion.{Potion, PotionEffect}
import java.util.Random
import monnef.jaffas.food.client.EntityCustomBubbleFX
import cpw.mods.fml.client.FMLClientHandler
import monnef.core.utils.scalautils._
import monnef.jaffas.food.block.BlockJaffaFiniteFluid.WhiteBubble
import monnef.core.api.IIntegerCoordinates
import net.minecraft.block.Block
import scala.Some
import scala.annotation.tailrec
import monnef.jaffas.technic.JaffasTechnic
import scalagameutils._

class BlockJaffaFiniteFluid(_fluid: Fluid, customIconIndex: Int, material: Material) extends BlockFluidFinite(_fluid, material) with GameObjectDescriptor with IconDescriptorJaffas with CustomBlockIconTrait {

  import BlockJaffaFiniteFluid._

  private[this] var isPoisonous = false
  private[this] var doesHeal = false
  private[this] var doesBurn = false
  private[this] var hasMiningBonus = false
  private[this] var bubbles: Option[BubbleType] = None
  private[this] var destroysBlocks = false
  private[this] var dropsDestroyedBlocks = false
  private[this] var fasterBreaking = false
  private[this] var unstable = false
  private[this] var lavaDestabilises: Option[BlockJaffaFiniteFluid] = None

  setCreativeTab(JaffasFood.instance.creativeTab)
  setIconsCount(2)
  setCustomIconIndex(customIconIndex)

  @SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int): IIcon = if (side == 0 || side == 1) icons(1) else icons(0)

  override def canDisplace(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = {
    if (world.getBlock(x, y, z).getMaterial.isLiquid) false
    else super.canDisplace(world, x, y, z)
  }

  override def displaceIfPossible(world: World, x: Int, y: Int, z: Int): Boolean = {
    if (world.getBlock(x, y, z).getMaterial.isLiquid) false
    else super.displaceIfPossible(world, x, y, z)
  }

  // fixing broken implementation in base class:
  // - always zero amount of fluid when draining
  // - setting a block on client makes block flicker
  override def drain(world: World, x: Int, y: Int, z: Int, doDrain: Boolean): FluidStack = {
    val r = new FluidStack(getFluid, MathHelper.floor_float(getQuantaPercentage(world, x, y, z) * FluidContainerRegistry.BUCKET_VOLUME))
    if (doDrain && !world.isRemote) {world.setBlock(x, y, z, Blocks.air)}
    r
  }

  def setIsPoisonous(): BlockJaffaFiniteFluid = {
    isPoisonous = true
    this
  }

  def setDoesHeal(): BlockJaffaFiniteFluid = {
    doesHeal = true
    this
  }

  def setDoesBurn(): BlockJaffaFiniteFluid = {
    doesBurn = true
    this
  }

  def setHasMiningBonus(): BlockJaffaFiniteFluid = {
    hasMiningBonus = true
    this
  }

  def setBubbles(bubbles: BubbleType): BlockJaffaFiniteFluid = {
    this.bubbles = Some(bubbles)
    this
  }

  def setDestroysBlocks(): BlockJaffaFiniteFluid = {
    destroysBlocks = true
    this
  }

  def setDropsDestroyedBlocks(): BlockJaffaFiniteFluid = {
    dropsDestroyedBlocks = true
    this
  }

  def setFasterBreaking(): BlockJaffaFiniteFluid = {
    fasterBreaking = true
    this
  }

  def setUnstable(): BlockJaffaFiniteFluid = {
    unstable = true
    this
  }

  def setLavaDestabilises(to: BlockJaffaFiniteFluid): BlockJaffaFiniteFluid = {
    lavaDestabilises = Some(to)
    this
  }

  override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, entity: Entity) {
    super.onEntityCollidedWithBlock(world, x, y, z, entity)
    entity match {
      case e: EntityLivingBase => applyPotionEffect(e)
      case _ =>
    }
  }

  def applyPotionEffect(entity: EntityLivingBase) {
    if (isPoisonous && !entity.isPotionActive(Potion.poison.id)) entity.addPotionEffect(new PotionEffect(Potion.poison.id, 100, 2))
    if (doesHeal && !entity.isPotionActive(Potion.regeneration.id)) entity.addPotionEffect(new PotionEffect(Potion.regeneration.id, 10, 1))
    if (hasMiningBonus) entity.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 10 * 20, 1))
    if (doesBurn) entity.setFire(8)
  }

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
    super.randomDisplayTick(world, x, y, z, rand)
    bubbles match {
      case Some(bubble) =>
        if (rand.nextInt(10) < 2)
          (bubble match {
            case WhiteBubble => EntityCustomBubbleFX.createWhiteOnSurface _
            case GreenBubble => EntityCustomBubbleFX.createGreenOnSurface _
            case OrangeBubble => EntityCustomBubbleFX.createOrangeOnSurface _
          })(world, x, y, z) |>
            FMLClientHandler.instance.getClient.effectRenderer.addEffect
    }
  }

  override def updateTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
    if (unstable && rand.nextFloat() < .33f) {
      BlockHelper.setAir(world, x, y, z)
      world.newExplosion(null, x + .5, y + .5, z + .5, 5f, true, true)
    } else if (destroysBlocks && rand.nextFloat() * 2 * 3 < (if (fasterBreaking) 2 else 1) * getQuantaPercentage(world, x, y, z)) {
      findBlockToMine(world, x, y, z, rand).foreach { pos =>
        if (dropsDestroyedBlocks) pos.getBlock.dropBlockAsItem(world, pos.getX, pos.getY, pos.getZ, pos.getBlockMetadata, 0)
        pos.setBlock(Blocks.air)
      }
    } else {
      super.updateTick(world, x, y, z, rand)
      checkDestabilisation(world, x, y, z)
    }
  }

  private[this] def isValidToMine(world: World, x: Int, y: Int, z: Int): Boolean = {
    if (world.isAirBlock(x, y, z)) false
    else {
      val b = world.getBlock(x, y, z)
      if (b == JaffasTechnic.constructionBlock) false
      else if (b.isFluid) false
      else if (b == Blocks.obsidian) false
      else if (b.isUnbreakable(world, x, y, z)) false
      else true
    }
  }

  private[this] def findBlockToMine(world: World, x: Int, y: Int, z: Int, rand: Random): Option[IIntegerCoordinates] = {
    @tailrec def loop(rem: Seq[(Int, Int, Int)]): Option[IIntegerCoordinates] = rem match {
      case (cx, cy, cz) :: t =>
        if (isValidToMine(world, cx, cy, cz)) Some(new IntegerCoordinates(cx, cy, cz, world))
        else loop(t)
      case Nil => None
    }
    loop(miningTargets.map { case (cx, cy, cz) => (cx + x, cy + y, cz + z)}.shuffled)
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    super.onNeighborBlockChange(world, x, y, z, block)
    checkDestabilisation(world, x, y, z)
  }

  private def checkDestabilisation(world: World, x: Int, y: Int, z: Int) {
    lavaDestabilises match {
      case Some(to) =>
        val res = new java.util.ArrayList[IIntegerCoordinates]()
        WorldHelper.getBlocksInBox(res, world, x, y, z, 1, 1, 0, Blocks.lava, -1)
        WorldHelper.getBlocksInBox(res, world, x, y, z, 1, 1, 0, Blocks.flowing_lava, -1)
        if (res.size() > 0) {
          val meta = world.getBlockMetadata(x, y, z)
          BlockHelper.setBlock(world, x, y, z, to, meta)
        }
      case None =>
    }
  }
}

object BlockJaffaFiniteFluid {
  def createAndRegister(fluid: JaffaFluid, customIconIndex: Int, material: Material): BlockJaffaFiniteFluid = {
    val b = new BlockJaffaFiniteFluid(fluid, customIconIndex, material)
    register(fluid, customIconIndex, b)
  }

  def register[B <: BlockJaffaFiniteFluid](fluid: JaffaFluid, customIconIndex: Int, block: B): B = {
    RegistryUtils.registerBlockWithName(block, fluid.getUnlocalizedName)
    fluid.setBlock(block)
    block
  }

  sealed abstract class BubbleType

  case object WhiteBubble extends BubbleType

  case object GreenBubble extends BubbleType

  case object OrangeBubble extends BubbleType

  final val miningTargets: Seq[(Int, Int, Int)] = Seq(
    (1, 0, 0),
    (-1, 0, 0),
    (0, 0, 1),
    (0, 0, -1),
    (0, -1, 0)
  )
}

object MaterialGoo extends MaterialLiquid(MapColor.limeColor) {
  setNoPushMobility()
}
