package codechicken.core.lighting;

import codechicken.core.render.CCModel;
import codechicken.core.render.Vertex5;
import net.minecraft.client.renderer.Tessellator;

public class CCRBModel extends CCModel
{
    public LC[] lightCoefficents;
    private LightMatrix lightMatrix;
    
    /**
     * Lighting sides and coefficients are computed for each vertex of the model. All faces must be axis planar and all verts are assumed to be in the range (0,0,0) to (1,1,1)
     */
    public CCRBModel(CCModel m)
    {
        super(m.vertexMode);
        verts = new Vertex5[m.verts.length];
        copy(m, 0, this, 0, m.verts.length);
        if(normals == null)
            computeNormals();
        if(colours == null)
            setColour(-1);
        computeLighting();
    }

    private void computeLighting()
    {
        lightCoefficents = new LC[verts.length];
        for(int k = 0; k < verts.length; k++)
            lightCoefficents[k] = LC.compute(verts[k].vec, normals[k]);
    }
    
    public CCRBModel setLightMatrix(LightMatrix m)
    {
        lightMatrix = m;
        return this;
    }
    
    @Override
    public void applyColour(Tessellator tess, int i)
    {
        if(lightMatrix == null)
             super.applyColour(tess, i);
        else
            lightMatrix.setColour(tess, lightCoefficents[i], colours[i]);
    }
    
    @Override
    public void applyVertexModifiers(Tessellator tess, int i)
    {
        if(lightMatrix != null)
            lightMatrix.setBrightness(tess, lightCoefficents[i]);
    }
}
