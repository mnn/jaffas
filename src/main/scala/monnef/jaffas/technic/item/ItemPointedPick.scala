package monnef.jaffas.technic.item

import net.minecraft.item.{ItemStack, Item}
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.entity.EntityLivingBase

class ItemPointedPick(_tex: Int, _mat: Item.ToolMaterial) extends ItemTechnicTool(_tex, _mat) {

  import ItemPointedPick._

  setMaxDamage(256)

  override protected def getCustomStrVsBlock(stack: ItemStack, block: Block): Float =
    getHarvestSpeed(block) match {
      case Some(speed) => speed
      case None => super.getCustomStrVsBlock(stack, block)
    }

  override def onBlockDestroyed(stack: ItemStack, world: World, block: Block, x: Int, y: Int, z: Int, entity: EntityLivingBase): Boolean = {
    if (getHarvestSpeed(block).isEmpty &&
      (block.getBlockHardness(world, x, y, z) > 0 || block.getHarvestLevel(world.getBlockMetadata(x, y, z)) > 0)
    ) damageTool(5, entity, stack)
    super.onBlockDestroyed(stack, world, block, x, y, z, entity)
  }
}

object ItemPointedPick {
  def getHarvestSpeed(block: Block): Option[Int] = {
    block match {
      case Blocks.obsidian => Some(200)
      case _ if block.getUnlocalizedName != null && block.getUnlocalizedName.toLowerCase.endsWith("blockskystone") => Some(500)
      case _ => None
    }
  }
}