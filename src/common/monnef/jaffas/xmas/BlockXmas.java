package monnef.jaffas.xmas;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

/**
 * Created with IntelliJ IDEA.
 * User: moen
 * Date: 29/11/12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class BlockXmas extends Block {
    public BlockXmas(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    public String getTextureFile() {
        return mod_jaffas_xmas.textureFile;
    }
}
