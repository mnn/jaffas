package monnef.jaffas.food.common

import net.minecraft.block.Block
import powercrystals.minefactoryreloaded.api.ReplacementBlock

object MfrHelper {
  def replacementBlockWithMeta(block: Block, meta: Int): ReplacementBlock = {
    val replacementBlock = new ReplacementBlock(block)
    replacementBlock.setMeta(meta)
    replacementBlock
  }
}
