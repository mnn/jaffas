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
    public void InitializeItemInfos() {
        setCurrentSheetNumber(4);
        AddItemInfo(JaffaItem.xcandyStarRaw, 18);
        AddItemInfo(JaffaItem.xcandyStar, 6);
        AddItemInfo(JaffaItem.xcandyStarJam, 7);
        AddItemInfo(JaffaItem.xcandyStarChoco, 8);

        AddItemInfo(JaffaItem.xcandySmallRollRaw, 9);
        AddItemInfo(JaffaItem.xcandySmallRoll, 9);
        AddItemInfo(JaffaItem.xcandySmallRollChoco, 10);

        AddItemInfo(JaffaItem.xcandyChocoCircleRaw, 17);
        AddItemInfo(JaffaItem.xcandyChocoCircle, 11);
        AddItemInfo(JaffaItem.xcandyChocoCircleCoated, 12);
        AddItemInfo(JaffaItem.xcandyChocoCircleSprinkled, 13);

        AddItemInfo(JaffaItem.xcandyChocoBall, 14);
        AddItemInfo(JaffaItem.xcandyChocoBallSprinkled, 15);

        AddItemInfo(JaffaItem.xcandyCane, 16);

        AddItemInfo(JaffaItem.xcandyGingerFigureRaw, 19);
        AddItemInfo(JaffaItem.xcandyGingerFigure, 20);
        AddItemInfo(JaffaItem.xcandyGingerCreamed, 21);
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
