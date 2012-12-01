package monnef.jaffas.xmas;

public class ItemBlockPresent extends ItemBlockXmasMulti {
    public ItemBlockPresent(int id) {
        super(id);

        subNames = new String[]{"white", "blue", "yellow", "black", "magenta"};
        subTitles = new String[]{"White Present", "Blue Present", "Yellow Present", "Black Present", "Magenta Present"};

        registerNames(mod_jaffas_xmas.BlockPresent);
    }
}
