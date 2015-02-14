package optimizer;

public class Ingot{
	private String name;
	private int resource;

	/**Creates an ingot
	 * 
	 * @param name Ingots name
	 * @param res Ingots picture
	 */
	public Ingot(String name, int res) {
		this.name = name;
		this.resource = res;
	}
	public String getName(){
		return this.name;
	}
	/**
	 * 
	 * @return Returns ingots picture
	 */
	public int getImageRes(){
		return this.resource;
	}
}
