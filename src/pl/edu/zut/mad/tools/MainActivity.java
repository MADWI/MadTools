package pl.edu.zut.mad.tools;

import pl.edu.zut.mad.tools.compass.Compass;
import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.lightmeter.LightMeter;
import pl.edu.zut.mad.tools.nanny.Nanny;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.whereIsCar.WhereIsCar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

    // TESTING BUTTONS
    Button btnNanny;
    Button btnInclinometer;
    Button btnNoise;
    Button btnWhereIsCar;
    Button btnCompass;
    Button btnLightMeter;
    
    //Wy³¹czenie przechodzenia telefonu w stan uœpienia
	//WakeLock
    private WakeLock mWakeLock = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	btnNanny = (Button) findViewById(R.id.btnNanny);
	btnInclinometer = (Button) findViewById(R.id.btnInclinometer);
	btnNoise = (Button) findViewById(R.id.btnNoiseMeter);
	btnWhereIsCar = (Button) findViewById(R.id.btnWhereIsCar);
	btnCompass = (Button) findViewById(R.id.btnCompass);
	btnLightMeter = (Button)findViewById(R.id.btnLightMeter);

	btnInclinometer.setOnClickListener(this);
	btnNanny.setOnClickListener(this);
	btnNoise.setOnClickListener(this);
	btnWhereIsCar.setOnClickListener(this);
	btnCompass.setOnClickListener(this);
	btnLightMeter.setOnClickListener(this);
	
	//WakeLock
    PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
    mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "mainActivity");

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btnNanny:
	    Intent nannyIntent = new Intent(this, Nanny.class);
	    startActivity(nannyIntent);
	    break;

	case R.id.btnInclinometer:
	    Intent inclinometerIntent = new Intent(this, Inclinometer.class);
	    startActivity(inclinometerIntent);
	    break;

	case R.id.btnNoiseMeter:
	    Intent noiseIntent = new Intent(this, NoiseMeterActivity.class);
	    startActivity(noiseIntent);
	    break;

	case R.id.btnWhereIsCar:
	    Intent whereIsCarIntent = new Intent(this, WhereIsCar.class);
	    startActivity(whereIsCarIntent);
	    break;

	case R.id.btnCompass:
	    Intent compassIntent = new Intent(this, Compass.class);
	    startActivity(compassIntent);
	    break;
	    
	case R.id.btnLightMeter:
	    Intent lightMeterIntent = new Intent(this, LightMeter.class);
	    startActivity(lightMeterIntent);
	    break;

	}

    }
}