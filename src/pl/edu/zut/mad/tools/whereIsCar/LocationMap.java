package pl.edu.zut.mad.tools.whereIsCar;

import com.google.android.maps.MapActivity;

import pl.edu.zut.mad.tools.R;
import android.os.Bundle;
import android.widget.TextView;

public class LocationMap extends MapActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locationmap);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
