/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.village.{MerchantRecipe, MerchantRecipeList}
import java.util.Random
import net.minecraft.item.{Item, ItemStack}
import monnef.core.utils.RandomHelper
import net.minecraft.block.Block
import monnef.jaffas.food.JaffasFood.getItem
import monnef.jaffas.food.item.JaffaItem._
import monnef.jaffas.trees.JaffasTrees
import monnef.jaffas.trees.block.TileFruitLeaves
import monnef.jaffas.trees.JaffasTrees.FruitType
import monnef.jaffas.trees.common.BushInfo
import monnef.jaffas.technic.JaffasTechnic
import RandomHelper.generateRandomFromInterval
import monnef.core.common.IMerchantRecipeListWrapper

trait IScalaVillagersTradeHandler {
  def manipulateTradesForVillager(villager: EntityVillager, recipeList: IMerchantRecipeListWrapper, random: Random)
}

class VillagersTradeHandler extends IScalaVillagersTradeHandler {

  def manipulateTradesForVillager(villager: EntityVillager, recipeList: IMerchantRecipeListWrapper, random: Random) {
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
          handleBlacksmith()
        case 4 =>
          // butcherVillager
          handleButcher()
        case _ =>
      }
    }

    def handleBlacksmith() {
      if (ModuleManager.isModuleEnabled(ModulesEnum.technic)) {
        addTradeBuys(recipeList, JaffasTechnic.jaffarrol, 8, 9, 1)

        addTradeSellsEquip(recipeList, getItem(jaffarrolChest), 16, 19)
        addTradeSellsEquip(recipeList, getItem(jaffarrolHelmet), 6, 9)
        addTradeSellsEquip(recipeList, getItem(jaffarrolLeggins), 11, 14)
        addTradeSellsEquip(recipeList, getItem(jaffarrolBoots), 6, 9)

        addTradeSellsEquip(recipeList, JaffasTechnic.swordJaffarrol, 12, 15)
        addTradeSellsEquip(recipeList, JaffasTechnic.axeJaffarrol, 9, 12)
        addTradeSellsEquip(recipeList, JaffasTechnic.pickaxeJaffarrol, 10, 12)
        addTradeSellsEquip(recipeList, JaffasTechnic.spadeJaffarrol, 7, 8)
        addTradeSellsEquip(recipeList, JaffasTechnic.hoeJaffarrol, 7, 8)
      }
    }

    def handleFarmer() {
      if (ModuleManager.isModuleEnabled(ModulesEnum.trees)) {
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

    def handleButcher() {
      addTradeBuys(recipeList, getItem(duckRaw), 14, 17, 1)
      addTradeBuys(recipeList, getItem(wolfMeatRaw), 14, 17, 1)
      addTradeBuys(recipeList, getItem(muttonRaw), 14, 17, 1)
      addTradeBuys(recipeList, getItem(spiderLegRaw), 18, 25, 1)

      addTradeSells(recipeList, getItem(duck), 6, 7, 1)
      addTradeSells(recipeList, getItem(wolfMeat), 6, 7, 1)
      addTradeSells(recipeList, getItem(mutton), 6, 7, 1)
      addTradeSells(recipeList, getItem(spiderLeg), 6, 7, 1)

      if (random.nextFloat() < 0.33f) addTradeSells(recipeList, getItem(meatCleaver), 1, 2)
    }

  }

  private def addTradeBuys(list: IMerchantRecipeListWrapper, in: ItemStack, itemCountLow: Int, itemCountHigh: Int, buysForEmeraldCount: Int) {
    val s = in.copy()
    s.stackSize = RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh)
    addTradeBuys(list, s, buysForEmeraldCount)
  }

  private def addTradeBuys(list: IMerchantRecipeListWrapper, in: ItemStack, buysForEmeraldCount: Int) {
    val recipe = new MerchantRecipe(in, new ItemStack(Item.emerald, buysForEmeraldCount))
    list.addToListWithCheck(recipe)
  }

  private def addTradeBuys(list: IMerchantRecipeListWrapper, in: Item, itemCount: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, new ItemStack(in, itemCount), buysForEmeraldCount)
  }

  private def addTradeBuys(list: IMerchantRecipeListWrapper, in: Block, itemCount: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, new ItemStack(in, itemCount), buysForEmeraldCount)
  }

  private def addTradeBuys(list: IMerchantRecipeListWrapper, in: Item, itemCountLow: Int, itemCountHigh: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, in, RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh), buysForEmeraldCount)
  }

  private def addTradeBuys(list: IMerchantRecipeListWrapper, in: Block, itemCountLow: Int, itemCountHigh: Int, buysForEmeraldCount: Int) {
    addTradeBuys(list, in, RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh), buysForEmeraldCount)
  }

  private def addTradeSells(list: IMerchantRecipeListWrapper, in: ItemStack, sellsForEmeraldCount: Int): MerchantRecipe = {
    val recipe = new MerchantRecipe(new ItemStack(Item.emerald, sellsForEmeraldCount), in)
    list.addToListWithCheck(recipe)
    recipe
  }

  private def addTradeSells(list: IMerchantRecipeListWrapper, in: Item, itemCount: Int, sellsForEmeraldCount: Int): MerchantRecipe = {
    addTradeSells(list, new ItemStack(in, itemCount), sellsForEmeraldCount)
  }

  private def addTradeSells(list: IMerchantRecipeListWrapper, in: Item, itemCountLow: Int, itemCountHigh: Int, sellsForEmeraldCount: Int): MerchantRecipe = {
    addTradeSells(list, in, RandomHelper.generateRandomFromInterval(itemCountLow, itemCountHigh), sellsForEmeraldCount)
  }

  private def addTradeSellsEquip(list: IMerchantRecipeListWrapper, in: Item, emeraldCountLow: Int, emeraldCountHigh: Int, equipCount: Int = 1): MerchantRecipe = {
    addTradeSells(list, in, equipCount, generateRandomFromInterval(emeraldCountLow, emeraldCountHigh))
  }
}
