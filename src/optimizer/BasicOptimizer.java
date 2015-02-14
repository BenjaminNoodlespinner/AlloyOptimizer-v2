package optimizer;

import java.util.ArrayList;

public class BasicOptimizer {
	protected Alloy target;
	protected OptimizationFailure[] fail;
	protected int[] material;
	protected ArrayList<MaterialInput> mat; //constructor should fill this up
	protected int ingots;
	protected int unitsTotal;
	
	public BasicOptimizer(Alloy target, int[] material, int ingots) {
		set(target, material, ingots);
	}
	public BasicOptimizer(){
		this.target = null;
		this.material = null;
		this.fail = null;
		this.ingots = 0;
		this.unitsTotal = 0;
	}
	public BasicOptimizer set(Alloy target, int[] material, int ingots){
		this.target = target;
		this.material = material;
		this.ingots = ingots;
		this.unitsTotal = ingots * Reference.UNITS_INGOT;
		this.fail = new OptimizationFailure[target.getMaterials().length];
		
		return this;
	}
	/**
	 * This method returns an array of how rare the materials are based on how much of the material is available 
	 * in comparison to the minimum material needed to complete the optimization
	 * @return an array of materials indices sorted from the most common to the rarest
	 */
	public int[] getMaterialRarity(){
		return null;
	}
	public Alloy getTarget() {
		return target;
	}
	public int[] getMaterials() {
		return material;
	}
	public BasicOptimizer setMaterial(int[] material) {
		this.material = material;
		
		return this;
	}
	public Ratio[] getRatios() {
		return target.getRatios();
	}
	public int getIngots() {
		return ingots;
	}
	public int getUnitsTotal() {
		int units = this.unitsTotal;
		return units;
	}
	//Unfinished
	public int[] getMaterialInstanceAmount(int index){
		Material mat = target.getMaterials()[index];
		int size = 0;
		if (mat.getOreInstances() != null){
			size += mat.getOreInstances().length * Reference.ORE_TYPES_AMOUNT;
		}
		size++; //account for the ingot instance at the end
		
		int[] material = new int[size];
		
		return material;
	}
	public static boolean isResultValid(int[][] res){
		for (int i = 0; i < res.length; i++){
			if (res[i] == null)
				return false;
		}
		return true;
	}
	public OptimizationFailure[] getOptimizationState(){
		return this.fail;
	}
}
