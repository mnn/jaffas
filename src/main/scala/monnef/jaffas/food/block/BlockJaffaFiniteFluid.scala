package monnef.jaffas.food.block

import net.minecraftforge.fluids.{FluidContainerRegistry, FluidStack, Fluid, BlockFluidFinite}
import net.minecraft.block.material.Material
import net.minecraft.util.{MathHelper, IIcon}
import monnef.jaffas.food.common.{IconDescriptorJaffas, JaffaFluid}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.world.{IBlockAccess, World}
import monnef.core.utils.RegistryUtils
import monnef.jaffas.food.JaffasFood
import monnef.core.block.{CustomBlockIconTrait, GameObjectDescriptor}
import net.minecraft.init.Blocks

class BlockJaffaFiniteFluid(_fluid: Fluid, customIconIndex: Int) extends BlockFluidFinite(_fluid, Material.water) with GameObjectDescriptor with IconDescriptorJaffas with CustomBlockIconTrait {
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
}

object BlockJaffaFiniteFluid {
  def createAndRegister(fluid: JaffaFluid, customIconIndex: Int): BlockJaffaFiniteFluid = {
    val b = new BlockJaffaFiniteFluid(fluid, customIconIndex)
    RegistryUtils.registerBlockWithName(b, fluid.getUnlocalizedName)
    fluid.setBlock(b)
    b
  }
}

