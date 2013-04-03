package pl.edu.zut.mad.tools.converter;

import org.w3c.dom.Document;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

import pl.edu.zut.mad.tools.MainActivity;
import pl.edu.zut.mad.tools.R;
import pl.edu.zut.mad.tools.compass.Compass;
import pl.edu.zut.mad.tools.inclinometer.Inclinometer;
import pl.edu.zut.mad.tools.lightmeter.LightMeter;
import pl.edu.zut.mad.tools.noise.meter.NoiseMeterActivity;
import pl.edu.zut.mad.tools.utils.TabCreator;
import pl.edu.zut.mad.tools.whereIsCar.WhereIsCar;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Converter extends SherlockActivity implements
		ActionBar.TabListener {
	private String[] navi_items;
	private boolean tabActive = false;

	private EditText edit_wprowadz;

	private TextView wynik1;

	private RadioButton kilo_gram;
	private RadioButton gram_kilo;

	private RadioButton minuty_sekundy;
	private RadioButton sekundy_minuty;

	private RadioButton kilowaty_konie;
	private RadioButton konie_kilowaty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_converter);

		navi_items = getResources().getStringArray(R.array.navi_items);

		edit_wprowadz = ((EditText) findViewById(R.id.liczba));
		wynik1 = (TextView) findViewById(R.id.wynik);
		kilo_gram = (RadioButton) findViewById(R.id.kilog_gram);
		gram_kilo = (RadioButton) findViewById(R.id.gram_kilog);
		minuty_sekundy = (RadioButton) findViewById(R.id.minute_sekunde);
		sekundy_minuty = (RadioButton) findViewById(R.id.sekunde_minute);
		kilowaty_konie = (RadioButton) findViewById(R.id.kW_KM);
		konie_kilowaty = (RadioButton) findViewById(R.id.KM_kW);

		kilo_gram.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Double k_g;
				try {
					EditText pierwszyK = (EditText) findViewById(R.id.liczba);
					k_g = Double.valueOf(pierwszyK.getText().toString());
					k_g = k_g * 1000;
					wynik1.setText(k_g.toString());
				}

				catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Podaj wartosc",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		gram_kilo.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double g_k;
				try {
					EditText pierwszyG = (EditText) findViewById(R.id.liczba);
					g_k = Double.valueOf(pierwszyG.getText().toString());
					g_k = g_k * 0.001;
					wynik1.setText(g_k.toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Podaj wartoœæ ",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		minuty_sekundy.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double min_sek;
				try {
					EditText minpersek = (EditText) findViewById(R.id.liczba);
					min_sek = Double.valueOf(minpersek.getText().toString());
					min_sek = min_sek * 60;
					wynik1.setText(min_sek.toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Podaj wartosc",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		sekundy_minuty.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double sek_min;
				try {
					EditText sekpermin = (EditText) findViewById(R.id.liczba);
					sek_min = Double.valueOf(sekpermin.getText().toString());
					sek_min = sek_min / 60;
					wynik1.setText(sek_min.toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Podaj wartosc",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		kilowaty_konie.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double kilowat_kon;
				try {
					EditText kwatperkon = (EditText) findViewById(R.id.liczba);
					kilowat_kon = Double.valueOf(kwatperkon.getText()
							.toString());
					kilowat_kon = kilowat_kon * 1.36;
					wynik1.setText(kilowat_kon.toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Podaj wartosc",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		konie_kilowaty.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double kon_kilowat;
				try {
					EditText konperkilowat = (EditText) findViewById(R.id.liczba);
					kon_kilowat = Double.valueOf(konperkilowat.getText()
							.toString());
					kon_kilowat = kon_kilowat / 1.36;
					wynik1.setText(kon_kilowat.toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Podaj wartosc",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Mad Tools");
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabCreator tc = new TabCreator(this);
		tc.createTab(5);

		tabActive = true;
	}

	public EditText getEdit_wprowadz() {
		return edit_wprowadz;
	}

	public void setEdit_wprowadz(EditText edit_wprowadz) {
		this.edit_wprowadz = edit_wprowadz;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.e("TABKonwerter", tab.getText().toString());
		if (tabActive) {
			if (tab.getText().toString().equals(navi_items[0])) {
				Intent main = new Intent(this, MainActivity.class);
				startActivity(main);
				finish();
			} else if (tab.getText().toString().equals(navi_items[1])) {
				Intent compssIntent = new Intent(this, Compass.class);
				startActivity(compssIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[2])) {
				Intent noiseIntent = new Intent(this, NoiseMeterActivity.class);
				startActivity(noiseIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[3])) {
				Intent inclinometerIntent = new Intent(this, Inclinometer.class);
				startActivity(inclinometerIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[4])) {
				Intent lightMeterIntent = new Intent(this, LightMeter.class);
				startActivity(lightMeterIntent);
				finish();
			} else if (tab.getText().toString().equals(navi_items[5])) {

			} else if (tab.getText().toString().equals(navi_items[6])) {
				Intent whereIsCarIntent = new Intent(this, WhereIsCar.class);
				startActivity(whereIsCarIntent);
				finish();
			}
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
