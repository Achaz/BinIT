package com.whispr.application.binit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
    Button setup, logout;
    EditText email;
    TextView status;
    String emailAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		 
		emailAd = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.EMAIL, null);
		
		email = (EditText)findViewById(R.id.USER_EMAIL);
		setup = (Button)findViewById(R.id.SETUP);
		status = (TextView)findViewById(R.id.E_STATUS);
		
		
		if(emailAd != null){
			status.setText("Logged in as: "+emailAd);
			status.setTextColor(Color.GREEN);
		}else{
			status.setTextColor(Color.RED);
		}
		
		setup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String emailAdd = email.getText().toString();			
				if(emailAdd != null){
					PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.EMAIL, emailAdd);
					startActivity(new Intent(getApplicationContext(), Settings.class));
					finish(); 
				}else{
					Toast.makeText(getApplicationContext(), "No email entered", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		logout = (Button)findViewById(R.id.LOGOUT);
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceConnector.getEditor(getApplicationContext()).remove(PreferenceConnector.EMAIL).commit();
				startActivity(new Intent(getApplicationContext(), Settings.class));
				finish();
			}
		});
		
	}
}
