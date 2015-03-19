package com.whispr.application.binit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BinRecieptActivity extends Activity{

	
	
	TextView placeName,binname,bindesc,contribName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		placeName=(TextView)findViewById(R.id.RECIEPT_PLACE_NAME);
		binname=(TextView)findViewById(R.id.BIN_NAME);
		bindesc=(TextView)findViewById(R.id.BIN_DESCRIPTION);
		contribName=(TextView)findViewById(R.id.RECIEPT_CONTRIBUTOR_NAME);
		
		Intent in= getIntent();
		String Street =in.getStringExtra("Street");
		String adder=in.getStringExtra("adder");
		String bname=in.getStringExtra("bname");
		String desc=in.getStringExtra("desc");
		String lat=in.getStringExtra("lat");
		String lon=in.getStringExtra("lon");
		String bearing=in.getStringExtra("bearing");
		String altitude=in.getStringExtra("altitude");
		
		placeName.setText(Street);
		binname.setText(bname);
		bindesc.setText(desc);
	    contribName.setText(adder);
		
		
	}
	
	

}
