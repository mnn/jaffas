package monnef.jaffas.technic.item

import monnef.jaffas.food.item.{ItemJaffaTool, ItemJaffaSword}
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import monnef.core.MonnefCorePlugin
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import monnef.jaffas.technic.common.IconDescriptorTechnic
import net.minecraft.init.Blocks

class ItemSwordTechnic(_tex: Int, _mat: Item.ToolMaterial) extends ItemJaffaSword(_tex, _mat) with IconDescriptorTechnic {
  setCreativeTab(JaffasTechnic.instance.creativeTab)
  setSecondCreativeTab(CreativeTabs.tabCombat)

  override def getSubItems(item: Item, par2CreativeTabs: CreativeTabs, output: java.util.List[_]) {
    val l = output.asInstanceOf[java.util.List[ItemStack]]
    l.add(new ItemStack(item, 1, 0))
    if (MonnefCorePlugin.debugEnv) l.add(new ItemStack(item, 1, getMaxDamage - 10))
  }

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, par8: Float, par9: Float, par10: Float): Boolean = {
    if (!ItemJaffaTool.nearlyDestroyed(stack)) {
      val block: Block = world.getBlock(x, y, z)
      if (block == Blocks.web) {
        if (!world.isRemote) {
          damageTool(2, player, stack)
          world.func_147480_a(x, y, z, true) // destroyBlock
        }
        true
      }
      else false
    }
    else false
  }
}

