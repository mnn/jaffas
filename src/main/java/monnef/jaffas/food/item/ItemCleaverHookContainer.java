/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import monnef.core.utils.DamageSourceHelper;
import monnef.core.utils.EntityHelper;
import monnef.core.utils.PlayerHelper;
import monnef.core.utils.WorldHelper;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.Random;

public class ItemCleaverHookContainer {

    private static Random rand = new Random();

    private static HashMap<Class<? extends EntityLivingBase>, ItemStack> AnimalToMeat;

    static {
        AnimalToMeat = new HashMap<Class<? extends EntityLivingBase>, ItemStack>();
        AnimalToMeat.put(EntityCow.class, new ItemStack(Items.beef));
        AnimalToMeat.put(EntityPig.class, new ItemStack(Items.porkchop));
        AnimalToMeat.put(EntityChicken.class, new ItemStack(Items.chicken));
        AnimalToMeat.put(EntityMooshroom.class, new ItemStack(Items.beef));
    }

    private ItemStack getMeatFromAnimal(EntityCreature animal) {
        return AnimalToMeat.get(animal.getClass()).copy();
    }

    public static void registerMeatFromAnimal(Class<? extends EntityLiving> animal, ItemStack meat) {
        if (AnimalToMeat.containsKey(animal)) {
            throw new RuntimeException("overriding animal to meat record.");
        }

        AnimalToMeat.put(animal, meat.copy());
    }

    @SubscribeEvent
    public void entityHurt(LivingHurtEvent event) {
        DamageSource source = event.source;
        EntityLivingBase mob = event.entityLiving;

        if (DamageSourceHelper.sourceIsPlayer(source)) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            if (PlayerHelper.playerHasEquipped(player, getMeatCleaver())) {
                if (AnimalToMeat.containsKey(mob.getClass())) {
                    event.ammount += 10;
                }
                PlayerHelper.damageCurrentItem(player);
            }
        }
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent event) {
        DamageSource source = event.source;
        EntityLivingBase mob = event.entityLiving;

        if (DamageSourceHelper.sourceIsPlayer(source)) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            if (PlayerHelper.playerHasEquipped(player, getMeatCleaver())) {
                if (AnimalToMeat.containsKey(mob.getClass())) {
                    EntityCreature animal = (EntityCreature) mob;
                    if (EntityHelper.animalIsAdult(animal)) {
                        for (int i = 0; i < 2; i++) {
                            if (rand.nextFloat() < .5) {
                                ItemStack loot = getMeatFromAnimal(animal);
                                WorldHelper.dropLoot(animal, loot);
                            }
                        }
                    }
                }
            }
        }
    }

    private Item getMeatCleaver() {
        return JaffasFood.getItem(JaffaItem.meatCleaver);
    }
}
