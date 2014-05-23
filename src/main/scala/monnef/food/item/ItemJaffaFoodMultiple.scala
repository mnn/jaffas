/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item

class ItemJaffaFoodMultiple(__id: Int, val subNames: Seq[String], val subTitles: Seq[String]) extends ItemJaffaBase(__id) with ItemJaffaFoodMultipleTrait[ItemJaffaFood] {
}

object ItemJaffaFoodMultiple {
  def fromPair(id: Int, nameTitles: Seq[(String, String)]): ItemJaffaFoodMultiple = {
    val names = nameTitles.map(_._1)
    val titles = nameTitles.map(_._2)
    new ItemJaffaFoodMultiple(id, names, titles)
  }
}