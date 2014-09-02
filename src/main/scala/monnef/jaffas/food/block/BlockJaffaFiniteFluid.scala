package monnef.jaffas.food.block

import net.minecraftforge.fluids.{FluidContainerRegistry, FluidStack, Fluid, BlockFluidFinite}
import net.minecraft.block.material.Material
import net.minecraft.util.{MathHelper, IIcon}
import monnef.jaffas.food.common.{IconDescriptorJaffas, JaffaFluid}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.world.{IBlockAccess, World}
import monnef.core.utils.{IntegerCoordinates, RegistryUtils}
import monnef.jaffas.food.JaffasFood
import monnef.core.block.{CustomBlockIconTrait, GameObjectDescriptor}
import net.minecraft.init.Blocks
import net.minecraft.entity.{EntityLivingBase, Entity}
import net.minecraft.potion.{Potion, PotionEffect}
import java.util.Random
import monnef.jaffas.food.client.EntityCustomBubbleFX
import cpw.mods.fml.client.FMLClientHandler
import monnef.core.utils.scalautils._
import monnef.jaffas.food.block.BlockJaffaFiniteFluid.{OrangeBubble, BubbleType, WhiteBubble, GreenBubble}
import monnef.core.api.IIntegerCoordinates

class BlockJaffaFiniteFluid(_fluid: Fluid, customIconIndex: Int) extends BlockFluidFinite(_fluid, Material.water) with GameObjectDescriptor with IconDescriptorJaffas with CustomBlockIconTrait {

  private[this] var isPoisonous = false
  private[this] var doesHeal = false
  private[this] var doesBurn = false
  private[this] var hasMiningBonus = false
  private[this] var bubbles: Option[BubbleType] = None
  private[this] var destroysBlocks = false
  private[this] var dropsDestroyedBlocks = false

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
    if (destroysBlocks && rand.nextInt(10) < 1) {
      findBlockToMine(world, x, y, z, rand).foreach { pos =>
        if (dropsDestroyedBlocks) pos.getBlock.dropBlockAsItem(world, pos.getX, pos.getY, pos.getZ, pos.getBlockMetadata, 0)
        pos.setBlock(Blocks.air)
      }
    } else super.updateTick(world, x, y, z, rand)
  }

  private[this] def findBlockToMine(world: World, x: Int, y: Int, z: Int, rand: Random): Option[IIntegerCoordinates] = {
    //TODO
    if (world.isAirBlock(x, y - 1, z)) None
    else Some(new IntegerCoordinates(x, y - 1, z, world))
  }
}

object BlockJaffaFiniteFluid {
  def createAndRegister(fluid: JaffaFluid, customIconIndex: Int): BlockJaffaFiniteFluid = {
    val b = new BlockJaffaFiniteFluid(fluid, customIconIndex)
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

}

class BlockWaterOfLife(_fluid: Fluid, customIconIndex: Int) extends BlockJaffaFiniteFluid(_fluid, customIconIndex) {
}
