/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.item;

import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManagerAccessor;

public class Items extends ItemManagerAccessor {
    @Override
    public ModulesEnum getMyModule() {
        return ModulesEnum.xmas;
    }

    @Override
    protected void AddItemInfo(JaffaItem item, String name, int iconIndex, String title) {
        super.AddItemInfo(item, "xcandy." + name, iconIndex, title);
    }

    @Override
    public void InitializeItemInfos() {
        setCurrentSheetNumber(4);
        AddItemInfo(JaffaItem.xcandyStarRaw, "starRaw", 18, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandyStar, "star", 6, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyStarJam, "starJam", 7, "Christmas Candy");
        AddItemInfo(JaffaItem.xcandyStarChoco, "starChoco", 8, "Choco Christmas Candy");

        AddItemInfo(JaffaItem.xcandySmallRollRaw, "smallRollRaw", 9, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandySmallRoll, "smallRoll", 9, "Christmas Candy");
        AddItemInfo(JaffaItem.xcandySmallRollChoco, "smallRollChoco", 10, "Christmas Candy");

        AddItemInfo(JaffaItem.xcandyChocoCircleRaw, "chocoCircleRaw", 17, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoCircle, "chocoCircle", 11, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoCircleCoated, "chocoCircleCoated", 12, "Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoCircleSprinkled, "chocoCircleSprinkled", 13, "Christmas Candy");

        AddItemInfo(JaffaItem.xcandyChocoBall, "chocoBall", 14, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoBallSprinkled, "chocoBallSprinkled", 15, "Christmas Candy");

        AddItemInfo(JaffaItem.xcandyCane, "cane", 16, "Candy Cane");

        AddItemInfo(JaffaItem.xcandyGingerFigureRaw, "gingerFigureRaw", 19, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandyGingerFigure, "gingerFigure", 20, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyGingerCreamed, "gingerFigureCreamed", 21, "Christmas Candy");
    }

    @Override
    public void CreateItems() {
        createJaffaItem(JaffaItem.xcandyStarRaw);
        createJaffaItem(JaffaItem.xcandyStar);
        createJaffaFood(JaffaItem.xcandyStarJam).Setup(3, .2f);
        createJaffaFood(JaffaItem.xcandyStarChoco).Setup(1, .7f);

        createJaffaItem(JaffaItem.xcandySmallRollRaw);
        createJaffaFood(JaffaItem.xcandySmallRoll).Setup(1, .1f);
        createJaffaFood(JaffaItem.xcandySmallRollChoco).Setup(2, .5f);

        createJaffaItem(JaffaItem.xcandyChocoCircleRaw);
        createJaffaItem(JaffaItem.xcandyChocoCircle);
        createJaffaFood(JaffaItem.xcandyChocoCircleCoated).Setup(2, .5f);
        createJaffaFood(JaffaItem.xcandyChocoCircleSprinkled).Setup(4, 1.5f);

        createJaffaItem(JaffaItem.xcandyChocoBall);
        createJaffaFood(JaffaItem.xcandyChocoBallSprinkled).Setup(3, .2f);

        createJaffaFood(JaffaItem.xcandyCane).Setup(2, 1f);

        createJaffaItem(JaffaItem.xcandyGingerFigureRaw);
        createJaffaItem(JaffaItem.xcandyGingerFigure);
        createJaffaFood(JaffaItem.xcandyGingerCreamed).Setup(3, 1f);
    }
}
