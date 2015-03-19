package com.whispr.application.binit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class AboutBinIT extends Activity {
    TextView abt, devteam;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
		
		abt = (TextView)findViewById(R.id.ABT_TXT);
		devteam = (TextView)findViewById(R.id.TEAM);
		devteam.setTextColor(Color.GREEN);
		//abt.setTextColor(Color.RED);
		
	}
}
