package codechicken.core.packet;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import codechicken.core.data.MCDataInput;
import codechicken.core.data.MCDataOutput;
import codechicken.core.vec.BlockCoord;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.NetworkModHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.liquids.LiquidStack;

public final class PacketCustom implements MCDataInput, MCDataOutput
{
    public static interface ICustomPacketHandler 
    {
    }
    
    public interface IClientPacketHandler extends ICustomPacketHandler
    {
        public void handlePacket(PacketCustom packetCustom, NetClientHandler nethandler, Minecraft mc);
    }
    
    public interface IServerPacketHandler extends ICustomPacketHandler
    {
        public void handlePacket(PacketCustom packetCustom, NetServerHandler nethandler, EntityPlayerMP sender);
    }
    
    private static abstract class CustomPacketHandler implements IPacketHandler
    {
        HashMap<Integer, ICustomPacketHandler> handlermap = new HashMap<Integer, ICustomPacketHandler>();
        
        public CustomPacketHandler(String channel) 
        {
            NetworkRegistry.instance().registerChannel(this, channel, getSide());
        }

        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
        {
            PacketCustom packetCustom = new PacketCustom(packet);
            ICustomPacketHandler handler = handlermap.get(packetCustom.type);
            if(handler != null)
                handle(handler, packetCustom, player);
        }

        public void registerRange(int firstID, int lastID, ICustomPacketHandler handler) 
        {
            for(int i = firstID; i <= lastID; i++)
                handlermap.put(i, handler);
        }
        
        public abstract Side getSide();
        public abstract void handle(ICustomPacketHandler handler, PacketCustom packet, Player player);
    }
    
    private static class ClientPacketHandler extends CustomPacketHandler
    {
        public ClientPacketHandler(String channel) 
        {
            super(channel);
        }

        @Override
        public Side getSide() 
        {
            return Side.CLIENT;
        }

        @Override
        public void handle(ICustomPacketHandler handler, PacketCustom packet, Player player) 
        {
            ((IClientPacketHandler)handler).handlePacket(packet, Minecraft.getMinecraft().getNetHandler(), Minecraft.getMinecraft());
        }
    }
    
    private static class ServerPacketHandler extends CustomPacketHandler
    {
        public ServerPacketHandler(String channel) 
        {
            super(channel);
        }

        @Override
        public Side getSide() 
        {
            return Side.SERVER;
        }

        @Override
        public void handle(ICustomPacketHandler handler, PacketCustom packet, Player player) 
        {
            ((IServerPacketHandler)handler).handlePacket(packet, ((EntityPlayerMP)player).playerNetServerHandler, (EntityPlayerMP)player);
        }
    }
    
    private static class ServerTinyPacketHandler
    {
        IServerPacketHandler serverHandler;
        
        public ServerTinyPacketHandler(IServerPacketHandler handler)
        {
            serverHandler = handler;
        }

        public void handle(PacketCustom packetCustom, NetHandler handler)
        {
            serverHandler.handlePacket(packetCustom, (NetServerHandler)handler, ((NetServerHandler)handler).playerEntity);
        }
    }
    
    private static class ClientTinyPacketHandler
    {
        IClientPacketHandler clientHandler;
        
        public ClientTinyPacketHandler(IClientPacketHandler handler)
        {
            clientHandler = handler;
        }

        public void handle(PacketCustom packetCustom, NetHandler handler)
        {
            clientHandler.handlePacket(packetCustom, (NetClientHandler)handler, Minecraft.getMinecraft());
        }
    }
    
    public static final class CustomTinyPacketHandler implements ITinyPacketHandler
    {        
        private ClientTinyPacketHandler clientDelegate;
        private ServerTinyPacketHandler serverDelegate;
        
        @Override
        public void handle(NetHandler handler, Packet131MapData packet)
        {
            PacketCustom packetCustom = new PacketCustom(packet);
            if(handler instanceof NetServerHandler)
                serverDelegate.handle(packetCustom, handler);
            else
                clientDelegate.handle(packetCustom, handler);
        }

