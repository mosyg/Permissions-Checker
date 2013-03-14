package edu.uiuc.permissionschecker.infocheck;

import java.util.Arrays;

import edu.uiuc.permissionschecker.infocheck.Result.Attempt;
import android.content.Context;
import android.telephony.TelephonyManager;

public class IMEI extends Info {
	public static String NAME = "IMEI";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
	        TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
	        
	        if (mTelephonyMgr.getDeviceId() != null) {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", mTelephonyMgr.getDeviceId()));
	        } else {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SEMI, "null pointer in mTelephonyMgr.getDeviceId()", ""));
	        }
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
        return result;
	}

}
