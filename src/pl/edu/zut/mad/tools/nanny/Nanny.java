package pl.edu.zut.mad.tools.nanny;

import pl.edu.zut.mad.tools.R;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Nanny extends Activity implements OnClickListener {

	private static final String TAG = "Nanny";

	// object to manage WiFi
	private WifiManager wifiManager;

	// view elements
	private TextView txtWifiStatus;
	private Button btnRefreshWifi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nanny);

		txtWifiStatus = (TextView) findViewById(R.id.txtWifiStatus);
		btnRefreshWifi = (Button) findViewById(R.id.btnRefreshWiFi);

		btnRefreshWifi.setOnClickListener(this);

		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			Log.d(TAG, "WiFi disabled");

			Dialog dialog = createAlertDialogDisabledWiFi(this,
					getString(R.string.no_enabled_wifi_title),
					getString(R.string.no_enabled_wifi_message),
					getString(R.string.yes_button_title),
					getString(R.string.cancel_button_title));
			dialog.show();
		} else
			Log.d(TAG, "WiFi enabled");

		refreshWifiStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_nanny, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRefreshWiFi:
			refreshWifiStatus();
			break;

		}

	}

	private Dialog createAlertDialogDisabledWiFi(Context ctx, String title,
			String message, String buttonPositive, String buttonNegative) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
		dialogBuilder.setTitle(title);
		dialogBuilder.setMessage(message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(buttonPositive,
				new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						wifiManager.setWifiEnabled(true);
						refreshWifiStatus();
					}
				});
		dialogBuilder.setNegativeButton(buttonNegative,
				new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});

		return dialogBuilder.create();
	}

	private void refreshWifiStatus() {
		// show WiFi status
		WifiInfo info = wifiManager.getConnectionInfo();
		txtWifiStatus.setText(info.toString());

	}

}
