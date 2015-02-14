package optimizer;

public class CrucibleOptimizer extends BasicOptimizer{
	protected int[] materialIngots;

	///TODO: if you want to variate procentages, the minimum procentage gain/loss per iteration
	// can be UNIT)IMPOSSIBLE  / units Total. 
	public CrucibleOptimizer(Alloy alloy, int[] oreMaterials, int[] ingotMaterials, int ingots){
		super(alloy, oreMaterials, ingots);
		materialIngots = ingotMaterials;
	}
	public CrucibleOptimizer(){
		super();
		this.materialIngots = null;
	}
	public int[][] optimize(int[] preferredRatios){
		double[] ratios = new double[preferredRatios.length];
		for (int i = 0; i < preferredRatios.length; i++){
			ratios[i] = preferredRatios[i] / 100.0;
		}
		int[][] mat = sumMaterialInstancesOfSameTypeAndCatagory();
		int[][] res = new int[mat.length][mat[0].length];
		for (int i = 0; i < mat.length; i++){
			res[i] = optimizeMaterial(mat[i], ratios[i], i);
		}
		for (int i = 0; i < res.length; i++){
			if(res[i] == null){
				System.out.println("No solution for " + target.getMaterials()[i].getName());
			} else {
				System.out.println(target.getMaterials()[i].getName() + ": " + res[i][0] + " " + res[i][1] + " " + res[i][2] + " " + res[i][3] + " " + res[i][4] + " ");
			}
		}
		return res;
	}
	public CrucibleOptimizer set(Alloy alloy, int[] oreMaterials, int[] ingotMaterials, int ingots){
		super.set(alloy, oreMaterials, ingots);
		materialIngots = ingotMaterials;
		
		return this;
	}
	/**
	 * 
	 * @param material First 4 spaces are ores, the last one is ingot. This material is available to make the alloy
	 * @param ratio ratio of the material in the alloy
	 * @return Returns a unique combination of ore types that consists only of available material and has no more
	 * metal units than needed to create the alloy. Returns null if solution hasn't been found.
	 */
	public int[] optimizeMaterial(int[] material, double ratio, int matIndex){
		int[] resultOre = new int[4];
		int resultIngot = 0;
		double amountOfNeededOreType;
		int[] subtractor;
		int unitsNeededOreInitial = (int) (unitsTotal * ratio);
		//if we are trying to find optimal combination for 5 units, abort right away
		if (unitsNeededOreInitial == Reference.UNITS_IMPOSSIBLE){
			this.fail[matIndex] = OptimizationFailure.IMPOSSIBLE_UNIT;
			return null;
		}
		int unitsNeededOre;
		//if there isn't enough material to even reach the maximum, abort. Optimization failed
		if (unitsNeededOreInitial > getSummedUnits(material)){
			this.fail[matIndex] = OptimizationFailure.UNSUFFICIENT_RESOURCES;
			return null;
		}
		//if we have less ore than needed for final product, but we have enough when counting in the ingots, add minimal amount of ingots and
		//proceed with ore optimization for amount that can be reached with ores
		if (unitsNeededOreInitial > getSummedOreUnits(material)){
			resultIngot = (int) Math.ceil((unitsNeededOreInitial - getSummedOreUnits(material)) / (double) Reference.UNITS_INGOT);
		}
		
		for (int k = resultIngot; k <= material[material.length - 1]; k++){
			resultOre = new int[Reference.ORE_TYPES_AMOUNT];
			amountOfNeededOreType = 0;
			subtractor = new int[Reference.ORE_TYPES_AMOUNT];
			//since some amount is taken care of by the ingots, we only have to do the rest of it by ore.
			unitsNeededOre = unitsNeededOreInitial - resultIngot * Reference.UNITS_INGOT;
			
			if (unitsNeededOre == 0){
				this.fail[matIndex] = OptimizationFailure.OPTIMIZATION_SUCCESSFUL;
				return getPackedResult(resultOre, resultIngot);
			}
			//try to find the optimal combination using just Ore
			//skip if there isn't any ore. The solution has not been found
			if (!isOreMaterialEmpty(material)){
				for (int i = 0; i < Reference.ORE_TYPES_AMOUNT; i++){
					//if current ore Type (e.g. Rich) is absent, skip
					//this cannot be used for the last ore type because it would jump out of the loop
					if ((material[i] == 0) && (i != Reference.ORE_TYPES_AMOUNT - 1)){
						continue;
					}
					//get how many whole pieces of current ore type can be used
					amountOfNeededOreType = Math.floor((double) unitsNeededOre / Reference.ORE_UNITS[i]);
					//but don't use more than we have
					if (amountOfNeededOreType > material[i])
						amountOfNeededOreType = material[i];

					amountOfNeededOreType -= subtractor[i];

					//save result
					resultOre[i] = (int) amountOfNeededOreType;
					//get the amount of remaining  needed ore
					unitsNeededOre -= amountOfNeededOreType * Reference.ORE_UNITS[i];
					//check if the remaining needed ore is impossible to achieve or we have used all there is and haven't reached solution
					if ((i == Reference.ORE_TYPES_AMOUNT - 1 && unitsNeededOre != 0) || (unitsNeededOre == Reference.UNITS_IMPOSSIBLE)){
						//this mechanism will make it so that next try, it will use one less of the biggest ore type, making space
						//for the smaller ones and possibly solving the problem. This ONLY works if we don't use all the material already.
						boolean done = true;
						//there is no point in using this method on the last ore type because at that point the result is only decreased.
						for (int j = 0; j < Reference.ORE_TYPES_AMOUNT; j++){
							//we skip if there is no instances of current ore type
							if (material[j] != 0){
								//if increasing the subtractor is pointless due to several conditions abort. Optimization failed.
								if (isFurtherSubtractionPointless(material, resultOre, unitsNeededOreInitial - resultIngot * Reference.UNITS_INGOT, j))
									break;
								//if the subtractor can sill be increased, meaning that there is still a change to be done to
								//possibly get the solution, increase the subtractor
								if (resultOre[j] > 0){
									//the done flag is used set to false meaning that there is still a permutation that hasn't been tried
									//that the subtractors have changed
									done = false;
									subtractor[j]++;
									break;
								}
							}
						}
						//if we are done it means that all the possibilities have been tried, and no solution has been found
						if (done){
							break;
						}
						//if we aren't done, reset the result from the previous try that is invalid
						//and reinitialize the loop for new try
						resultOre = new int[Reference.ORE_TYPES_AMOUNT];
						amountOfNeededOreType = 0;
						unitsNeededOre = unitsNeededOreInitial - resultIngot * Reference.UNITS_INGOT;
						i = -1;
						continue;
					}
					//if we need no more ore, the optimization is done, return the result
					//it can happen that this condition is met for the first ore type. Further computation is redundant
					if (unitsNeededOre == 0){
						int[] res = getPackedResult(resultOre, resultIngot);
						if (unitsNeededOreInitial != getSummedUnits(res)){
							this.fail[i] = OptimizationFailure.FALSE_POSITIVE; //if we give false positive, abort immediately
							return null;
						} else {
							this.fail[matIndex] = OptimizationFailure.OPTIMIZATION_SUCCESSFUL;
							return getPackedResult(resultOre, resultIngot);
						}
					} else {
						continue;
					}
				}
			}
			//if an ingot is too much to fix the result, nothing can be done. Optimization failed
			if (unitsNeededOreInitial < Reference.UNITS_INGOT){
				this.fail[matIndex] = OptimizationFailure.UNSUFFICIENT_VARIATION;
				return null;
			}
			//when we break out of the ore optimization loop it means that we failed. Add one ingot to the solution and
			//optimize ore for initial amount - the amount taken care of by the ingots
			resultIngot++;
		}
		//if we are here, it means that the solution can't be found
		resultIngot = 0;
		resultOre = null;
		this.fail[matIndex] = OptimizationFailure.UNSUFFICIENT_VARIATION;
		//if the result is all 0, set it to null for easier recognition of failed optimization
		return getPackedResult(resultOre, resultIngot);
	}
	/**
	 * 
	 * @param resultMaterial Current result
	 * @param availableMaterial Available material to be used for this alloy
	 * @param subtractors Current subtractor array
	 * @param index Index of current ore type thats subtractor is to be increased
	 * @return Returns whether decreasing usage of ore at index has a point. Decreasing usage of ore that is of the last type (small ore) or
	 * decreasing usage of ore that is the last usable ore out of available ore has no point because nothing of lesser type than the ore type at index can be
	 * increased to affect result.
	 */
	private boolean isFurtherSubtractionPointless(int[] availableMaterial, int[] resultMaterial, int neededUnits, int index){
		//last ore type doesn't need to be subtracted from because there is no ore type of lesser type. Subtraction pointless.
		if (index == Reference.ORE_TYPES_AMOUNT - 1)
			return true;
		boolean isPointless = true;
		int replacementSum = 0;
		//beginning at the end of the result and going towards the index, checking if there is something that can be increased
		for (int i = Reference.ORE_TYPES_AMOUNT - 1; i > index; i--){
			//check if result can be higher at this index
			if (resultMaterial[i] < availableMaterial[i]){
				//this stores how much more of ore can be added next iteration
				replacementSum += (availableMaterial[i] - resultMaterial[i]) * Reference.ORE_UNITS[i];
				//If the replacement ore is less than the ore that we take away, the subtraction is still pointless. Maybe next iteration
				if (!(replacementSum < Reference.ORE_UNITS[index])){
					isPointless = false;
					break;
				}
			}
		}
		return isPointless; 
	}
	/**
	 * 
	 * @param oreResult Array of ore types used
	 * @param ingotResult Amount of ingots used
	 * @return Returns an array containing the ores first and the ingots at the end. Returns null if the result is empty
	 */
	private int[] getPackedResult(int[] oreResult, int ingotResult){
		if (isOreMaterialEmpty(oreResult) && ingotResult == 0)
			return null;
		int[] packedResult = new int[Reference.ORE_TYPES_AMOUNT + 1];
		for (int i = 0; i < Reference.ORE_TYPES_AMOUNT; i++){
			packedResult[i] = oreResult[i];
		}
		packedResult[packedResult.length - 1] = ingotResult;
		return packedResult;
	}
	private int[] listOresByRarity(int[][] mat){
		int[] rarity = new int[target.recipe.length];
		for (int i = 0; i < rarity.length; i++){
			rarity[i] = target.getRatios()[i].getMin() * unitsTotal / getSummedUnits(mat[i]);
		}
		return null;
	}
	/**
	 * @param material First 4 spaces are ores, the last one is ingot
	 * @return Returns true if there are no ores in this material. Doesn't care about ingot
	 */
	private boolean isOreMaterialEmpty(int[] material){
		if (material == null)
			return true;
		return ((material[0] + material[1] + material[2] + material[3]) == 0) ? true : false;
	}
	/**
	 * 
	 * @param material First 4 spaces are ores, the last one is ingot
	 * @return Returns how many units of metal this material has, excluding ingots
	 */
	private int getSummedOreUnits(int[] material){
		return material[0] * Reference.UNITS_RICH + material[1] * Reference.UNITS_NORMAL + material[2] * Reference.UNITS_POOR + material[3] * Reference.UNITS_SMALL;
	}
	/**
	 * 
	 * @param material First 4 spaces are ores, the last one is ingot
	 * @return Returns how many units of metal this material has, including ingots
	 */
	private int getSummedUnits(int[] material){
		return material[0] * Reference.UNITS_RICH + material[1] * Reference.UNITS_NORMAL + material[2] * Reference.UNITS_POOR + material[3] * Reference.UNITS_SMALL + material[4] * Reference.UNITS_INGOT;
	}
	public int[][] sumMaterialInstancesOfSameTypeAndCatagory(){
		int[][] summedInstances = new int[target.getMaterials().length][Reference.ORE_TYPES_AMOUNT + 1];
		
		int[] ores = new int[Reference.ORE_TYPES_AMOUNT + 1];
		int position = 0;
		for (int i = 0; i < target.getMaterials().length; i++){
			ores = new int[Reference.ORE_TYPES_AMOUNT + 1];
			if (target.getMaterials()[i].hasOre()){
				for (int j = 0; j < target.getMaterials()[i].getOreInstances().length; j++){
					for (int k = 0; k < Reference.ORE_TYPES_AMOUNT; k++){
						ores[k] += this.material[position];
						position++;
					}
				}
			}
			//adds the ingot at the end of the array
			ores[ores.length - 1] = this.materialIngots[i];
			for (int j = 0; j < Reference.ORE_TYPES_AMOUNT + 1; j++){
				summedInstances[i][j] = ores[j];
			}
		}
		
		return summedInstances;
	}
}
