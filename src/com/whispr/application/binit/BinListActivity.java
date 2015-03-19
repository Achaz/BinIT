package com.whispr.application.binit;

import java.util.ArrayList;
import java.util.HashMap;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whispr.binit.utilities.JSONParser;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
 
public class BinListActivity extends Activity{
 
    // url to make request
    private static String url = "http://cipher256.com/binITproject/app/get_bins.php?mode=all";
 
    // JSON Node names
    private static final String TAG_BINS = "bins";
    private static final String TAG_ID = "bin_id";
    private static final String TAG_BNAME = "bin_name";
    private static final String TAG_PNAME = "place_name";
    private static final String TAG_LAT = "bin_latitude";
    private static final String TAG_LON = "bin_longitude";
    private static final String TAG_DESC = "bin_description";
    // static final String TAG_ALTITUDE = "bin_altitude";
    private static final String TAG_EMAIL = "email_address";
    
    private ProgressDialog pd;
   
    TextView name, desc;
	EditText search_box;
	ImageView go_button, expand_button;
	ListView bins_l;
	Animation rotate_list = null;
	ArrayList<HashMap<String, String>> binList;
	//HashMap Values
	String id = "";
    String b_name = "";
    String b_desc = "";
    String lat = "";
    String lon = "";
    String place, email;
    String emailAdd="";
    int pos = 0;
    // contacts JSONArray
    JSONArray bins = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bins_list);
 
       build();
 
    }
    
    public void build(){
		setUpViews();
		setUpListeners();
		Intent in = getIntent();
	    emailAdd = in.getStringExtra("email");
		//pd = ProgressDialog.show(getApplicationContext(), "Working...", "Fetching Bins Near You");
        loadBinList();
       
	}
	
	public void setUpViews(){
		bins_l = (ListView)findViewById(R.id.BINS_LIST);
		name = (TextView)findViewById(R.id.BIN_LIST_HEAD);
		desc = (TextView)findViewById(R.id.BIN_LIST_DESC);
		expand_button = (ImageView)findViewById(R.id.BIN_LIST_EXPAND);
	}
	
	public void setUpListeners(){
		//go_button.setOnClickListener(goListener);
	}
	
	public void setUpAnimations(){
		//rotate_list = AnimationUtils.loadAnimation(this, R.anim.listloader_anim);	
	}
	
	public void loadBinList(){
		//bins_l.startAnimation(rotate_list);
		
		// Hashmap for ListView
        binList = new ArrayList<HashMap<String, String>>();
       
        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();
        @SuppressWarnings("static-access")
		JSONObject json = jParser.getJSONfromURL(url);
        
//        Toaster("JSON FROM DB",json.toString());
        
        
     if(json != null){
       try {
        
            bins = json.getJSONArray(TAG_BINS);
            // looping through All Bins
            for(int i = 0; i < bins.length(); i++){
                JSONObject c = bins.getJSONObject(i);
 
                // Storing each json item in variable
                id = c.getString(TAG_ID);
                b_name = c.getString(TAG_BNAME);
                b_desc = c.getString(TAG_DESC);
                lat=c.getString(TAG_LAT);
                lon=c.getString(TAG_LON);
                
                place = c.getString(TAG_PNAME);
                email = c.getString(TAG_EMAIL);
                

 
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
 
                // adding each child node to HashMap key => value
                map.put(TAG_ID, id);
                map.put(TAG_BNAME, b_name);
                map.put(TAG_DESC, b_desc);
                map.put(TAG_PNAME, place);
                map.put(TAG_EMAIL, email);
                map.put(TAG_LAT, lat);
                map.put(TAG_LON, lon);
               // map.put(TAG_ALTITUDE, alt);
 
                // adding HashList to ArrayList
                binList.add(map);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "No Data Gotten\n "+e.getMessage(), Toast.LENGTH_LONG).show();
           // finish();
        }
  }else{
     Toast.makeText(this, "Failure to connect to Servers", Toast.LENGTH_LONG).show();
  }//0782 132866
    /**
     * Updating parsed JSON data into ListView
     * */
    ListAdapter adapter = new SimpleAdapter(this, binList, R.layout.bin_list_layout, new String[] { TAG_BNAME, TAG_DESC}, new int[] { R.id.BIN_LIST_HEAD, R.id.BIN_LIST_DESC});
    bins_l.setAdapter(adapter);

        
        bins_l.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
        		pos = position;
        		String choice[] = {"View", "Edit", "Delete", "Back"};
        		String choice2[] = {"View", "Back"};
        		
        		if(emailAdd != null && emailAdd.equals(binList.get(pos).get(TAG_EMAIL).trim())){
        			
        		AlertDialog.Builder opt = new AlertDialog.Builder(BinListActivity.this);	
				   opt.setItems(choice, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
//							Toast.makeText(getApplicationContext(), "Viewing...", Toast.LENGTH_LONG);
							Intent i=new Intent(BinListActivity.this,ViewBin.class);
							i.putExtra(TAG_ID, binList.get(pos).get(TAG_ID));
							i.putExtra(TAG_LAT, binList.get(pos).get(TAG_LAT));
							i.putExtra(TAG_LON, binList.get(pos).get(TAG_LON));
							i.putExtra(TAG_BNAME, binList.get(pos).get(TAG_BNAME));
							i.putExtra(TAG_DESC, binList.get(pos).get(TAG_DESC));
							startActivity(i);
							
							
							break;
						case 1:
							Intent in=new Intent(getApplication(),EditBin.class);
							in.putExtra(TAG_LAT, binList.get(pos).get(TAG_LAT));
							in.putExtra(TAG_LON, binList.get(pos).get(TAG_LON));
							in.putExtra(TAG_BNAME, binList.get(pos).get(TAG_BNAME));
							in.putExtra(TAG_DESC, binList.get(pos).get(TAG_DESC));
							in.putExtra(TAG_EMAIL, binList.get(pos).get(TAG_EMAIL));	
							in.putExtra(TAG_PNAME, binList.get(pos).get(TAG_PNAME));
							in.putExtra(TAG_ID, binList.get(pos).get(TAG_ID));
							startActivity(in);
							break;
						case 2:
							Intent in2=new Intent(getApplication(),DeleteBin.class);
							in2.putExtra(TAG_ID, binList.get(pos).get(TAG_ID));
							in2.putExtra(TAG_BNAME, binList.get(pos).get(TAG_BNAME));
							in2.putExtra(TAG_EMAIL, binList.get(pos).get(TAG_EMAIL));
							startActivity(in2);
							break;
						case 3:
							dialog.cancel();
							break;	
						}
						
					}
				});
				opt.setTitle("Options");
				   
				opt.show();
        	}
        	else{
        	   
        		AlertDialog.Builder opt = new AlertDialog.Builder(BinListActivity.this);	
				   opt.setItems(choice2, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							Intent i=new Intent(getApplication(),ViewBin.class);
							i.putExtra(TAG_LAT, binList.get(pos).get(TAG_LAT));
							i.putExtra(TAG_LON, binList.get(pos).get(TAG_LON));
							i.putExtra(TAG_BNAME, binList.get(pos).get(TAG_BNAME));
							i.putExtra(TAG_DESC, binList.get(pos).get(TAG_DESC));
							startActivity(i);
							break;
						case 1:
							dialog.cancel();
							break;
						}
						
					}
				});
				opt.setTitle("Options");
				   
				opt.show();
        	}
			
        	}
		});
	}
	
	View.OnClickListener goListener = new View.OnClickListener() {
			
			public void onClick(View arg0) {
			   Toast.makeText(getApplicationContext(), "Re-Loading list", Toast.LENGTH_LONG).show();	
			}
	};
		
	public void Toaster(String title, String body){
		final AlertDialog.Builder toast = new AlertDialog.Builder(this);
		toast.setTitle(title);
		toast.setMessage(body);
		toast.show();
	} 		
 
}