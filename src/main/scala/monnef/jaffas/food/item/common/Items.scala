package monnef.jaffas.food.item.common

import monnef.core.mod.MonnefCoreNormalMod
import monnef.core.utils.WolfFoodRegistry
import monnef.jaffas.food.block.BlockPie
import monnef.jaffas.food.block.BlockSink
import monnef.jaffas.food.block.TileMeatDryer
import monnef.jaffas.food.common.ConfigurationManager
import monnef.jaffas.food.common.ContentHolder
import monnef.jaffas.food.common.ModulesEnum
import monnef.jaffas.food.crafting.Recipes
import monnef.jaffas.food.item._
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraftforge.oredict.OreDictionary
import monnef.jaffas.food.JaffasFood
import monnef.jaffas.food
import monnef.jaffas.food.item.juice.{ItemJuiceGlass, ItemJuice}
import net.minecraft.init.Blocks
import net.minecraftforge.fluids.{FluidRegistry, FluidContainerRegistry}

class Items extends ItemManagerAccessor {

  import monnef.jaffas.food.item.JaffaItem._
  import monnef.jaffas.food.crafting.Recipes._

  override def getMyModule(): ModulesEnum = ModulesEnum.food

  private def createJaffaFood(jaffaItem: JaffaItem, heal: Int, saturation: Float): ItemJaffaFoodTrait[ItemJaffaFood] =
    createJaffaFood(jaffaItem).Setup(heal, saturation).asInstanceOf[ItemJaffaFoodTrait[ItemJaffaFood]]

  private def createJaffaTool(ji: JaffaItem, durability: Int): ItemJaffaRecipeTool = createJaffaTool(ji).Setup(durability).asInstanceOf[ItemJaffaRecipeTool]

  private def getItem(jaffaItem: JaffaItem): Item = ItemManager.getItem(jaffaItem)

