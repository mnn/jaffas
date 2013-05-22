package codechicken.core.render;

import net.minecraft.util.Icon;

/**
 * Icon index is specified as (int)u>>1
 */
public class MultiIconTransformation implements IUVTransformation
{
    public Icon[] icons;
    
    public MultiIconTransformation(Icon[] icons)
    {
        this.icons = icons;
    }
    
    @Override
    public void transform(UV texcoord)
    {
        int i = (int)texcoord.u>>1;
        Icon icon = icons[i%icons.length];
        texcoord.u = icon.getInterpolatedU(texcoord.u%2*16);
        texcoord.v = icon.getInterpolatedV(texcoord.v%2*16);
    }
}
