package edu.uiuc.permissionschecker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class AudioActivity extends Activity {
	ImprovedCreepyAudioRecorder recorder;
	Spinner audiospinner;
	Spinner uploadspinner;
	ToggleButton recordtoggle;
	
	public final static String PROCESS_SHARED_PREFS = "whatever";
	public final static String AUDIO_INTERVAL = "audiointerval";
	public final static String UPLOAD_INTERVAL = "uploadinterval";
	public  String LETS_STEAL_ALL_THE_USERS_AUDIO_URL = "http://mosyg2.cs.uiuc.edu/android/app/something2.php";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);
      
        recorder = new ImprovedCreepyAudioRecorder(this,LETS_STEAL_ALL_THE_USERS_AUDIO_URL);
        
        setupAudioSpinner();
        setupUploadSpinner();
        setupToggle();
    }
	
	void setupToggle() {
		recordtoggle = (ToggleButton)findViewById(R.id.recordtoggle);
		recordtoggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					record();
				} else {
					stopRecord();
				}
			}
		});
	}
	
	void record() {
		recorder.startBackgroundRecording();
		recordtoggle.setPressed(true);
	}
	void stopRecord() {
		recorder.stopBackgroundRecording();
		recordtoggle.setPressed(false);
	}
	
	void setupAudioSpinner() {
		audiospinner = (Spinner)findViewById(R.id.audiospinner);
		
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.audio_scantimes_text_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audiospinner.setAdapter(adapter);
        final int[] values = this.getResources().getIntArray(R.array.audio_scantimes_integer_array);
        long interval = getAudioInterval();
        int intervalint = (int)(interval/1000);
        for (int i=0; i<values.length; i++) 
        	if (intervalint == values[i]) 
        		audiospinner.setSelection(i);

    	audiospinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 >= 0 && arg2 <= values.length) {
					Log.d("ConfigureActivity", "Setting interval to:"+values[arg2]*1000);
					setAudioInterval(values[arg2]*1000);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
    		
		});
	}
	void setupUploadSpinner() {
		uploadspinner = (Spinner)findViewById(R.id.uploadspinner);
		
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.upload_scantimes_text_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uploadspinner.setAdapter(adapter);
        final int[] values = this.getResources().getIntArray(R.array.upload_scantimes_integer_array);
        long interval = getUploadInterval();
        int intervalint = (int)(interval/1000);
        for (int i=0; i<values.length; i++) 
        	if (intervalint == values[i]) 
        		uploadspinner.setSelection(i);

        uploadspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 >= 0 && arg2 <= values.length) {
					Log.d("ConfigureActivity", "Setting upload interval to:"+values[arg2]*1000);
					setUploadInterval(values[arg2]*1000);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
    		
		});
	}
	
	public void setAudioInterval(long value) {
		SharedPreferences prefs = getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(AUDIO_INTERVAL, value);
		edit.commit();
	}
	public long getAudioInterval() {
		SharedPreferences prefs = getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getLong(AUDIO_INTERVAL, 30000L);
	}
	public void setUploadInterval(long value) {
		SharedPreferences prefs = getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(UPLOAD_INTERVAL, value);
		edit.commit();
	}
	public long getUploadInterval() {
		SharedPreferences prefs = getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getLong(UPLOAD_INTERVAL, 30000L);
	}


}
