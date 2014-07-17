package monnef.jaffas.technic.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizer;

public class ItemCompost extends ItemTechnic implements IFactoryFertilizer {
    public ItemCompost(int textureIndex) {
        super(textureIndex);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        if (ItemDye.applyBonemeal(stack, world, x, y, z, player)) {
            if (!world.isRemote) {
                world.playAuxSFX(2005, x, y, z, 0);
            }

            return true;
        }

        return false;
    }

    @Override
    public int getFertilizerId() {
        return itemID;
    }

    @Override
    public int getFertilizerMeta() {
        return 0;
    }

    @Override
    public FertilizerType getFertilizerType() {
        return FertilizerType.GrowPlant;
    }

    @Override
    public void consume(ItemStack fertilizer) {
        fertilizer.stackSize--;
    }
}
