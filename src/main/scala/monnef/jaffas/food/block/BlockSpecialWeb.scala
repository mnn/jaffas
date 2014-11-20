package monnef.jaffas.food.block

import net.minecraft.util.AxisAlignedBB
import net.minecraft.item.Item
import net.minecraft.entity.Entity
import net.minecraft.world.World
import java.util.Random
import net.minecraft.block.Block
import monnef.core.utils.RandomHelper
import monnef.jaffas.food.JaffasFood

class BlockSpecialWeb(_texture: Int) extends BlockJaffas(_texture, JaffasFood.webMaterial) {
  setLightOpacity(1)
  setHardness(1f)

  private var _descriptor: CobWebDescriptor = _

  override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, entity: Entity) { entity.setInWeb() }

  override def isOpaqueCube: Boolean = false

  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null

  override def getRenderType: Int = 1

  override def renderAsNormalBlock: Boolean = false

  override def getItemDropped(meta: Int, rand: Random, fortune: Int): Item = descriptor.drop()

  override def quantityDropped(rand: Random): Int = RandomHelper.generateRandomFromInterval(descriptor.dropMinCount, descriptor.dropMaxCount)

  protected override def canSilkHarvest: Boolean = true

  def descriptor = _descriptor

  def descriptor_=(v: CobWebDescriptor) {
    if (_descriptor != null) throw new RuntimeException("re-setting cob web descriptor")
    _descriptor = v
  }
}

case class CobWebInfluencingBlock(value: Int, block: () => Block, meta: Int = -1) {
  def blockValue(suspectedBlock: Block, suspectedMeta: Int): Int = if (blockMatch(suspectedBlock, suspectedMeta)) value else 0

  def blockMatch(suspectedBlock: Block, suspectedMeta: Int): Boolean =
    if (suspectedBlock != block) false
    else if (meta != -1 && suspectedMeta != meta) false
    else true
}

case class CobWebDescriptor(drop: () => Item, dropMinCount: Int, dropMaxCount: Int, materialName: String, block: Block, textureId: Int, influencingBlocks: Seq[CobWebInfluencingBlock])

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
      db += item.block -> item
      item.influencingBlocks.foreach { ib => infDb += ib.block() ->(ib.value, item)}
    }
    db = db.empty
    infDb = infDb.empty
    tmpDb.foreach(addToDb)

    constructed = true
  }

  def registerAndCreateBlock(desc: CobWebDescriptor): BlockSpecialWeb = {
    val b = new BlockSpecialWeb(desc.textureId)
    val newDesc = desc.copy(block = b)
    b.descriptor = newDesc
    addToDb(newDesc)
    b
  }

  def blockRegistered(block: Block): Boolean = db.contains(block)

  def getDescriptor(block: Block): CobWebDescriptor = db(block)

  def getDescriptorInfluencedBy(influencingBlock: Block): Option[(Int, CobWebDescriptor)] = infDb.get(influencingBlock)

  def getDescriptors: Seq[CobWebDescriptor] = db.values.toSeq
}
