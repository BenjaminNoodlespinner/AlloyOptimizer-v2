package optimizer;

import com.jam.alloyoptimizer.R;

public class Reference {
	public static final int UNITS_RICH = 35;
	public static final int UNITS_NORMAL = 25;
	public static final int UNITS_POOR = 15;
	public static final int UNITS_SMALL = 10;
	public static final int UNITS_IMPOSSIBLE = 5;
	public static final int SPACE_VESSEL = 4;
	public static final int ORE_STACK_SIZE = 16;
	public static final int ORE_TYPES_AMOUNT = 4;
	public static final int MATERIAL_TYPES_AMOUNT = 5;
	public static final int UNITS_INGOT = 100;
	
	public static final String ORE_RICH = "Rich";
	public static final String ORE_NORMAL = "Normal";
	public static final String ORE_POOR = "Poor";
	public static final String ORE_SMALL = "Small";
	public static final String INGOT = "Ingot";
	public static final String[] ORE_TYPE_NAMES = {ORE_RICH, ORE_NORMAL, ORE_POOR, ORE_SMALL};
	public static final String[] MATERIAL_TYPE_NAMES = {ORE_RICH, ORE_NORMAL, ORE_POOR, ORE_SMALL, INGOT};
	public static final int[] ORE_UNITS = {UNITS_RICH, UNITS_NORMAL, UNITS_POOR, UNITS_SMALL};
	public static final int[] MATERIAL_UNITS = {UNITS_RICH, UNITS_NORMAL, UNITS_POOR, UNITS_SMALL, UNITS_INGOT};
	
	//Ores
	public static final Ore nativeCopper = new Ore("Native Copper", new int[]{R.drawable.rich_native_copper_ore, R.drawable.native_copper_ore, R.drawable.poor_native_copper_ore, R.drawable.native_copper_small_ore});
	public static final Ore tetrahedrite = new Ore("Tetrahedrite", new int[]{R.drawable.rich_tetrahedrite_ore, R.drawable.tetrahedrite_ore, R.drawable.poor_tetrahedrite_ore, R.drawable.tetrahedrite_small_ore});
	public static final Ore malachite = new Ore("Malachite", new int[]{R.drawable.rich_malachite_ore, R.drawable.malachite_ore, R.drawable.poor_malachite_ore, R.drawable.malachite_small_ore});
	public static final Ore cassiterite = new Ore("Cassiterite", new int[]{R.drawable.rich_cassiterite_ore, R.drawable.cassiterite_ore, R.drawable.poor_cassiterite_ore, R.drawable.cassiterite_small_ore});
	public static final Ore bismuthinite = new Ore("Bismuthinite", new int[]{R.drawable.rich_bismuthinite_ore, R.drawable.bismuthinite_ore, R.drawable.poor_bismuthinite_ore, R.drawable.bismuthinite_small_ore});
	public static final Ore sphalerite = new Ore("Sphalerite", new int[]{R.drawable.rich_sphalerite_ore, R.drawable.sphalerite_ore, R.drawable.poor_sphalerite_ore, R.drawable.sphalerite_small_ore});
	public static final Ore nativeGold = new Ore("Native Gold", new int[]{R.drawable.rich_native_gold_ore, R.drawable.native_gold_ore, R.drawable.poor_native_gold_ore, R.drawable.native_gold_small_ore});
	public static final Ore nativeSilver = new Ore("Native Silver", new int[]{R.drawable.rich_native_silver_ore, R.drawable.native_silver_ore, R.drawable.poor_native_silver_ore, R.drawable.native_silver_small_ore});
	public static final Ore garnierite = new Ore("Garnierite", new int[]{R.drawable.rich_garnierite_ore, R.drawable.garnierite_ore, R.drawable.poor_garnierite_ore, R.drawable.garnierite_small_ore});
	
