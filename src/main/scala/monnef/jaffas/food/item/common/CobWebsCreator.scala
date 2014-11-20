package monnef.jaffas.food.item.common

import monnef.jaffas.food.item.JaffaItem
import net.minecraft.block.Block
import monnef.jaffas.food.block._
import monnef.core.utils.{DyeColor, DyeHelper, StringsHelper, RegistryUtils}
import monnef.jaffas.food.item.JaffaItem._
import monnef.jaffas.food.block.CobWebInfluencingBlock
import monnef.jaffas.food.block.CobWebDescriptor
import net.minecraft.init.Blocks
import monnef.jaffas.food.common.ContentHolder._
import monnef.jaffas.technic.JaffasTechnic
import monnef.jaffas.food.JaffasFood.getItem

object CobWebsCreator {
  private def createWrapped(drop: JaffaItem, dropMin: Int, dropMax: Int, name: String, textureId: Int, influentialBlocks: Seq[(Int, () => Block, Int)]): BlockSpecialWeb = {
    val block = SpecialCobWebRegistry.registerAndCreateBlock(
      CobWebDescriptor(() => getItem(drop), dropMin, dropMax, name, null, textureId, influentialBlocks.map { case (v, b, m) => CobWebInfluencingBlock(v, b, m)})
    )
    RegistryUtils.registerBlockWithName(block, "specialCobWeb" + StringsHelper.makeFirstCapital(block.descriptor.materialName))
    block
  }

  def create() {
    webEnder = createWrapped(pileEnder, 1, 9, "ender", 313, Seq(
      (2, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.BLACK)),
      (1, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.GREEN))
    ))

    webGlowstone = createWrapped(pileGlowstone, 1, 3, "glowstone", 314, Seq(
      (1, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.YELLOW)),
      (10, () => Blocks.glowstone, -1)
    ))

    webRedstone = createWrapped(pileRedstone, 1, 3, "redstone", 316, Seq(
      (10, () => Blocks.redstone_block, -1),
      (4, () => Blocks.redstone_ore, -1),
      (1, () => Blocks.redstone_wire, -1),
      (1, () => Blocks.redstone_lamp, -1),
      (1, () => Blocks.redstone_torch, -1),
      (1, () => Blocks.lit_redstone_lamp, -1),
      (1, () => Blocks.lit_redstone_ore, -1)
    ))
  }

  def createLimsewCobWeb() {
    webLimsew = createWrapped(pileLimsew, 1, 3, "limsew", 315, Seq(
      (10, () => JaffasTechnic.blockLimsew, -1),
      (4, () => JaffasTechnic.blockLimsewOre, -1)
    ))
  }
}
