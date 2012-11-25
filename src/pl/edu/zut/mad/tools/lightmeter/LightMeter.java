package pl.edu.zut.mad.tools.lightmeter;

import pl.edu.zut.mad.tools.R;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LightMeter extends Activity implements SensorEventListener {

    private TextView readValue;
    private Sensor lightSensor;
    private SensorManager sensor_manager;

    private double luxValue = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_light_meter);

	readValue = (TextView) findViewById(R.id.readVal);

	sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
	lightSensor = sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onPause() {
	super.onPause();
	sensor_manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
	super.onResume();
	sensor_manager.registerListener(this, lightSensor,
		SensorManager.SENSOR_DELAY_FASTEST);
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
	    readValue.setText(String.valueOf(luxValue));
	}
    }
}
