/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.Interval;
import net.minecraft.item.ItemStack;

public class HighPlantInfo {
    public int id = -1;
    public int totalPlantHeight = 2;
    public ItemStack seed;
    public ItemStack fruit;
    public Interval fruitCount;
    public Class<? extends IHighPlantModel> renderer;
    public IHighPlantLifeCycleDescriptor lifeCycle;

    public int getSlavesCount() {
        return totalPlantHeight - 1;
    }

    public IHighPlantModel createRenderer() {
        try {
            return renderer.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
