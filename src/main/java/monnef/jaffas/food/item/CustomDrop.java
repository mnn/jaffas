/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import monnef.core.utils.EntityHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CustomDrop {
    private static HashMap<Class<? extends EntityLiving>, ArrayList<CustomDropEntry>> drops;
    private static Random rand = new Random();

    public CustomDrop() {
        drops = new HashMap<Class<? extends EntityLiving>, ArrayList<CustomDropEntry>>();
        createDropTable();
    }

    private void createDropTable() {
        addDrop(EntityWolf.class, JaffaItem.wolfSkin, 0.7f).setBabyFlagCheck(true);
        addDrop(EntityWolf.class, JaffaItem.wolfSkin, 0.5f).setBabyFlagCheck(true);

        addDrop(EntityWolf.class, JaffaItem.wolfMeatRaw, 1f).setBabyFlagCheck(true);
        addDrop(EntityWolf.class, JaffaItem.wolfMeatRaw, 0.5f).setBabyFlagCheck(true);

        addDrop(EntitySheep.class, JaffaItem.muttonRaw, 1f).setBabyFlagCheck(true);
        addDrop(EntitySheep.class, JaffaItem.muttonRaw, 0.5f).setBabyFlagCheck(true);

        addDrop(EntitySpider.class, JaffaItem.spiderLegRaw, 1f);
        addDrop(EntitySpider.class, JaffaItem.spiderLegRaw, 0.5f);
        addDrop(EntitySpider.class, new ItemStack(Items.spawn_egg, 1, ContentHolder.spiderEntityID), 0.07f);
    }

    public static CustomDropEntry addDrop(Class<? extends EntityLiving> clazz, JaffaItem item, float chance) {
        return addDrop(clazz, JaffasFood.getItem(item), chance);
    }

    public static CustomDropEntry addDrop(Class<? extends EntityLiving> clazz, Item item, float chance) {
        return addDrop(clazz, new ItemStack(item), chance);
    }

    public static CustomDropEntry addDrop(Class<? extends EntityLiving> clazz, ItemStack item, float chance) {
        if (!drops.containsKey(clazz)) drops.put(clazz, new ArrayList<CustomDropEntry>());

        ArrayList<CustomDropEntry> list = drops.get(clazz);
        CustomDropEntry newEntry = new CustomDropEntry(chance, item);
        list.add(newEntry);
        return newEntry;
    }

    @SubscribeEvent
    public void entityDrop(LivingDropsEvent event) {
        EntityLivingBase mob = event.entityLiving;

        ArrayList<CustomDropEntry> data = drops.get(mob.getClass());
        if (data != null) {
            for (CustomDropEntry drop : data) {
                if (drop.checkBabyFlag) {
                    if (drop.expectedValueOfAdultFlag != EntityHelper.animalIsAdult(mob))
                        continue;
                }

                if (drop.chance > rand.nextFloat()) {
                    EntityItem item = new EntityItem(mob.worldObj, mob.posX, mob.posY + .2, mob.posZ, drop.item.copy());
                    event.drops.add(item);
                }
            }
        }
    }
}
