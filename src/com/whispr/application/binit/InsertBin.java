package com.whispr.application.binit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class InsertBin extends Activity {

	TextView tv;
	String text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		
		tv 	= (TextView)findViewById(R.id.textview);
		text 	= "";

		try {
			postData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
		}
	}
		public void postData() throws JSONException{  
			
		try {
				Intent i=getIntent();
				String bname=i.getStringExtra("bin_name");
				String temp = i.getStringExtra("bin_description").replace(" ", "%2");
				String desc=temp;
				String adder=i.getStringExtra("email_address");
				String lat=i.getStringExtra("bin_latitude");
				String lon=i.getStringExtra("bin_longitude");
				String place_name=i.getStringExtra("place_name");
				String altitude=i.getStringExtra("bin_altitude");
			
			    HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://cipher256.com/binITproject/app/add_bin.php?bin_name="+bname+"&bin_description="+desc+
						                         "&email_address="+adder+"&bin_latitude="+lat+"&bin_longitude="+lon+
						                         "&place_name="+place_name);
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
				
				Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
				finish();
//				tv.setText(text);
		}catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
	    		// TODO Auto-generated catch block
	   	}
	}
	
	public void Toaster(String title, String body){
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
		toast.show();
	} 
	

}
