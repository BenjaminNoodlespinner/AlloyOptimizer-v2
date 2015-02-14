package optimizer;

public class Alloy extends Material{
	Ratio[] ratios;
	Material[] recipe;
	
	/**Creates an alloy, e.g. Bronze. An alloy must be made out of multiple materials of different kind.
	 * These materials can be alloys, although they don't have to be.
	 * 
	 * @param name Name of the alloy
	 * @param alloyIngot Ingot made of this alloy. Note that the alloy can be made out of its ingot
	 * @param ratios Ratio of Materials that has to be followed in order to create this alloy
	 * @param recipe Materials to be used to create this alloy
	 */
	public Alloy(String name, Ingot alloyIngot, Ratio[] ratios, Material[] recipe){
		super(name, null, alloyIngot);
		this.ratios = ratios;
		this.recipe = recipe;
	}

	/**
	 * This returns a list of names of all the possible ores that can be used to create this alloy
	 * No idea what this is for
	 */
	@Override @Deprecated
	public String[] getOreInstancesNames(){
		int length = 0;
		for (int i = 0; i < recipe.length; i++){
			length += recipe[i].getOreInstancesNames().length;
		}
		String[] names = new String[length];
		int counter = -1;
		for (int i = 0; i < recipe.length; i++){
			for (int j = 0; j < recipe[i].getOreInstancesNames().length; j++){
				counter++;
				names[counter] = recipe[i].getOreInstancesNames()[j];
			}
		}
		
		return names;
	}
	/**
	 * No idea what this does TBH
	 */
	@Override @Deprecated
	public String[] getResInstancesNames(){
		String[] oreNames = getOreInstancesNames();
		String[] names = new String[oreNames.length + recipe.length];
		for (int i = 0; i < oreNames.length; i++){
			names[i] = oreNames[i];
		}
		for (int i = 0; i < recipe.length; i++){
			names[oreNames.length + i] = recipe[i].getName();
		}
		
		return names;
	}
	public int getNumberIngredients(){
		return recipe.length;
	}
	/**
	 * 
	 * @return Returns how many variances of material ingots there are
	 */
	public int getArraySizeIngots(){
		return this.recipe.length;
	}
	@Override
	public int getArraySizeOres(){
		int size = 0;
		for (Material material : recipe){
			if (material instanceof Alloy)
				continue;
			size += material.getArraySizeOres();
		}
		return size;
	}
	@Override @Deprecated
	public int getArraySizeRes(){
		return getArraySizeOres() + recipe.length;
	}
	/** 
	 * 
	 * @return Returns ratios of this alloy
	 */
	public Ratio[] getRatios(){
		return this.ratios;
	}
	@Override
	public String getName(){
		return super.getName();
	}
	@Override
	public String getCompressedName(){
		return super.getName().replace(" ", "\n");
	}
	/**
	 * 
	 * @return Returns materials that need to be used to create this alloy
	 */
	public Material[] getMaterials(){
		return this.recipe;
	}
	/**
	 * 
	 * @return Returns pictures of the ingots that can be used to create this alloy
	 */
	public int[] getCombinedIngotImageRes(){
		int[] imageResources = new int[this.recipe.length];
		for (int i = 0; i < this.recipe.length; i++){
			imageResources[i] = this.recipe[i].getIngot().getImageRes();
		}
		return imageResources;
	}
	/**
	 * 
	 * @return Checks whether this alloy has any ingredient that can be found as Ore
	 */
	public boolean hasMaterialWithOre(){
		for (int i = 0; i < this.recipe.length; i++){
			if (recipe[i].getOreInstances() != null){
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean hasOre(){
		if (this.oreInstances == null)
			return false;
		return true;
	}
	@Override 
	public int getMaterialOccurences() {
		return 1;
	}
	@Override
	public int[] getCombinedOreImageRes(){
		int[] imageResources = null;
		int length = 0;
		//counts how big the array must be
		for (int i = 0; i < this.recipe.length; i++){
			if (recipe[i].getOreInstances() != null){
				for (int j = 0; j < recipe[i].getOreInstances().length; j++){
					for (int k = 0; k < recipe[i].getOreInstances()[j].getImageRes().length; k++){
						length++;
					}
				}
			}
		}
		//fills the array
		imageResources = new int[length];
		int counter = 0;
		for (int i = 0; i < this.recipe.length; i++){
			if (recipe[i].getOreInstances() != null){
				for (int j = 0; j < recipe[i].getOreInstances().length; j++){
					for (int k = 0; k < recipe[i].getOreInstances()[j].getImageRes().length; k++){
						imageResources[counter] = recipe[i].getOreInstances()[j].getImageRes()[k];
						counter++;
					}
				}
			}
		}
		
		return imageResources;
	}
}
