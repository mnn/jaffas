/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.JaffasFood;

import java.util.Collection;
import java.util.HashMap;

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
}
