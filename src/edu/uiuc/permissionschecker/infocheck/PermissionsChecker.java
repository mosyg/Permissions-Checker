package edu.uiuc.permissionschecker.infocheck;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class PermissionsChecker {
	public static interface PermCheckerProgressListener {
		public void newResult(Result result, List<Result> results);
		public void startTest(String name, List<Result> results);
	}
	
	public List<Result> runTest(Context context, PermCheckerProgressListener listener) {
		OutputLog log = new OutputLog("PermissionsChecker");
		ArrayList<Result> results = new ArrayList<Result>();
		runTest(new PhoneNumber(), context, log, listener, results);
		runTest(new IMEI(), context, log, listener, results);
		runTest(new AnyLocation(), context, log, listener, results);
		runTest(new NetworkLocation(), context, log, listener, results);
		runTest(new CameraInfo(), context, log, listener, results); //not functional yet, but implemented.
		runTest(new Pictures(), context, log, listener, results); //not yet.
		runTest(new Contacts(), context, log, listener, results);
		runTest(new Accounts(), context, log, listener, results);
		runTest(new CallLogs(), context, log, listener, results);
		//runTest(new Logs(), context, log, listener, results); //not yet
		runTest(new Microphone(), context, log, listener, results); //not yet
		runTest(new TextMessages(), context, log, listener, results);
		runTest(new BrowserBookmarks(), context, log, listener, results); 
		runTest(new BrowserHistory(), context, log, listener, results); //untested
		runTest(new Calendar(), context, log, listener, results); //untested
		//runTest(new Feeds(), context, log, listener, results); //not yet //nah. don't bother.
		return results;
	}
	
	public void runTest(Info info, Context context, OutputLog log, PermCheckerProgressListener listener, List<Result> results) {
		try {
			listener.startTest(info.getName(), results);
			Result result = info.find(context, log);
			results.add(result);
			listener.newResult(result, results);
		} catch (Exception e) {
			e.printStackTrace();
			handleError(e);
		}
	}
	
	
	public void handleError(Exception e) {
		//do something
	}
	
}
