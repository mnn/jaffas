/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.common;

import monnef.jaffas.food.item.JaffaItem;

import static monnef.jaffas.food.item.JaffaItem.glassApple;
import static monnef.jaffas.food.item.JaffaItem.glassOrange;
import static monnef.jaffas.food.item.JaffaItem.glassRaspberry;
import static monnef.jaffas.food.item.JaffaItem.juiceApple;
import static monnef.jaffas.food.item.JaffaItem.juiceOrange;
import static monnef.jaffas.food.item.JaffaItem.juiceRaspberry;

public enum JuiceItemsEnum {
    LEMON(JaffaItem.juiceLemon, 181, "Lemon Juice", JaffaItem.glassLemon, 186, ""),
    ORANGE(juiceOrange, 182, "Orange Juice", glassOrange, 187, ""),
    APPLE(juiceApple, 183, "Apple Juice", glassApple, 188, ""),
    RASPBERRY(juiceRaspberry, 184, "Raspberry Juice", glassRaspberry, 189, "");

    public final JaffaItem juiceBottle;
    public final int textureIndex;
    public final String title;
    public final JaffaItem glass;
    public final int textureIndexGlass;
    public final String glassTitle;

    JuiceItemsEnum(JaffaItem juiceBottle, int textureIndex, String title, JaffaItem glass, int textureIndexGlass, String glassTitle) {
        this.juiceBottle = juiceBottle;
        this.textureIndex = textureIndex;
        this.title = title;
        this.textureIndexGlass = textureIndexGlass;
        this.glass = glass;

        if (glassTitle.equals("")) {
            this.glassTitle = "Glass of " + this.title;
        } else {
            this.glassTitle = glassTitle;
        }
    }
}
