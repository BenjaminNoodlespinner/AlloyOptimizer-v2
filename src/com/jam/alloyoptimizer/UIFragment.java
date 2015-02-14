package com.jam.alloyoptimizer;

import java.util.Arrays;

import com.jam.alloyoptimizer.R;

import optimizer.Alloy;
import optimizer.OptimizationFailure;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public abstract class UIFragment extends Fragment implements OnItemSelectedListener, OnCheckedChangeListener, OnClickListener, DialogClickListener, OnLongClickListener{
	TableLayout availableResOreTable;
	TableRow[] oreRows;
	ToggleButton viewAmountsTButton;
	int[] imageRefOre, resourceAmountOre;
	DialogFragment resDialogFrag, wrongInputDialogFrag, optimizationFailDialogFrag;
	ImageButton[] oreButtons = null;
	TextView[] resourceAmountViewsOre;
	EditText ingotInput;
	EditText[] ratios;
	TableRow row1, row2;
	Alloy quickRef;
	Alloy[] possibleAlloys;
	View partIA, partM, partR, partB, root;
	int screenWidth;
	int[][] result;
	final int buttonSize = (int) (50 * Resources.getSystem().getDisplayMetrics().density);
	final int padding = (int) (10 * Resources.getSystem().getDisplayMetrics().density);

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Display disp = getActivity().getWindowManager().getDefaultDisplay();
		Point p = new Point();
		disp.getSize(p);
		screenWidth = p.x;
		return null;
	}
	
	public Spinner createSpinner(View view, View partIA, OnItemSelectedListener listener, int textRes){
		Spinner spinner = (Spinner) partIA.findViewById(R.id.alloyInputSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), textRes, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(listener);
		return spinner;
	}
	public boolean isProcentageValid(int[] procentages){
		int sum = 0;
		for (int i = 0; i < procentages.length; i++){
			sum += procentages[i];
		}
		if (sum != 100)
			return false;
		return true;
	}
	@Override
	public void onOkClick(String string, int index, boolean ingot) {
		if (!ingot){
			if (!string.equals("")){
				resourceAmountOre[index] = Integer.valueOf(string);
			} else {
				resourceAmountOre[index] = 0;
			}
			resourceAmountViewsOre[index].setText(string);
		}
	}
	@Override
	public void onClearCLick(String string, int index, boolean ingot) {
		if (!ingot){
			resourceAmountOre[index] = 0;
			resourceAmountViewsOre[index].setText(string);
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		quickRef = possibleAlloys[position];
		setUpMaterialArrayInput(position);
		setUpRatioTableInput();
		//oreButtons[2].getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
	}
	private void setUpRatioTableInput(){
		//remove old views from the table
		if (ratios != null){
			row1.removeAllViews();
			row2.removeAllViews();
			ratios = null;
		}
		TableLayout ratioTable = (TableLayout) partR;
		ratioTable.setStretchAllColumns(true);
		//get names
		String[] materialNames = new String[quickRef.getMaterials().length];
		for (int i = 0; i < materialNames.length; i++){
			materialNames[i] = quickRef.getMaterials()[i].getCompressedName();
		}
		//create textviews to sign materials
		for (int i = 0; i < materialNames.length; i++){
			TextView ratioNameView = new TextView(getActivity());
			//sets the column to skip the first one
			ratioNameView.setPadding(15, 0, 0, 0);
			ratioNameView.setText(materialNames[i]);
			//ratioNameView.setEms(5);
			row1.addView(ratioNameView);
		}
		//creating edittexts for preferred ratios
		ratios = new EditText[materialNames.length];
		for (int i = 0; i < materialNames.length; i++){
			EditText ratioEditView = new EditText(getActivity());
			ratios[i] = ratioEditView;
			ratioEditView.setHint(quickRef.getRatios()[i].getRatioBoundriesCompressed());
			ratioEditView.setInputType(InputType.TYPE_CLASS_NUMBER);
			if (!(i + 1 < materialNames.length))
				ratioEditView.setImeOptions(EditorInfo.IME_ACTION_DONE);
			else
				ratioEditView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			row2.addView(ratioEditView);
		}
	}
	private void setUpMaterialArrayInput(int position){
		resourceAmountOre = new int[quickRef.getArraySizeOres()];

		//clear all the arrays of their previous content
		resourceAmountViewsOre = null;
		imageRefOre = null;
		if (oreButtons != null){
			availableResOreTable.removeAllViews();
			oreButtons = null;
			availableResOreTable.setVisibility(TableLayout.GONE);
		}

		int buttonsPerRow = (int) Math.floor(screenWidth / (double)(buttonSize + padding));
		//if material has any ores in its recipe, handle them
		if (quickRef.hasMaterialWithOre()){
			availableResOreTable.setVisibility(TableLayout.VISIBLE);
			//fetch data about how many buttons are to be created and create needed stuff
			imageRefOre = quickRef.getCombinedOreImageRes();
			oreButtons = new ImageButton[imageRefOre.length];
			resourceAmountViewsOre = new TextView[imageRefOre.length];

			//calculates how many rows of buttons there will be for the OREs and creates them
			oreRows = new TableRow[(int)  Math.ceil(oreButtons.length /((screenWidth / (double)(buttonSize + padding))))];
			for(int i = 0; i < oreRows.length; i++){
				oreRows[i] = new TableRow(getActivity());
				availableResOreTable.addView(oreRows[i]);
			}

			for (int i = 0; i < oreRows.length; i++){
				for (int j = 0; j < buttonsPerRow; j++){
					//in case we have an partially filled row, abort if there are no more buttons
					if (i * buttonsPerRow + j >= imageRefOre.length)
						break;

					//each button is in a FrameLayout along with a TextEdit that describes the ores amount 
					FrameLayout frame = new FrameLayout(getActivity());
					ImageButton button = new ImageButton(getActivity());
					TextView textView = new TextView(getActivity());

					//add the button and textview to an array for later access
					oreButtons[i * buttonsPerRow + j] = button;
					resourceAmountViewsOre[i * buttonsPerRow + j] = textView;

					button.setScaleType(ScaleType.FIT_CENTER);
					button.setLayoutParams(new FrameLayout.LayoutParams(buttonSize, buttonSize));
					button.setImageResource(imageRefOre[i * buttonsPerRow + j]);
					button.setOnClickListener(new OnClickListener() {
						//this makes buttons create a dialog for resource input
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							//we send the ores image, the buttons index and the ores current amount for various purposes
							bundle.putInt("image", imageRefOre[Arrays.asList(oreButtons).indexOf(v)]);
							bundle.putInt("index", Arrays.asList(oreButtons).indexOf(v));
							bundle.putInt("amount", resourceAmountOre[Arrays.asList(oreButtons).indexOf(v)]);
							bundle.putBoolean("ingot", false);
							resDialogFrag.setArguments(bundle);

							resDialogFrag.setTargetFragment(UIFragment.this, 0);
							resDialogFrag.show(getFragmentManager(), "dialog");
						}
					});
					frame.addView(button);

					//the amount is invisible by default until user switches to the Amount-mode
					textView.setVisibility(TextView.GONE);
					textView.setPadding(10, 5, 10, 5);
					frame.addView(textView);

					oreRows[i].addView(frame);
				}
			}
		} else {
			//if there are no ores, clean up previous alloys mess
			oreButtons = new ImageButton[]{};
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked){
			//if the button is checked change appearance of the buttons and make amountViews visible
			if (oreButtons != null){
				if (oreButtons.length != 0){
					//for each button set transparency to half
					for (ImageButton button : oreButtons){
						button.setImageAlpha(128);
					}
					//for each amountView for ore, set visible and update amount
					for (int i = 0; i < resourceAmountViewsOre.length; i++){
						resourceAmountViewsOre[i].setVisibility(TextView.VISIBLE);
						resourceAmountViewsOre[i].setText(Integer.toString(resourceAmountOre[i]));
					}
				}
			}
		} else {
			//if the button is not checked, a.k.a. Amount-mode off
			if (oreButtons != null){
				if (oreButtons.length != 0){
					//if there are any ore buttons, set their transparency back to normal
					for (ImageButton button : oreButtons){
						button.setImageAlpha(255);
					}
					//and hide the amountViews
					for (int i = 0; i < resourceAmountViewsOre.length; i++){
						resourceAmountViewsOre[i].setVisibility(TextView.GONE);
					}
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		//check if the given ratios are correct and if ingot count is not 0
		int[] procentages = getProcentageArray();
		if (!isProcentageValid(procentages)){
			wrongInputDialogFrag = WrongInputDialog.newInstance(R.string.wrong_procentage_dialog_name);
			Bundle bundle = new Bundle();
			bundle.putString("message", "The sum of all ratios must be 100%!");
			bundle.putInt("title", R.string.wrong_procentage_dialog_name);
			wrongInputDialogFrag.setArguments(bundle);
			wrongInputDialogFrag.show(getFragmentManager(), "dialog");
			return;
		}
		int ingots = getIngotNumber();
		if (ingots <= 0 || ingots > 30){
			wrongInputDialogFrag = WrongInputDialog.newInstance(R.string.wrong_ingot_dialog_name);
			Bundle bundle = new Bundle();
			bundle.putString("message", "The ingot count must be between 1-30 ingots!");
			bundle.putInt("title", R.string.wrong_ingot_dialog_name);
			wrongInputDialogFrag.setArguments(bundle);
			wrongInputDialogFrag.show(getFragmentManager(), "dialog");
			return;
		}
		for (int i = 0; i < quickRef.getMaterials().length; i++){
			if (!quickRef.getRatios()[i].isBetween(procentages[i])){
				wrongInputDialogFrag = WrongInputDialog.newInstance(R.string.wrong_ratio_dialog_name);
				Bundle bundle = new Bundle();
				bundle.putString("message", "The ratio of " + quickRef.getMaterials()[i].getName() + " is incorrect!\nTry between " + quickRef.getRatios()[i].getRatioBoundries());
				bundle.putInt("title", R.string.wrong_ratio_dialog_name);
				wrongInputDialogFrag.setArguments(bundle);
				wrongInputDialogFrag.show(getFragmentManager(), "dialog");
				return;
			}
		}
	}
	protected int[] getProcentageArray(){
		int[] procentages = new int[ratios.length];
		for (int i = 0; i < procentages.length; i++){
			procentages[i] = (ratios[i].getText().toString().equals("")) ? 0 : Integer.valueOf(ratios[i].getText().toString());
		}
		return procentages;
	}
	protected int getIngotNumber(){
		return (ingotInput.getText().toString().equals("")) ? 0 : Integer.valueOf(ingotInput.getText().toString());
	}
	protected boolean areInputsValid(){
		if (!isProcentageValid(getProcentageArray())){
			return false;
		}
		if (getIngotNumber() <= 0 || getIngotNumber() > 30){
			return false;
		}
		int[] procentages = getProcentageArray();
		for (int i = 0; i < quickRef.getMaterials().length; i++)
			if (!quickRef.getRatios()[i].isBetween(procentages[i]))
				return false;
		return true;
	}
	public static int px2dp(int px){
		return (int) (px * Resources.getSystem().getDisplayMetrics().density);
	}
	public static int dp2px(int dp){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
	}
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
	protected boolean isSuccessDisplayErrors(OptimizationFailure[] state){
		for (int i = 0; i < state.length; i++){
			if (state[i] == OptimizationFailure.OPTIMIZATION_SUCCESSFUL){
				continue;
			} else if (state[i] == OptimizationFailure.FALSE_POSITIVE){
				optimizationFailDialogFrag = OptimizationFailDialog.newInstance(R.string.opt_failed);
				Bundle bundle = new Bundle();
				bundle.putString("message", "The algorithm has given false positive. This is a critical error. Contact developer.");
				bundle.putInt("title", R.string.opt_failed);
				optimizationFailDialogFrag.setArguments(bundle);
				optimizationFailDialogFrag.show(getFragmentManager(), "dialog");
				return false;
			} else if (state[i] == OptimizationFailure.IMPOSSIBLE_UNIT){
				optimizationFailDialogFrag = OptimizationFailDialog.newInstance(R.string.opt_failed);
				Bundle bundle = new Bundle();
				bundle.putString("message", "Impossible to calculate solution for 5 Units! (you shouldn't see this message)");
				bundle.putInt("title", R.string.opt_failed);
				optimizationFailDialogFrag.setArguments(bundle);
				optimizationFailDialogFrag.show(getFragmentManager(), "dialog");
				return false;
			} else if (state[i] == OptimizationFailure.UNSUFFICIENT_RESOURCES){
				optimizationFailDialogFrag = OptimizationFailDialog.newInstance(R.string.opt_failed);
				Bundle bundle = new Bundle();
				bundle.putString("message", "There isn't enough of " + quickRef.getMaterials()[i].getName() + "! Try gathering some more");
				bundle.putInt("title", R.string.opt_failed);
				optimizationFailDialogFrag.setArguments(bundle);
				optimizationFailDialogFrag.show(getFragmentManager(), "dialog");
				return false;
			} else if (state[i] == OptimizationFailure.UNSUFFICIENT_VARIATION){
				optimizationFailDialogFrag = OptimizationFailDialog.newInstance(R.string.opt_failed);
				Bundle bundle = new Bundle();
				bundle.putString("message", "To few types of " + quickRef.getMaterials()[i].getName() + " to create optimal solution! Try gathering a few pieces of some other type");
				bundle.putInt("title", R.string.opt_failed);
				optimizationFailDialogFrag.setArguments(bundle);
				optimizationFailDialogFrag.show(getFragmentManager(), "dialog");
				return false;
			}
		}
		return true;
	}
}
