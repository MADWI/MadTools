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

/**
 * Simple class for inclinometer
 * 
 * @author Dawid Glinski
 * @version 1.1.0
 * */
public class Inclinometer extends Activity implements SensorEventListener {

    private DrawInclinometer drawInclinometer;

    /** Object responsible for access to accelerometer */
    private SensorManager sensor_manager;

    private float[] inR = new float[16];
    private float[] I = new float[16];
    private float[] gravity = new float[3];
    private float[] geomag = new float[3];
    private float[] orientVals = new float[3];

    private double azimuth = 0.0; // angle around the z-axis
    private double pitch = 0.0; // angle around the x-axis
    private double roll = 0.0; // angle around the y-axis

    @Override
    public void onCreate(Bundle savedInstanceState) {

	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	super.onCreate(savedInstanceState);
	drawInclinometer = new DrawInclinometer(this);
	drawInclinometer.setBackgroundColor(getResources().getColor(
		R.color.MadColor));
	sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
	setContentView(drawInclinometer);
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

	sensor_manager.registerListener(this,
		sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
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

	azimuth = (azimuth + 360.0) % 360.0;
	pitch = (pitch + 360.0) % 360.0;
	roll = (roll + 360.0) % 360.0;

	drawInclinometer.updateData(pitch);

    }
}
