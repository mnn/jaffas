package monnef.jaffas.food.block

import net.minecraft.block.material.Material
import net.minecraft.block.Block
import monnef.core.utils.{ColorHelper, DyeHelper}
import net.minecraft.world.IBlockAccess
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.creativetab.CreativeTabs
import java.util
import monnef.jaffas.food.common.ContentHolder
import monnef.core.MonnefCorePlugin

class BlockTintedMulti(_texture: Int, _material: Material, multiItemTitle: String) extends BlockJaffas(_texture, _material) {

  private final val colors: Array[Int] = generateColors()
  if (MonnefCorePlugin.debugEnv) MonnefCorePlugin.Log.printInfo(s"$multiItemTitle's colors: ${colors.map { ci => ColorHelper.getColor(ci).toString}.mkString(", ")}")

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

  def generateColors(): Array[Int] =
    (for (i <- 0 until 16) yield {
      var c = DyeHelper.getIntColor(i)
      if (contrastFixEnabled) c = applyContrastFix(c, i)
      c = processColor(c, i)
      c
    }).toArray

  def contrastFixEnabled = true

  def applyContrastFix(c: Int, i: Int): Int = { if (i > 0 && i <= 9) ColorHelper.addContrast(c, 1.33f) else c }

  def processColor(color: Int, index: Int): Int = color

  final val titles: Array[String] =
    (for (i <- 0 until 16) yield DyeHelper.getDyeColorTitle(i) + " " + multiItemTitle).toArray

  final val subNames = (for (i <- 0 until 16) yield DyeHelper.getDyeColorName(i)).toArray
}

class BlockLightlyTintedMulti(_texture: Int, _material: Material, multiItemTitle: String) extends BlockTintedMulti(_texture, _material, multiItemTitle) {
  override def processColor(color: Int, index: Int): Int = ColorHelper.addBrightness(color, 50)
}