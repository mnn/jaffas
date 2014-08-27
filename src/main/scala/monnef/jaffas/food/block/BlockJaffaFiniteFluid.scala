package monnef.jaffas.food.block

import net.minecraftforge.fluids.{Fluid, BlockFluidFinite}
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import monnef.core.common.CustomIconHelper
import monnef.jaffas.food.common.{IconDescriptorJaffas, JaffaFluid, Reference}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.world.{IBlockAccess, World}
import monnef.core.utils.RegistryUtils
import monnef.jaffas.food.JaffasFood
import monnef.core.block.GameObjectDescriptor
import monnef.core.item.CustomItemIconTrait

class BlockJaffaFiniteFluid(_fluid: Fluid, stillCustomIconIndex: Int, flowingCustomIconIndex: Int) extends BlockFluidFinite(_fluid, Material.water) with GameObjectDescriptor with IconDescriptorJaffas {
  private var iconStill, iconFlowing: IIcon = _
  setCreativeTab(JaffasFood.instance.creativeTab)
  initCustomIcon()

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(register: IIconRegister) {
    super.registerBlockIcons(register)
    iconStill = register.registerIcon(CustomIconHelper.generateId(this, stillCustomIconIndex))
    iconFlowing = register.registerIcon(CustomIconHelper.generateId(this, flowingCustomIconIndex))
  }

  @SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int): IIcon = if (side == 0 || side == 1) iconStill else iconFlowing

  override def canDisplace(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = {
    if (world.getBlock(x, y, z).getMaterial.isLiquid) false
    else super.canDisplace(world, x, y, z)
  }

  override def displaceIfPossible(world: World, x: Int, y: Int, z: Int): Boolean = {
    if (world.getBlock(x, y, z).getMaterial.isLiquid) false
    else super.displaceIfPossible(world, x, y, z)
  }

  def isFullyFilled(world: World, x: Int, y: Int, z: Int): Boolean = getQuantaValue(world, x, y, z) == quantaPerBlock

  def getMaxMeta(): Int = getMaxRenderHeightMeta
}

object BlockJaffaFiniteFluid {
  def createAndRegister(fluid: JaffaFluid, stillCustomIconIndex: Int, flowingCustomIconIndex: Int): BlockJaffaFiniteFluid = {
    val b = new BlockJaffaFiniteFluid(fluid, stillCustomIconIndex, flowingCustomIconIndex)
    RegistryUtils.registerBlockWithName(b, fluid.getUnlocalizedName)
    b
  }
}