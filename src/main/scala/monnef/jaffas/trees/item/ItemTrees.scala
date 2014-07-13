package monnef.jaffas.trees.item

import monnef.jaffas.food.item.ItemJaffaBase
import monnef.jaffas.trees.JaffasTrees
import monnef.jaffas.trees.common.IconDescriptorTrees

class ItemTrees(_id: Int, _texture: Int) extends ItemJaffaBase(_id, _texture) with IconDescriptorTrees {
  def this(v: Int) {
    this(v, 0)
    setCreativeTab(JaffasTrees.instance.creativeTab)
  }
}


