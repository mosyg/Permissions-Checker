package edu.uiuc.permissionschecker.infocheck;

import java.util.Arrays;

import android.content.Context;

public abstract class Info {
	public abstract Result find(Context context, OutputLog out);
	
	public String exceptionToString(Exception e) {
		return Arrays.toString(e.getStackTrace());
	}
	
	public abstract String getName();
}
