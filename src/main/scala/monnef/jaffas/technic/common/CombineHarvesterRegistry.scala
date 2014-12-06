package monnef.jaffas.technic.common

import monnef.core.api.IIntegerCoordinates
import monnef.core.utils.BlockHelper
import monnef.jaffas.trees.block.BlockJaffaCrops
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import monnef.core.utils.scalautils._

object CombineHarvesterRegistry {
  var harvestingTable: Map[Block, BlockHarvestingInstruction] = Map()

  abstract class BlockHarvestingInstruction() {
    def canBeHarvested(pos: IIntegerCoordinates): Boolean = canBeHarvested(pos.getWorld, pos.getX, pos.getY, pos.getZ)

    def canBeHarvested(world: World, x: Int, y: Int, z: Int): Boolean

    def harvest(pos: IIntegerCoordinates, fortune: Int): Seq[ItemStack] = harvest(pos.getWorld, pos.getX, pos.getY, pos.getZ, fortune)

    def harvest(world: World, x: Int, y: Int, z: Int, fortune: Int): Seq[ItemStack]
  }

  class SimpleDestroyBlockHarvestingInstruction extends BlockHarvestingInstruction {
    override def canBeHarvested(world: World, x: Int, y: Int, z: Int): Boolean = true

    override def harvest(world: World, x: Int, y: Int, z: Int, fortune: Int): Seq[ItemStack] = {
      import scala.collection.JavaConverters._
      val drops = world.getBlock(x, y, z).getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), fortune).asScala
      afterDropEnumerated(world, x, y, z, fortune)
      processDrops(world, x, y, z, fortune, drops)
    }

    protected def afterDropEnumerated(world: World, x: Int, y: Int, z: Int, fortune: Int) {
      BlockHelper.setAir(world, x, y, z)
    }

    protected def processDrops(world: World, x: Int, y: Int, z: Int, fortune: Int, drops: Seq[ItemStack]): Seq[ItemStack] = drops
  }

  class SimpleDestroyMetaSensitiveBlockHarvestingInstruction(harvestMetas: Set[Int]) extends SimpleDestroyBlockHarvestingInstruction {
    def this(oneMeta: Int) = this(Set(oneMeta))

    override def canBeHarvested(world: World, x: Int, y: Int, z: Int): Boolean =
      if (harvestMetas.contains(world.getBlockMetadata(x, y, z))) super.canBeHarvested(world, x, y, z)
      else false
  }

  class MetaReplantBlockHarvestingInstruction(harvestMetas: Set[Int], replantMeta: Int) extends SimpleDestroyMetaSensitiveBlockHarvestingInstruction(harvestMetas) {
    override protected def afterDropEnumerated(world: World, x: Int, y: Int, z: Int, fortune: Int) {
      BlockHelper.setBlockMetadata(world, x, y, z, replantMeta)
    }
  }

  class JaffaCropHarvestingInstruction extends BlockHarvestingInstruction {
    override def canBeHarvested(world: World, x: Int, y: Int, z: Int): Boolean =
      world.getBlockMetadata(x, y, z) |> world.getBlock(x, y, z).asInstanceOf[BlockJaffaCrops].isMature

    override def harvest(world: World, x: Int, y: Int, z: Int, fortune: Int): Seq[ItemStack] = {
      val b = world.getBlock(x, y, z).asInstanceOf[BlockJaffaCrops]
      BlockHelper.setBlockMetadata(world, x, y, z, 0)
      Seq(b.constructProduct())
    }
  }

  def registerBlockHarvestingInstruction(block: Block, instruction: BlockHarvestingInstruction) {
    harvestingTable += block -> instruction
  }
}
