package forestry.api.apiculture;

import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBeeHousing extends IBeeModifier {

	int getXCoord();

	int getYCoord();

	int getZCoord();

	ItemStack getQueen();

	ItemStack getDrone();

	void setQueen(ItemStack itemstack);

	void setDrone(ItemStack itemstack);

	int getBiomeId();

	EnumTemperature getTemperature();

	EnumHumidity getHumidity();

	World getWorld();

	/**
	 * @return String containing the login of this housing's owner.
	 */
	String getOwnerName();

	void setErrorState(int state);

	int getErrorOrdinal();

	/**
	 * @return true if princesses and drones can (currently) mate in this housing to generate queens.
	 */
	boolean canBreed();

	/**
	 * Called by IBeekeepingLogic to add products to the housing's inventory.
	 * 
	 * @param product
	 *            ItemStack with the product to add.
	 * @param all
	 * @return Boolean indicating success or failure.
	 */
	boolean addProduct(ItemStack product, boolean all);

	/**
	 * Called when the bees wear out the housing's equipment.
	 * 
	 * @param amount
	 *            Integer indicating the amount worn out.
	 */
	void wearOutEquipment(int amount);

	/**
	 * Called on queen update.
	 * 
	 * @param queen
	 */
	void onQueenChange(ItemStack queen);

	/**
	 * @return Boolean indicating if housing can ignore rain
	 */
	boolean isSealed();

	/**
	 * @return Boolean indicating if housing can ignore darkness/night
	 */
	boolean isSelfLighted();

	/**
	 * @return Boolean indicating if housing can ignore not seeing the sky
	 */
	boolean isSunlightSimulated();

	/**
	 * @return Boolean indicating whether this housing simulates the nether
	 */
	boolean isHellish();
}