  override def InitializeItemInfos(): Unit = {
    setCurrentSheetNumber(1)
    addItemInfo(pastrySweet, 13)
    addItemInfo(cake, 1)
    addItemInfo(jamO, 2)
    addItemInfo(jamR, 3)
    AddItemInfoForJaffa(jaffaO, 4)
    AddItemInfoForJaffa(jaffaR, 5)
    AddItemInfoForJaffa(jaffa, 6)
    addItemInfo(chocolate, 7)
    addItemInfo(apples, 10)
    addItemInfo(beans, 8)
    addItemInfo(sweetBeans, 9)
    addItemInfo(butter, 12)
    addItemInfo(mallet, 23)
    addItemInfo(malletStone, 24)
    addItemInfo(malletIron, 25)
    addItemInfo(malletDiamond, 26)
    addItemInfo(malletHead, 27)
    addItemInfo(malletHeadStone, 28)
    addItemInfo(malletHeadIron, 29)
    addItemInfo(malletHeadDiamond, 30)
    addItemInfo(browniesPastry, 14)
    addItemInfo(puffPastry, 15)
    addItemInfo(creamSweet, 17)
    addItemInfo(sweetRoll, 18)
    addItemInfo(creamRoll, 19)
    addItemInfo(cakeTin, 20)
    addItemInfo(browniesInTin, 21)
    addItemInfo(brownie, 22)
    addItemInfo(sweetRollRaw, 31)
    addItemInfo(browniesInTinRaw, 32)
    addItemInfo(bunRaw, 44)
    addItemInfo(bun, 45)
    addItemInfo(sausageRaw, 46)
    addItemInfo(sausage, 47)
    addItemInfo(hotdog, 48)
    addItemInfo(flour, 49)
    addItemInfo(chocolateWrapper, 33)
    addItemInfo(chocolateBar, 34)
    addItemInfo(wrapperJaffas, 50) // TODO: title of jaffa wrapper and pack from ConfigurationManager.jaffasTitle
    addItemInfo(jaffasPack, 51)
    addItemInfo(vanillaBeans, 52)
    addItemInfo(waferIcecream, 53)
    addItemInfo(cone, 54)
    addItemInfo(vanillaPowder, 55)
    addItemInfo(vanillaIcecreamRaw, 56)
    addItemInfo(chocolateIcecreamRaw, 57)
    addItemInfo(icecreamRaw, 58)
    addItemInfo(vanillaIcecream, 59)
    addItemInfo(chocolateIcecream, 60)
    addItemInfo(russianIcecream, 61)
    addItemInfo(vanillaIcecreamFrozen, 62)
    addItemInfo(chocolateIcecreamFrozen, 63)
    addItemInfo(icecreamFrozen, 64)
    addItemInfo(donutRaw, 71)
    addItemInfo(donut, 72)
    addItemInfo(donutChocolate, 73)
    addItemInfo(donutPink, 74)
    addItemInfo(donutSugar, 75)
    addItemInfo(donutSprinkled, 76)
    AddItemInfoForJaffa(jaffaV, 77)
    AddItemInfoForJaffa(jaffaL, 78)
    addItemInfo(jamP, 79)
    addItemInfo(jamL, 80)
    addItemInfo(jamV, 81)
    addItemInfo(lemons, 82)
    addItemInfo(oranges, 83)
    addItemInfo(plums, 84)
    addItemInfo(sprinkles, 87)
    addItemInfo(magnifier, 91)
    AddItemInfoForJaffa(jaffaP, 86)
    addItemInfo(jamMix, 110)
    addItemInfo(kettle, 92)
    addItemInfo(cup, 93)
    addItemInfo(cupCoffee, 94)
    addItemInfo(cupRaw, 109)
    addItemInfo(kettleWaterCold, 92)
    addItemInfo(kettleWaterHot, 92)
    addItemInfo(omeletteRaw, 97)
    addItemInfo(omelette, 98)
    addItemInfo(tomatoChopped, 99)
    addItemInfo(paprikaChopped, 100)
    addItemInfo(wienerCocktail, 102)
    AddItemInfoForJaffa(jaffaStrawberry, 103)
    AddItemInfoForJaffa(jaffaRaspberry, 104)
    addItemInfo(raspberries, 105)
    addItemInfo(strawberries, 106)
    addItemInfo(jamRaspberry, 107)
    addItemInfo(jamStrawberry, 108)
    addItemInfo(rollRaw, 111)
    addItemInfo(roll, 65)
    addItemInfo(rollChopped, 66)
    addItemInfo(meatChopped, 67)
    addItemInfo(skewer, 68)
    addItemInfo(ironSkewer, 69)
    addItemInfo(knifeKitchen, 70)
    addItemInfo(coffee, 8)
    addItemInfo(coffeeRoasted, 112)
    addItemInfo(skewerRaw, 85)
    addItemInfo(brownPastry, 14)
    addItemInfo(coconutPowder, 139)
    addItemInfo(honey, 140)
    addItemInfo(gingerbread, 14)
    addItemInfo(hamburgerBunRaw, 113)
    addItemInfo(hamburgerBun, 114)
    addItemInfo(cheese, 115)
    addItemInfo(cheeseSlice, 116)
    addItemInfo(rawBurger, 117)
    addItemInfo(burger, 118)
    addItemInfo(onionSliced, 119)
    addItemInfo(hamburger, 120)
    addItemInfo(cheeseburger, 121)
    addItemInfo(fryingPan, 122)
    addItemInfo(fryingPanBurgerRaw, 123)
    addItemInfo(fryingPanEggRaw, 124)
    addItemInfo(fryingPanBurger, 123)
    addItemInfo(fryingPanEgg, 124)
    addItemInfo(eggFried, 125)
    addItemInfo(breadRaw, 126)
    addItemInfo(bread, 127)
    addItemInfo(breadSlice, 128)
    addItemInfo(breadSliceToasted, 129)
    addItemInfo(breadSliceJam, 130)
    addItemInfo(breadSliceButter, 131)
    addItemInfo(breadSliceEgg, 132)
    addItemInfo(bottleEmpty, 133)
    addItemInfo(bottleKetchup, 134)
    addItemInfo(bottleMustard, 135)
    addItemInfo(bottleBrownMustard, 136)
    addItemInfo(meatCleaver, 137)
    addItemInfo(mincedMeat, 138)
    addItemInfo(sink, 141)
    addItemInfo(coneRaw, 143)
    addItemInfo(waferIcecreamRaw, 144)
    addItemInfo(grater, 145)
    addItemInfo(cheeseGrated, 146)
    addItemInfo(salami, 147)
    addItemInfo(salamiSliced, 148)
    addItemInfo(pizza, 149)
    addItemInfo(pizzaRaw, 150)
    addItemInfo(wolfHelmet, 153)
    addItemInfo(wolfBoots, 154)
    addItemInfo(wolfChest, 152)
    addItemInfo(wolfLeggins, 155)
    addItemInfo(wolfSkin, 151)
    addItemInfo(pastry, 13)
    addItemInfo(milkBoxEmpty, 164)
    addItemInfo(milkBoxFull, 165)
    addItemInfo(crumpledPaper, 162)
    addItemInfo(scrap, 162)
    addItemInfo(JaffaItem.chipsRaw, 166)
    addItemInfo(JaffaItem.chips, 167)
    addItemInfo(JaffaItem.fryingPanChipsRaw, 168)
    addItemInfo(JaffaItem.fryingPanChips, 168)
    addItemInfo(JaffaItem.pieStrawberryRaw, BlockPie.textureIndexFromMeta(0) + 13)
    addItemInfo(JaffaItem.pieRaspberryRaw, BlockPie.textureIndexFromMeta(1) + 13)
    addItemInfo(JaffaItem.pieVanillaRaw, BlockPie.textureIndexFromMeta(2) + 13)
    addItemInfo(JaffaItem.piePlumRaw, BlockPie.textureIndexFromMeta(3) + 13)
    addItemInfo(JaffaItem.spawnStoneLittle, 173)
    addItemInfo(JaffaItem.spawnStoneMedium, 174)
    addItemInfo(JaffaItem.spawnStoneBig, 175)
    addItemInfo(JaffaItem.jaffarrolHelmet, 177)
    addItemInfo(JaffaItem.jaffarrolBoots, 178)
    addItemInfo(JaffaItem.jaffarrolChest, 176)
    addItemInfo(JaffaItem.jaffarrolLeggins, 179)
    addItemInfo(JaffaItem.juiceBottle, 180)
    for (juice <- JuiceItemsEnum.values()) {
      addItemInfo(juice.juiceBottle, juice.textureIndex)
      addItemInfo(juice.glass, juice.textureIndexGlass)
    }
    addItemInfo(JaffaItem.glassEmpty, 185)
    addItemInfo(JaffaItem.glassMilk, 190)
    addItemInfo(JaffaItem.woodenBowl, 195)
    addItemInfo(JaffaItem.cookedMushroomsRaw, 196)
    addItemInfo(JaffaItem.cookedMushrooms, 197)
    addItemInfo(JaffaItem.peanutsSugar, 191)
    addItemInfo(JaffaItem.peanutsCaramelized, 192)
    addItemInfo(JaffaItem.pepperStuffedRaw, 193)
    addItemInfo(JaffaItem.pepperStuffed, 194)
    addItemInfo(JaffaItem.wolfMeatRaw, 198)
    addItemInfo(JaffaItem.wolfMeat, 199)
    addItemInfo(JaffaItem.muttonRaw, 200)
    addItemInfo(JaffaItem.mutton, 201)
    addItemInfo(JaffaItem.spiderLegRaw, 202)
    addItemInfo(JaffaItem.spiderLeg, 203)
    addItemInfo(JaffaItem.featherDuck, 207)
    addItemInfo(JaffaItem.duckRaw, 208)
    addItemInfo(JaffaItem.duck, 209)
    addItemInfo(JaffaItem.plateRaw, 230)
    addItemInfo(JaffaItem.plate, 210)
    addItemInfo(JaffaItem.plateDuckOrange, 211)
    addItemInfo(JaffaItem.tinDuckOrangeRaw, 212)
    addItemInfo(JaffaItem.tinDuckOrange, 213)
    addItemInfo(JaffaItem.duckHelmet, 215)
    addItemInfo(JaffaItem.duckBoots, 216)
    addItemInfo(JaffaItem.duckChest, 214)
    addItemInfo(JaffaItem.duckLeggins, 217)
    addItemInfo(JaffaItem.strawberryIcecreamRaw, 204)
    addItemInfo(JaffaItem.strawberryIcecreamFrozen, 205)
    addItemInfo(JaffaItem.strawberryIcecream, 206)
    addItemInfo(JaffaItem.chocIce, 219)
    addItemInfo(JaffaItem.chocIceStick, 218)
    addItemInfo(duckEgg, 226)
    addItemInfo(JaffaItem.muffinRaw, 220)
    addItemInfo(JaffaItem.muffinUnfinished, 221)
    addItemInfo(JaffaItem.muffin, 222)
    addItemInfo(JaffaItem.beansWithTomatoRaw, 223)
    addItemInfo(JaffaItem.beansWithTomato, 224)
    addItemInfo(JaffaItem.sandwich1, 225)
    addItemInfo(JaffaItem.lambWithPeasInTinRaw, 227)
    addItemInfo(JaffaItem.lambWithPeasInTin, 228)
    addItemInfo(JaffaItem.lambWithPeas, 229)
    addItemInfo(JaffaItem.cocoBarWrapper, 231)
    addItemInfo(JaffaItem.cocoBar, 232)
    addItemInfo(JaffaItem.cupCocoa, 233)
    addItemInfo(JaffaItem.cookingPot, 234)
    addItemInfo(JaffaItem.cookingPotCocoaCold, 235)
    addItemInfo(JaffaItem.cookingPotCocoaHot, 235)
    addItemInfo(JaffaItem.eggHardBoiled, 237)
    addItemInfo(JaffaItem.cookingPotWater, 236)
    addItemInfo(JaffaItem.cookingPotEggsRaw, 236)
    addItemInfo(JaffaItem.cookingPotEggs, 236)
    addItemInfo(JaffaItem.bananaInChocolate, 238)
    addItemInfo(JaffaItem.fruitSalad, 245)
    addItemInfo(JaffaItem.friedMushroomsInTinRaw, 244)
    addItemInfo(JaffaItem.friedMushroomsInTinCooked, 243)
    addItemInfo(JaffaItem.friedMushrooms, 246)
    addItemInfo(JaffaItem.shroomburger, 242)
    addItemInfo(JaffaItem.shroomburgerInBun, 240)
    addItemInfo(JaffaItem.shroomburgerInBunWithCheese, 239)
    addItemInfo(JaffaItem.mincedMushrooms, 241)
    addItemInfo(JaffaItem.shroomburgerRaw, 247)
    addItemInfo(fryingPanShroomburgerRaw, 123)
    addItemInfo(fryingPanShroomburger, 123)
    addItemInfo(JaffaItem.flyAgaricChopped, 248)
    addItemInfo(JaffaItem.meatDryer, 250)
    addItemInfo(JaffaItem.driedMeat, 249)
    addItemInfo(JaffaItem.beerMugEmpty, 251)
    addItemInfo(JaffaItem.beerMugFull, 252)
    addItemInfo(JaffaItem.potatesSliced, 253)
    addItemInfo(JaffaItem.potatesSlicedInTinRaw, 254)
    addItemInfo(JaffaItem.potatesSlicedInTin, 255)
    addItemInfo(JaffaItem.crisps, 256)
    addItemInfo(JaffaItem.cheeseRaw, 257)
    addItemInfo(JaffaItem.cream, 258)
    addItemInfo(JaffaItem.lollipop, 260)
    addItemInfo(JaffaItem.fishFillet, 280)
    addItemInfo(JaffaItem.fishStickRaw, 281)
    addItemInfo(JaffaItem.fishStickCooked, 282)
    addItemInfo(JaffaItem.fryingPanFishStickRaw, 283)
    addItemInfo(JaffaItem.fryingPanFishStick, 283)
    addItemInfo(JaffaItem.soupPeaRaw, 284)
    addItemInfo(JaffaItem.soupPeaCooked, 285)
    addItemInfo(JaffaItem.soupFishRaw, 286)
    addItemInfo(JaffaItem.soupFishCooked, 287)
    addItemInfo(JaffaItem.soupTomatoRaw, 288)
    addItemInfo(JaffaItem.soupTomatoCooked, 289)
    addItemInfo(JaffaItem.breadCrumbs, 290)
    addItemInfo(juiceInBottle, 181)
    addItemInfo(juiceGlass, 186)
    addItemInfo(bucketWaterOfLife, 6)
    addItemInfo(bucketCorrosiveGoo, 6)
    addItemInfo(bucketMiningGoo, 6)
    addItemInfo(bucketUnstableGoo, 6)
    addItemInfo(spongeCloth, 305)
    addItemInfo(dustEnder, 307)
    addItemInfo(pileEnder, 308)
    addItemInfo(pileGlowstone, 309)
    addItemInfo(pileLimsew, 310)
    addItemInfo(pileRedstone, 311)
    addItemInfo(littleSpiderEggs, 312)
    addItemInfo(switchgrassCharcoal, 317)
  }

