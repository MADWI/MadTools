package pl.edu.zut.mad.tools.whereIsCar;

import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.utils.Constans;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WhereIsCar extends Activity implements OnClickListener, LocationListener {
	
	private Button btnSaveLocation;
	private Button button1;
	
	double lattitude;
	double longitude;
	
	public String test1;
	public String test2 = "";
	
	private LocationManager locationManager;
	private Location location;
	
	private TextView TextLongi;
	private TextView TextLatti;
	
	public SharedPreferences settings;
	SharedPreferences.Editor preferencesEditor;
	
	public static String TAG = "Michazzz";

    //Wy³¹czenie przechodzenia telefonu w stan uœpienia
	//WakeLock
    private WakeLock mWakeLock = null;		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whereiscar);
		
		btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
		btnSaveLocation.setOnClickListener(this);
		
		button1 = (Button) findViewById(R.id.btnTakeMe);
		button1.setOnClickListener(this);
	
		TextLongi = (TextView) findViewById(R.id.textTakeLongitude);
		TextLatti = (TextView) findViewById(R.id.textTakeLattitude);
		
		settings = getSharedPreferences(Constans.GPS_LOCATION_PREFERENCES, Activity.MODE_PRIVATE);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 10, 10, this);
	    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    	if (location != null) {
			      Log.d(TAG, location.toString());
				  TextLatti.setText( String.valueOf(lattitude));
				  TextLongi.setText(String.valueOf(longitude));
		
			      this.onLocationChanged(location);
			    }
			//WakeLock
		    PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		    mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "noiseMeterActivity");		
	    	
		}
	
	@Override
	protected void onPause() {
		locationManager.removeUpdates(this);
		super.onPause();

		//WakeLock
		mWakeLock.release();
	}
	
	@Override
	protected void onResume() {
		restoreData(settings);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 10, 10, this);
		super.onResume();
		//WakeLock
		mWakeLock.acquire();			
	}

	
	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btnSaveLocation:		
			restoreData(settings);
			saveData(settings, "Dlugosc", (float)longitude);
			saveData(settings, "Szerokosc", (float)lattitude);
			Toast.makeText( getApplicationContext(),"Zapisano!  \n" + "Dlugoœæ: "+ String.valueOf(longitude) + "\n" + "Szerokoœæ: " + String.valueOf(lattitude) ,	Toast.LENGTH_SHORT ).show();
			break;
			
		case R.id.btnTakeMe:
		    Intent locationMapIntent = new Intent(this, LocationMap.class);
		    startActivity(locationMapIntent);
		    break;
		}
	}
    
    private void saveData(SharedPreferences settings,String key, float value) {
    	preferencesEditor = settings.edit();
    	preferencesEditor.putFloat(key, value);
    	preferencesEditor.commit();
    }
    
    
    private void restoreData(SharedPreferences settings) {
    	preferencesEditor = settings.edit();
    	preferencesEditor.clear();
    	preferencesEditor.commit();
    }


		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLocationChanged(Location location) {
			if(location != null)				{
				longitude = location.getLongitude();
				lattitude = location.getLatitude();
				
				 TextLatti.setText( String.valueOf(lattitude));
				 TextLongi.setText(String.valueOf(longitude));
				}
		}
		
	}