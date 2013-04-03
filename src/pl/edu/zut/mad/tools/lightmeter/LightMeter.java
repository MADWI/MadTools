package pl.edu.zut.mad.tools.lightmeter;

import org.achartengine.GraphicalView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

import pl.edu.zut.mad.tools.MainActivity;
import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.compass.Compass;
import pl.edu.zut.mad.tools.converter.Converter;
import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.noise.meter.MicrophoneInputListener;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.utils.GraphPoint;
import pl.edu.zut.mad.tools.utils.LinearGraph;
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
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LightMeter extends SherlockActivity implements
		ActionBar.TabListener, SensorEventListener {
	private String[] navi_items;
	private boolean tabActive = false;

	private TextView readValue;
	private Sensor lightSensor;
	private SensorManager sensor_manager;

	private double luxValue = 0.0;

	private static GraphicalView view;
	private LinearGraph lineGraph;
	private int count = 0;

	// Wy³¹czenie przechodzenia telefonu w stan uœpienia
	// WakeLock
	private WakeLock mWakeLock = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_meter);
		navi_items = getResources().getStringArray(R.array.navi_items);
		lineGraph = new LinearGraph(getString(R.string.light_graph_title), "",
				"lx", 400f, 0.0f, 100.0f, 0.0f);

		readValue = (TextView) findViewById(R.id.readVal);

		sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lightSensor = sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);

		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Mad Tools");
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabCreator tc = new TabCreator(this);
		tc.createTab(4);

		tabActive = true;
		// WakeLock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "lightMeter");

	}

	@Override
	protected void onStart() {
		super.onStart();
		view = lineGraph.getView(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayoutLight);
		layout.addView(view);

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
		sensor_manager.registerListener(this, lightSensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		// WakeLock
		mWakeLock.acquire();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			luxValue = event.values[0];
			readValue.setText(String.valueOf(Math.round(luxValue) + " lx"));
			count++;
			GraphPoint p = new GraphPoint(count, luxValue);
			lineGraph.addNewPoints(p);

			if (p.getX() > 100) {
				lineGraph.setXAxisMin(p.getX() - 100);
				lineGraph.setXAxisMax(p.getX());
			}

			view.repaint();
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.e("TABLight", tab.getText().toString());
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
