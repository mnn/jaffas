/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import java.util.Collection;
import java.util.HashMap;

public class JaffasHelper {
    private static final HashMap<JaffaItem, String> titles;

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
    }

    public static String getTitle(JaffaItem jaffa) {
        return titles.get(jaffa);
    }

    public static Collection<JaffaItem> getJaffas() {
        return titles.keySet();
    }
}
