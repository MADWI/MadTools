package pl.edu.zut.mad.tools.whereIsCar;

import pl.edu.zut.mad.tools.MainActivity;
import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.compass.Compass;
import pl.edu.zut.mad.tools.converter.Converter;
import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.lightmeter.LightMeter;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.utils.Constans;
import pl.edu.zut.mad.tools.utils.TabCreator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class WhereIsCar extends SherlockActivity implements
		ActionBar.TabListener, OnClickListener, LocationListener {
	private String[] navi_items;
	private boolean tabActive = false;

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

	public static final String TAG = "WhereIsCar";

	// Wy³¹czenie przechodzenia telefonu w stan uœpienia
	// WakeLock
	private WakeLock mWakeLock = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whereiscar);
		navi_items = getResources().getStringArray(R.array.navi_items);

		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Mad Tools");
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabCreator tc = new TabCreator(this);
		tc.createTab(6);

		tabActive = true;

		btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
		btnSaveLocation.setOnClickListener(this);

		button1 = (Button) findViewById(R.id.btnTakeMe);
		button1.setOnClickListener(this);

		TextLongi = (TextView) findViewById(R.id.textTakeLongitude);
		TextLatti = (TextView) findViewById(R.id.textTakeLattitude);

		settings = getSharedPreferences(Constans.GPS_LOCATION_PREFERENCES,
				Activity.MODE_PRIVATE);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (checkGPS())
			refreshGPSstatus();

		// WakeLock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"noiseMeterActivity");

	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
		refreshGPSstatus();

		// WakeLock
		mWakeLock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		restoreData(settings);
		if (checkGPS()) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 10, 10, this);
			refreshGPSstatus();
		}

		// WakeLock
		mWakeLock.acquire();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSaveLocation:
			restoreData(settings);
			saveData(settings, "Dlugosc", (float) longitude);
			saveData(settings, "Szerokosc", (float) lattitude);
			Toast.makeText(
					getApplicationContext(),
					"Zapisano!  \n" + "Dlugoœæ: " + String.valueOf(longitude)
							+ "\n" + "Szerokoœæ: " + String.valueOf(lattitude),
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.btnTakeMe:
			Intent locationMapIntent = new Intent(this, LocationMap.class);
			startActivity(locationMapIntent);
			break;
		}
	}

	private void saveData(SharedPreferences settings, String key, float value) {
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
		if (location != null) {
			longitude = location.getLongitude();
			lattitude = location.getLatitude();

			TextLatti.setText(String.valueOf(lattitude));
			TextLongi.setText(String.valueOf(longitude));
		}
	}

	private Boolean checkGPS() {
		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Log.d(TAG, "GPS disabled");

			Dialog dialog = createAlertDialogDisabledGPS(
					getString(R.string.no_enabled_gps_title),
					getString(R.string.no_enabled_gps_message),
					getString(R.string.yes_button_title),
					getString(R.string.cancel_button_title));
			dialog.show();
			return false;
		} else {
			Log.d(TAG, "GPS enabled");

			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				Log.d(TAG, location.toString());
				TextLatti.setText(String.valueOf(lattitude));
				TextLongi.setText(String.valueOf(longitude));

				this.onLocationChanged(location);

			}
			return true;
		}

	}

	private Dialog createAlertDialogDisabledGPS(String title, String message,
			String buttonPositive, String buttonNegative) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(title);
		dialogBuilder.setMessage(message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(buttonPositive,
				new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent myIntent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(myIntent);
						refreshGPSstatus();
					}
				});
		dialogBuilder.setNegativeButton(buttonNegative,
				new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent main = new Intent(WhereIsCar.this, MainActivity.class);
						startActivity(main);
						finish();
					}
				});

		return dialogBuilder.create();
	}

	private void refreshGPSstatus() {
		// show WiFi status
		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			// Toast.makeText(getApplicationContext(), "GPS Enable!",
			// Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.e("TABCar", tab.getText().toString());
		if (tabActive) {
			if (tab.getText().toString().equals(navi_items[0])) {
				Intent main = new Intent(this, MainActivity.class);
				startActivity(main);
				finish();
			} else if (tab.getText().toString().equals(navi_items[1])) {
				Intent compssIntent = new Intent(this, Compass.class);
				startActivity(compssIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[2])) {
				Intent noiseIntent = new Intent(this, NoiseMeterActivity.class);
				startActivity(noiseIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[3])) {
				Intent inclinometerIntent = new Intent(this, Inclinometer.class);
				startActivity(inclinometerIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[4])) {
				Intent lightMeterIntent = new Intent(this, LightMeter.class);
				startActivity(lightMeterIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[5])) {
				Intent converterIntent = new Intent(this, Converter.class);
				startActivity(converterIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[6])) {

			}
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}