        private void registerSidedHandler(ICustomPacketHandler handler)
        {
            if(handler instanceof IClientPacketHandler)
            {
                if(clientDelegate != null)
                    throw new IllegalStateException("Client handler already registered");
                
                clientDelegate = new ClientTinyPacketHandler((IClientPacketHandler) handler);
            }
            else if(handler instanceof IServerPacketHandler)
            {
                if(serverDelegate != null)
                    throw new IllegalStateException("Server handler already registered");
                
                serverDelegate = new ServerTinyPacketHandler((IServerPacketHandler) handler);
            }
            else
            {
                throw new IllegalStateException("Handler is not a client or server handler");
            }
        }
    }
    
    private PacketCustom(Packet250CustomPayload packet)
    {
        incoming = true;
        channel = packet.channel;
        
        type = packet.data[0];
        datain = new DataInputStream(new ByteArrayInputStream(
            decompress(Arrays.copyOfRange(packet.data, 1, packet.data.length))));
    }

    private PacketCustom(Packet131MapData packet)
    {
        incoming = true;
        channel = packet.itemID;

        type = packet.uniqueID;
        datain = new DataInputStream(new ByteArrayInputStream(
            decompress(packet.itemData)));
    }
    
    public PacketCustom(Object channel, int type)
    {
        if(type <= 0 || type >= 0x80)
            throw new IllegalArgumentException("Packet type: "+type+" is not within required 0 < t < 0x80");
        
        this.channel = channel;
        this.type = type;
        incoming = false;
        isChunkDataPacket = false;
        
        dataarrayout = new ByteArrayOutputStream();
        dataout = new DataOutputStream(dataarrayout);
    }

    public int getType()
    {
        return type&0x7F;
    }
    
    public PacketCustom setChunkDataPacket()
    {
        isChunkDataPacket = true;
        return this;
    }
    
    private byte[] decompress(byte[] cdata)
    {
        if((type&0x80) == 0)
            return cdata;
        
        Inflater inflater = new Inflater();
        try
        {
            byte[] ddata = new byte[cdata[0]&0xFF|(cdata[1]&0xFF)<<8|(cdata[2]&0xFF)<<16|(cdata[3]&0xFF)<<24];
            inflater.setInput(cdata, 4, cdata.length-4);
            inflater.inflate(ddata);
            return ddata;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            inflater.end();
        }
    }
    
    public PacketCustom compressed()
    {
        if((type&0x80) != 0)
            throw new IllegalStateException("Packet already compressed");
        type|=0x80;
        return this;
    }
    
