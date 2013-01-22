package pl.edu.zut.mad.tools.gpsmeasure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.xmlpull.v1.XmlSerializer;

import pl.edu.zut.mad.tools.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PomDlActivity extends Activity implements OnClickListener {

	private LocationManager locMgr = null;
	Location LastLocation = null;
	private TextView textLP;
	private TextView textP;
	private TextView textD;
	private Vector<Location> punkty;
	private float dlugosc = 0;
	LocationListener mlocListener;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pom_dl);
		punkty = new Vector<Location>();
		locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);
		textLP = (TextView) findViewById(R.id.textPunkty);
		textP = (TextView) findViewById(R.id.textPole);
		textD = (TextView) findViewById(R.id.textDystans);
		findViewById(R.id.buttonPlus).setOnClickListener(this);
		findViewById(R.id.buttonMinus).setOnClickListener(this);
		findViewById(R.id.buttonSave).setOnClickListener(this);
		findViewById(R.id.buttonClose).setOnClickListener(this);
	}
	
	@Override
	    protected void onPause() {
		super.onPause();
		locMgr.removeUpdates(mlocListener);
	    }

	public class MyLocationListener implements LocationListener

	{

		public void onLocationChanged(Location loc)

		{

			LastLocation = loc;

		}

		public void onProviderDisabled(String provider)

		{

			Toast.makeText(getApplicationContext(), "GPS Disabled",
					Toast.LENGTH_SHORT).show();

		}

		public void onProviderEnabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"GPS Enabled",

			Toast.LENGTH_SHORT).show();

		}

		public void onStatusChanged(String provider, int status, Bundle extras)

		{

		}

	}/* End of Class MyLocationListener */

	public void ShowInfo() {

		String Text =

		"Szerokoœæ geo:      " + LastLocation.getLatitude() + "\n" +

		"D³ugoœæ geo:        " + LastLocation.getLongitude();

		Toast.makeText(this, Text, 0).show();

	}

	private Location NewLocation(double Latitude, double Longitude) {
		Location tmp = new Location("tmp");
		tmp.setLatitude(Latitude);
		tmp.setLongitude(Longitude);
		return (tmp);

	}

	@SuppressWarnings("unused")
	private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
		float pk = (float) (180 / 3.14169);

		float a1 = lat_a / pk;
		float a2 = lng_a / pk;
		float b1 = lat_b / pk;
		float b2 = lng_b / pk;

		float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1)
				* FloatMath.cos(b2);
		float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1)
				* FloatMath.sin(b2);
		float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
		double tt = Math.acos(t1 + t2 + t3);

		return 6366000 * tt;
	}

	private String mToString(double m) {
		if (m > 1000)
			return ((m / 1000) + " km");
		else
			return (m + " m");

	}

	private String m2ToString(double m2) {
		if (m2 > 1000000)
			return ((m2 / 1000000) + " km^2");
		else if (m2 > 10000)
			return ((m2 / 10000) + " ha");
		else
			return (m2 + " m^2");

	}

	double oblicz_powierzchnie()

	{

		int CountPoints = punkty.size();

		double[][] punkty_wzg = new double[CountPoints][2];

		double SkalaX, SkalaY;

		SkalaX = LastLocation.distanceTo(NewLocation(
				LastLocation.getLatitude(), LastLocation.getLongitude() + 0.1)) * 10;
		SkalaY = LastLocation.distanceTo(NewLocation(
				LastLocation.getLatitude() + 0.1, LastLocation.getLongitude())) * 10;

		for (int i = 0; i < CountPoints; i++)

		{

			punkty_wzg[i][0] = SkalaX * punkty.elementAt(i).getLongitude()
					- SkalaX * punkty.firstElement().getLongitude();

			punkty_wzg[i][1] = SkalaY * punkty.elementAt(i).getLatitude()
					- SkalaY * punkty.firstElement().getLatitude();

		}

		double powierzchnia = 0.0;

		for (int i = 1; i < CountPoints; i++)

		{

			if (i < CountPoints - 1)

				powierzchnia += punkty_wzg[i][0]
						* (punkty_wzg[i + 1][1] - punkty_wzg[i - 1][1]);

			else

				powierzchnia += punkty_wzg[i][0]
						* (punkty_wzg[0][1] - punkty_wzg[i - 1][1]);

		}

		return Math.abs(powierzchnia / 2.0);

	}

	public void refresh() {
		textD.setText(mToString(dlugosc));
		textLP.setText(punkty.size() + "");
		if (punkty.size() > 2)
			textP.setText(m2ToString(oblicz_powierzchnie()));
		else
			textP.setText(m2ToString(0));

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonPlus:
			if (LastLocation != null) {
				if (punkty.size() >= 1)
					dlugosc += LastLocation.distanceTo(punkty.lastElement());
				else
					dlugosc = 0;
				punkty.addElement(LastLocation);
				refresh();
				// ShowInfo();
			}

			break;

		case R.id.buttonClose:

			if (punkty.size() > 1) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				alertDialog.setTitle("Wyjœcie...");
				alertDialog.setMessage("Czy jesteœ pewnien?");
				alertDialog.setPositiveButton("Tak",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				alertDialog.setNegativeButton("Nie",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alertDialog.setIcon(R.drawable.off);
				alertDialog.show();
			} else
				finish();
			break;

		case R.id.buttonMinus:
			if (punkty.size() > 1)
				dlugosc -= punkty.lastElement().distanceTo(
						punkty.elementAt(punkty.size() - 2));
			else
				dlugosc = 0;
			if (punkty.size() > 0)
				punkty.removeElementAt(punkty.size() - 1);
			refresh();
			break;

		case R.id.buttonSave:

			if (!android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				Toast.makeText(getBaseContext(), "Brak karty pamiêci",
						Toast.LENGTH_SHORT).show();
				return;
			}
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("Zapis...");
			alertDialog.setMessage("Podaj nazwê pliku KML");
			final EditText input = new EditText(this);
			input.setMaxLines(1);
			input.setText("NazwaPliku");
			alertDialog.setView(input);
			alertDialog.setPositiveButton("Zapisz",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// getDir("EXTERNAL_STORAGE", "/sdcard");

							File newxmlfile = new File(Environment
									.getExternalStorageDirectory()
									.getAbsolutePath()
									+ File.separator + input.getText() + ".kml");
							try {
								newxmlfile.createNewFile();
							} catch (IOException e) {
								Log.e("IOException",
										"Exception in create new File(");
							}
							FileOutputStream fileos = null;
							try {
								fileos = new FileOutputStream(newxmlfile);

							} catch (FileNotFoundException e) {
								Log.e("FileNotFoundException", e.toString());
							}

							XmlSerializer serializer = Xml.newSerializer();
							try {
								serializer.setOutput(fileos, "UTF-8");
								// StringWriter a = new StringWriter();
								// serializer.setOutput(a);
								serializer.startDocument(null,
										Boolean.valueOf(true));
								// serializer.setFeature("http://earth.google.com/kml/2.x",
								// true);

								serializer.startTag(null, "kml");
								serializer.attribute(null, "xmlns",
										"http://earth.google.com/kml/2.x");
								for (int i = 0; i < punkty.size(); i++) {
									serializer.startTag(null, "Placemark");
									serializer.startTag(null, "name");
									serializer.text(i + "");
									serializer.endTag(null, "name");
									serializer.startTag(null, "Point");
									serializer.startTag(null, "coordinates");
									serializer
											.text(punkty.elementAt(i)
													.getLongitude()
													+ ","
													+ punkty.elementAt(i)
															.getLatitude()
													+ ","
													+ punkty.elementAt(i)
															.getAltitude());
									serializer.endTag(null, "coordinates");
									serializer.endTag(null, "Point");
									serializer.endTag(null, "Placemark");
								}
								serializer.endTag(null, "kml");
								serializer.endDocument();

								serializer.flush();

								fileos.close();

								// Toast.makeText(getBaseContext(),
								// (CharSequence)a.toString(), 3).show();

								// textP.setText(a.toString());
								// a.close();
								// TextView tv = (TextView)findViewById(R.);

							} catch (Exception e) {
								Log.e("Exception",
										"Exception occured in wroting");
							}

							dialog.cancel();
						}
					});
			alertDialog.setNegativeButton("Anuluj",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alertDialog.setIcon(R.drawable.off);
			alertDialog.show();

			break;

		}

	}
}