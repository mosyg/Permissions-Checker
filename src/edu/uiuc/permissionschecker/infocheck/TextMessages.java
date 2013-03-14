package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class TextMessages extends Info {
	public static String NAME = "Text Messages";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			Uri uri = Uri.parse("content://sms/inbox");
			Cursor c= context.getContentResolver().query(uri, null, null ,null,null);
	
			                
			if(c.moveToFirst()){
			        for(int i=0;i<c.getCount();i++){

			                String body= c.getString(c.getColumnIndexOrThrow("body")).toString();
			                String number=c.getString(c.getColumnIndexOrThrow("address")).toString();
			                c.moveToNext();
							result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", "Number: "+number+ " message: "+body));
			        }
			}
			c.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

	
	
}
