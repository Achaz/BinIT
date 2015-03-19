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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteBin extends Activity {

	int id;
    String email, b_name, text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent in = getIntent();
		id = Integer.parseInt(in.getStringExtra("bin_id"));
		email = in.getStringExtra("email_address");
		b_name = in.getStringExtra("bin_name");
		
		Echo("Are you sure you want to delete this?","ID: "+id+"\n Bin name: "+b_name +" \nBy: "+email);
		
	}
	
	public  void postData() throws JSONException{  
		
		try {
		     	Intent i=getIntent();
		    	String id=i.getStringExtra("bin_id");
			    String email=i.getStringExtra("email_address");
			    
			    HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://cipher256.com/binITproject/app/delete_bin.php?bin_id="+id+"&email_address="+email);
				
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
			} catch (IOException e) {
	    		// TODO Auto-generated catch block
	   	}
	}

	
	public void Echo(String title, String body){
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
        toast.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					postData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
				startActivity(new Intent(getApplicationContext(), BinListActivity.class));
				finish();
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
}
