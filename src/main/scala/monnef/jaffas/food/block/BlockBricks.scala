package monnef.jaffas.food.block

import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess
import net.minecraft.util.IIcon
import net.minecraft.block.Block
import monnef.core.utils.{ColorHelper, DyeHelper, DirectionHelper}
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.creativetab.CreativeTabs
import java.util
import monnef.jaffas.food.common.ContentHolder

class BlockBricks(_texture: Int) extends BlockJaffas(_texture, Material.rock) {

  import BlockBricks._

  setIconsCount(3)
  setHardness(1.2F)
  setResistance(15.0F)
  setStepSound(Block.soundTypePiston)

  override def getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): IIcon =
    getCustomIcon(
      if (DirectionHelper.isYAxis(side)) topSideFix((z + 1) % 3)
      else y % 3
    )

  override def colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    colors(world.getBlockMetadata(x, y, z))
  }


  override def getRenderColor(meta: Int): Int = colors(meta)

  override def getSubBlocks(item: Item, tab: CreativeTabs, result: util.List[_]) {
    val r = result.asInstanceOf[util.List[ItemStack]]
    for (i <- 0 until 16) r.add(new ItemStack(this, 1, i))
  }

  override def damageDropped(meta: Int): Int = meta

  override def getRenderType: Int = ContentHolder.renderBlockID
}

object BlockBricks {
  private final val topSideFix = Array(0, 2, 1)
  private final val colors: Array[Int] =
    (for (i <- 0 until 16) yield {
      var c = DyeHelper.getIntColor(i)
      if (i > 0 && i <= 9) c = ColorHelper.addContrast(c, 1.33f)
      c
    }).toArray

  final val titles: Array[String] =
    (for (i <- 0 until 16) yield DyeHelper.getDyeColorTitle(i) + " Small Brick Wall").toArray

  final val subNames = (for (i <- 0 until 16) yield DyeHelper.getDyeColorName(i)).toArray
}
