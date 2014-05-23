/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.item.common.Items;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.HashMap;

import static monnef.jaffas.food.item.JaffaItem.jaffa;

public class JaffasHelper {
    private static final HashMap<JaffaItem, String> titles;
    private static final JaffaItem[] jaffas;

    static {
        titles = new HashMap<JaffaItem, String>();
        titles.put(JaffaItem.jaffa, "Without filling");
        titles.put(JaffaItem.jaffaL, "Lemon");
        titles.put(JaffaItem.jaffaO, "Orange");
        titles.put(JaffaItem.jaffaR, "Apple");
        titles.put(JaffaItem.jaffaP, "Plum");
        titles.put(JaffaItem.jaffaRaspberry, "Raspberry");
        titles.put(JaffaItem.jaffaStrawberry, "Strawberry");
        titles.put(JaffaItem.jaffaV, "Vanilla");

        jaffas = titles.keySet().toArray(new JaffaItem[]{});
    }

    public static String getTitle(JaffaItem jaffa) {
        return titles.get(jaffa);
    }

    public static Collection<JaffaItem> getJaffas() {
        return titles.keySet();
    }

    public static JaffaItem getRandomJaffa() {
        return jaffas[JaffasFood.rand.nextInt(jaffas.length)];
    }

    public static boolean isFilled(JaffaItem ji) {
        return ji != jaffa;
    }

    public static String getJaffaTitleForItem(JaffaItem ji) {
        String cakeTitle = ConfigurationManager.jaffaTitle;

        if (!isFilled(ji)) {
            return cakeTitle;
        }
        StringBuilder s = new StringBuilder(getTitle(ji));
        s.append(" ");
        s.append(cakeTitle);
        return s.toString();
    }

    public static void registerJaffasInOreDict() {
        for (JaffaItem ji : titles.keySet()) {
            Item item = JaffasFood.getItem(ji);
            if (isFilled(ji)) {
                OreDictionary.registerOre(Items.JAFFA_FILLED(), item);
            }
            OreDictionary.registerOre(Items.JAFFA(), item);
        }
    }
}
