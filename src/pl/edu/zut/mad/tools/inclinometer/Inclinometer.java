package pl.edu.zut.mad.tools.inclinometer;

import pl.edu.zut.mad.tools.R;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Inclinometer extends Activity implements SensorEventListener {

    private TextView xPostv, yPostv, zPostv;
    private SensorManager sensor_manager;
    private long lastUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_inclinometer);

	xPostv = (TextView) findViewById(R.id.xPostv);
	yPostv = (TextView) findViewById(R.id.yPostv);
	zPostv = (TextView) findViewById(R.id.zPostv);

	sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
	lastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
	super.onPause();
	sensor_manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
	super.onResume();
	// register this class as a listener for the orientation and
	// accelerometer sensors
	sensor_manager.registerListener(this,
		sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		sensor_manager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_inclinometer, menu);
	return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	    getAccelerometer(event);
	}
    }

    private void getAccelerometer(SensorEvent event) {
	float[] values = event.values;

	float x = values[0];
	float y = values[1];
	float z = values[2];

	float accelationSquareRoot = (x * x + y * y + z * z)
		/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

	long actualTime = System.currentTimeMillis();

	if (accelationSquareRoot <= 0.6) //
	{
	    xPostv.setText(String.format("%.2f", x));
	    yPostv.setText(String.format("%.2f", y));
	    zPostv.setText(String.format("%.2f", z));
	    
	    if (actualTime - lastUpdate < 200) {
		return;
	    }
	    lastUpdate = actualTime;

	}
    }
}
