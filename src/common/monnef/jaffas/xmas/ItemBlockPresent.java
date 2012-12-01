package monnef.jaffas.xmas;

public class ItemBlockPresent extends ItemBlockXmasMulti {
    public ItemBlockPresent(int id) {
        super(id);
    }

    @Override
    protected String[] getSubNames() {
        return new String[]{"white", "blue", "yellow", "black", "magenta"};
    }

    @Override
    protected String[] getSubTitles() {
        return new String[]{"White Present", "Blue Present", "Yellow Present", "Black Present", "Magenta Present"};
    }

    @Override
    protected BlockXmasMulti getParentBlock() {
        return mod_jaffas_xmas.BlockPresent;
    }
}
