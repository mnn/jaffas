/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item

import monnef.jaffas.trees.client.GuiHandlerTrees

class ItemBagCollecting(_id: Int, _texture: Int) extends ItemBagBase(_id, _texture) {
  def getGuiId: Int = GuiHandlerTrees.GuiId.BAG_COLLECTING.ordinal()
}
