/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.juice

object Juices {
  val list: List[JuiceInfo] = List(
    JuiceInfo.create(0, "Lemon Juice", 186),
    JuiceInfo.create(1, "Orange Juice", 187),
    JuiceInfo.create(2, "Apple Juice", 188),
    JuiceInfo.create(3, "Raspberry Juice", 189),
    JuiceInfo.create(2, "Tomato Juice", 189),
    JuiceInfo.create(1, "Carrot Juice", 189),
    JuiceInfo.create(3, "Melon Juice", 189),
    JuiceInfo.create(3, "Strawberry Juice", 189)
  )

  val juiceCount: Int = list length

  val juiceTitles: List[String] = list map {_.title}
  val juiceOffsets: List[Int] = list map {_.textureIndex}
  val juiceMaxOffset = juiceOffsets.max

  val glassTitles: List[String] = list map {_.titleGlass}
  val glassOffsets: List[Int] = list map {_.glassTextureIndex}
  val glassMaxOffset = glassOffsets.max
}

object JuiceInfo {
  def create(textureIndex: Int, title: String, glassTextureIndex: Int, titleGlass: String = ""): JuiceInfo = {
    val tGlass = if (titleGlass.isEmpty) "Glass of " + title else titleGlass
    new JuiceInfo(textureIndex, title, glassTextureIndex, tGlass)
  }
}

case class JuiceInfo(textureIndex: Int, title: String, glassTextureIndex: Int, titleGlass: String)
