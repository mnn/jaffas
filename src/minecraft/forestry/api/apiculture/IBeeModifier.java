package forestry.api.apiculture;

public interface IBeeModifier {

	/**
	 * 
	 * @param genome
	 * @return Float used to modify the base territory.
	 */
	float getTerritoryModifier(IBeeGenome genome);

	/**
	 * @param genome
	 * @param mate
	 * @return Float used to modify the base mutation chance.
	 */
	float getMutationModifier(IBeeGenome genome, IBeeGenome mate);

	/**
	 * @param genome
	 * @param mate
	 * @return Float used to modify the life span of queens.
	 */
	float getLifespanModifier(IBeeGenome genome, IBeeGenome mate);

	/**
	 * @param genome
	 * @param mate
	 * @return Fload modifying the production speed of queens.
	 */
	float getProductionModifier(IBeeGenome genome);

}
