package pl.edu.zut.mad.tools.noise.meter;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class RecordHandler {
	private static final String TAG = "RecordHandler";
	private static MediaRecorder recorder;
	private static File audiofile = null;
	
	public static void startRecording() throws IOException {

		

		File sampleDir = Environment.getExternalStorageDirectory();
		try {
			audiofile = File.createTempFile("sound", ".3gp", sampleDir);
		} catch (IOException e) {
			Log.e(TAG, "sdcard access error");
			return;
		}
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(audiofile.getAbsolutePath());
		recorder.prepare();
		recorder.start();
		
	}

	public static void stopRecording() {

		recorder.stop();
		recorder.release();

	}

	public static MediaRecorder getRecorder() {
		return recorder;

	}

	public static void setRecorder(MediaRecorder record) {
		recorder = record;

	}
}
