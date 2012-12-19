package pl.edu.zut.mad.tools.gpsmeasure;

import pl.edu.zut.mad.tools.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

public class PomMain extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pom);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_pom, menu);
		return true;
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button1:
			intent = new Intent(PomMain.this, PomDlActivity.class);
			startActivity(intent);
			break;

		case R.id.button3:
			startActivity(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

			break;
		case R.id.button4:
			finish();

			break;

		case R.id.button2:
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setTitle("Informacje");
			dialogBuilder.setMessage("Autor: Rafa³ Cichoñ" + "\n"
					+ "Licencja: GNU GPL");
			dialogBuilder.setIcon(R.drawable.off);
			dialogBuilder.show();
			break;

		}
	}
}
