package monnef.jaffas.food.common

import net.minecraft.block.Block
import net.minecraft.item.Item
import monnef.jaffas.food.block.BlockSpecialWeb

object SpecialCobWebRegistry {
  private var db = Map[Block, CobWebDescriptor]()
  private var infDb = Map[Block, (Int, CobWebDescriptor)]()

  private var tmpDb = Seq[CobWebDescriptor]()

  private var constructed = false

  private def addToDb(item: CobWebDescriptor) {
    tmpDb :+= item
  }

  def constructDatabases() {
    if (constructed) throw new RuntimeException("Re-constructing databases.")

    def addToDb(item: CobWebDescriptor) {
      db += item.block() -> item
      item.influencingBlocks.foreach { ib => infDb += ib.block() ->(ib.value, item)}
    }
    db = db.empty
    infDb = infDb.empty
    tmpDb.foreach(addToDb)

    constructed = true
  }

  def registerBlock(desc: CobWebDescriptor) {
    if (tmpDb.exists(_.block == desc.block)) throw new RuntimeException("Descriptor with same block is already registered!")
    addToDb(desc)
  }

  def registerAndCreateBlock(desc: CobWebDescriptor): BlockSpecialWeb = {
    val b = new BlockSpecialWeb(desc.textureId)
    val newDesc = desc.copy(block = () => b)
    b.descriptor = newDesc
    registerBlock(newDesc)
    b
  }

  def blockRegistered(block: Block): Boolean = db.contains(block)

  def getDescriptor(block: Block): CobWebDescriptor = db(block)

  def getDescriptorInfluencedBy(influencingBlock: Block): Option[(Int, CobWebDescriptor)] = infDb.get(influencingBlock)

  def getDescriptors: Seq[CobWebDescriptor] = db.values.toSeq
}

case class CobWebInfluencingBlock(value: Int, block: () => Block, meta: Int = -1) {
  def blockValue(suspectedBlock: Block, suspectedMeta: Int): Int = if (blockMatch(suspectedBlock, suspectedMeta)) value else 0

  def blockMatch(suspectedBlock: Block, suspectedMeta: Int): Boolean =
    if (suspectedBlock != block) false
    else if (meta != -1 && suspectedMeta != meta) false
    else true
}

case class CobWebDescriptor(drop: () => Item, dropMinCount: Int, dropMaxCount: Int, materialName: String, block: () => Block, textureId: Int, influencingBlocks: Seq[CobWebInfluencingBlock])
