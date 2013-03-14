package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.location.LocationManager;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class Pictures extends Info {
	public static String NAME = "Pictures";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {

			
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