	//Ingots
	public static final Ingot copperIngot = new Ingot("Copper Ingot", R.drawable.copper_ingot);
	public static final Ingot bismuthIngot = new Ingot("Bismuth Ingot", R.drawable.bismuth_ingot);
	public static final Ingot tinIngot = new Ingot("Tin Ingot", R.drawable.tin_ingot);
	public static final Ingot zincIngot = new Ingot("Zinc Ingot", R.drawable.zinc_ingot);
	public static final Ingot nickelIngot = new Ingot("Nickel Ingot", R.drawable.nickel_ingot);
	public static final Ingot goldIngot = new Ingot("Gold Ingot", R.drawable.gold_ingot);
	public static final Ingot silverIngot = new Ingot("Silver Ingot", R.drawable.silver_ingot);
	public static final Ingot bronzeIngot = new Ingot("Bronze Ingot", R.drawable.bronze_ingot);
	public static final Ingot blackBronzeIngot = new Ingot("Black Bronze Ingot", R.drawable.black_bronze_ingot);
	public static final Ingot bismuthBronzeIngot = new Ingot("Bismuth Bronze Ingot", R.drawable.bismuth_bronze_ingot);
	public static final Ingot brassIngot = new Ingot("Brass Ingot", R.drawable.brass_ingot);
	public static final Ingot roseGoldIngot = new Ingot("Rose Gold Ingot", R.drawable.rose_gold_ingot);
	public static final Ingot sterlingSilverIngot = new Ingot("Sterling Silver Ingot", R.drawable.sterling_silver_ingot);
	public static final Ingot blackSteelIngot = new Ingot("Black Steel Ingot", R.drawable.black_steel_ingot);
	public static final Ingot steelIngot = new Ingot("Steel Ingot", R.drawable.steel_ingot);
	public static final Ingot redSteelIngot = new Ingot("Red Steel Ingot", R.drawable.red_steel_ingot);
	public static final Ingot blueSteelIngot = new Ingot("Blue Steel Ingot", R.drawable.blue_steel_ingot);
	
	//Materials
	public static final Material copper = new Material("Copper", new Ore[]{nativeCopper, tetrahedrite, malachite}, copperIngot);
	public static final Material bismuth = new Material("Bismuth", new Ore[]{bismuthinite}, bismuthIngot);
	public static final Material zinc = new Material("Zinc", new Ore[]{sphalerite}, zincIngot);
	public static final Material tin = new Material("Tin", new Ore[]{cassiterite}, tinIngot);
	public static final Material gold = new Material("Gold", new Ore[]{nativeGold}, goldIngot);
	public static final Material silver = new Material("Silver", new Ore[]{nativeSilver}, silverIngot);
	public static final Material nickel = new Material("Nickel", new Ore[]{garnierite}, nickelIngot);
	
	public static final Material steel = new Material("Steel", steelIngot);
	
	//Alloys
	public static final Alloy bronze = new Alloy("Bronze", bronzeIngot, new Ratio[]{new Ratio(88, 92, 90), new Ratio(8, 12, 10)}, new Material[]{copper, tin});
	public static final Alloy blackBronze = new Alloy("Black Bronze", blackBronzeIngot, new Ratio[]{new Ratio(50, 70, 70), new Ratio(10, 25, 15), new Ratio(10, 25, 15)}, new Material[]{copper, silver, gold});
	public static final Alloy bismuthBronze = new Alloy("Bismuth Bronze", bismuthBronzeIngot, new Ratio[]{new Ratio(50, 70, 70), new Ratio(20, 30, 20), new Ratio(10, 20, 10)}, new Material[]{copper, zinc, bismuth});
	public static final Alloy roseGold = new Alloy("Rose Gold", roseGoldIngot, new Ratio[]{new Ratio(15, 30, 30), new Ratio(70, 85, 70)}, new Material[]{copper, gold});
	public static final Alloy sterlingSilver = new Alloy("Sterling Silver", sterlingSilverIngot, new Ratio[]{new Ratio(20, 40, 40), new Ratio(60, 80, 60)}, new Material[]{copper, silver});
	public static final Alloy brass = new Alloy("Brass", brassIngot, new Ratio[]{new Ratio(88, 92, 90), new Ratio(8, 12, 10)}, new Material[]{copper, zinc});
	public static final Alloy blackSteel = new Alloy("Black Steel", blackSteelIngot, new Ratio[]{new Ratio(50, 70, 60), new Ratio(15, 25, 15), new Ratio(15, 25, 25)}, new Material[]{steel, nickel, blackBronze});
	public static final Alloy redSteel = new Alloy("Red Steel", redSteelIngot, new Ratio[]{new Ratio(50, 60, 50), new Ratio(10, 15, 15), new Ratio(10, 15, 15), new Ratio(20, 25, 20)}, new Material[]{blackSteel, roseGold, brass, steel});
	public static final Alloy blueSteel = new Alloy("Blue Steel", blueSteelIngot, new Ratio[]{new Ratio(50, 60, 50), new Ratio(10, 15, 15), new Ratio(10, 15, 15), new Ratio(20, 25, 20)}, new Material[]{blackSteel, bismuthBronze, sterlingSilver, steel});
	
	//Material Array Vessel
	public static final Alloy[] vesselAlloys = new Alloy[]{bronze, bismuthBronze, blackBronze, brass, roseGold, sterlingSilver};
	public static final Alloy[] crucibleAlloys = new Alloy[]{bronze, bismuthBronze, blackBronze, brass, roseGold, sterlingSilver, blackSteel, blueSteel, redSteel};
}
