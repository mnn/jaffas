/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.common;

import net.minecraft.item.Item;

public interface IItemFood extends IItemJaffa {
    Item Setup(int healAmount, float saturation);
}
