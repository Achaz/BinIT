package com.whispr.application.binit;

import java.io.IOException;
import java.util.List;

import com.whispr.binit.utilities.AlertDialogManager;
import com.whispr.binit.utilities.GPSTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddBin extends Activity{

	Button add_bin;
	EditText bin_name, bin_desc;
	String lat,lon,altitude;
	String email;
	TextView user;
	GPSTracker gps;	
	AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bin);
		
		Intent in = getIntent();
	    email = in.getStringExtra("email");
		
		add_bin = (Button)findViewById(R.id.ADD_BIN_BTN);
		bin_name = (EditText)findViewById(R.id.BIN_NAME);
		bin_desc = (EditText)findViewById(R.id.BIN_DESCRIPTION);
		user = (TextView)findViewById(R.id.LOGGED_USER);
		user.setText("Hi "+email);
		user.setTextColor(Color.RED);
		
		
		add_bin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gps = new GPSTracker(getApplicationContext());

				// check if GPS location can get
				if (gps.canGetLocation()) {
					
					lat=Double.toString(gps.getLatitude());
					lon=Double.toString(gps.getLongitude());
					altitude=Double.toString(gps.getAltitude());
					String bname = bin_name.getText().toString();
					String desc = bin_desc.getText().toString();
					
					
					Geocoder gCoder=new Geocoder(getApplicationContext());
					List<Address> address;
					try {
						address = gCoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
						if(address!=null&&address.size()>0){
							String Street=address.get(0).getLocality();
							//Toast.makeText(getApplicationContext(), "name: "+bname+"\n desc: "+desc+"\n adder:"+adder+"\n bearing: "+bearing+"\n"+"altitude: "+altitude+"\n lat: "+gps.getLatitude()+"\n"+"lon:"+gps.getLongitude()+"\n"+Street+"", Toast.LENGTH_LONG).show();
							Toaster("Bin Reciept", "User: "+email+"\nName: "+bname+"\nDescription: "+desc+"\nLatitude: "+gps.getLatitude()+"\nLongitude: "+gps.getLongitude()+"\nPlace/Street: "+Street);
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
					
					
					
				} else {
					// Can't get user's current location
					alert.showAlertDialog(AddBin.this, "GPS Status",
							"Couldn't get location information. Please enable GPS",
							false);
					// stop executing code by return
					return;
				}
				
				
			 }
		});
		
	}
	

	public void Toaster(String title, String body){
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
		toast.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			
				Geocoder gCoder=new Geocoder(getApplicationContext());
				List<Address> address;
				try {
					address = gCoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
					if(address!=null&&address.size()>0){
						String Street=address.get(0).getLocality();
						
						lat=Double.toString(gps.getLatitude());
						lon=Double.toString(gps.getLongitude());
						altitude=Double.toString(gps.getAltitude());
						String bname = bin_name.getText().toString();
						String desc = bin_desc.getText().toString();
						
						
						//Toaster("Bin Reciept", "Name: "+bname+"\nDescription: "+desc+"\nUser: "+adder+"\nBearing: "+bearing+"\nAltitude: "+altitude+"\nLatitude: "+gps.getLatitude()+"\nLongitude: "+gps.getLongitude()+"\nPlace/Street: "+Street);
						
						Intent in=new Intent(getApplication(),InsertBin.class);
						
						in.putExtra("bin_name", bname);
						in.putExtra("place_name", Street);
						in.putExtra("bin_description", desc);
						in.putExtra("bin_latitude", lat);
						in.putExtra("bin_longitude", lon);
						in.putExtra("bin_altitude", altitude);
						in.putExtra("email_address", email.trim());	
						startActivity(in);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				
			}
		});
		toast.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		toast.show();
	}

}
