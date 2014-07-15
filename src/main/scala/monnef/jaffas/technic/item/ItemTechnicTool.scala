package monnef.jaffas.technic.item

import monnef.jaffas.food.item.ItemJaffaTool
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import monnef.core.MonnefCorePlugin
import monnef.jaffas.technic.common.IconDescriptorTechnic

class ItemTechnicTool(_textureIndex: Int, _material: Item.ToolMaterial) extends ItemJaffaTool(_textureIndex, _material) with IconDescriptorTechnic {
  setCreativeTab(JaffasTechnic.instance.creativeTab)
  setSecondCreativeTab(CreativeTabs.tabTools)
  durabilityLossOnEntityHit = 2

  override def getSubItems(par1: Item, par2CreativeTabs: CreativeTabs, list: java.util.List[_]) {
    val l = list.asInstanceOf[java.util.List[ItemStack]]
    l.add(new ItemStack(par1, 1, 0))
    if (MonnefCorePlugin.debugEnv) l.add(new ItemStack(par1, 1, getMaxDamage - 5))
  }
}