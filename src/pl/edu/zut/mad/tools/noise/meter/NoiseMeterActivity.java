package pl.edu.zut.mad.tools.noise.meter;

import java.io.IOException;

import org.achartengine.GraphicalView;

import pl.edu.zut.mad.tools.R;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoiseMeterActivity extends Activity {

	private static final String TAG = "NoiseMeterActivity";

	TextView noiseLevel;
	TextView test;

	double currentAmp = -1;
	double maxAmp = MediaRecorder.getAudioSourceMax();
	private Handler handler = new Handler();
	double noise = 0.00;

	int count = 0;
	private static GraphicalView view;
	private NoiseBarGraph bar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noise_meter);

		bar = new NoiseBarGraph(this);
		noiseLevel = (TextView) findViewById(R.id.noiseLevel);

	}

	@Override
	protected void onStart() {
		super.onStart();
		view = bar.getView(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayout);
		layout.addView(view);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");

		handler.removeCallbacks(mUpdateTimeTask);
		handler.postDelayed(mUpdateTimeTask, 500);

		try {

			RecordHandler.startRecording();

		} catch (IOException e) {
			Log.d(TAG, "Record IO error");
			e.printStackTrace();
		}

		maxAmp = MediaRecorder.getAudioSourceMax() / 2700.0;

		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		handler.removeCallbacks(mUpdateTimeTask);

		RecordHandler.stopRecording();

		super.onPause();
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			currentAmpRead();
			handler.postDelayed(mUpdateTimeTask, 500);
		}
	};

	private void currentAmpRead() {
		Log.d(TAG, "currentAmpRead");
		count++;

		Log.d(TAG, Double.toString(maxAmp));
		if (RecordHandler.getRecorder() != null)
			currentAmp = RecordHandler.getRecorder().getMaxAmplitude() / 2700.0;

		double tempNoise = 20.00 * Math.log10(currentAmp / maxAmp);
		if (tempNoise > 0.0)
			noise = tempNoise;

		noiseLevel.setText(String.format("%4.1f", noise) + " dB");

		GraphPoint p = new GraphPoint(count, noise);
		bar.addNewPoints(p);
		view.repaint();
	}

}
