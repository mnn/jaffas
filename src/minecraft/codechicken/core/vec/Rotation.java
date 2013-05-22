package codechicken.core.vec;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Rotation implements ITransformation
{
    public static ITransformation[] sideRotations = new ITransformation[]{
        new RedundantTransformation(),
        new VariableTransformation(new Matrix4(1, 0, 0, 0, 0,-1, 0, 0, 0, 0,-1, 0, 0, 0, 0, 1)){
            @Override public void transform(Vector3 vec){
                vec.y = -vec.y; vec.z = -vec.z;
            }},
        new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 0, 0, 0, 1)){
            @Override public void transform(Vector3 vec){
                double d1 = vec.y; double d2 = vec.z;
                vec.y = -d2; vec.z = d1;
            }},
        new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, 0, 1, 0, 0,-1, 0, 0, 0, 0, 0, 1)){
            @Override public void transform(Vector3 vec){
                double d1 = vec.y; double d2 = vec.z;
                vec.y = d2; vec.z = -d1;
            }},
        new VariableTransformation(new Matrix4(0, 1, 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)){
            @Override public void transform(Vector3 vec){
                double d0 = vec.x; double d1 = vec.y;
                vec.x = d1; vec.y = -d0;
            }},
        new VariableTransformation(new Matrix4(0,-1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)){
            @Override public void transform(Vector3 vec){
                double d0 = vec.x; double d1 = vec.y;
                vec.x = -d1; vec.y = d0;
            }}
        };
    public static Rotation[] sideRotationsG = new Rotation[]{
        new Rotation(1, 0, 0, 0),
        new Rotation(1, 0, 0, Math.PI),
        new Rotation(1, 0, 0, Math.PI/2),
        new Rotation(1, 0, 0,-Math.PI/2),
        new Rotation(0, 0, 1,-Math.PI/2),
        new Rotation(0, 0, 1, Math.PI/2)};
    
    public static Quat[] sideQuats = new Quat[6];
    public static ITransformation[] sideRotationsR = new ITransformation[6];
    public static Rotation[] sideRotationsGR = new Rotation[6];
    public static Quat[] sideQuatsR = new Quat[6];
    
    public static Vector3[] axes = new Vector3[]{
        new Vector3( 0,-1, 0),
        new Vector3( 0, 1, 0),
        new Vector3( 0, 0,-1),
        new Vector3( 0, 0, 1),
        new Vector3(-1, 0, 0),
        new Vector3( 1, 0, 0)};
    
    public static int[] sideSides = new int[]{
        2, 5, 3, 4, 
        3, 5, 2, 4, 
        1, 5, 0, 4, 
        0, 5, 1, 4, 
        2, 0, 3, 1, 
        2, 1, 3, 0};
    
    static
    {
        int[] rev = new int[]{0, 1, 3, 2, 5, 4};
        for(int i = 0; i < 6; i++)
            sideQuats[i] = sideRotationsG[i].toQuat();
        for(int i = 0; i < 6; i++)
        {
            int r = rev[i];
            sideRotationsR[i] = sideRotations[r];
            sideRotationsGR[i] = sideRotationsG[r];
            sideQuatsR[i] = sideQuats[r];
        }
    }
    
    public double angle;
    public Vector3 axis;
    public Vector3 point;
    
    private Vector3 negate;
    private Quat quat;
    
    public Rotation(double angle, Vector3 axis, Vector3 point)
    {
        this.angle = angle;
        this.axis = axis;
        this.point = point;
    }
    
    public Rotation(double angle, Vector3 axis)
    {
        this(angle, axis, null);
    }
    
    public Rotation(double x, double y, double z, double angle)
    {
        this(angle, new Vector3(x, y, z));
    }

    @Override
    public void transform(Vector3 vec)
    {
        if(quat == null)
            quat = Quat.aroundAxis(axis, angle);
        
        if(point == null)
            vec.rotate(quat);
        else
            vec.subtract(point).rotate(quat).add(point);
    }

    @Override
    public void apply(Matrix4 mat)
    {
        if(point == null)
            mat.rotate(angle, axis);
        else
        {
            if(negate == null)
                negate = point.copy().negate();
            
            mat.translate(point);
            mat.rotate(angle, axis);
            mat.translate(negate);
        }
    }
    
    public Quat toQuat()
    {
        if(quat == null)
            quat = Quat.aroundAxis(axis, angle);
        return quat;
    }

    @SideOnly(Side.CLIENT)
    public void glRotate()
    {
        if(point == null)
            GL11.glRotatef((float)angle*57.2958F, (float)axis.x, (float)axis.y, (float)axis.z);
        else
        {
            GL11.glTranslated(point.x, point.y, point.z);
            GL11.glRotatef((float)angle*57.2958F, (float)axis.x, (float)axis.y, (float)axis.z);
            GL11.glTranslated(-point.x, -point.y, -point.z);
        }
    }
}
