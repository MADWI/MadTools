package pl.edu.zut.mad.tools.noise.meter;

import java.io.IOException;

import pl.edu.zut.mad.tools.R;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class NoiseMeterActivity extends Activity {

	private static final String TAG = "NoiseMeterActivity";

	TextView noiseLevel;
	TextView test;

	int count = 0;
	boolean recordingNotOver = false;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noise_meter);
		Log.d(TAG, "OnCreate");
		noiseLevel = (TextView) findViewById(R.id.noiseLevel);

		handler.removeCallbacks(mUpdateTimeTask);
		handler.postDelayed(mUpdateTimeTask, 500);

	}

	@Override
	protected void onPause() {
		handler.removeCallbacks(mUpdateTimeTask);
		super.onPause();
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			record100Miliseconds();
			handler.postDelayed(mUpdateTimeTask, 500);
		}
	};

	private void record100Miliseconds() {

		recordingNotOver = true;
		double currentAmp = -1;
		double maxAmp = MediaRecorder.getAudioSourceMax();
		Handler mHandler = new Handler();

		try {
			RecordHandler.startRecording();
		} catch (IOException e) {
			Log.d(TAG, "Record IO error");
			e.printStackTrace();
		}

		mHandler.postDelayed(new Runnable() {
			public void run() {
				recordingNotOver = false;
			}
		}, 100);

		while (recordingNotOver) {
			if (RecordHandler.getRecorder() != null)
				currentAmp = RecordHandler.getRecorder().getMaxAmplitude();
		}

		RecordHandler.stopRecording();

		Log.d(TAG,
				"Max= " + Double.toString(maxAmp) + "   current = "
						+ Double.toString(currentAmp));
		double noise = 20 * Math.log10(currentAmp / maxAmp);
		noiseLevel.setText(Double.toString(noise) + "db");

	}

}
