package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class CallLogs extends Info {
	public static String NAME = "CallLogs";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			/**
			 * http://malsandroid.blogspot.com/2010/06/accessing-call-logs.html
			 */
			Uri allCalls = Uri.parse("content://call_log/calls");
	        Cursor c = context.getContentResolver().query(allCalls, null, null, null, null);
//	        for(String colName : c.getColumnNames())
//	            Log.v(NAME, "Column Name: " + colName);

	        if (c.moveToFirst())
	        {
	           do{
	               String id = c.getString(c.getColumnIndex(CallLog.Calls._ID));
	               String num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
	               int type = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));

	               
		        	result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "",  "type:"+type+" number:"+num+" id:"+id   ));
//
//	                switch (type)
//	                {
//	                    case 1: Log.v(NAME, id + ", " +num + ": INCOMING") ; break;
//	                    case 2: Log.v(NAME, id + ", " +num + ": OUTGOING") ;break;
//	                    case 3: Log.v(NAME, id + ", " +num + ": MISSED") ; break;
//	                }
	           } while (c.moveToNext());
	           
	           
	        }	
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
