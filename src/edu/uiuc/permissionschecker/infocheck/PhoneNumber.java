package edu.uiuc.permissionschecker.infocheck;

import java.util.Arrays;

import edu.uiuc.permissionschecker.infocheck.Result.Attempt;
import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneNumber extends Info {
	public static String NAME = "Phone Number";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
	        TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
	        
	        
			result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", mTelephonyMgr.getLine1Number()));
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
        return result;
	}

}
