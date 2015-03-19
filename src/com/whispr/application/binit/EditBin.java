package com.whispr.application.binit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whispr.binit.utilities.AlertDialogManager;
import com.whispr.binit.utilities.GPSTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditBin extends Activity {

	Button edit_bin;
	EditText bin_name, bin_desc;
	String id, b_name, b_desc, email, bin_lat, bin_lon, bin_alt, place, text;
	GPSTracker gps;
	AlertDialogManager alert = new AlertDialogManager();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bin);
        build();
    }
	
	public void build(){
		setUpViews();
		setUpListeners();
		
		Intent in = getIntent();
		b_name = in.getStringExtra("bin_name");
		b_desc = in.getStringExtra("bin_description");
		bin_lat = in.getStringExtra("bin_latitude");
		bin_lon = in.getStringExtra("bin_longitude");
		bin_alt = in.getStringExtra("bin_altitude");
		id = in.getStringExtra("bin_id");
		place = in.getStringExtra("place_name");
		email = in.getStringExtra("email_address");
		
		edit_bin.setText("Edit Bin");
		bin_name.setText(b_name);
		bin_desc.setText(b_desc);
		
		Toaster("Are you sure you want to edit?", "ID: "+id+"\nNAME: "+ b_name+"\nDESC: "+ b_desc+"\nEMAIL: "+ email+"\nLAT: "+ bin_lat+"\nLON: "+bin_lon+"\nALT: "+ bin_alt+"\nPLACE: "+ place);
	}
	public void setUpViews(){
		edit_bin = (Button)findViewById(R.id.ADD_BIN_BTN);
		bin_name = (EditText)findViewById(R.id.BIN_NAME);
		bin_desc = (EditText)findViewById(R.id.BIN_DESCRIPTION);	
	}
	
	public void setUpListeners(){
		edit_bin.setOnClickListener(editBinListener);
	}

    
	View.OnClickListener editBinListener = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			
			gps = new GPSTracker(getApplicationContext());

			// check if GPS location can get
	 if (gps.canGetLocation()) {
		
		bin_lat=Double.toString(gps.getLatitude());
		bin_lon=Double.toString(gps.getLongitude());
		bin_alt=Double.toString(gps.getAltitude());
		b_name = bin_name.getText().toString();
		String temp = bin_desc.getText().toString().replace(" ", "%2"); 
		b_desc = temp;
	
		Geocoder gCoder=new Geocoder(getApplicationContext());
		List<Address> address;
	     try{
				address = gCoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
				if(address!=null&&address.size()>0){
					place=address.get(0).getLocality();				
					
					ToastReciept("Edit Reciept", "ID: "+id+"\nNAME: "+ bin_name.getText().toString()+
							     "\nDESC: "+ bin_desc.getText().toString()+"EMAIL: \n"+ email+"\nLAT: "+ bin_lat+
							     "\nLON: "+bin_lon+"\nALT: "+ bin_alt+"\nPLACE: "+ place);
				}
				else{
					Toast.makeText(getApplicationContext(), "Cannot get Street name", Toast.LENGTH_LONG).show();
				}
			
	        }catch(IOException e){
	        	Toast.makeText(getApplicationContext(), "Cannot fetch bin information", Toast.LENGTH_LONG).show();		
	        }
	  }else{
		// Can't get user's current location
		alert.showAlertDialog(EditBin.this, "GPS Status",
				"Couldn't get location information. Please enable GPS",
				false);
		// stop executing code by return
		return;
	}					
			
		}
	};
	
    public void ToastReciept(String title, String body){
		
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
		toast.setPositiveButton("Continue", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					postData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getBaseContext(), "Unable to post edit", Toast.LENGTH_LONG).show();
				}
				
				startActivity(new Intent(getApplicationContext(), BinListActivity.class));
				finish();
			}
		});
		toast.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		toast.show();
	}
	
	public void Toaster(String title, String body){
		
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
		toast.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		toast.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), BinListActivity.class));
				finish();
			}
		});
		toast.show();
	}
	
	public void postData() throws JSONException{  
		
		
		try {
							
		     	HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://cipher256.com/binITproject/app/edit_bin.php?bin_id="+id+"&bin_name="+b_name+
						                         "&bin_description="+b_desc+"&email_address="+email+"&bin_latitude="+bin_lat+
						                         "&bin_longitude="+bin_lon+"&place_name="+place+"&bin_altitude="+bin_alt);
			
				HttpResponse response = httpclient.execute(httppost);

				// for JSON:
				if(response != null)
				{
					InputStream is = response.getEntity().getContent();

					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();

					String line = null;
					try {
						while ((line = reader.readLine()) != null) {
							
							sb.append(line + "\n");
						}
					} catch (IOException e) {
					
    					e.printStackTrace();
						
					} finally {
						
					try {
							
							is.close();
							
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
					text = sb.toString();
				}
				JSONObject res = new JSONObject(text);
				String output = res.getString("message");
				//tv.setText(text);
				Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
		  }catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
	      }
		catch (IOException e) {
	    		// TODO Auto-generated catch block
	   	}
	}
}
