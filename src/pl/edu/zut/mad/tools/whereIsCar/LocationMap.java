package pl.edu.zut.mad.tools.whereIsCar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.utils.Constans;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class LocationMap extends MapActivity implements LocationListener {
	 
	public SharedPreferences settings;
	SharedPreferences.Editor preferencesEditor;
	
	private TextView locationTextView;
	
	private LocationManager locationManager;
	private MapController mapController;
	private MapView mapView;
	private GeoPoint point;
	private GeoPoint point2;
	
	private float lattitude;
	private float longitude;
	
	private float lattitude2;
	private float longitude2;
	
	private float lattitude_from;
	private float longitude_from;
	private float lattitude_from2;
	private float longitude_from2;
	private float result[] = new float[5];
	
	 MapOverlay mapOvlay;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locationmap);
		
		locationTextView = (TextView) findViewById(R.id.locationTextView);
		settings = getSharedPreferences(Constans.GPS_LOCATION_PREFERENCES, Activity.MODE_PRIVATE);
		
		longitude = loadFloat(settings, "Dlugosc");
		lattitude = loadFloat(settings, "Szerokosc");
		
		locationTextView.setText("Dlugosc: " + String.valueOf(longitude2) + "\n" + "Szerokosc: " + String.valueOf(lattitude2));
		
	    mapView = (MapView) findViewById(R.id.mapview);
		mapView.setSatellite(false);	
	    mapView.setBuiltInZoomControls(true);
        
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 10, 10, this);
	    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    
	    locationTextView.setText("Dlugosc: " + String.valueOf(longitude) + "\n" + "Szerokosc: " + String.valueOf(lattitude));
	    
	    
		longitude2 =  (longitude * 1000000);
		lattitude2 =  (lattitude * 1000000);
		
		point = new GeoPoint((int)longitude2, (int)lattitude2 );
		point2 = new GeoPoint((int)longitude_from2 , (int)lattitude_from2);
		
	    mapController = mapView.getController();
	    mapController.setCenter(point);
	    mapController.setZoom(18);	
	    
	}

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
	}
	
    public static float loadFloat(SharedPreferences preferences, String key) {
    	return preferences.getFloat(key, 0);
    }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		if(location != null){
			
			mapView.getOverlays().clear();
			longitude_from = (float) location.getLongitude();
			lattitude_from= (float) location.getLatitude();
			
			longitude_from2 = longitude_from * 1000000;
			lattitude_from2= lattitude_from * 1000000;
			point2 = new GeoPoint((int)longitude_from2 , (int)lattitude_from2);
			Location.distanceBetween(lattitude_from, longitude_from, lattitude, longitude, result);
			
			locationTextView.setText("Dlugosc: " + String.valueOf(longitude_from) + "\n" + "Szerokosc: " + String.valueOf(lattitude_from) + "  Do auta: " + String.valueOf((int)result[0]) + " m");
			//locationTextView.setText(String.valueOf((int)result[0]+" ")+String.valueOf((int)result[1]+" ")+String.valueOf((int)result[2]+" ")+String.valueOf((int)result[3]+" ") + String.valueOf((int)result[4]+" "));
			
			mapOvlay = new MapOverlay(point, point2);
			mapView.getOverlays().add(mapOvlay);
			
		    mapController.setCenter(point2);
		    mapController.setZoom(16);	
			}
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
}
