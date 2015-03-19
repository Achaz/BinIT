package com.whispr.application.binit;

import com.whispr.binit.utilities.AlertDialogManager;
import com.whispr.binit.utilities.ConnectionDetector;
import com.whispr.binit.utilities.DrawSurfaceView;
import com.whispr.binit.utilities.LocationUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class Compass extends Activity {

	private static final String TAG = "Compass";
	private static boolean DEBUG = false;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private DrawSurfaceView mDrawView;
	LocationManager locMgr;
	ConnectionDetector cd;
	
	ProgressDialog pDialog;
	
	Boolean isInternetPresent = false;
	
	AlertDialogManager alert = new AlertDialogManager();

	private final SensorEventListener mListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			
			if (DEBUG)
				Log.d(TAG, "sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
			if (mDrawView != null) {
				mDrawView.setOffset(event.values[0]);
				mDrawView.invalidate();
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};

	@Override
	protected void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		setContentView(R.layout.radar);
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(Compass.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}else{
			Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
		}

		
		
		
		mDrawView = (DrawSurfaceView) findViewById(R.id.drawSurfaceView);
		
		locMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE); // <2>
		LocationProvider high = locMgr.getProvider(locMgr.getBestProvider(
				LocationUtils.createFineCriteria(), true));

		// using high accuracy provider... to listen for updates
		locMgr.requestLocationUpdates(high.getName(), 0, 0f,
				new LocationListener() {
			
					public void onLocationChanged(Location location) {
						// do something here to save this new location
						Log.d(TAG, "Location Changed");
						mDrawView.setMyLocation(location.getLatitude(), location.getLongitude());
						mDrawView.invalidate();
						
					}

					public void onStatusChanged(String s, int i, Bundle bundle) {

					}

					public void onProviderEnabled(String s) {
						// try switching to a different provider
					}

					public void onProviderDisabled(String s) {
						// try switching to a different provider
					}
				});

	}

	@Override
	protected void onResume() {
		if (DEBUG)
			Log.d(TAG, "onResume");
		super.onResume();

		mSensorManager.registerListener(mListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		if (DEBUG)
			Log.d(TAG, "onStop");
		mSensorManager.unregisterListener(mListener);
		super.onStop();
	}
}