/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.item.common;

import net.minecraft.item.Item;

public interface IItemFood {
    Item Setup(int healAmount, float saturation);
}
