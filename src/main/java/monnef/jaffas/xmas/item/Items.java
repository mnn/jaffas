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
        addItemInfo(JaffaItem.xcandyStarRaw, 18);
        addItemInfo(JaffaItem.xcandyStar, 6);
        addItemInfo(JaffaItem.xcandyStarJam, 7);
        addItemInfo(JaffaItem.xcandyStarChoco, 8);

        addItemInfo(JaffaItem.xcandySmallRollRaw, 9);
        addItemInfo(JaffaItem.xcandySmallRoll, 9);
        addItemInfo(JaffaItem.xcandySmallRollChoco, 10);

        addItemInfo(JaffaItem.xcandyChocoCircleRaw, 17);
        addItemInfo(JaffaItem.xcandyChocoCircle, 11);
        addItemInfo(JaffaItem.xcandyChocoCircleCoated, 12);
        addItemInfo(JaffaItem.xcandyChocoCircleSprinkled, 13);

        addItemInfo(JaffaItem.xcandyChocoBall, 14);
        addItemInfo(JaffaItem.xcandyChocoBallSprinkled, 15);

        addItemInfo(JaffaItem.xcandyCane, 16);

        addItemInfo(JaffaItem.xcandyGingerFigureRaw, 19);
        addItemInfo(JaffaItem.xcandyGingerFigure, 20);
        addItemInfo(JaffaItem.xcandyGingerCreamed, 21);
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
