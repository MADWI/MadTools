package pl.edu.zut.mad.tools.converter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import pl.edu.zut.mad.tools.R;
public class Converter extends Activity{

	private Button podany_kilogram;
	private Button podany_gram;

	private EditText edit_wprowadz;
	private EditText edit_wprowadz1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_converter);

		podany_kilogram = (Button) findViewById(R.id.podany_kilogram);
		podany_gram = (Button) findViewById(R.id.podany_gram);

		edit_wprowadz = (EditText) findViewById(R.id.podany_gram);
		edit_wprowadz1 = (EditText) findViewById(R.id.podany_kilogram);

		podany_gram.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

			}

		});

		podany_kilogram.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public EditText getEdit_wprowadz() {
		return edit_wprowadz;
	}

	public void setEdit_wprowadz(EditText edit_wprowadz) {
		this.edit_wprowadz = edit_wprowadz;
	}

	public EditText getEdit_wprowadz1() {
		return edit_wprowadz1;
	}

	public void setEdit_wprowadz1(EditText edit_wprowadz1) {
		this.edit_wprowadz1 = edit_wprowadz1;
	}
}
