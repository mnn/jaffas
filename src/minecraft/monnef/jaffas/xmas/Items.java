package monnef.jaffas.xmas;

import monnef.jaffas.food.ItemManagerAccessor;
import monnef.jaffas.food.JaffaItem;
import monnef.jaffas.food.ModulesEnum;

public class Items extends ItemManagerAccessor {
    @Override
    public ModulesEnum getMyModule() {
        return ModulesEnum.xmas;
    }

    @Override
    protected void InitializeItemInfos() {
        AddItemInfo(JaffaItem.xcandyStarRaw, "xcandy.starRaw", 18, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandyStar, "xcandy.star", 6, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyStarJam, "xcandy.starJam", 7, "Christmas Candy");
        AddItemInfo(JaffaItem.xcandyStarChoco, "xcandy.starChoco", 8, "Choco Christmas Candy");

        AddItemInfo(JaffaItem.xcandySmallRollRaw, "xcandy.smallRollRaw", 9, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandySmallRoll, "xcandy.smallRoll", 9, "Christmas Candy");
        AddItemInfo(JaffaItem.xcandySmallRollChoco, "xcandy.smallRollChoco", 10, "Christmas Candy");

        AddItemInfo(JaffaItem.xcandyChocoCircleRaw, "xcandy.chocoCircleRaw", 17, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoCircle, "xcandy.chocoCircle", 11, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoCircleCoated, "xcandy.chocoCircleCoated", 12, "Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoCircleSprinkled, "xcandy.chocoCircleSprinkled", 13, "Christmas Candy");

        AddItemInfo(JaffaItem.xcandyChocoBall, "xcandy.chocoBall", 14, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyChocoBallSprinkled, "xcandy.chocoBallSprinkled", 15, "Christmas Candy");

        AddItemInfo(JaffaItem.xcandyCane, "xcandy.cane", 16, "Candy Cane");

        AddItemInfo(JaffaItem.xcandyGingerFigureRaw, "xcandy.gingerFigureRaw", 19, "Raw Christmas Candy");
        AddItemInfo(JaffaItem.xcandyGingerFigure, "xcandy.gingerFigure", 20, "Unfinished Christmas Candy");
        AddItemInfo(JaffaItem.xcandyGingerCreamed, "xcandy.gingerFigureCreamed", 21, "Christmas Candy");
    }

    @Override
    protected void CreateItems() {
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