  private def registerWolfFood(item: JaffaItem) {
    WolfFoodRegistry.registerWolfFood(getItem(item), getItem(item).asInstanceOf[IItemFood].getHealAmount())
  }

  private def AddItemInfoForJaffa(ji: JaffaItem, textureId: Int) {
    addItemInfo(ji, textureId)
  }

  override def CreateItems() {
    createJaffaItem(pastrySweet)
    createJaffaItem(jamO)
    createJaffaItem(jamR)
    createJaffaFood(cake, 1, 0.2F)
    createJaffaFood(jaffaO, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaFood(jaffaR, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaFood(jaffa, 2, 0.5F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.2F)
    createJaffaItem(chocolate)
    createJaffaItem(apples)
    createJaffaItem(beans)
    createJaffaItem(sweetBeans)
    createJaffaItem(butter)
    createJaffaTool(mallet, 8)
    createJaffaTool(malletStone, 16)
    createJaffaTool(malletIron, 64)
    createJaffaTool(malletDiamond, 512)
    createJaffaItem(malletHead)
    createJaffaItem(malletHeadStone)
    createJaffaItem(malletHeadIron)
    createJaffaItem(malletHeadDiamond)
    createJaffaItem(browniesPastry)
    createJaffaItem(puffPastry)
    createJaffaItem(creamSweet)
    createJaffaItem(sweetRoll)
    createJaffaItem(cakeTin)
    createJaffaItem(browniesInTin)
    createJaffaItem(sweetRollRaw)
    createJaffaItem(browniesInTinRaw)
    createJaffaFood(creamRoll, 4, 1F).addPotionEffect(Potion.digSpeed.id, 60, 0, 0.15F)
    createJaffaFood(brownie, 2, 0.6F).addPotionEffect(Potion.jump.id, 60, 0, 0.15F)
    createJaffaItem(bunRaw)
    createJaffaItem(bun)
    createJaffaItem(sausageRaw)
    createJaffaItem(sausage)
    createJaffaItem(flour)
    createJaffaFood(hotdog, 3, 0.7F).addPotionEffect(Potion.damageBoost.id, 60, 0, 0.15F)
    createJaffaItem(chocolateWrapper)
    createJaffaFood(chocolateBar, 3, 0.9F).addPotionEffect(Potion.moveSpeed.id, 60, 0, 0.25F)
    createJaffaItem(wrapperJaffas)
    createJaffaItemManual(jaffasPack, classOf[ItemJaffaPack]).asInstanceOf[IItemJaffa].setRarity(MonnefCoreNormalMod.proxy.getEpicRarity())
    createJaffaItem(vanillaBeans)
    createJaffaItem(waferIcecream)
    createJaffaItem(cone)
    createJaffaItem(vanillaPowder)
    createJaffaItem(vanillaIcecreamRaw)
    createJaffaItem(chocolateIcecreamRaw)
    createJaffaItem(icecreamRaw)
    createJaffaItem(vanillaIcecreamFrozen)
    createJaffaItem(chocolateIcecreamFrozen)
    createJaffaItem(icecreamFrozen)
    createJaffaFood(vanillaIcecream, 2, 0.3F).addPotionEffect(Potion.moveSpeed.id, 70, 0, 0.25F)
    createJaffaFood(chocolateIcecream, 2, 0.3F).addPotionEffect(Potion.moveSpeed.id, 70, 0, 0.25F)
    createJaffaFood(russianIcecream, 2, 0.3F).addPotionEffect(Potion.moveSpeed.id, 70, 0, 0.25F)
    createJaffaItem(donutRaw)
    createJaffaItem(donut)
    createJaffaItem(jamP)
    createJaffaItem(jamL)
    createJaffaItem(jamV)
    createJaffaItem(lemons)
    createJaffaItem(oranges)
    createJaffaItem(plums)
    createJaffaItem(sprinkles)
    createJaffaItemManual(magnifier, classOf[ItemMagnifier])
    createJaffaFood(jaffaP, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaFood(jaffaV, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaFood(jaffaL, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaFood(donutChocolate, 2, 0.3F).addPotionEffect(Potion.digSpeed.id, 60, 0, 0.15F)
    createJaffaFood(donutPink, 2, 0.3F).addPotionEffect(Potion.digSpeed.id, 60, 0, 0.15F)
    createJaffaFood(donutSugar, 2, 0.3F).addPotionEffect(Potion.damageBoost.id, 60, 0, 0.15F)
    createJaffaFood(donutSprinkled, 2, 0.9F).addPotionEffect(Potion.damageBoost.id, 45, 1, 0.20F)
    createJaffaItem(jamMix)
    createJaffaItem(kettle)
    createJaffaItem(kettleWaterCold)
    createJaffaItem(kettleWaterHot).setMaxDamage(5).setMaxStackSize(1)
    createJaffaItem(cup)
    createJaffaFood(cupCoffee, 2, 0.2F).setReturnItem(new ItemStack(getItem(cup))).setIsDrink().addPotionEffect(Potion.digSpeed.id, 90, 0, 1F).setAlwaysEdible().setMaxStackSize(16)
    createJaffaItem(cupRaw)
    createJaffaItem(omeletteRaw)
    createJaffaFood(omelette, 3, 0.5F).addPotionEffect(Potion.regeneration.id, 4, 0, 0.2F).setMaxStackSize(16)
    createJaffaItem(tomatoChopped)
    createJaffaItem(paprikaChopped)
    createJaffaItem(wienerCocktail)
    createJaffaItem(jamRaspberry)
    createJaffaItem(jamStrawberry)
    createJaffaItem(raspberries)
    createJaffaItem(strawberries)
    createJaffaItem(rollRaw)
    createJaffaItem(roll)
    createJaffaItem(rollChopped)
    createJaffaItem(meatChopped)
    createJaffaItem(ironSkewer)
    createJaffaFood(skewer, 4, 0.5F).setReturnItem(new ItemStack(getItem(ironSkewer))).addPotionEffect(Potion.jump.id, 80, 0, 0.15F)
    createJaffaItem(skewerRaw)
    createJaffaItem(knifeKitchen).setMaxDamage(256).setMaxStackSize(1)
    createJaffaFood(jaffaStrawberry, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaFood(jaffaRaspberry, 3, 0.7F).addPotionEffect(Potion.regeneration.id, 2, 1, 0.4F)
    createJaffaItem(coffee)
    createJaffaItem(coffeeRoasted)
    createJaffaItem(brownPastry)
    createJaffaItem(coconutPowder)
    createJaffaItem(honey)
    createJaffaItem(gingerbread)
    createJaffaItem(hamburgerBunRaw)
    createJaffaItem(hamburgerBun)
    createJaffaItem(cheese)
    createJaffaItem(cheeseSlice)
    createJaffaItem(rawBurger)
    createJaffaItem(burger)
    createJaffaItem(onionSliced)
    createJaffaFood(hamburger, 6, 0.9f).addPotionEffect(Potion.nightVision.id, 60, 0, 0.3f)
    createJaffaFood(cheeseburger, 6, 0.7f).addPotionEffect(Potion.nightVision.id, 80, 0, 0.3f)
    createJaffaItem(fryingPan)
    createJaffaItem(fryingPanBurgerRaw)
    createJaffaItem(fryingPanEggRaw)
    createJaffaItem(fryingPanBurger)
    createJaffaItem(fryingPanEgg)
    createJaffaFood(eggFried, 2, 0.5f).asItem.setMaxStackSize(16)
    createJaffaItem(bread)
    createJaffaItem(breadRaw)
    createJaffaItem(breadSlice)
    createJaffaFood(breadSliceToasted, 3, 0.33f)
    createJaffaFood(breadSliceJam, 3, 0.33f).addPotionEffect(Potion.waterBreathing.id, 60, 0, 0.15F)
    createJaffaFood(breadSliceButter, 2, 0.33f).addPotionEffect(Potion.waterBreathing.id, 30, 0, 0.1F)
    createJaffaFood(breadSliceEgg, 3, 0.33f).addPotionEffect(Potion.waterBreathing.id, 60, 0, 0.15F)
    createJaffaItem(bottleEmpty)
    createJaffaItem(bottleKetchup)
    createJaffaItem(bottleMustard)
    createJaffaItem(bottleBrownMustard)
    createJaffaItem(mincedMeat)
    var cleaverInfo: JaffaItemInfo = ItemManager.getItemInfo(meatCleaver)
    createJaffaItemManual(meatCleaver, new ItemCleaver(cleaverInfo.getIconIndex, ContentHolder.EnumToolMaterialCleaver))
    createJaffaItemManual(sink, classOf[ItemSink])
    createJaffaItem(coneRaw)
    createJaffaItem(waferIcecreamRaw)
    createJaffaItem(grater)
    createJaffaItem(cheeseGrated)
    createJaffaItem(salami)
    createJaffaItem(salamiSliced)
    createJaffaItem(pizzaRaw)
    createJaffaItemManual(pizza, classOf[ItemPizza])
    createJaffaItem(wolfSkin)
    createJaffaArmorSet("wolf", ContentHolder.EnumArmorMaterialWolf, "jaffas_wolf1.png", "jaffas_wolf2.png", getItem(wolfSkin), Array[JaffaItem](wolfHelmet, wolfChest, wolfLeggins, wolfBoots))
    createJaffaItem(pastry)
    createJaffaItem(milkBoxEmpty)
    createJaffaItem(milkBoxFull)
    createJaffaItem(crumpledPaper).asInstanceOf[IItemJaffa].setInfo("temporary recipe")
    createJaffaItem(scrap)
    createJaffaFood(chips, 2, 1.5f).addPotionEffect(Potion.moveSlowdown.id, 5, 0, 0.2f)
    createJaffaItem(chipsRaw)
    createJaffaItem(fryingPanChips)
    createJaffaItem(fryingPanChipsRaw)
    createJaffaItem(pieStrawberryRaw)
    createJaffaItem(pieRaspberryRaw)
    createJaffaItem(pieVanillaRaw)
    createJaffaItem(piePlumRaw)
    if (ConfigurationManager.spawnStonesEnabled) {
      createJaffaItemManual(spawnStoneLittle, new ItemSpawnStone(ItemManager.getItemInfo(spawnStoneLittle), ConfigurationManager.spawnStoneLittleCD))
      createJaffaItemManual(spawnStoneMedium, new ItemSpawnStone(ItemManager.getItemInfo(spawnStoneMedium), ConfigurationManager.spawnStoneMediumCD))
      createJaffaItemManual(spawnStoneBig, new ItemSpawnStone(ItemManager.getItemInfo(spawnStoneBig), ConfigurationManager.spawnStoneBigCD))
    }

    createJaffaItem(juiceBottle)
    createJaffaItem(glassEmpty)
    for (juice <- JuiceItemsEnum.values()) {
      createJaffaFood(juice.juiceBottle, 12, 1f).setIsDrink().setReturnItem(new ItemStack(getItem(juiceBottle)))
      createJaffaFood(juice.glass, 5, 0.25f).setIsDrink().setReturnItem(new ItemStack(getItem(glassEmpty)))
    }
    createJaffaFood(glassMilk).Setup(1, 0.1f).asInstanceOf[ItemJaffaFood].setIsDrink().setReturnItem(new ItemStack(getItem(glassEmpty)))
    createJaffaItem(woodenBowl)
    createJaffaItem(cookedMushroomsRaw)
    createJaffaFood(cookedMushrooms, 6, 0.5f).setReturnItem(new ItemStack(getItem(woodenBowl))).addPotionEffect(Potion.fireResistance.id, 30, 0, 0.1f).setMaxStackSize(32)
    createJaffaItem(pepperStuffedRaw)
    createJaffaFood(pepperStuffed, 5, 1.0f).addPotionEffect(Potion.resistance.id, 60, 0, 0.2F).setMaxStackSize(16)
    createJaffaItem(peanutsSugar)
    createJaffaFood(peanutsCaramelized, 4, 0.2f).addPotionEffect(Potion.jump.id, 30, 0, 0.2F)
    createJaffaFood(wolfMeatRaw, 1, 0.05f).addPotionEffect(Potion.hunger.id, 15, 1, 0.2F)
    createJaffaFood(muttonRaw, 1, 0.05f).addPotionEffect(Potion.hunger.id, 15, 1, 0.2F)
    registerWolfFood(muttonRaw)
    createJaffaFood(spiderLegRaw, 1, 0.07f).addPotionEffect(Potion.poison.id, 4, 0, 0.2F)
    registerWolfFood(spiderLegRaw)
    createJaffaFood(wolfMeat, 4, 0.7f)
    createJaffaFood(mutton, 4, 0.7f)
    registerWolfFood(mutton)
    createJaffaFood(spiderLeg, 4, 0.7f)
    registerWolfFood(spiderLeg)
    createJaffaItem(JaffaItem.featherDuck)
    createJaffaFood(duckRaw, 1, 0.05f).addPotionEffect(Potion.hunger.id, 15, 1, 0.2F)
    registerWolfFood(duckRaw)
    createJaffaFood(JaffaItem.duck, 4, 0.7f)
    registerWolfFood(duck)
    createJaffaItem(JaffaItem.plateRaw)
    createJaffaItem(JaffaItem.plate)
    createJaffaFood(JaffaItem.plateDuckOrange, 8, 0.8f).setReturnItem(getItemStack(plate, 1)).addPotionEffect(Potion.regeneration.id, 10, 0, 1f).setMaxStackSize(24)
    createJaffaItem(JaffaItem.tinDuckOrangeRaw)
    createJaffaItem(JaffaItem.tinDuckOrange)
    createJaffaArmorSet("duck", ContentHolder.EnumArmorMaterialDuck, "jaffas_duckarmor1.png", "jaffas_duckarmor2.png", getItem(featherDuck), Array[JaffaItem](duckHelmet, duckChest, duckLeggins, duckBoots))
    createJaffaItem(strawberryIcecreamRaw)
    createJaffaItem(strawberryIcecreamFrozen)
    createJaffaFood(strawberryIcecream, 2, 0.3f).addPotionEffect(Potion.moveSpeed.id, 70, 0, 0.25F)
    createJaffaItem(chocIceStick)
    createJaffaFood(chocIce, 4, 1f).setReturnItem(new ItemStack(getItem(chocIceStick))).addPotionEffect(Potion.moveSpeed.id, 70, 0, 0.25F)
    createJaffaItemManual(duckEgg, classOf[ItemDuckEgg]).setMaxStackSize(16)
    createJaffaItem(muffinRaw)
    createJaffaItem(muffinUnfinished)
    createJaffaFood(muffin, 3, 0.5f).setReturnItem(getItemStack(crumpledPaper, 1)).addPotionEffect(Potion.regeneration.id, 4, 0, 0.1f)
    createJaffaItem(beansWithTomatoRaw)
    createJaffaFood(beansWithTomato, 6, 0.7f).setReturnItem(getItemStack(woodenBowl, 1)).addPotionEffect(Potion.fireResistance.id, 30, 0, 0.1f).setMaxStackSize(16)
    createJaffaFood(sandwich1, 6, 0.5f).addPotionEffect(Potion.waterBreathing.id, 90, 0, 0.25f)
    createJaffaItem(lambWithPeasInTinRaw)
    createJaffaItem(lambWithPeasInTin)
    createJaffaFood(lambWithPeas, 8, 1f).setReturnItem(getItemStack(plate, 1)).addPotionEffect(Potion.regeneration.id, 6, 0, 1f).setMaxStackSize(24)
    createJaffaItem(cocoBarWrapper)
    createJaffaItem(cookingPot).setMaxStackSize(16)
    createJaffaItem(cookingPotCocoaCold).setMaxStackSize(16)
    createJaffaItem(cookingPotCocoaHot).setMaxStackSize(16)
    createJaffaFood(eggHardBoiled, 2, 0.5f).asItem.setMaxStackSize(16)
    createJaffaFood(cupCocoa, 4, 0.5F).setReturnItem(new ItemStack(getItem(cup))).setIsDrink().addPotionEffect(Potion.moveSpeed.id, 60, 0, .55F).setAlwaysEdible().setMaxStackSize(16)
    createJaffaFood(cocoBar, 5, 0.5F).setReturnItem(getItemStack(crumpledPaper), 0.3333f).addPotionEffect(Potion.moveSpeed.id, 60, 0, 0.15F)
    createJaffaItem(cookingPotWater).setMaxStackSize(16)
    createJaffaItem(cookingPotEggsRaw).setMaxStackSize(16)
    createJaffaItem(cookingPotEggs).setMaxStackSize(16)
    BlockSink.addFillableItem(getItem(kettle), getItem(kettleWaterCold))
    BlockSink.addFillableItem(getItem(cookingPot), getItem(cookingPotWater))
    createJaffaFood(bananaInChocolate, 3, 1.2f).addPotionEffect(Potion.regeneration.id, 4, 0, 0.1f)
    createJaffaFood(fruitSalad, 4, 0.6f).addPotionEffect(Potion.moveSpeed.id, 30, 0, 0.33f).setReturnItem(getItemStack(woodenBowl))
    createJaffaItem(friedMushroomsInTinRaw)
    createJaffaItem(friedMushroomsInTinCooked)
    createJaffaFood(friedMushrooms, 6, 0.95f).addPotionEffect(Potion.regeneration.id, 6, 0, 1f).setReturnItem(getItemStack(plate)).setMaxStackSize(24)
    createJaffaItem(mincedMushrooms)
    createJaffaItem(shroomburger)
    createJaffaItem(shroomburgerRaw)
    createJaffaFood(shroomburgerInBunWithCheese, 6, 0.7f).addPotionEffect(Potion.nightVision.id, 80, 0, 0.15f)
    createJaffaFood(shroomburgerInBun, 5, 0.85f).addPotionEffect(Potion.nightVision.id, 60, 0, 0.15f)
    createJaffaItem(fryingPanShroomburgerRaw)
    createJaffaItem(fryingPanShroomburger)
    createJaffaItem(flyAgaricChopped)
    createJaffaItemManual(meatDryer, classOf[ItemMeatDryer])
    createJaffaFood(driedMeat, 5, 1f)
    createJaffaItem(beerMugEmpty).setMaxStackSize(16)
    createJaffaFood(beerMugFull, 4, 1.5f).setIsDrink().setAlwaysEdible().setReturnItem(getItemStack(beerMugEmpty)).
      addPotionEffect(Potion.damageBoost.id, 30, 0, 0.35f).addPotionEffect(Potion.confusion.id, 10, 0, 0.35f).setMaxStackSize(1)
    createJaffaItem(potatesSliced)
    createJaffaItem(potatesSlicedInTinRaw)
    createJaffaItem(potatesSlicedInTin)
    createJaffaFood(crisps, 3, 0.8f).setReturnItem(getItemStack(woodenBowl))
    createJaffaItem(cream)
    createJaffaItem(cheeseRaw).setMaxStackSize(1)
    createLollipops()

    createJaffaItems(fishFillet, fishStickRaw, fryingPanFishStickRaw, fryingPanFishStick, soupPeaRaw, soupFishRaw, soupTomatoRaw, breadCrumbs)
    createJaffaFood(soupPeaCooked, 6, 0.5f).setReturnItem(new ItemStack(getItem(woodenBowl))).addPotionEffect(Potion.resistance.id, 25, 0, 0.1f).setMaxStackSize(32)
    createJaffaFood(soupFishCooked, 6, 0.5f).setReturnItem(new ItemStack(getItem(woodenBowl))).addPotionEffect(Potion.resistance.id, 30, 0, 0.1f).setMaxStackSize(32)
    createJaffaFood(soupTomatoCooked, 6, 0.5f).setReturnItem(new ItemStack(getItem(woodenBowl))).addPotionEffect(Potion.fireResistance.id, 30, 0, 0.1f).setMaxStackSize(32)
    createJaffaFood(fishStickCooked, 5, 0.95f)

    createJaffaItemManual(juiceInBottle, new ItemJuice)
    createJaffaItemManual(juiceGlass, new ItemJuiceGlass)

    createJaffaItemManual(bucketWaterOfLife, new ItemJaffaBucket(ContentHolder.blockWaterOfLife))
    createJaffaItemManual(bucketCorrosiveGoo, new ItemJaffaBucket(ContentHolder.blockCorrosiveGoo))
    createJaffaItemManual(bucketMiningGoo, new ItemJaffaBucket(ContentHolder.blockMiningGoo))
    createJaffaItemManual(bucketUnstableGoo, new ItemJaffaBucket(ContentHolder.blockUnstableGoo))
    createJaffaItemManual(spongeCloth, classOf[ItemSpongeCloth])

    createJaffaItem(dustEnder)
    createJaffaItem(pileEnder)
    createJaffaItem(pileGlowstone)
    createJaffaItem(pileLimsew)
    createJaffaItem(pileRedstone)
    createJaffaItemManual(littleSpiderEggs, classOf[ItemSpiderEgg]).setMaxStackSize(16)

    createJaffaItem(switchgrassCharcoal)

    CobWebsCreator.create()

    createItemsOreDictRegistration()
    addMeatsToDryerDatabase()
    markJaffasRare()
  }

  private def createJaffaItems(items: JaffaItem*) {
    for (item <- items) createJaffaItem(item)
  }

  import Items._
  import net.minecraft.init.{Items => VanillaItems}

  private def createLollipops() {
    val info = ItemManager.getItemInfo(lollipop)
    val nameTitleList = lollipops map {_.nameTitlePair}
    val item = food.item.ItemJaffaFoodMultiple.fromPair(nameTitleList)
    val metaLollipop = createJaffaItemManual(lollipop, item)
    metaLollipop.Setup(3, 1.2f)
    metaLollipop.setAlwaysEdible().setReturnItem(new ItemStack(VanillaItems.stick)).addPotionEffect(Potion.regeneration.id, 15, 0, 0.33f).addPotionEffect(Potion.moveSpeed.id, 5, 2, 0.33f).setMaxStackSize(16)
    metaLollipop.setItemUseDuration(2f)
    metaLollipop.registerNames()
  }

  import scala.collection.JavaConverters._

  private def markJaffasRare() {
    for (ji <- JaffasHelper.getJaffas().asScala) {
      getItem(ji).asInstanceOf[IItemJaffa].setRarity(MonnefCoreNormalMod.proxy.getUncommonRarity())
    }
  }

  private def addMeatsToDryerDatabase() {
    TileMeatDryer.addNormalMeat(Recipes.getItem(JaffaItem.duckRaw))
    TileMeatDryer.addNormalMeat(Recipes.getItem(JaffaItem.muttonRaw))
    TileMeatDryer.addNormalMeat(Recipes.getItem(JaffaItem.wolfMeatRaw))
    TileMeatDryer.addZombieMeat(Recipes.getItem(JaffaItem.spiderLegRaw))
  }

  def createJaffaArmor(item: JaffaItem, material: ArmorMaterial, renderIndex: Int, __kwd_type: ItemJaffaPlate.ArmorType, texture: String, repairItem: Item) {
    var info: JaffaItemInfo = ItemManager.getItemInfo(item)
    createJaffaItemManual(item, new ItemJaffaPlate(material, renderIndex, __kwd_type, texture, repairItem, info.getIconIndex))
  }

  def createJaffaArmor(item: JaffaItem, material: ArmorMaterial, renderIndex: Int, __kwd_type: ItemJaffaPlate.ArmorType, texture: String, repairItem: JaffaItem) {
    createJaffaArmor(item, material, renderIndex, __kwd_type, texture, if (repairItem == null || repairItem == _last) null else getItem(repairItem))
  }

  def createJaffaArmorSet(renderName: String, material: ArmorMaterial, file1: String, file2: String, repairItem: Item, pieces: Array[JaffaItem]) {
    var renderIndex: Int = JaffasFood.proxy.addArmor(renderName)
    createJaffaArmor(pieces(0), material, renderIndex, ItemJaffaPlate.ArmorType.helm, file1, repairItem)
    createJaffaArmor(pieces(1), material, renderIndex, ItemJaffaPlate.ArmorType.chest, file1, repairItem)
    createJaffaArmor(pieces(2), material, renderIndex, ItemJaffaPlate.ArmorType.leggings, file2, repairItem)
    createJaffaArmor(pieces(3), material, renderIndex, ItemJaffaPlate.ArmorType.boots, file1, repairItem)
  }

  import monnef.jaffas.food.item.common.Items._
  import net.minecraft.init.{Items => VanillaItems}

  private def createItemsOreDictRegistration() {
    OreDictionary.registerOre(MINCEABLEMEAT, VanillaItems.porkchop)
    OreDictionary.registerOre(MINCEABLEMEAT, VanillaItems.beef)
    OreDictionary.registerOre(MINCEABLEMEAT, VanillaItems.chicken)
    OreDictionary.registerOre(MINCEABLEMEAT, getItem(muttonRaw))
    OreDictionary.registerOre(MINCEABLEMEAT, getItem(wolfMeatRaw))
    OreDictionary.registerOre(MINCEABLEMEAT, getItem(duckRaw))
    JaffasHelper.registerJaffasInOreDict()
    OreDictionary.registerOre(MUSHROOM, Blocks.brown_mushroom)
    OreDictionary.registerOre(MUSHROOM, Blocks.red_mushroom)
    OreDictionary.registerOre(ANY_EGG, VanillaItems.egg)
    OreDictionary.registerOre(ANY_EGG, getItem(duckEgg))
    OreDictionary.registerOre(MALLET, getItemStackAnyDamage(mallet))
    OreDictionary.registerOre(MALLET, getItemStackAnyDamage(malletStone))
    OreDictionary.registerOre(MALLET, getItemStackAnyDamage(malletIron))
    OreDictionary.registerOre(MALLET, getItemStackAnyDamage(malletDiamond))
    OreDictionary.registerOre(ANY_MILK, VanillaItems.milk_bucket)
    OreDictionary.registerOre(ANY_MILK, getItem(milkBoxFull))
    OreDictionary.registerOre(Recipes.STRING, net.minecraft.init.Items.string)
  }
}

object Items {

  import monnef.core.utils.scalautils._
  import JaffaItem._

  val MINCEABLEMEAT: String = "jaffasMinceAbleMeat"
  val JAFFA: String = "jaffasAny"
  val JAFFA_FILLED: String = "jaffasFilled"
  val MUSHROOM: String = "jaffasMushroom"
  val ANY_EGG: String = "jaffasEgg"
  val MALLET: String = "jaffasMallet"
  val ANY_MILK: String = "jaffasMilk"

  case class LollipopRecord(name: String, title: String, id: Int, jam: JaffaItem) {
    def nameTitlePair: (String, String) = name -> title
  }

  var currLollipopDmg = 0

  def createLollipopRecord(name: String, jam: JaffaItem): LollipopRecord = {
    val title = name.insertSpaceOnLowerUpperCaseChange.makeFirstCapital + " Lollipop"
    val lr = LollipopRecord(name, title, currLollipopDmg, jam)
    currLollipopDmg += 1
    lr
  }

  val lollipops = List(
    createLollipopRecord("strawberry", jamStrawberry),
    createLollipopRecord("vanilla", jamV),
    createLollipopRecord("plum", jamP),
    createLollipopRecord("lemon", jamL),
    createLollipopRecord("orange", jamO),
    createLollipopRecord("raspberry", jamRaspberry),
    createLollipopRecord("apple", jamR),
    createLollipopRecord("chocolate", chocolate)
  )
}
