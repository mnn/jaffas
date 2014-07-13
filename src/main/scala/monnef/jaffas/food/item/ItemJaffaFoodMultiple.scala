/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item

class ItemJaffaFoodMultiple(val subNames: Seq[String], val subTitles: Seq[String]) extends ItemJaffaBase with ItemJaffaFoodMultipleTrait[ItemJaffaFood] {
}

object ItemJaffaFoodMultiple {
  def fromPair(nameTitles: Seq[(String, String)]): ItemJaffaFoodMultiple = {
    val names = nameTitles.map(_._1)
    val titles = nameTitles.map(_._2)
    new ItemJaffaFoodMultiple(names, titles)
  }
}