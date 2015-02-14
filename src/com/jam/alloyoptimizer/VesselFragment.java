package com.jam.alloyoptimizer;

import optimizer.Reference;
import optimizer.VesselOptimizer;

import com.jam.alloyoptimizer.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

public class VesselFragment extends UIFragment{
	VesselOptimizer opt; // add constructor
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.vessel_fragment, container, false);
		
		//link the parts
		partIA = view.findViewById(R.id.vesselIApart);
		partM = view.findViewById(R.id.vesselMpart);
		partR = view.findViewById(R.id.vesselRpart);
		partB = view.findViewById(R.id.vesselBpart);
		
		createSpinner(view, partIA, this, R.array.alloys_vessel);
		
		availableResOreTable = (TableLayout) partM;
		
		viewAmountsTButton = (ToggleButton) partB.findViewById(R.id.showResButton);
		viewAmountsTButton.setOnCheckedChangeListener(this);
		Button optimize = (Button) partB.findViewById(R.id.optimizeButton);
		optimize.setOnClickListener(this);
		optimize.setText(R.string.unimplemented);//temporary until done
		ingotInput = (EditText) partIA.findViewById(R.id.ingotsInputEdit);
		ingotInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		resDialogFrag = ResAmountDialog.newInstance(R.string.res_dialog_title);
		
		row1 = (TableRow) partR.findViewById(R.id.ratiosTableRow1);
		row2 = (TableRow) partR.findViewById(R.id.ratiosTableRow2);
		
		possibleAlloys = Reference.vesselAlloys;
		
		//this.setRetainInstance(true);
		
		return view;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		super.onItemSelected(parent, view, position, id);
		viewAmountsTButton.setChecked(false);
	}
	@Override
	public void onClick(View v) {
		//since we didn't implement this class yet, this button should do nothing - get rid of super method.
	}
}