    private byte[] compress(boolean withType, byte[] data)
    {
        int clen = 0;
        byte[] cdata = null;
        byte[] header = null;
        if((type&0x80) != 0)
        {
            Deflater deflater = new Deflater();
            try
            {
                deflater.setInput(data, 0, data.length);
                deflater.finish();
                cdata = new byte[data.length];
                clen = deflater.deflate(cdata);
                if(clen == data.length || !deflater.finished())//not worth compressing, gets larger
                {
                    type &= 0x7F;
                }
                else
                {
                    header = new byte[]{
                        (byte) (data.length&0xFF), 
                        (byte) (data.length>>8&0xFF), 
                        (byte) (data.length>>16&0xFF), 
                        (byte) (data.length>>>24)};
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                deflater.end();
            }
        }
        
        if((type&0x80) == 0)
        {
            clen = data.length;
            cdata = data;
            header = new byte[0];
        }
        
        byte[] packed = new byte[clen+header.length+(withType?1:0)];
        int i = 0;
        if(withType)
        {
            packed[0] = (byte) type;
            i++;
        }
        if(header.length > 0)
        {
            System.arraycopy(header, 0, packed, i, header.length);
            i+=header.length;
        }
        System.arraycopy(cdata, 0, packed, i, clen);
        return packed;
    }
    
    public Packet toPacket()
    {
        if(incoming)
            throw new IllegalStateException("Tried to write an incoming packet");
        
        try
        {
            dataout.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        if(channel instanceof String)
        {        
            Packet250CustomPayload payload = new Packet250CustomPayload();
            payload.channel = (String) channel;
            payload.isChunkDataPacket = isChunkDataPacket;
            payload.data = compress(true, dataarrayout.toByteArray());
            payload.length = payload.data.length;            
            return payload;
        }
        
        NetworkModHandler nmh = FMLNetworkHandler.instance().findNetworkModHandler(channel);
        if(nmh == null)
            throw new IllegalStateException("Invalid mod object for channel: "+channel);
        
        byte[] data = compress(false, dataarrayout.toByteArray());
        Packet131MapData payload = new Packet131MapData((short) nmh.getNetworkId(), (short) type, data);
        payload.isChunkDataPacket = isChunkDataPacket;
        
        return payload;
    }
        
    public PacketCustom writeBoolean(boolean b)
    {
        try
        {
            dataout.writeBoolean(b);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeByte(int b)
    {
        try
        {
            dataout.writeByte(b);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeShort(int s)
    {
        try
        {
            dataout.writeShort(s);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeInt(int i)
    {
        try
        {
            dataout.writeInt(i);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeFloat(float f)
    {
        try
        {
            dataout.writeFloat(f);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeDouble(double d)
    {
        try
        {
            dataout.writeDouble(d);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeLong(long l)
    {
        try
        {
            dataout.writeLong(l);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
        
    @Override
    public PacketCustom writeChar(char c)
    {
        try
        {
            dataout.writeChar(c);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeByteArray(byte[] barray)
    {
        try
        {
            dataout.write(barray);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeCoord(int x, int y, int z)
    {
        writeInt(x);
        writeInt(y);
        writeInt(z);
        return this;
    }
    
    public PacketCustom writeCoord(BlockCoord coord)
    {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
        return this;
    }
    
    public PacketCustom writeString(String s)
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
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeItemStack(ItemStack stack)
    {
        writeItemStack(stack, false);
        return this;
    }
    
    public PacketCustom writeItemStack(ItemStack stack, boolean large)
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
        
    public PacketCustom writeNBTTagCompound(NBTTagCompound compound)
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
            throw new RuntimeException(e);
        }
        return this;
    }

    public PacketCustom writeLiquidStack(LiquidStack liquid)
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

    public boolean readBoolean()
    {
        try
        {
            return datain.readBoolean();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public int readUnsignedByte()
    {
        return readByte() & 0xFF;
    }
    
    public int readUnsignedShort()
    {
        return readShort() & 0xFFFF;
    }
    
    public byte readByte()
    {
        try
        {
            return datain.readByte();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public short readShort()
    {
        try
        {
            return datain.readShort();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public int readInt()
    {
        try
        {
            return datain.readInt();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public float readFloat()
    {
        try
        {
            return datain.readFloat();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public double readDouble()
    {
        try
        {
            return datain.readDouble();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public long readLong()
    {
        try
        {
            return datain.readLong();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public char readChar()
    {
        try
        {
            return datain.readChar();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public BlockCoord readCoord()
    {
        return new BlockCoord(readInt(), readInt(), readInt());
    }
    
    public byte[] readByteArray(int length)
    {
        try
        {
            byte[] barray = new byte[length];
            datain.readFully(barray, 0, length);
            return barray;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String readString()
    {
        try
        {
            int length = datain.readUnsignedShort();
            char[] chars = new char[length];
            for(int i = 0; i < length; i++)
            {
                chars[i] = readChar();
            }
            return new String(chars);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        
    }

    public ItemStack readItemStack()
    {
        return readItemStack(false);
    }
    
    public ItemStack readItemStack(boolean large)
    {
        ItemStack var2 = null;
        short itemID = readShort();

        if (itemID >= 0)
        {
            int stackSize = large ? readInt() : readByte();
            short damage = readShort();
            var2 = new ItemStack(itemID, stackSize, damage);
            var2.stackTagCompound = readNBTTagCompound();
        }

        return var2;
    }
    
    public NBTTagCompound readNBTTagCompound()
    {
        try
        {
            short var2 = readShort();
    
            if (var2 < 0)
                return null;
            
            byte[] var3 = readByteArray(var2);
            return CompressedStreamTools.decompress(var3);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
        
    public LiquidStack readLiquidStack()
    {
        LiquidStack var2 = null;
        short liquidID = readShort();

        if (liquidID >= 0)
            var2 = new LiquidStack(liquidID, readInt(), readUnsignedShort());

        return var2;
    }

    public boolean more()
    {
        try
        {
            return datain.available() > 0;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private Object channel;
    private int type;
    private boolean isChunkDataPacket;
    
    private boolean incoming;
    
    private ByteArrayOutputStream dataarrayout;
    private DataOutputStream dataout;
    
    private DataInputStream datain;
    
    private static HashMap<String, CustomPacketHandler> clienthandlermap = new HashMap<String, CustomPacketHandler>();    
    private static HashMap<String, CustomPacketHandler> serverhandlermap = new HashMap<String, CustomPacketHandler>();
    
    public static void assignHandler(String channel, int firstID, int lastID, ICustomPacketHandler IHandler)
    {
        Side side = IHandler instanceof IClientPacketHandler ? Side.CLIENT : Side.SERVER;
        HashMap<String, CustomPacketHandler> handlerMap = side.isClient() ? clienthandlermap : serverhandlermap;
        CustomPacketHandler handler = handlerMap.get(channel);
            
        if(handler == null)
        {
            if(side.isClient())
                handler = new ClientPacketHandler(channel);
            else
                handler = new ServerPacketHandler(channel);
            
            handlerMap.put(channel, handler);
        }
        handler.registerRange(firstID, lastID, IHandler);
    }

    public static void assignHandler(Object mod, ICustomPacketHandler handler)
    {
        NetworkModHandler nmh = FMLNetworkHandler.instance().findNetworkModHandler(mod);
        if(nmh == null || nmh.getTinyPacketHandler() == null || !(nmh.getTinyPacketHandler() instanceof CustomTinyPacketHandler))
            throw new IllegalStateException("Invalid network tiny packet handler for mod: "+mod);
        
        ((CustomTinyPacketHandler)nmh.getTinyPacketHandler()).registerSidedHandler(handler);
    }
    
    public void sendToPlayer(EntityPlayer player)
    {
        sendToPlayer(toPacket(), player);
    }
    
    public static void sendToPlayer(Packet packet, EntityPlayer player)
    {
        if(player == null)
            sendToClients(packet);
        else
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
    }
    
    public void sentToClients()
    {
        sendToClients(toPacket());
    }    
    
    public static void sendToClients(Packet packet)
    {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
    }
    
    public void sendPacketToAllAround(double x, double y, double z, double range, int dim)
    {
        sendToAllAround(toPacket(), x, y, z, range, dim);
    }
    
    public static void sendToAllAround(Packet packet, double x, double y, double z, double range, int dim)
    {
        MinecraftServer.getServer().getConfigurationManager().sendToAllNear(x, y, z, range, dim, packet);
    }
    
    public void sendToDimension(int dim)
    {
        sendToDimension(toPacket(), dim);
    }
    
    public static void sendToDimension(Packet packet, int dim)
    {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dim);
    }

    public void sendToChunk(World world, int chunkX, int chunkZ)
    {
        sendToChunk(toPacket(), world, chunkX, chunkZ);
    }
    
    public static void sendToChunk(Packet packet, World world, int chunkX, int chunkZ)
    {
        PlayerInstance p = ((WorldServer)world).getPlayerManager().getOrCreateChunkWatcher(chunkX, chunkZ, false);
        if(p != null)
            p.sendToAllPlayersWatchingChunk(packet);
    }
    
    public void sendToOps()
    {
        sendToOps(toPacket());
    }

    public static void sendToOps(Packet packet)
    {
        for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            if(MinecraftServer.getServer().getConfigurationManager().areCommandsAllowed(player.username))
                sendToPlayer(packet, player);
    }
    
    @SideOnly(Side.CLIENT)
    public void sendToServer()
    {
        sendToServer(toPacket());
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(Packet packet)
    {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }
}
