package pl.edu.zut.mad.tools;

import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.nanny.Nanny;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	btnNanny = (Button) findViewById(R.id.btnNanny);
	btnNanny.setOnClickListener(this);

	btnInclinometer = (Button) findViewById(R.id.btnInclinometer);
	btnInclinometer.setOnClickListener(this);
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

	}

    }
}
