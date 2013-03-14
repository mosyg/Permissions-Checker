package edu.uiuc.permissionschecker.infocheck;

import android.util.Log;

public class OutputLog {
	String tag;
	public OutputLog(String tag) {
		this.tag = tag;
	}
	public void write(String out) {
		Log.d(tag, out);
	}
}
