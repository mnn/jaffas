/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block

import monnef.core.api.ICustomIcon
import monnef.core.common.CustomIconHelper
import monnef.core.utils.InventoryUtils
import monnef.jaffas.food.block.BlockContainerJaffas
import monnef.jaffas.trees.JaffasTrees
import monnef.jaffas.trees.common.{IconDescriptorTrees, Reference}
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Icon
import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection
import java.util.Random

class BlockFruitCollector(_id: Int) extends BlockContainerJaffas(_id, 73, Material.rock) with IconDescriptorTrees {
  setHardness(2.0F)
  setResistance(5.0F)
  setUnlocalizedName("blockFruitCollector")
  setCreativeTab(JaffasTrees.instance.creativeTab)

  override def getDefaultModName: String = Reference.ModName

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, idk: Int, what: Float, these: Float, are: Float): Boolean = {
    val tileEntity: TileEntity = world.getBlockTileEntity(x, y, z)
    if (tileEntity == null || player.isSneaking) {
      return false
    }
    player.openGui(JaffasTrees.instance, 0, world, x, y, z)
    true
  }

  override def breakBlock(world: World, x: Int, y: Int, z: Int, par5: Int, par6: Int) {
    super.breakBlock(world, x, y, z, par5, par6)
    InventoryUtils.dropItems(world, x, y, z)
  }

  override def randomDisplayTick(world: World, i: Int, j: Int, k: Int, random: Random) {
    spawnParticlesOfTargetedItem(world, random, i, j, k, force = false)
  }

  def spawnParticlesOfTargetedItem(world: World, random: Random, i: Int, j: Int, k: Int, force: Boolean) {
    val et: TileFruitCollector = world.getBlockTileEntity(i, j, k).asInstanceOf[TileFruitCollector]
    if (et.getState == TileFruitCollector.CollectorStates.targeted || force) {
      for (a <- 0 to 15)
        JaffasTrees.proxy.addEffect("sucking", world, et.getIX + randomShift(random), et.getIY + randomShift(random), et.getIZ + randomShift(random),
          (random.nextDouble - 0.5) / 40D, random.nextDouble / 200D, (random.nextDouble - 0.5) / 40D)
    }
  }

  private def randomShift(random: Random): Double = random.nextDouble % 1D - 0.5D

  def createNewTileEntity(world: World): TileEntity = new TileFruitCollector

  override def getIcon(side: Int, meta: Int): Icon = getTextureFromSide(side)

  private def getTextureFromSide(side: Int): Icon = {
    val s: ForgeDirection = ForgeDirection.getOrientation(side)
    if (s == ForgeDirection.DOWN || s == ForgeDirection.UP) topIcon
    else blockIcon
  }

  override def registerIcons(iconRegister: IconRegister) {
    super.registerIcons(iconRegister)
    topIcon = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this.asInstanceOf[ICustomIcon], 1))
  }

  private var topIcon: Icon = null
}