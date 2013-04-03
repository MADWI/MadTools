package pl.edu.zut.mad.tools.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
/**
 * Class for checking sensors and other hardware devices.
 * 
 * @author Sebastian Swierczek
 * 
 * @version 1.0.0
 */
public class Checker {

	static private List<Sensor> sensorsList = null;

	private Checker()
	{
		
	}
	/**
	 * Get list of all available sensors.
	 * 
	 * @param ctx
	 *            Application context
	 * @return all available sensors list.
	 */
	static public List<Sensor> getAllSensorsList(Context ctx) {
		if (sensorsList == null)
			populateSensorsList(ctx);
		return sensorsList;
	}

	/**
	 * Checking accelerometer availability.
	 * 
	 * @param ctx
	 *            Application context
	 * @return true if device have accelerometer otherwise false
	 */
	public static boolean haveAccelerometr(Context ctx) {

		if (sensorsList == null)
			populateSensorsList(ctx);
		return sensorsList.contains(Sensor.TYPE_ACCELEROMETER) ? true : false;
	}

	/**
	 * Checking magnetometr availability.
	 * 
	 * @param ctx
	 *            Application context
	 * @return true if device have magnetometr otherwise false
	 */
	public static boolean haveMagnetometr(Context ctx) {

		if (sensorsList == null)
			populateSensorsList(ctx);
		return sensorsList.contains(Sensor.TYPE_MAGNETIC_FIELD) ? true : false;
	}

	/**
	 * Checking gyroscope availability.
	 * 
	 * @param ctx
	 *            Application context
	 * @return true if device have gyroscope otherwise false
	 */
	public static boolean haveGyroscope(Context ctx) {

		if (sensorsList == null)
			populateSensorsList(ctx);
		return sensorsList.contains(Sensor.TYPE_GYROSCOPE) ? true : false;
	}

	/**
	 * Checking light sensor availability.
	 * 
	 * @param ctx
	 *            Application context
	 * @return true if device have light sensor otherwise false
	 */
	public static boolean haveLightSensor(Context ctx) {

		if (sensorsList == null)
			populateSensorsList(ctx);
		return sensorsList.contains(Sensor.TYPE_LIGHT) ? true : false;
	}

	/**
	 * Checking flash light availability.
	 * 
	 * @param ctx
	 *            Application context
	 * @return true if device have flash light otherwise false
	 */
	public static boolean haveFlashLight(Context ctx) {
		return ctx.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);
	}

	/**
	 * Checking if GPS is enabled.
	 * 
	 * @param ctx
	 *            Application context
	 * @return true if GPS is enabled otherwise false.
	 */
	public static boolean enabledGPS(Context ctx) {
		final LocationManager manager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Method for populate list of all available sensors on device.
	 * 
	 * @param ctx
	 *            Application context
	 */
	private static void populateSensorsList(Context ctx) {
		SensorManager sm = (SensorManager) ctx
				.getSystemService(Context.SENSOR_SERVICE);
		sensorsList = sm.getSensorList(Sensor.TYPE_ALL);
	}
}
