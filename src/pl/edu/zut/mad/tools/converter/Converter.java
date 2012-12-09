package pl.edu.zut.mad.tools.converter;

import org.w3c.dom.Document;

import pl.edu.zut.mad.tools.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class Converter extends Activity {
    
 
    
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
       
        
        edit_wprowadz = ((EditText) findViewById(R.id.liczba));
         wynik1 = (TextView) findViewById(R.id.wynik);
         kilo_gram=(RadioButton) findViewById(R.id.kilog_gram);
         gram_kilo=(RadioButton) findViewById(R.id.gram_kilog);
         minuty_sekundy=(RadioButton) findViewById(R.id.minute_sekunde);
         sekundy_minuty=(RadioButton) findViewById(R.id.sekunde_minute);
         kilowaty_konie=(RadioButton) findViewById(R.id.kW_KM);
         konie_kilowaty=(RadioButton) findViewById(R.id.KM_kW);
         kilo_gram.setOnClickListener(new OnClickListener() {	
			
			public void onClick(View arg0) {
				Double k_g;
				EditText pierwszyK = (EditText) findViewById(R.id.liczba);
	        	 k_g = Double.valueOf(pierwszyK.getText().toString());
	        	 k_g = k_g * 1000;
	        	 wynik1.setText(k_g.toString());	
			}
		});
         
         gram_kilo.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double g_k;
				EditText pierwszyG = (EditText) findViewById(R.id.liczba);
				g_k = Double.valueOf(pierwszyG.getText().toString());
				g_k = g_k *0.001;
				wynik1.setText(g_k.toString());
			}
		});
         
         minuty_sekundy.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double min_sek;
				EditText minpersek = (EditText) findViewById(R.id.liczba);
				min_sek = Double.valueOf(minpersek.getText().toString());
				min_sek = min_sek * 60;
				wynik1.setText(min_sek.toString());
			}
		});
         
         sekundy_minuty.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double sek_min;
				EditText sekpermin = (EditText) findViewById(R.id.liczba);
				sek_min = Double.valueOf(sekpermin.getText().toString());
				sek_min = sek_min / 60;
				wynik1.setText(sek_min.toString());
			}
		});
         
         kilowaty_konie.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double kilowat_kon;
				EditText kwatperkon = (EditText) findViewById(R.id.liczba);
				kilowat_kon = Double.valueOf(kwatperkon.getText().toString());
				kilowat_kon = kilowat_kon * 1.36;
				wynik1.setText(kilowat_kon.toString());
			}
		});
         
         konie_kilowaty.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Double kon_kilowat;
				EditText konperkilowat = (EditText) findViewById(R.id.liczba);
				kon_kilowat = Double.valueOf(konperkilowat.getText().toString());
				kon_kilowat = kon_kilowat / 1.36;
				wynik1.setText(kon_kilowat.toString());
			}
		});  
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_converter, menu);
        return true;
    }
    
	public EditText getEdit_wprowadz() {
		return edit_wprowadz;
	}
	
	public void setEdit_wprowadz(EditText edit_wprowadz) {
		this.edit_wprowadz = edit_wprowadz;
	}
}
