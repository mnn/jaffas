/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.juice

import net.minecraft.item.{Item, ItemStack}
import monnef.jaffas.trees.{BushType, JaffasTrees}
import net.minecraftforge.oredict.OreDictionary
import BushType._
import cpw.mods.fml.common.registry.GameRegistry
import monnef.jaffas.food.JaffasFood
import monnef.jaffas.food.item.JaffaItem
import monnef.jaffas.power.block.TileJuiceMaker
import monnef.jaffas.food.crafting.PersistentItemsCraftingHandler
import net.minecraft.init.Items

object Juices {
  val list: List[JuiceInfo] = {
    val treeItemCount = 4
    val cropItemCount = 3

    List(
      JuiceInfo.create(0, "Lemon Juice", {JuiceRecipe.create(JaffasTrees.LEMON, treeItemCount)()}),
      JuiceInfo.create(1, "Orange Juice", JuiceRecipe.create(JaffasTrees.ORANGE, treeItemCount)()),
      JuiceInfo.create(2, "Apple Juice", JuiceRecipe.create(Items.apple, treeItemCount)),
      JuiceInfo.create(3, "Raspberry Juice", JuiceRecipe.create(Raspberry, cropItemCount)),
      JuiceInfo.create(2, "Tomato Juice", JuiceRecipe.create(Tomato, cropItemCount)),
      JuiceInfo.create(1, "Carrot Juice", JuiceRecipe.create(Items.carrot, cropItemCount)),
      JuiceInfo.create(3, "Melon Juice", JuiceRecipe.create(Items.melon, cropItemCount)),
      JuiceInfo.create(3, "Strawberry Juice", JuiceRecipe.create(Strawberry, cropItemCount))
    )
  }

  val juiceCount: Int = list.length

  val juiceTitles: List[String] = list map {_.title}
  val juiceOffsets: List[Int] = list map {_.textureIndex}
  val juiceMaxOffset = juiceOffsets.max

  val glassTitles: List[String] = list map {_.titleGlass}
  val glassOffsets: List[Int] = list map {_.glassTextureIndex}
  val glassMaxOffset = glassOffsets.max

  lazy val juiceItem = JaffasFood.getItem(JaffaItem.juiceInBottle)
  lazy val juiceEmptyBottle = JaffasFood.getItem(JaffaItem.juiceBottle)
  lazy val glassItem = JaffasFood.getItem(JaffaItem.juiceGlass)
  lazy val glassEmpty = JaffasFood.getItem(JaffaItem.glassEmpty)

  val glassesPerBottle = 3

  private var recipesAreBeingProcessed = false

  def checkPhase() { if (!recipesAreBeingProcessed) throw new RuntimeException("Premature item stack evaluation in juice recipe") }

  def addRecipes() {
    recipesAreBeingProcessed = true
    PersistentItemsCraftingHandler.AddPersistentItem(JaffaItem.juiceInBottle, false, JaffaItem.juiceBottle)
    for {
      i <- 0 until list.length
      curr = list(i)
    } {
      val glassJuiceIngredients = Seq(new ItemStack(juiceItem, 1, i)) ++ Seq.fill(glassesPerBottle)(glassEmpty)
      GameRegistry.addShapelessRecipe(new ItemStack(glassItem, glassesPerBottle, i), glassJuiceIngredients: _*)
      for {recipe <- curr.recipes} {
        val juiceBottleIngredients = Seq(juiceEmptyBottle) ++ recipe.recipeList
        val out = new ItemStack(juiceItem, 1, i)
        GameRegistry.addShapelessRecipe(out.copy(), juiceBottleIngredients: _*)
        val juicerInput = recipe.edible.copy()
        juicerInput.stackSize -= 1
        TileJuiceMaker.addJuiceRecipe(juicerInput, out.copy())
      }
    }
  }
}

object JuiceInfo {
  def create(textureIndex: Int, title: String, recipes: => Seq[JuiceRecipe], glassTextureIndex: Int = -1, titleGlass: String = ""): JuiceInfo = {
    val tGlass = if (titleGlass.isEmpty) "Glass of " + title else titleGlass
    val gTexIdx = if (glassTextureIndex == -1) textureIndex else glassTextureIndex
    new JuiceInfo(textureIndex, title, gTexIdx, tGlass, recipes)
  }
}

class JuiceInfo(val textureIndex: Int, val title: String, val glassTextureIndex: Int, val titleGlass: String, _recipes: => Seq[JuiceRecipe]) {
  lazy val recipes = _recipes
}

class JuiceRecipe(_edible: => ItemStack) {
  lazy val edible = _edible

  def recipeList: Seq[ItemStack] = {
    val i = edible.copy()
    i.stackSize = 1
    Seq.fill(edible.stackSize)(i.copy())
  }
}

object JuiceRecipe {
  def apply(edible: => ItemStack): JuiceRecipe = new JuiceRecipe(edible)

  def create(item: => Item, count: Int): Seq[JuiceRecipe] = List(JuiceRecipe({Juices.checkPhase(); new ItemStack(item, count)}))

  def create(bush: BushType, count: Int): Seq[JuiceRecipe] = create({Juices.checkPhase(); JaffasTrees.getFruit(bush)}, count)

  def create(oreName: String, count: Int): () => Seq[JuiceRecipe] = {
    val ores = OreDictionary.getOres(oreName)
    if (ores.isEmpty) throw new RuntimeException(s"No items for oreName: $oreName")
    () => {
      ores.toArray(Array[ItemStack]()).map {
        a => {
          Juices.checkPhase()
          val b = a.copy()
          b.stackSize = count
          JuiceRecipe(b)
        }
      }
    }
  }
}
