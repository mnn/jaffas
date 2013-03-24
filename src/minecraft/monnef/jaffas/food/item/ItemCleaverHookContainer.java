package monnef.jaffas.food.item;

import monnef.core.utils.EntityHelper;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.Random;

public class ItemCleaverHookContainer {
    private static String DamageSourcePlayer = "player";

    private static int meatCleaverID = 0;
    private static Random rand = new Random();

    private static HashMap<Class<? extends EntityLiving>, ItemStack> AnimalToMeat;

    static {
        AnimalToMeat = new HashMap<Class<? extends EntityLiving>, ItemStack>();
        AnimalToMeat.put(EntityCow.class, new ItemStack(Item.beefRaw));
        AnimalToMeat.put(EntityPig.class, new ItemStack(Item.porkRaw));
        AnimalToMeat.put(EntityChicken.class, new ItemStack(Item.chickenRaw));
        AnimalToMeat.put(EntityMooshroom.class, new ItemStack(Item.beefRaw));

        /*
        AnimalToMeat.put(EntitySheep.class, new ItemStack(Item.bone));
        AnimalToMeat.put(EntityWolf.class, new ItemStack(Item.bone));
        AnimalToMeat.put(EntityOcelot.class, new ItemStack(Item.bone));
        AnimalToMeat.put(EntitySquid.class, new ItemStack(Item.bone));
        */
    }

    private ItemStack getMeatFromAnimal(EntityCreature animal) {
        return AnimalToMeat.get(animal.getClass()).copy();
    }

    @ForgeSubscribe
    public void entityHurt(LivingHurtEvent event) {
        DamageSource source = event.source;
        EntityLiving mob = event.entityLiving;

        if (SourceIsPlayer(source)) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            if (PlayerHelper.PlayerHasEquipped(player, getMeatCleaverID())) {
                if (AnimalToMeat.containsKey(mob.getClass())) {
                    event.ammount += 10;
                }
                PlayerHelper.damageCurrentItem(player);
            }
        }
    }

    @ForgeSubscribe
    public void entityDeath(LivingDeathEvent event) {
        DamageSource source = event.source;
        EntityLiving mob = event.entityLiving;

        if (SourceIsPlayer(source)) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            if (PlayerHelper.PlayerHasEquipped(player, getMeatCleaverID())) {
                if (AnimalToMeat.containsKey(mob.getClass())) {
                    EntityCreature animal = (EntityCreature) mob;
                    if (EntityHelper.animalIsAdult(animal)) {
                        for (int i = 0; i < 2; i++) {
                            if (rand.nextFloat() < .5) {
                                ItemStack loot = getMeatFromAnimal(animal);
                                DropLoot(animal, loot);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean SourceIsPlayer(DamageSource source) {
        return source.damageType.equals(DamageSourcePlayer);
    }

    private void DropLoot(EntityLiving entity, ItemStack loot) {
        EntityItem item = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, loot);
        entity.worldObj.spawnEntityInWorld(item);
    }

    private int getMeatCleaverID() {
        if (meatCleaverID == 0) {
            meatCleaverID = mod_jaffas_food.getItem(JaffaItem.meatCleaver).itemID;
        }

        return meatCleaverID;
    }
}
