package edu.uiuc.permissionschecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import edu.uiuc.permissionschecker.infocheck.Result;
import edu.uiuc.permissionscheckernoui.IRemoteScanner;

public class RemoteTester {
	public static String[] permissions  = {
		"android.permission.READ_PHONE_STATE",
		"android.permission.ACCESS_FINE_LOCATION",
		"android.permission.ACCESS_COURSE_LOCATION",
		"com.android.browser.permission.READ_HISTORY_BOOKMARKS" ,
		"android.permission.CAMERA",
		"android.permission.GET_ACCOUNTS",
		"android.permission.CALL_PHONE",
		"android.permission.READ_CALENDAR",
		"android.permission.READ_CONTACTS",
		"android.permission.READ_LOGS",
		"android.permission.READ_SMS",
	};
	
	public static String global_packagename = "edu.uiuc.permissionscheckernoui.%s";
	public static String global_servicename = "edu.uiuc.permissionscheckernoui.ScannerService";
	
	public static Map<String, List<Result>> testAll(Context context) {
		Map<String, List<Result>> resultmap = new HashMap<String, List<Result>>();
		for (String perm : permissions) {
			String name = perm.substring(perm.lastIndexOf(".")+1).toLowerCase();
			Log.d("StaticRemoteTester", name);
			List<Result> results = new RemoteTester(context, String.format(global_packagename, name), global_servicename).scan();
			resultmap.put(perm, results);
		}
		return resultmap;
	}
	
	
	private IRemoteScanner remoteService;
	private boolean started = false;
	private RemoteServiceConnection conn = null;
	Context context;
	String packagename;
	String servicename;

	public RemoteTester(Context context, String packagename, String servicename) {
		this.context = context;
		this.packagename = packagename;
		this.servicename = servicename;
	}

	public List<Result> scan() {
		startService();
		bindService();
		List<String> result = invokeService();
		List<Result> out = new ArrayList<Result>();
		for (String str : result) {
			Result r = new Result();
			try {
				r.fromJSON(new JSONObject(str));
				out.add(r);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		stopService();
		releaseService();
		return null;
	}

	private void startService() {
		Intent i = new Intent();
		i.setClassName(packagename,
				servicename);
		context.startService(i);
		started = true;
		Log.d(getClass().getSimpleName(), "startService()");

	}

	private void stopService() {
		Intent i = new Intent();
		i.setClassName(packagename,
				servicename);
		context.stopService(i);
		started = false;
		Log.d(getClass().getSimpleName(), "stopService()");
	}

	private void bindService() {
		conn = new RemoteServiceConnection();
		Intent i = new Intent();
		i.setClassName(packagename,
				servicename);
		context.bindService(i, conn, Context.BIND_AUTO_CREATE);
		Log.d(getClass().getSimpleName(), "bindService()");
	}

	private void releaseService() {
		context.unbindService(conn);
		conn = null;
		Log.d(getClass().getSimpleName(), "releaseService()");
	}

	private List<String> invokeService() {
		try {
			while(remoteService == null) {
				//spin. it's ugly but I'm lazy;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			List<String> result = remoteService.scan("");
			Log.d(getClass().getSimpleName(), "invokeService()");
			return result;
		} catch (RemoteException re) {
			Log.e(getClass().getSimpleName(), "RemoteException");
			return null;
		}
	}

	class RemoteServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			remoteService = IRemoteScanner.Stub.asInterface((IBinder) boundService);
			Log.d(getClass().getSimpleName(), "onServiceConnected()");
		}

		public void onServiceDisconnected(ComponentName className) {
			remoteService = null;
			Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		}
	};

}
