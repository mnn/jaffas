package forestry.api.circuits;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

public interface ICircuit {
	String getUID();

	boolean requiresDiscovery();

	int getLimit();

	String getName();

	boolean isCircuitable(TileEntity tile);

	void onInsertion(TileEntity tile);

	void onLoad(TileEntity tile);

	void onRemoval(TileEntity tile);

	void onTick(TileEntity tile);

	void addTooltip(List<String> list);
}
