/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.village.{MerchantRecipe, MerchantRecipeList}
import java.util.Random
import cpw.mods.fml.common.registry.VillagerRegistry
import net.minecraft.item.{Item, ItemStack}
import monnef.core.utils.RandomHelper
import net.minecraft.block.Block
import monnef.jaffas.food.JaffasFood.getItem
import monnef.jaffas.food.item.JaffaItem._
import monnef.jaffas.food.item.JaffaItem
import monnef.jaffas.trees.JaffasTrees
import monnef.jaffas.trees.block.TileFruitLeaves
import monnef.jaffas.trees.JaffasTrees.FruitType
import monnef.jaffas.trees.common.BushInfo
import scala.collection.JavaConverters._

class VillagersTradeHandler extends IVillageTradeHandler {
  def manipulateTradesForVillager(villager: EntityVillager, recipeList: MerchantRecipeList, random: Random) {
    if (ConfigurationManager.villagerTrades) {
      villager.getProfession match {
        case 0 =>
          // farmerVillager
          handleFarmer()
        case 1 =>
        // librarianVillager
        case 2 =>
        // priestVillager
        case 3 =>
        // smithVillager
        case 4 =>
        // butcherVillager
        case _ =>
      }
    }

    def handleFarmer() {
      for {
        fruit <- JaffasTrees.FruitType.values()
        if fruit.doesGenerateFruitAndSeeds() // skips "normal" (~ blank) type
        if fruit != FruitType.Cocoa // no dye!
      } {
        if (random.nextFloat() < 0.2f) {
          val fres = TileFruitLeaves.getItemFromFruit(fruit)
          if (fres.getStack != null) addTradeBuys(recipeList, fres.getStack, 20, 30, 1)
        }
      }
      for {bush: BushInfo <- JaffasTrees.bushesList.values().toArray(Array[BushInfo]())} {
        if (random.nextFloat() < 0.2f) {
          addTradeBuys(recipeList, bush.itemFruit, 18, 28, 1)
        }
      }
    }
  }

  private def addTradeBuys(list: MerchantRecipeList, in: ItemStack, itemCountLow: Int, itemCountHigh: Int, buysForEmeraldCount: Int) {
    val s = in.copy()
    s.stackSize = RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh)
    addTradeBuys(list, s, buysForEmeraldCount)
  }

  private def addTradeBuys(list: MerchantRecipeList, in: ItemStack, buysForEmeraldCount: Int) {
    val recipe = new MerchantRecipe(in, new ItemStack(Item.emerald, buysForEmeraldCount))
    list.addToListWithCheck(recipe)
  }

  private def addTradeBuys(list: MerchantRecipeList, in: Item, itemCount: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, new ItemStack(in, itemCount), buysForEmeraldCount)
  }

  private def addTradeBuys(list: MerchantRecipeList, in: Block, itemCount: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, new ItemStack(in, itemCount), buysForEmeraldCount)
  }

  private def addTradeBuys(list: MerchantRecipeList, in: Item, itemCountLow: Int, itemCountHigh: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, in, RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh), buysForEmeraldCount)
  }

  private def addTradeBuys(list: MerchantRecipeList, in: Block, itemCountLow: Int, itemCountHigh: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, in, RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh), buysForEmeraldCount)
  }
}
