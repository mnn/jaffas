package codechicken.core.vec;

public abstract class VariableTransformation implements ITransformation
{
    public Matrix4 mat;
    
    public VariableTransformation(Matrix4 mat)
    {
        this.mat = mat;
    }

    @Override
    public void apply(Matrix4 mat)
    {
        mat.multiply(this.mat);
    }
}