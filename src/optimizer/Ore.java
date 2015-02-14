package optimizer;

public class Ore{
	private String name;
	private int[] resources;
	
	/**This creates an ore
	 * @param name Name of the ore
	 * @param resources A list of the ores pictures;
	 */
	public Ore(String name, int[] resources){
		this.name = name;
		this.resources = resources;
	}
	public String getName() {
		return name;
	}
	/**
	 * @return Returns pictures of this ore
	 */
	public int[] getImageRes(){
		return this.resources;
	}
	/**
	 * @return Returns a list of full names of its ores e.g. Rich Native Copper Ore
	 */
	public String[] getTypeNames(){
		String[] names = new String[Reference.ORE_TYPE_NAMES.length];
		for (int i = 0; i < Reference.ORE_TYPE_NAMES.length; i++){
			names[i] = Reference.ORE_TYPE_NAMES[i] + " " + name;
		}
		return names;
	}
}
