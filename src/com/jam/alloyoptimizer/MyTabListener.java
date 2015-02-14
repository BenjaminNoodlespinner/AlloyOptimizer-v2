package com.jam.alloyoptimizer;

import com.jam.alloyoptimizer.R;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyTabListener implements TabListener {
	Fragment fragment;
	
	public MyTabListener(Fragment fragment){
		this.fragment = fragment;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
}
