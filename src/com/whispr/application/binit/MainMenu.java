package com.whispr.application.binit;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainMenu extends Activity {

	Animation leftIn, rightIn;
	ImageButton add, view, about, settings,radar;
	String user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        build();
    }
	
	public void build(){
		setUpViews();
		setAnimations();
		
		//Animations
		add.startAnimation(leftIn);
		view.startAnimation(rightIn);
		about.startAnimation(leftIn);
		settings.startAnimation(rightIn);
		
		leftIn.setFillAfter(true);
		rightIn.setFillAfter(true);
	}
	
	public void setAnimations(){
		rightIn = AnimationUtils.loadAnimation(this, R.anim.translate_alpha_right_in);
		leftIn = AnimationUtils.loadAnimation(this, R.anim.translate_alpha_left_in);
	}
    
	public void setUpViews(){
		add = (ImageButton)findViewById(R.id.ADD_BINS);
		view = (ImageButton)findViewById(R.id.VIEW_BINS);
		about = (ImageButton)findViewById(R.id.ABOUT_APP);
		settings = (ImageButton)findViewById(R.id.SETTINGS);
		radar=(ImageButton)findViewById(R.id.BIN_RADAR);
	}
	
	public void onClickFeature (View v)
	{
	    user = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.EMAIL, null);
		
	    int id = v.getId ();
	    switch (id) {
	      case R.id.ADD_BINS :
	    	  if(user != null){
	    		  Intent in = new Intent(getApplicationContext(), AddBin.class);
	    		  in.putExtra("email", user);
	    		  startActivity(in);  
	    	  }else{
	    		 Echo("No User Details", "Set Up Email?"); 
	    		 Toast.makeText(getApplicationContext(), "You cannot add a bin without your email set up", Toast.LENGTH_SHORT).show();
	    	  }
	    	  
	           break;
	      case R.id.VIEW_BINS :
	    	  Intent in = new Intent(getApplicationContext(), BinListActivity.class);
    		  in.putExtra("email", user);
    		  startActivity(in); 
	    	  break;
	      case R.id.SETTINGS :
	    	  startActivity(new Intent(this, Settings.class)); 
	    	  break;
	      case R.id.ABOUT_APP :
	    	  startActivity(new Intent(this, AboutBinIT.class));
	    	  break;
	      case R.id.BIN_RADAR:
	    	  startActivity(new Intent(this,Compass.class));
	    	  break;
	      default: 
	    	   break;
	    }
	}
	
	public void Toaster(String title, String body){
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
		toast.show();
	}
	
	public void Echo(String title, String body){
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
        toast.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), Settings.class));
				onPause();
			}
		});
		toast.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		toast.show();
		
	}
}
