package com.jam.alloyoptimizer;

import java.util.Arrays;

import optimizer.CrucibleOptimizer;
import optimizer.Reference;

import com.jam.alloyoptimizer.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CrucibleFragment extends UIFragment {
	TableLayout availableResIngotTable, resultTable;
	TableRow[] ingotRows, resultRows;
	int[] imageRefIngots, resourceAmountIngots;
	ImageButton[] ingotButtons = null;
	TextView[] resourceAmountViewsIngot;
	CrucibleOptimizer opt = new CrucibleOptimizer();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		root = inflater.inflate(R.layout.crucible_fragment, container, false);
		opt = new CrucibleOptimizer();
		//link the parts
		partIA = root.findViewById(R.id.crucibleIApart);
		partM = root.findViewById(R.id.crucibleMpart);
		partR = root.findViewById(R.id.crucibleRpart);
		partB = root.findViewById(R.id.crucibleBpart);
		//setup the spinner with alloy choice
		createSpinner(root, partIA, this, R.array.alloys_crucible);
		
		ingotInput = (EditText) partIA.findViewById(R.id.ingotsInputEdit);
		ingotInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
		//find the layouts and get the window width for the buttons
		viewAmountsTButton = (ToggleButton) partB.findViewById(R.id.showResButton);
		viewAmountsTButton.setOnCheckedChangeListener(this);
		availableResOreTable = (TableLayout) partM;
		availableResIngotTable = (TableLayout) root.findViewById(R.id.resourcesIngotAvailableTableCrucible);
		
		//setup the Amount-mode toggle button
		Button optimize = (Button) partB.findViewById(R.id.optimizeButton);
		optimize.setOnClickListener(this);
		
		resDialogFrag = ResAmountDialog.newInstance(R.string.res_dialog_title);
		
		row1 = (TableRow) partR.findViewById(R.id.ratiosTableRow1);
		row2 = (TableRow) partR.findViewById(R.id.ratiosTableRow2);
		
		possibleAlloys = Reference.crucibleAlloys;
		
		//this.setRetainInstance(true);
		
		return root;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		super.onItemSelected(parent, view, position, id);
		
		resourceAmountIngots = new int[quickRef.getArraySizeIngots()];
		
		//clear all the arrays of their previous content
		if (ingotButtons != null){
			availableResIngotTable.removeAllViews();
			ingotButtons = null;
		}
		
		int buttonsPerRow = (int) Math.floor(screenWidth / (double)(buttonSize + padding));
		
		//fetch data about how many buttons are to be created and create needed stuff
		imageRefIngots = quickRef.getCombinedIngotImageRes();
		ingotButtons = new ImageButton[imageRefIngots.length];
		resourceAmountViewsIngot = new TextView[imageRefIngots.length];

		//calculates how many rows of buttons there will be for the OREs and creates them
		ingotRows = new TableRow[(int)  Math.ceil(ingotButtons.length /((screenWidth / (double)(buttonSize + padding))))];
		for(int i = 0; i < ingotRows.length; i++){
			ingotRows[i] = new TableRow(getActivity());
			availableResIngotTable.addView(ingotRows[i]);
		}
		
		for (int i = 0; i < ingotRows.length; i++){
			for (int j = 0; j < buttonsPerRow; j++){
				//in case we have an partially filled row, abort if there are no more buttons
				if (i * buttonsPerRow + j >= imageRefIngots.length)
					break;
				
				//each button is in a FrameLayout along with a TextEdit that describes the ores amount 
				FrameLayout frame = new FrameLayout(getActivity());
				ImageButton button = new ImageButton(getActivity());
				TextView textView = new TextView(getActivity());
				
				//add the button and textview to an array for later access
				ingotButtons[i * buttonsPerRow + j] = button;
				resourceAmountViewsIngot[i * buttonsPerRow + j] = textView;
				
				button.setScaleType(ScaleType.FIT_CENTER);
				button.setLayoutParams(new FrameLayout.LayoutParams(buttonSize, buttonSize));
				button.setImageResource(imageRefIngots[i * buttonsPerRow + j]);
				button.setOnClickListener(new OnClickListener() {
					//this makes buttons create a dialog for resource input
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						//we send the ores image, the buttons index and the ores current amount for various purposes
						bundle.putInt("image", imageRefIngots[Arrays.asList(ingotButtons).indexOf(v)]);
						bundle.putInt("index", Arrays.asList(ingotButtons).indexOf(v));
						bundle.putInt("amount", resourceAmountIngots[Arrays.asList(ingotButtons).indexOf(v)]);
						bundle.putBoolean("ingot", true);
						resDialogFrag.setArguments(bundle);
						
						resDialogFrag.setTargetFragment(CrucibleFragment.this, 0);
				        resDialogFrag.show(getFragmentManager(), "dialog");
					}
				});
				frame.addView(button);
				
				//the amount is invisible by default until user switches to the Amount-mode
				textView.setVisibility(TextView.GONE);
				textView.setPadding(10, 5, 10, 5);
				frame.addView(textView);
				
				ingotRows[i].addView(frame);
			}
		}
		//remove result from previous Alloy
		if (resultTable != null){
			((LinearLayout) resultTable.getParent()).removeView(resultTable); //either way we want to redraw the table
			resultTable = null;
			result = null;
		}
		//toggle the resource display whenever user switches to a new alloy
		viewAmountsTButton.setChecked(false);
	}
	@Override
	public void onOkClick(String string, int index, boolean ingot) {
		super.onOkClick(string, index, ingot);
		
		if (ingot){
			if (!string.equals("")){
				resourceAmountIngots[index] = Integer.valueOf(string);
			} else {
				resourceAmountIngots[index] = 0;
			}
			resourceAmountViewsIngot[index].setText(string);
		}
	}
	@Override
	public void onClearCLick(String string, int index, boolean ingot){
		super.onClearCLick(string, index, ingot);
		
		if (ingot){
			resourceAmountIngots[index] = 0;
			resourceAmountViewsIngot[index].setText(string);
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		super.onCheckedChanged(buttonView, isChecked);
		
		if (isChecked){
			//for the crucible, material ingots are always available
			//for each button set transparency to half
			for (ImageButton button : ingotButtons){
				button.setImageAlpha(128);
			}
			//for each amountView for ingot, set visible and update amount
			for (int i = 0; i < resourceAmountViewsIngot.length; i++){
				resourceAmountViewsIngot[i].setVisibility(TextView.VISIBLE);
				resourceAmountViewsIngot[i].setText(Integer.toString(resourceAmountIngots[i]));
			}
		} else {
			//for the crucible, material ingots are always available
			//set the transparency back to normal
			for (ImageButton button : ingotButtons){
				button.setImageAlpha(255);
			}
			//hide he amountViews
			for (int i = 0; i < resourceAmountViewsIngot.length; i++){
				resourceAmountViewsIngot[i].setVisibility(TextView.GONE);
			}
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (areInputsValid()){
			int ingots = getIngotNumber();

			opt.set(quickRef, resourceAmountOre, resourceAmountIngots, ingots);
			result = opt.optimize(getProcentageArray());
			//we check if the optimization was successful for every material, otherwise dont show result
			displayResult();
		}
	}
	private void displayResult(){
		if (resultTable != null){
			((LinearLayout) resultTable.getParent()).removeView(resultTable); //either way we want to redraw the table
			resultTable = null;
		}
		if (isSuccessDisplayErrors(opt.getOptimizationState())){
			if (CrucibleOptimizer.isResultValid(result)){
				//if the result is valid
				resultTable = new TableLayout(getActivity());
				LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				tableParams.setMargins(dp2px(2), dp2px(2), dp2px(2), dp2px(2));
				resultTable.setLayoutParams(tableParams);

				TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams();
				rowParams.setMargins(0, dp2px(1), 0, 0);

				resultTable.setBackgroundResource(R.drawable.table_back);
				resultTable.setPadding(dp2px(3), dp2px(3), dp2px(3), dp2px(3));
				resultTable.setStretchAllColumns(true);
				resultRows = new TableRow[Reference.MATERIAL_TYPES_AMOUNT + 1];
				for (int i = 0; i < resultRows.length; i++){
					resultRows[i] = new TableRow(getActivity());
					resultTable.addView(resultRows[i]);
					if (i > 0){ //for every but first rown add padding above
						resultRows[i].setLayoutParams(rowParams);
					}
				}

				//add the type names to the first column
				TextView titleTex = new TextView(getActivity());
				titleTex.setText(R.string.result);
				titleTex.setBackgroundResource(R.drawable.table_item_title_back);
				titleTex.setPadding(dp2px(5), 0, 0, 0);
				resultRows[0].addView(titleTex);
				String[] typeNames = Reference.MATERIAL_TYPE_NAMES;
				for (int i = 1; i < resultRows.length; i++){
					TextView typeTex = new TextView(getActivity());
					typeTex.setText(typeNames[i - 1]);
					typeTex.setBackgroundResource(R.drawable.table_item_back);
					typeTex.setPadding(dp2px(5), 0, 0, 0);
					resultRows[i].addView(typeTex);
				}
				//add the names of the possible materials
				String[] materialNames = new String[quickRef.getMaterials().length];
				for (int i = 0; i < materialNames.length; i++){
					materialNames[i] = quickRef.getMaterials()[i].getCompressedName();
				}
				for (int i = 0; i < materialNames.length; i++){
					TextView matTex = new TextView(getActivity());
					matTex.setText(materialNames[i]);
					matTex.setBackgroundResource(R.drawable.table_item_back);
					matTex.setPadding(dp2px(5), 0, 0, 0);
					resultRows[0].addView(matTex);
				}
				//add the result to the table
				for (int i = 0; i < materialNames.length; i++){
					for (int j = 0; j < typeNames.length; j++){
						TextView resTex = new TextView(getActivity());
						resTex.setText(Integer.toString(result[i][j]));
						resTex.setPadding(dp2px(5), 0, 0, 0);
						resultRows[j + 1].addView(resTex);
					}
				}
				((ViewGroup) root.findViewById(R.id.root_linear)).addView(resultTable);
			} 
		}
	}
}