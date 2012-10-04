package jaffas.common;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class JaffaCraftingHandler implements ICraftingHandler {

    private boolean debug = true;

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item,
                           IInventory craftMatrix) {
        /*
        if (!Thread.currentThread().getName().equals("Server thread")) {
            if (debug) System.out.println("not server thread, skipping");
            return;
        }
        */

        MalletHelper recipeTest = MalletHelper.findMallet(craftMatrix);

        if (debug) System.out.println("craft from " + Thread.currentThread().getName());

        if (recipeTest != null) {
            if (debug) System.out.println("mallet detected");
            ItemStack mallet = recipeTest.getMallet();
            int position = recipeTest.getPosition();
            ItemStack newTool = new ItemStack(mallet.getItem(), 2);

            int damage;

            if (recipeTest.getIngredientsCount() == 9) {
                //got automation recipe - hurt mallet realy bad
                damage = 8;
            } else if (recipeTest.getIngredientsCount() == 2) {
                //simple recipe, minor damage
                damage = 1;
            } else {
                throw new Error("Unknown ingredients count (" + recipeTest.getIngredientsCount() + "), something is terribly broken.");
            }

            int newDamage = mallet.getItemDamage() + damage;
            if (debug)
                System.out.println("damage ~ " + damage + ", newDmg ~ " + newDamage + ", newTool.maxDmg ~ " + newTool.getMaxDamage());
            newTool.setItemDamage(newDamage);

            if (newDamage < newTool.getMaxDamage()) {
                if (debug) System.out.println("adding new mallets");
                craftMatrix.setInventorySlotContents(position, newTool);
            }
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
        // TODO Auto-generated method stub

    }

}
