package pl.edu.zut.mad.tools.nanny;

import pl.edu.zut.mad.tools.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Nanny extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nanny);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_nanny, menu);
		return true;
	}
}
