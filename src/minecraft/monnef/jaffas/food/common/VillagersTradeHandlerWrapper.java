/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.registry.VillagerRegistry;
import monnef.core.common.MerchantRecipeListWrapper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

// comfy java wrapper just to circumvent scala compiler flaw(s) (raw type in MerchantRecipeList)
public class VillagersTradeHandlerWrapper implements VillagerRegistry.IVillageTradeHandler {
    private IScalaVillagersTradeHandler scalaHandler;

    public VillagersTradeHandlerWrapper(IScalaVillagersTradeHandler scalaHandler) {
        this.scalaHandler = scalaHandler;
    }

    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        scalaHandler.manipulateTradesForVillager(villager, new MerchantRecipeListWrapper(recipeList), random);
    }
}
