package optimizer;

public class Ratio {
	private int min, max, optimal;
	
	/**
	 * Creates a ratio of a material to be used to create an alloy. Should only be used in combination with Material
	 * @param min Minimum ratio of a material
	 * @param max Maximum ratio of a material
	 * @param opt Optimal ratio of a material based on rarity of the material
	 */
	public Ratio(int min, int max, int opt) {
		this.min = min;
		this.max = max;
		this.optimal = opt;
	}
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public int getOptimal(){
		return this.optimal;
	}
	public boolean isBetween(int ratio){
		if (ratio < min || ratio > max)
			return false;
		return true;
	}
	public String getRatioBoundries(){
		return min + "% - " + max + "%";
	}
	public String getRatioBoundriesCompressed(){
		return min + "-" + max + "%";
	}
}
