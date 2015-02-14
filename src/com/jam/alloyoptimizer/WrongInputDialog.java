package com.jam.alloyoptimizer;

import com.jam.alloyoptimizer.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class WrongInputDialog extends DialogFragment{
	TextView message;
	
	public static WrongInputDialog newInstance(int title) {
		WrongInputDialog frag = new WrongInputDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	@SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstance){
		//Build the dialog, set the ores Image
		AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
		LayoutInflater li = LayoutInflater.from(getActivity());
		View dialogView = li.inflate(R.layout.wrong_input_dialog_layout, null);
		myDialog.setView(dialogView);
		myDialog.setIcon(R.drawable.ic_action_error);
		myDialog.setTitle(getArguments().getInt("title"));
		
		message = (TextView) dialogView.findViewById(R.id.wrong_input_message);
		message.setText(getArguments().getString("message"));
		
		//set up buttons
		myDialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		
		AlertDialog dialog = myDialog.create();
		
		return dialog;
	}
}
