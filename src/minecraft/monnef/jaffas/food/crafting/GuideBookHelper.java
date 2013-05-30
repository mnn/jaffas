/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import monnef.core.Reference;
import monnef.core.utils.BookWriter;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.common.CompostRegister;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Scanner;

public class GuideBookHelper {

    public static final String PAGE_BREAK_LINE = "$$page$$";
    public static final String BOARD_RECIPES_LINE = "$$board$$";
    public static final String COMPOST_RECIPES_LINE = "$$compost$$";
    public static final String TITLE_LINE_STARTER = "$$--";

    private static String formatItemStack(ItemStack stack) {
        if (stack.stackSize == 1) {
            return String.format("%s", RegistryUtils.getTitle(stack));
        } else {
            return String.format("%dx %s", stack.stackSize, RegistryUtils.getTitle(stack));
        }
    }

    public static void generateGuideBook() {
        BookWriter bookWriter = new BookWriter(Reference.MONNEF + " & " + Reference.TIARTYOS, monnef.jaffas.food.common.Reference.ModName + " - Basic Guide");

        Scanner scanner = new Scanner(JaffasFood.instance.getClass().getResourceAsStream("/jaffas_guide.txt"), "UTF-8");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(PAGE_BREAK_LINE)) {
                bookWriter.endPage();
            } else if (line.equals(BOARD_RECIPES_LINE)) {
                int counter = 2;
                for (BoardRecipe recipe : RecipesBoard.recipes.values()) {
                    ItemStack input = recipe.getInput();
                    ItemStack output = recipe.getOutput();
                    bookWriter.addLine(String.format("%s -> %s", formatItemStack(input), formatItemStack(output)));
                    bookWriter.addBlankLine();
                    if (counter++ > 4) {
                        counter = 0;
                        bookWriter.endPage();
                    }
                }
            } else if (line.equals(COMPOST_RECIPES_LINE)) {
                List<String> list = CompostRegister.generateTextForGuide();
                int counter = 1;
                for (String item : list) {
                    bookWriter.addLine(item);
                    bookWriter.addBlankLine();
                    if (counter++ > 4) {
                        counter = 0;
                        bookWriter.endPage();
                    }
                }
            } else {
                if (line.startsWith(TITLE_LINE_STARTER)) {
                    line = String.format("§l%s§r", line.substring(TITLE_LINE_STARTER.length()));
                }
                bookWriter.addLine(line);
            }
        }

        JaffasFood.instance.guideBook = bookWriter.finish();
    }
}
