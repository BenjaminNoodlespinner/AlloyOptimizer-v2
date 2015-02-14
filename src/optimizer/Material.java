package optimizer;

public class Material {
	protected String name;
	protected Ore[] oreInstances;
	protected Ingot ingotInstance;
	
	/**
	 * Creates a material, e.g. Copper. A material can possibly be made out of multiple ore types e.g. both Native Copper Ore
	 * and Malachite are Copper
	 * @param name Name of the material e.g. Copper
	 * @param ores Ores that create this material e.g. for copper they are Native Copper, Malachite and Tetrahedrite
	 * @param ingot An ingot of this material e.g. Copper Ingot. Note that the material can be made out of its own ingot.
	 */
	public Material(String name, Ore[] ores, Ingot ingot){
		this.name = name;
		this.oreInstances = ores;
		this.ingotInstance = ingot;
	}
	/**
	 * This creates a material that is not a product of ore e.g. Steel
	 * @param name Name of the material
	 * @param ingot An Ingot of this material e.g. Copper Ingot. Note that the material can be made out of its own ingot.
	 */
	public Material(String name, Ingot ingot){
		this(name, null, ingot);
	}
	/**
	 * 
	 * @return Returns full names of all the Ores that can be used to create this material
	 */
	public String[] getOreInstancesNames(){
		String[] names = new String[oreInstances.length * Reference.ORE_TYPES_AMOUNT];
		for (int i = 0; i < oreInstances.length; i++){
			for (int j = 0; j < Reference.ORE_TYPES_AMOUNT; j++){
				names[i * Reference.ORE_TYPES_AMOUNT + j] = oreInstances[i].getTypeNames()[j];
			}
		}
		
		return names;
	}
	/**
	 * Deprecated ingots and ores should be handled separately
	 */
	@Deprecated
	public String[] getResInstancesNames(){
		String[] oreNames = getOreInstancesNames();
		String[] names = new String[oreNames.length + 1];
		for (int i = 0; i < oreNames.length; i++){
			names[i] = oreNames[i];
		}
		names[names.length - 1] = ingotInstance.getName();
		
		return names;
	}
	/**
	 * 
	 * @param ore 
	 * @return Checks if this material can be made out of this Ore
	 */
	public boolean hasInstanceOf(Ore ore){
		for (int i = 0; i < oreInstances.length; i++){
			if (oreInstances[i].getName().equals(name))
				return true;
		}
		return false;
	}
	/**
	 * 
	 * @param ingot
	 * @return Checks if this material can be made out of this ingot
	 */
	public boolean hasInstaceOf(Ingot ingot){
		if (this.ingotInstance == ingot)
			return true;
		return false;
	}
	/**
	 * 
	 * @return Returns all the Ores that can be used to create this material
	 */
	public Ore[] getOreInstances(){
		return this.oreInstances;
	}
	/**
	 * 
	 * @return Returns the size of the array of the ores that can be used to create this material
	 */
	public int getArraySizeOres(){
		if (oreInstances == null)
			return 0;
		return oreInstances.length * Reference.ORE_TYPES_AMOUNT;
	}
	/**
	 * Ores and ingots should be handled separately
	 * @return
	 */
	@Deprecated
	public int getArraySizeRes(){
		return getArraySizeOres() + 1;
	}
	/**
	 * 
	 * @return Returns an ingot of this material
	 */
	public Ingot getIngot(){
		return this.ingotInstance;
	}
	/**
	 * 
	 * @return Returns pictures of all ores that can be used to create this material
	 */
	public int[] getCombinedOreImageRes(){
		int[] imageResources;
		int length = 0;
		//count how many picture there are
		for (int j = 0; j < getOreInstances().length; j++){
			for (int k = 0; k < getOreInstances()[j].getImageRes().length; k++){
				length++;
			}
		}
		//puts the images into an array
		imageResources = new int[length];
		int counter = 0;
		for (int j = 0; j < getOreInstances().length; j++){
			for (int k = 0; k < getOreInstances()[j].getImageRes().length; k++){
				imageResources[counter] = getOreInstances()[j].getImageRes()[k];
				counter++;
			}
		}
		return imageResources;
	}
	/**
	 * 
	 * @return Checks whether this material can be made out of Ore
	 */
	public boolean hasOre(){
		if (this.oreInstances == null)
			return false;
		return true;
	}
	public String getName(){
		return this.name;
	}
	public String getCompressedName(){
		return name.replace(" ", "\n");
	}
	public int getMaterialOccurences(){
		return this.oreInstances.length;
	}
}
