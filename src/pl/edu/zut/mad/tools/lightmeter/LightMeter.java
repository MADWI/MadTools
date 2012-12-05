package pl.edu.zut.mad.tools.lightmeter;

import org.achartengine.GraphicalView;

import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.utils.GraphPoint;
import pl.edu.zut.mad.tools.utils.LinearGraph;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LightMeter extends Activity implements SensorEventListener {

	private TextView readValue;
	private Sensor lightSensor;
	private SensorManager sensor_manager;

	private double luxValue = 0.0;

	private static GraphicalView view;
	private LinearGraph lineGraph;
	private int count = 0;

    //Wy³¹czenie przechodzenia telefonu w stan uœpienia
	//WakeLock
    private WakeLock mWakeLock = null;		
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_meter);

		lineGraph = new LinearGraph(getString(R.string.light_graph_title), "",
				"lx", 400f, 0.0f, 100.0f, 0.0f);

		readValue = (TextView) findViewById(R.id.readVal);

		sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lightSensor = sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);

		//WakeLock
	    PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
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
		//WakeLock
		mWakeLock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensor_manager.registerListener(this, lightSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		//WakeLock
		mWakeLock.acquire();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_light_meter, menu);
		return true;
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
}
