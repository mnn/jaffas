package monnef.jaffas.trees.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.trees.JaffasTrees
import monnef.jaffas.trees.common.IconDescriptorTrees

class ItemTrees(_texture: Int) extends ItemJaffaBase(_texture) with IconDescriptorTrees {
  def this() {
    this(0)
    setCreativeTab(JaffasTrees.instance.creativeTab)
  }
}


