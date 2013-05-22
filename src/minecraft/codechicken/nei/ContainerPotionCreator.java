package codechicken.nei;

import codechicken.core.inventory.ContainerExtended;
import codechicken.core.inventory.InventoryNBT;
import codechicken.core.inventory.InventoryUtils;
import codechicken.core.inventory.SlotHandleClicks;
import codechicken.core.packet.PacketCustom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;

public class ContainerPotionCreator extends ContainerExtended
{
    public class SlotPotion extends Slot
    {
        public SlotPotion(IInventory inv, int slotIndex, int x, int y)
        {
            super(inv, slotIndex, x, y);
        }
        
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return stack.getItem() instanceof ItemPotion;
        }
        
        @Override
        public void onSlotChanged()
        {
            super.onSlotChanged();
            if(getHasStack())
            {
                ItemStack stack = getStack();
                if(!stack.hasTagCompound())
                    stack.setTagCompound(new NBTTagCompound("tag"));
                if(!stack.getTagCompound().hasKey("CustomPotionEffects"))
                    stack.getTagCompound().setTag("CustomPotionEffects", new NBTTagList());
            }
        }
    }
    
    public class SlotPotionStore extends SlotHandleClicks
    {
        public SlotPotionStore(IInventory inv, int slotIndex, int x, int y)
        {
            super(inv, slotIndex, x, y);
        }

        @Override
        public ItemStack slotClick(ContainerExtended container, EntityPlayer player, int button, int modifier)
        {
            ItemStack held = player.inventory.getItemStack();
            if(button == 0 && modifier == 1)
            {
                NEIClientUtils.cheatItem(getStack(), button, -1);
            }
            else if(button == 1)
            {
                putStack(null);
            }
            else if(held != null)
            {
                if(isItemValid(held))
                {
                    putStack(InventoryUtils.copyStack(held, 1));
                    player.inventory.setItemStack(null);
                }
            }
            else if(getHasStack())
                player.inventory.setItemStack(getStack());

            return null;
        }
        
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return stack.getItem() instanceof ItemPotion;
        }
    }
    
    public static class InventoryPotionStore extends InventoryNBT
    {
        public InventoryPotionStore()
        {
            super(9, NEIClientConfig.saveCompound.getCompoundTag("potionStore"));
        }
        
        @Override
        public void onInventoryChanged()
        {
            super.onInventoryChanged();
            NEIClientConfig.saveCompound.setCompoundTag("potionStore", tag);
            NEIClientConfig.saveConfig();
        }
    }
    
    InventoryPlayer playerInv;
    InventoryBasic potionInv;
    IInventory potionStoreInv;
    
    public ContainerPotionCreator(InventoryPlayer inventoryPlayer, IInventory potionStoreInv)
    {
        playerInv = inventoryPlayer;
        potionInv = new InventoryBasic("Potion", true, 1);
        this.potionStoreInv = potionStoreInv;
        
        addSlotToContainer(new SlotPotion(potionInv, 0, 25, 102));
        for(int i = 0; i < 9; i++)
            addSlotToContainer(new SlotPotionStore(potionStoreInv, i, 8+i*18, 14));
        bindPlayerInventory(inventoryPlayer, 8, 125);
    }
    
    @Override
    public boolean doMergeStackAreas(int slotIndex, ItemStack stack)
    {
        if(slotIndex < 10)
            return mergeItemStack(stack, 10, 46, true);
        return mergeItemStack(stack, 0, 1, false);
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
    
    @Override
    public void onCraftGuiClosed(EntityPlayer player)
    {
        super.onCraftGuiClosed(player);
        
        if(!player.worldObj.isRemote)
            InventoryUtils.dropOnClose(player, potionInv);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void handleInputPacket(PacketCustom packet)
    {
        ItemStack potion = potionInv.getStackInSlot(0);
        if(potion == null)
            return;
        
        NBTTagList potionTagList = potion.getTagCompound().getTagList("CustomPotionEffects");
        
        if(packet.readBoolean())
        {
            PotionEffect e = new PotionEffect(packet.readUnsignedByte(), packet.readInt(), packet.readUnsignedByte());
            for(int i = 0; i < potionTagList.tagCount(); i++)
            {
                PotionEffect pe = PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound) potionTagList.tagAt(i));
                if(pe.getPotionID() == e.getPotionID())
                {
                    potionTagList.tagList.set(i, e.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                    return;
                }
            }
            potionTagList.appendTag(e.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        }
        else
        {
            int rmID = packet.readUnsignedByte();
            for(int i = 0; i < potionTagList.tagCount(); )
            {
                PotionEffect pe = PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound) potionTagList.tagAt(i));
                if(pe.getPotionID() == rmID)
                {
                    potionTagList.removeTag(i);
                    continue;
                }
                i++;
            }
        }
    }
    
    public void setPotionEffect(int effectID, int duration, int amplifier)
    {
        PacketCustom packet = NEICPH.createContainerPacket();
        packet.writeBoolean(true);
        packet.writeByte(effectID);
        packet.writeInt(duration);
        packet.writeByte(amplifier);
        packet.sendToServer();
    }
    
    public void removePotionEffect(int effectID)
    {
        PacketCustom packet = NEICPH.createContainerPacket();
        packet.writeBoolean(false);
        packet.writeByte(effectID);
        packet.sendToServer();
    }
}