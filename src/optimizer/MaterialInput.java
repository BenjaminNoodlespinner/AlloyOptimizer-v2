package optimizer;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MaterialInput {
	private Material material;
	private Ratio ratio;
	private int[] ore;
	private int ingots;
	
	public MaterialInput(Material material, Ratio ratio, int[] mat) {
		this.material = material;
		this.ratio = ratio;
		if (material.hasOre()){
		this.ore = new int[Reference.ORE_TYPES_AMOUNT];
			for (int i = 0; i < ore.length; i++){
				this.ore[i] = mat[i];
			}
		} else {
			this.ore = null;
		}
		this.ingots = mat[mat.length - 1];
	}
	public int getRich(){
		if (material.hasOre()){
			return ore[0];
		} else {
			throw new NoSuchElementException();
		}
	}
	public int getNormal(){
		if (material.hasOre()){
			return ore[1];
		} else {
			throw new NoSuchElementException();
		}
	}
	public int getPoor(){
		if (material.hasOre()){
			return ore[2];
		} else {
			throw new NoSuchElementException();
		}
	}
	public int getSmall(){
		if (material.hasOre()){
			return ore[3];
		} else {
			throw new NoSuchElementException();
		}
	}
	public int getIngots(){
		return ingots;
	}
	public ArrayList<Integer> getListOre(){
		if (material.hasOre()){
			ArrayList<Integer> result = new ArrayList<Integer>();
			for (int i = 0; i < ore.length; i++){
				result.add(new Integer(ore[i]));
			}
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}
	public ArrayList<Integer> getListMaterial(){
		ArrayList<Integer> result;
		if (material.hasOre()){
			result = getListOre();
		} else {
			result = new ArrayList<Integer>();
			for (int i = 0; i < Reference.ORE_TYPES_AMOUNT; i++){
				result.add(0);
			}
		}
		result.add(new Integer(ingots));
		return result;
	}
	public Material getMaterial(){
		return this.material;
	}
	public Ratio getRatio(){
		return this.ratio;	
	}
	public boolean hasOre(){
		return this.material.hasOre();
	}
}
