package pl.edu.zut.mad.tools.inclinometer;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

import pl.edu.zut.mad.tools.MainActivity;
import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.compass.Compass;
import pl.edu.zut.mad.tools.converter.Converter;
import pl.edu.zut.mad.tools.lightmeter.LightMeter;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.utils.TabCreator;
import pl.edu.zut.mad.tools.whereIsCar.WhereIsCar;
import android.app.Activity;
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
import android.view.Window;
import android.view.WindowManager;

/**
 * Simple class for inclinometer
 * 
 * @author Dawid Glinski
 * @version 1.1.0
 * */
public class Inclinometer extends SherlockActivity implements
		ActionBar.TabListener, SensorEventListener {
	private String[] navi_items;
	private boolean tabActive = false;
	private DrawInclinometer drawInclinometer;

	/** Object responsible for access to accelerometer */
	private SensorManager sensor_manager;

	private final float[] inR = new float[16];
	private final float[] I = new float[16];
	private float[] gravity = new float[3];
	private float[] geomag = new float[3];
	private final float[] orientVals = new float[3];

	private double azimuth = 0.0; // angle around the z-axis
	private double pitch = 0.0; // angle around the x-axis
	private double roll = 0.0; // angle around the y-axis

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
		tc.createTab(3);

		tabActive = true;

		drawInclinometer = new DrawInclinometer(this);
		drawInclinometer.setBackgroundColor(getResources().getColor(
				R.color.MadColor));
		sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(drawInclinometer);

		// WakeLock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "inclinometer");

	}

	@Override
	protected void onPause() {
		super.onPause();
		sensor_manager.unregisterListener(this);

		// WakeLock
		mWakeLock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensor_manager.registerListener(this,
				sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		sensor_manager.registerListener(this,
				sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);

		// WakeLock
		mWakeLock.acquire();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/** Called when phone moves */
	@Override
	public void onSensorChanged(SensorEvent event) {

		// If the sensor data is unreliable return
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
			return;

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			gravity = event.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			geomag = event.values.clone();
			break;
		}
		// If gravity and geomag have values then find rotation matrix
		if (gravity != null && geomag != null) {

			// checks that the rotation matrix is found
			boolean success = SensorManager.getRotationMatrix(inR, I, gravity,
					geomag);
			if (success) {
				SensorManager.getOrientation(inR, orientVals);

				azimuth = Math.toDegrees(orientVals[0]);
				pitch = Math.toDegrees(orientVals[1]);
				roll = Math.toDegrees(orientVals[2]);

			}
		}

		drawInclinometer.updateData(pitch);

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
