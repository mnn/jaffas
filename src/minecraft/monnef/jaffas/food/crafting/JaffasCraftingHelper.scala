/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting

import monnef.jaffas.food.common.ConfigurationManager

object JaffasCraftingHelper {
  def doesCraftingTableSupportCraftingHandlers(className: String): Boolean = !ConfigurationManager.craftingTablesWithBrokenCraftingHandlerSupport.exists(a => className.equals(a))
}
