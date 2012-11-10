package pl.edu.zut.mad.tools.noise.meter;

import java.io.IOException;

import android.media.MediaRecorder;

public class RecordHandler {
	private static MediaRecorder recorder;

	public static void startRecording() throws IllegalStateException, IOException {

		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile("/dev/null");
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
}
