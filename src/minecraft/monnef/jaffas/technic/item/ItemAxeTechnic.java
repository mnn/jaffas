package monnef.jaffas.technic.item;

import monnef.core.base.CustomIconHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;

public class ItemAxeTechnic extends ItemAxe {
    private final int textureOffset;

    public ItemAxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        this.textureOffset = textureOffset;
        setCreativeTab(JaffasTechnic.CreativeTab);
    }

    @Override
    public void updateIcons(IconRegister par1IconRegister) {
        this.iconIndex = par1IconRegister.registerIcon(CustomIconHelper.generateId(Reference.ModName, 3, textureOffset));
    }
}

