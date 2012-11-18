package pl.edu.zut.mad.tools.whereIsCar;

import java.util.Map;

import pl.edu.zut.mad.tools.R;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class whereIsCar extends Activity implements OnClickListener {
	
	private Button btnSaveLocation;
	private Button takeMetoMyCar;
	double lattitude;
	double longitude;
	
	private String Lattitude;
	private String Longitude;
	
	public String test1;
	public String test2 = "";
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private TextView TextLongi;
	private TextView TextLatti;
	public SharedPreferences settings;
	
	myLocationListener obj = new myLocationListener();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whereiscar);
		
		btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
		btnSaveLocation.setOnClickListener(this);
		
		takeMetoMyCar = (Button) findViewById(R.id.takeMeToMyCar);
		takeMetoMyCar.setOnClickListener(this);
		
		
		
	    settings = PreferenceManager.getDefaultSharedPreferences(this);
	    settings.getBoolean("keystring", true);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new myLocationListener();
		
		TextLongi = (TextView) findViewById(R.id.textTakeLongitude);
		TextLatti = (TextView) findViewById(R.id.textTakeLattitude);
		
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		

	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btnSaveLocation:
			//SharedPreferences settings = getSharedPreferences(localizationSettings, Activity.MODE_PRIVATE);
			settings = saveData(settings);
			test1 = settings.getString("myPrefs", "");
			TextLatti.setText(String.valueOf(longitude));
			TextLongi.setText(String.valueOf(lattitude));
			Toast.makeText( getApplicationContext(), settings.getString("STOREDVALUE", ""),	Toast.LENGTH_SHORT ).show();
			break;
			
		case R.id.takeMeToMyCar:
			Intent locationMapDrawerIntent = new Intent(this, locationMapDrawer.class);
			startActivity(locationMapDrawerIntent);
			break;

		}
	}
	
    private SharedPreferences saveData(SharedPreferences settings) {
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.putString("STOREDVALUE", Longitude);
        preferencesEditor.putString("STOREDVALUE", Lattitude);
        preferencesEditor.commit();
        return settings;
    }
    
    private SharedPreferences restoreData(SharedPreferences settings) {
        String textFromPreferences = settings.getString("myPrefs", "");
		TextLatti.setText(" ");
		TextLongi.setText(" ");
        return settings;
    }
	
	public class myLocationListener implements LocationListener
	{	

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText( getApplicationContext(),"Gps Disabled",	Toast.LENGTH_SHORT ).show();
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText( getApplicationContext(),"Gps Enabled",	Toast.LENGTH_SHORT ).show();
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLocationChanged(Location location) {
			longitude = location.getLongitude();
			lattitude = location.getLatitude();
		}
		
	}
}
