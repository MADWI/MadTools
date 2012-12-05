package pl.edu.zut.mad.tools.compass;

import pl.edu.zut.mad.tools.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Compass extends Activity {

	private static SensorManager sensorService;
	private MyCompassView compassView;
	private Sensor sensor;
	
    //Wy³¹czenie przechodzenia telefonu w stan uœpienia
	//WakeLock
    private WakeLock mWakeLock = null;	

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		compassView = new MyCompassView(this);		
		compassView.setBackgroundColor(getResources()
				.getColor(R.color.MadColor));
		setContentView(compassView);

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
		//WakeLock
	    PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
	    mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "compass");		
	}
    //WakeLock
    @Override
	public void onPause ()
    {
    	super.onPause();
    	mWakeLock.release();
    }
    //WakeLock    
    @Override
	public void onResume ()
    {
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

}