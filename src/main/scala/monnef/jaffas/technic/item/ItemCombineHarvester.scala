package monnef.jaffas.technic.item

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import monnef.jaffas.technic.entity.{EntityCombineHarvester, EntityLocomotive}

class ItemCombineHarvester(_texture: Int, combineColor: Int) extends ItemTechnic(_texture) {
  setMaxStackSize(1)

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, p_77648_7_ : Int, p_77648_8_ : Float, p_77648_9_ : Float, p_77648_10_ : Float): Boolean = {
    if (!world.isRemote) {
      val combine = new EntityCombineHarvester(world)
      combine.setPosition(x + .5f, y + .5, z + .5)
      world.spawnEntityInWorld(combine)
    }
    true
  }
}
