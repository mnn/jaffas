package monnef.jaffas.food.item.common

import monnef.jaffas.food.item.JaffaItem
import net.minecraft.block.Block
import monnef.jaffas.food.block._
import monnef.core.utils.{DyeColor, DyeHelper, StringsHelper, RegistryUtils}
import monnef.jaffas.food.item.JaffaItem._
import net.minecraft.init.Blocks
import monnef.jaffas.food.common.ContentHolder._
import monnef.jaffas.technic.JaffasTechnic
import monnef.jaffas.food.JaffasFood.getItem
import monnef.jaffas.food.common.{CobWebInfluencingBlock, CobWebDescriptor, SpecialCobWebRegistry}

object CobWebsCreator {
  private def createWrapped(drop: JaffaItem, dropMin: Int, dropMax: Int, name: String, textureId: Int, influentialBlocks: Seq[(Int, () => Block, Int)]): BlockSpecialWeb = {
    val block = SpecialCobWebRegistry.registerAndCreateBlock(
      CobWebDescriptor(() => getItem(drop), dropMin, dropMax, name, null, textureId, influentialBlocks.map { case (v, b, m) => CobWebInfluencingBlock(v, b, m)})
    )
    RegistryUtils.registerBlockWithName(block, "specialCobWeb" + StringsHelper.makeFirstCapital(block.descriptor.materialName))
    block
  }

  def create() {
    SpecialCobWebRegistry.registerBlock(CobWebDescriptor(() => null, 0, 0, "", () => Blocks.web, 0, Seq()))

    webEnder = createWrapped(pileEnder, 1, 9, "ender", 313, Seq(
      (2, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.BLACK)),
      (1, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.GREEN))
    ))

    webGlowstone = createWrapped(pileGlowstone, 1, 3, "glowstone", 314, Seq(
      (1, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.YELLOW)),
      (10, () => Blocks.glowstone, -1)
    ))

    webRedstone = createWrapped(pileRedstone, 1, 3, "redstone", 316, Seq(
      (15, () => Blocks.redstone_block, -1),
      (5, () => Blocks.redstone_ore, -1),
      (1, () => Blocks.redstone_wire, -1),
      (1, () => Blocks.redstone_lamp, -1),
      (1, () => Blocks.redstone_torch, -1),
      (1, () => Blocks.lit_redstone_lamp, -1),
      (1, () => Blocks.lit_redstone_ore, -1),
      (1, () => Blocks.powered_repeater, -1),
      (1, () => Blocks.unpowered_repeater, -1),
      (1, () => Blocks.powered_comparator, -1),
      (1, () => Blocks.unpowered_comparator, -1)
    ))
  }

  def createLimsewCobWeb() {
    webLimsew = createWrapped(pileLimsew, 1, 3, "limsew", 315, Seq(
      (20, () => JaffasTechnic.blockLimsew, -1),
      (7, () => JaffasTechnic.blockLimsewOre, -1),
      (1, () => Blocks.wool, DyeHelper.getWoolNum(DyeColor.LIME))
    ))
  }
}
