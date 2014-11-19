package monnef.jaffas.food.block

import net.minecraft.block.material.Material
import net.minecraft.util.AxisAlignedBB
import net.minecraft.item.Item
import net.minecraft.init.Items
import net.minecraft.entity.Entity
import net.minecraft.world.World
import java.util.Random

class BlockSpecialWeb(_texture: Int) extends BlockJaffas(_texture, Material.web) {
  override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, entity: Entity) { entity.setInWeb() }

  override def isOpaqueCube: Boolean = false

  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null

  override def getRenderType: Int = 1

  override def renderAsNormalBlock: Boolean = false

  override def getItemDropped(meta: Int, rand: Random, fortune: Int): Item = Items.string

  protected override def canSilkHarvest: Boolean = true
}

case class CobWebDescriptor() // TODO
