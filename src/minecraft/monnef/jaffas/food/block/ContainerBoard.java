/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

public class ContainerBoard extends ContainerJaffas {
    protected TileEntityBoard board;
    private int lastChopTime;

    public ContainerBoard(InventoryPlayer inventoryPlayer, TileEntityBoard te) {
        super(inventoryPlayer, te);
        board = te;

        addSlotToContainer(new Slot(board, 0, 56, 35)); //  input
        addSlotToContainer(new Slot(board, 1, 116, 35)); // output
        addSlotToContainer(new Slot(board, 2, 22, 35)); //  knife
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.board.chopTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.lastChopTime != this.board.chopTime) {
                var2.sendProgressBarUpdate(this, 0, this.board.chopTime);
            }
        }

        this.lastChopTime = this.board.chopTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.board.chopTime = par2;
        }
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
        board.checkKnife();
    }

    @Override
    protected int getSlotsCount() {
        return 3;
    }
}