package codechicken.core.data;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.LiquidStack;
import codechicken.core.vec.BlockCoord;
import cpw.mods.fml.common.FMLCommonHandler;

public class MCOutputStreamWrapper implements MCDataOutput
{
    public DataOutputStream dataout;
    
    public MCOutputStreamWrapper(DataOutputStream out)
    {
        dataout = out;
    }
    
    public MCOutputStreamWrapper writeBoolean(boolean b)
    {
        try
        {
            dataout.writeBoolean(b);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeByte(int b)
    {
        try
        {
            dataout.writeByte(b);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeShort(int s)
    {
        try
        {
            dataout.writeShort(s);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeInt(int i)
    {
        try
        {
            dataout.writeInt(i);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeFloat(float f)
    {
        try
        {
            dataout.writeFloat(f);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeDouble(double d)
    {
        try
        {
            dataout.writeDouble(d);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeLong(long l)
    {
        try
        {
            dataout.writeLong(l);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
        
    @Override
    public MCOutputStreamWrapper writeChar(char c)
    {
        try
        {
            dataout.writeChar(c);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeByteArray(byte[] barray)
    {
        try
        {
            dataout.write(barray);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeCoord(int x, int y, int z)
    {
        writeInt(x);
        writeInt(y);
        writeInt(z);
        return this;
    }
    
    public MCOutputStreamWrapper writeCoord(BlockCoord coord)
    {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
        return this;
    }
    
    public MCOutputStreamWrapper writeString(String s)
    {
        try
        {
            if(s.length() > 65535)
                throw new IOException("String length: "+s.length()+"too long.");
            dataout.writeShort(s.length());
            dataout.writeChars(s);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeItemStack(ItemStack stack)
    {
        writeItemStack(stack, false);
        return this;
    }
    
    public MCOutputStreamWrapper writeItemStack(ItemStack stack, boolean large)
    {
        if (stack == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(stack.itemID);
            if(large)
                writeInt(stack.stackSize);
            else
                writeByte(stack.stackSize);
            writeShort(stack.getItemDamage());
            writeNBTTagCompound(stack.stackTagCompound);
        }
        return this;
    }
        
    public MCOutputStreamWrapper writeNBTTagCompound(NBTTagCompound compound)
    {
        try
        {            
            if (compound == null)
            {
                writeShort(-1);
            }
            else
            {
                byte[] var3 = CompressedStreamTools.compress(compound);
                writeShort((short)var3.length);
                writeByteArray(var3);
            }
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "MCDataOutput", true);
        }
        return this;
    }

    public MCOutputStreamWrapper writeLiquidStack(LiquidStack liquid)
    {
        if (liquid == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(liquid.itemID);
            writeInt(liquid.amount);
            writeShort(liquid.itemMeta);
        }
        return this;
    }
}
