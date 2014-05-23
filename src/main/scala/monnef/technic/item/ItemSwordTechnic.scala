package monnef.jaffas.technic.item

import monnef.jaffas.food.item.{ItemJaffaTool, ItemJaffaSword}
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{EnumToolMaterial, ItemStack}
import monnef.core.MonnefCorePlugin
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import monnef.jaffas.technic.common.IconDescriptorTechnic

class ItemSwordTechnic(_id: Int, _tex: Int, _mat: EnumToolMaterial) extends ItemJaffaSword(_id, _tex, _mat) with IconDescriptorTechnic {
  setCreativeTab(JaffasTechnic.instance.creativeTab)
  setSecondCreativeTab(CreativeTabs.tabCombat)

  override def getSubItems(par1: Int, par2CreativeTabs: CreativeTabs, par3List: java.util.List[_]) {
    val l = par3List.asInstanceOf[java.util.List[ItemStack]]
    l.add(new ItemStack(par1, 1, 0))
    if (MonnefCorePlugin.debugEnv) l.add(new ItemStack(par1, 1, getMaxDamage - 10))
  }

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, par8: Float, par9: Float, par10: Float): Boolean = {
    if (!ItemJaffaTool.nearlyDestroyed(stack)) {
      val blockId: Int = world.getBlockId(x, y, z)
      if (blockId == Block.web.blockID) {
        if (!world.isRemote) {
          damageTool(2, player, stack)
          world.destroyBlock(x, y, z, true)
        }
        true
      }
      else false
    }
    else false
  }
}

