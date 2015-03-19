package com.whispr.application.binit;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.whispr.binit.utilities.AlertDialogManager;
import com.whispr.binit.utilities.GPSTracker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class ViewBin extends MapActivity{

	AlertDialogManager alert = new AlertDialogManager();
    
    GPSTracker gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.map);
		
	    MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        
        MapController mc = mapView.getController();
        
        
        Intent i=getIntent();
		Double bin_lat=Double.parseDouble(i.getStringExtra("bin_latitude"));
		Double bin_lon=Double.parseDouble(i.getStringExtra("bin_longitude"));
		String bin_name=i.getStringExtra("bin_name");
		String desc=i.getStringExtra("bin_description");
        
        
        GeoPoint geoPoint = new GeoPoint((int)(bin_lat * 1E6), (int)(bin_lon * 1E6));
        //mc.animateTo(geoPoint);
        mc.setZoom(10);
        mapView.invalidate(); 
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable_bin = this.getResources().getDrawable(R.drawable.mark_blue);
        AddItemizedOverlay itemizedOverlay = new AddItemizedOverlay(drawable_bin, this);
        
        OverlayItem overlayitem = new OverlayItem(geoPoint, bin_name, desc);
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        itemizedOverlay.populateNow();
		
		// Drawable marker icon
		Drawable drawable = this.getResources().getDrawable(R.drawable.mark_red);
		AddItemizedOverlay itemizedOverlay2 = new AddItemizedOverlay(drawable, this);

		mc = mapView.getController();

		// These values are used to get map boundary area
		// The area where you can see all the markers on screen
//		int minLat = Integer.MAX_VALUE;
//		int minLong = Integer.MAX_VALUE;
//		int maxLat = Integer.MIN_VALUE;
//		int maxLong = Integer.MIN_VALUE;
		
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			
			Double longitude=gps.getLatitude();
			Double latitude=gps.getLongitude();
			
			geoPoint = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
			
			// Map overlay item
			overlayitem = new OverlayItem(geoPoint, "Your Location",
					"This is your Location");

			itemizedOverlay2.addOverlay(overlayitem);
			
			
			// calculating map boundary area
//			minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
//		    minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
//		    maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
//		    maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
		    
            mapOverlays.add(itemizedOverlay2);
			
			// showing all overlay items
			itemizedOverlay2.populateNow();
			
		} else {
			// Can't get user's current location
			alert.showAlertDialog(ViewBin.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			// stop executing code by return
			return;
		}
		// Adjusting the zoom level so that you can see all the markers on map
//		mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));
//				
//		// Showing the center of the map
//		mc.animateTo(new GeoPoint((maxLat + minLat)/2, (maxLong + minLong)/2 ));
		mc.animateTo(geoPoint);		
		mapView.postInvalidate();
        
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
