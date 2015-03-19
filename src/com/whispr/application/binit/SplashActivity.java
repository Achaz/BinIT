package com.whispr.application.binit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timer = new Thread(){
        	public void run(){
        		try{
        			sleep(4000);
        		}catch(InterruptedException e){
        			e.printStackTrace();
        		}finally{
        			Intent start = new Intent("com.whispr.application.binit.SplashActivity");
        			start.setClass(getApplicationContext(), MainMenu.class);
        			startActivity(start);
        		}
        	}
        };
        timer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash, menu);
        return true;
    }

    
}
