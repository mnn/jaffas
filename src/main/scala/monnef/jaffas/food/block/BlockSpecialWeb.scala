package monnef.jaffas.food.block

import net.minecraft.util.AxisAlignedBB
import net.minecraft.item.Item
import net.minecraft.entity.Entity
import net.minecraft.world.World
import java.util.Random
import net.minecraft.block.Block
import monnef.core.utils.RandomHelper
import monnef.jaffas.food.JaffasFood
import monnef.jaffas.food.common.CobWebDescriptor

class BlockSpecialWeb(_texture: Int) extends BlockJaffas(_texture, JaffasFood.webMaterial) {
  setLightOpacity(1)
  setHardness(4f)

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

  override def getHarvestTool(metadata: Int): String = super.getHarvestTool(metadata)
}
