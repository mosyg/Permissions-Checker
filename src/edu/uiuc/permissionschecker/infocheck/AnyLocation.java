package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.location.LocationManager;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class AnyLocation extends Info {
	public static String NAME = "Any Location";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			android.location.Location lastknown = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	
	        if (lastknown != null) {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", ""+lastknown.getLatitude()+","+lastknown.getLongitude()+" accuracy:"+lastknown.getAccuracy()));
	        } else {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SEMI, "getLastKnownLocation == null", ""));
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
