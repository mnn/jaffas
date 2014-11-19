package monnef.jaffas.technic.item

import net.minecraft.item.{ItemStack, Item}
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.entity.EntityLivingBase

class ItemPointedPick(_tex: Int, _mat: Item.ToolMaterial) extends ItemTechnicTool(_tex, _mat) {

  import ItemPointedPick._

  setMaxDamage(256)

  override protected def getCustomStrVsBlock(stack: ItemStack, block: Block): Float = {
    if (isHarvestAbleBlock(block)) 150
    else super.getCustomStrVsBlock(stack, block)
  }

  override def onBlockDestroyed(stack: ItemStack, world: World, block: Block, x: Int, y: Int, z: Int, entity: EntityLivingBase): Boolean = {
    if (!isHarvestAbleBlock(block) &&
      (block.getBlockHardness(world, x, y, z) > 0 || block.getHarvestLevel(world.getBlockMetadata(x, y, z)) > 0)
    ) damageTool(15, entity, stack)
    super.onBlockDestroyed(stack, world, block, x, y, z, entity)
  }
}

object ItemPointedPick {
  def isHarvestAbleBlock(block: Block): Boolean = {
    block match {
      case Blocks.obsidian => true
      case _ => false
    }
  }
}