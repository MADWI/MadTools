package pl.edu.zut.mad.tools.compass;

import pl.edu.zut.mad.tools.MainActivity;
import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.converter.Converter;
import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.lightmeter.LightMeter;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.utils.TabCreator;
import pl.edu.zut.mad.tools.whereIsCar.WhereIsCar;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class Compass extends SherlockActivity implements ActionBar.TabListener {
	private String[] navi_items;

	private static SensorManager sensorService;
	private MyCompassView compassView;
	private Sensor sensor;
	private boolean tabActive = false;
	// Wy��czenie przechodzenia telefonu w stan u�pienia
	// WakeLock
	private WakeLock mWakeLock = null;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		compassView = new MyCompassView(this);
		compassView.setBackgroundColor(getResources()
				.getColor(R.color.MadColor));
		setContentView(compassView);

		navi_items = getResources().getStringArray(R.array.navi_items);

		sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (sensor != null) {
			sensorService.registerListener(mySensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");

		} else {
			Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
			Toast.makeText(this, "ORIENTATION Sensor not found",
					Toast.LENGTH_LONG).show();
			finish();
		}

		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Mad Tools");
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabCreator tc = new TabCreator(this);
		tc.createTab(1);

		// WakeLock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "compass");

		tabActive = true;

	}

	// WakeLock
	@Override
	public void onPause() {
		super.onPause();
		mWakeLock.release();
	}

	// WakeLock
	@Override
	public void onResume() {
		super.onResume();
		mWakeLock.acquire();
	}

	private final SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// angle between the magnetic north directio
			// 0=North, 90=East, 180=South, 270=West
			float azimuth = event.values[0];
			compassView.updateData(azimuth);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sensor != null) {
			sensorService.unregisterListener(mySensorEventListener);
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.e("TABkompass", tab.getText().toString());
		if (tabActive) {
			if (tab.getText().toString().equals(navi_items[0])) {
				Intent main = new Intent(this, MainActivity.class);
				startActivity(main);
				finish();
			} else if (tab.getText().toString().equals(navi_items[1])) {

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
				Intent whereIsCarIntent = new Intent(this, WhereIsCar.class);
				startActivity(whereIsCarIntent);
				finish();
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