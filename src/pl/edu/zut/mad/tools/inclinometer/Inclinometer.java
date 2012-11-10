package pl.edu.zut.mad.tools.inclinometer;

import pl.edu.zut.mad.tools.R;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Simple class for accelerometer
 * 
 * @author Dawid Glinski
 * @version 1.1.0
 * */
public class Inclinometer extends Activity implements SensorEventListener {

    private static final String TAG = "Inclinometer";

    /** Variable which contain phone x position */
    private TextView xPostv;
    /** Variable which contain phone y position */
    private TextView yPostv;
    /** Variables which contain phone z position */
    private TextView zPostv;

    /** Variable which contain angle value */
    private TextView angle;

    /** Object responsible for access to accelerometer */
    private SensorManager sensor_manager;

    /** Variable which contain updating time in miliseconds */
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

	angle = (TextView) findViewById(R.id.angle_value);

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
		SensorManager.SENSOR_DELAY_NORMAL);
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
	    computeAngle(event);
	}
    }

    /**
     * Computing angle from phone movement
     * 
     * @param event
     *            phone movement
     */
    private void computeAngle(SensorEvent event) {
	float[] values = event.values;

	float x = values[0];
	float y = values[1];
	float z = values[2];

	float accelationSquareRoot = (x * x + y * y + z * z)
		/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

	// ----------computing angle (need to fix)-------------------
	// [10 0 0] - vector of phone coordinates (orientation: horizontal)

	// computing angle cosinus between two vectors
	float angle1 = (x * 10 + y * 0 + z * 0)
		/ ((FloatMath.sqrt((x * x) + (y * y) + (z * z))) * (FloatMath
			.sqrt(10 * 10)));
	float kat = 1 / angle1;
	Log.e(TAG, "kat: " + kat);
	// ----------------------------------------------------------

	long actualTime = System.currentTimeMillis();

	if (accelationSquareRoot > 0.5) //
	{
	    xPostv.setText(String.format("%.1f", x));
	    yPostv.setText(String.format("%.1f", y));
	    zPostv.setText(String.format("%.1f", z));

	    angle.setText(String.format("%.2f", kat));
	    Log.d(TAG, "cos: " + angle1);

	    if (actualTime - lastUpdate < 500) {
		return;
	    }
	    lastUpdate = actualTime;
	}
    }
}
