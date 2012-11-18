package pl.edu.zut.mad.tools;

import pl.edu.zut.mad.tools.compass.Compass;
import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.nanny.Nanny;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.whereIsCar.whereIsCar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	btnNanny = (Button) findViewById(R.id.btnNanny);
	btnInclinometer = (Button) findViewById(R.id.btnInclinometer);
	btnNoise = (Button) findViewById(R.id.btnNoiseMeter);
	btnWhereIsCar = (Button) findViewById(R.id.btnWhereIsCar);
	btnCompass = (Button) findViewById(R.id.btnCompass);

	btnInclinometer.setOnClickListener(this);
	btnNanny.setOnClickListener(this);
	btnNoise.setOnClickListener(this);
	btnWhereIsCar.setOnClickListener(this);
	btnCompass.setOnClickListener(this);
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
	    Intent whereIsCarIntent = new Intent(this, whereIsCar.class);
	    startActivity(whereIsCarIntent);
	    break;

	case R.id.btnCompass:
	    Intent compassIntent = new Intent(this, Compass.class);
	    startActivity(compassIntent);
	    break;

	}

    }
}