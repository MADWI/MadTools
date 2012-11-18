package pl.edu.zut.mad.tools.whereIsCar;

import pl.edu.zut.mad.tools.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;



public class locationMapDrawer extends MapActivity {
	
	private TextView TextLongi;
	private TextView TextLatti;
	private TextView locationTextView;
	public SharedPreferences settings;
	
	private String Lattitude2;
	private String Longitude2;
	public String StoredValue;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locationmapdrawer);
		
		TextLongi = (TextView) findViewById(R.id.textTakeLongitude);
		TextLatti = (TextView) findViewById(R.id.textTakeLattitude);
		locationTextView = (TextView) findViewById(R.id.locationTextView);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		StoredValue = settings.getString("STOREDVALUE", "");
		locationTextView.setText(StoredValue.toString());
		//TextLongi.setText(String.valueOf(settings.getString(localizationSettings, Longitude2)));
		
		

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}