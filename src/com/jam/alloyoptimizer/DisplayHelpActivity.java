package com.jam.alloyoptimizer;

import com.jam.alloyoptimizer.R;

import optimizer.Alloy;
import optimizer.Reference;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayHelpActivity extends Activity implements OnItemSelectedListener{
	final int buttonSize = (int) (50 * Resources.getSystem().getDisplayMetrics().density);
	final int padding = (int) (10 * Resources.getSystem().getDisplayMetrics().density);

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		View root = findViewById(android.R.id.content);

		Display disp = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		disp.getSize(p);
		int screenWidth = p.x;

		Spinner spinner = (Spinner) root.findViewById(R.id.alloyInputSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alloys_crucible, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		View partM = root.findViewById(R.id.helpMpart);
		View partR = root.findViewById(R.id.helpRpart);

		int[] imageRefOre = null;
		Alloy quickRef = Reference.bronze;

		TableLayout availableResOreTable = (TableLayout) partM;

		int buttonsPerRow = (int) Math.floor(screenWidth / (double)(buttonSize + padding));
		//fetch data about how many buttons are to be created and create needed stuff
		imageRefOre = quickRef.getCombinedOreImageRes();

		//calculates how many rows of buttons there will be for the OREs and creates them
		TableRow[] oreRows = new TableRow[(int)  Math.ceil(imageRefOre.length /((screenWidth / (double)(buttonSize + padding))))];
		for(int i = 0; i < oreRows.length; i++){
			oreRows[i] = new TableRow(this);
			availableResOreTable.addView(oreRows[i]);
		}

		for (int i = 0; i < oreRows.length; i++){
			for (int j = 0; j < buttonsPerRow; j++){
				//in case we have an partially filled row, abort if there are no more buttons
				if (i * buttonsPerRow + j >= imageRefOre.length)
					break;

				//each button is in a FrameLayout along with a TextEdit that describes the ores amount 
				FrameLayout frame = new FrameLayout(this);
				ImageButton button = new ImageButton(this);
				TextView textView = new TextView(this);

				button.setScaleType(ScaleType.FIT_CENTER);
				button.setLayoutParams(new FrameLayout.LayoutParams(buttonSize, buttonSize));
				button.setImageResource(imageRefOre[i * buttonsPerRow + j]);

				frame.addView(button);

				//the amount is invisible by default until user switches to the Amount-mode
				textView.setVisibility(TextView.GONE);
				textView.setPadding(10, 5, 10, 5);
				frame.addView(textView);

				oreRows[i].addView(frame);
			}
		}
		
		TableLayout ratioTable = (TableLayout) partR;
		TableRow row1 = (TableRow) partR.findViewById(R.id.ratiosTableRow1);
		TableRow row2 = (TableRow) partR.findViewById(R.id.ratiosTableRow2);
		ratioTable.setStretchAllColumns(true);
		//get names
		String[] materialNames = new String[quickRef.getMaterials().length];
		for (int i = 0; i < materialNames.length; i++){
			materialNames[i] = quickRef.getMaterials()[i].getCompressedName();
		}
		//create textviews to sign materials
		for (int i = 0; i < materialNames.length; i++){
			TextView ratioNameView = new TextView(this);
			//sets the column to skip the first one
			ratioNameView.setPadding(15, 0, 0, 0);
			ratioNameView.setText(materialNames[i]);
			//ratioNameView.setEms(5);
			row1.addView(ratioNameView);
		}
		//creating edittexts for preferred ratios
		for (int i = 0; i < materialNames.length; i++){
			EditText ratioEditView = new EditText(this);
			ratioEditView.setHint(quickRef.getRatios()[i].getRatioBoundriesCompressed());
			ratioEditView.setInputType(InputType.TYPE_CLASS_NUMBER);
			if (!(i + 1 < materialNames.length))
				ratioEditView.setImeOptions(EditorInfo.IME_ACTION_DONE);
			else
				ratioEditView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			row2.addView(ratioEditView);
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {	}
}
