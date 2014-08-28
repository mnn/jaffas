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
import monnef.core.block.{CustomBlockIconTrait, GameObjectDescriptor}
import monnef.core.item.CustomItemIconTrait

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

  def isFullyFilled(world: World, x: Int, y: Int, z: Int): Boolean = getQuantaValue(world, x, y, z) == quantaPerBlock

  def getMaxMeta(): Int = getMaxRenderHeightMeta
}

object BlockJaffaFiniteFluid {
  def createAndRegister(fluid: JaffaFluid, customIconIndex: Int): BlockJaffaFiniteFluid = {
    val b = new BlockJaffaFiniteFluid(fluid, customIconIndex)
    RegistryUtils.registerBlockWithName(b, fluid.getUnlocalizedName)
    b
  }
}