package forestry.api.circuits;

import java.util.HashMap;

import net.minecraft.world.World;

public interface ICircuitRegistry {

	HashMap<String, ICircuit> getRegisteredCircuits();

	void registerCircuit(ICircuit circuit);

	ICircuit getCircuit(String uid);

	ICircuitLibrary getCircuitLibrary(World world, String playername);

	void registerLegacyMapping(int id, String uid);

	ICircuit getFromLegacyMap(int id);

}
