package pl.edu.zut.mad.tools.whereIsCar;

import pl.edu.zut.mad.tools.R;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.Menu;

public class whereIsCar extends Activity implements OnClickListener {
	
	private Button btnSaveLocation;
	double lattitude;
	double longitude;
	
	private String Lattitude;
	private String Longitude;
	
	private Location location;
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private TextView TextLongi;
	private TextView TextLatti;
	
	myLocationListener obj = new myLocationListener();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whereiscar);
		
		btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
		btnSaveLocation.setOnClickListener(this);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new myLocationListener();
		
		TextLongi = (TextView) findViewById(R.id.textTakeLongitude);
		TextLatti = (TextView) findViewById(R.id.textTakeLattitude);
		
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 10000, 10, locationListener);


	}
	
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSaveLocation:
			Longitude = " " + longitude;
			Lattitude = " " + lattitude;
			TextLatti.setText(Lattitude);
			TextLongi.setText(Longitude);
			break;

		}

	}
	
	public class myLocationListener implements LocationListener
	{	

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
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
