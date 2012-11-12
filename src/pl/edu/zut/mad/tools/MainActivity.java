package pl.edu.zut.mad.tools;

import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.nanny.Nanny;
<<<<<<< HEAD
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
=======
import pl.edu.zut.mad.tools.whereIsCar.whereIsCar;
>>>>>>> Dodanie klasy whereIsCar
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

<<<<<<< HEAD
	// TESTING BUTTONS
	Button btnNanny;
	Button btnInclinometer;
	Button btnNoise;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnNanny = (Button) findViewById(R.id.btnNanny);
		btnInclinometer = (Button) findViewById(R.id.btnInclinometer);
		btnNoise = (Button) findViewById(R.id.btnNoiseMeter);

		btnInclinometer.setOnClickListener(this);
		btnNanny.setOnClickListener(this);
		btnNoise.setOnClickListener(this);
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

		}

	}
=======
    // TESTING BUTTONS
    Button btnNanny;
    Button btnInclinometer;
    Button btnWhereIsCar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	btnNanny = (Button) findViewById(R.id.btnNanny);
	btnNanny.setOnClickListener(this);

	btnInclinometer = (Button) findViewById(R.id.btnInclinometer);
	btnInclinometer.setOnClickListener(this);
	
	btnWhereIsCar = (Button) findViewById(R.id.btnWhereIsCar);
	btnWhereIsCar.setOnClickListener(this);
	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

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
	
	case R.id.btnWhereIsCar:
		Intent whereIsCarIntent = new Intent(this, whereIsCar.class);
		startActivity(whereIsCarIntent);
		break;

	}

    }
>>>>>>> Dodanie klasy whereIsCar
}
