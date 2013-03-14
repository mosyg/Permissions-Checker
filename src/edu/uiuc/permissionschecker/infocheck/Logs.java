package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.location.LocationManager;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class Logs extends Info {
	public static String NAME = "Logs";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			android.location.Location lastknown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	
	        if (lastknown != null) {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", ""+lastknown.getLatitude()+","+lastknown.getLongitude()+" accuracy:"+lastknown.getAccuracy()));
	        } else {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "getLastKnownLocation == null", ""));
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
