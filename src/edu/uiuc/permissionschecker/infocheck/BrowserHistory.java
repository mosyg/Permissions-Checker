package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.provider.Browser;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class BrowserHistory extends Info {
	public static String NAME = "BrowserHistory";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			/*
			 * http://stackoverflow.com/questions/2577084/android-read-browser-history
			 */
			Cursor mCur = context.getContentResolver().query(Browser.BOOKMARKS_URI,
                    Browser.HISTORY_PROJECTION, null, null, null);
            mCur.moveToFirst();
            if (mCur.moveToFirst() && mCur.getCount() > 0) {
                while (mCur.isAfterLast() == false) {
    				String title = mCur.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX);
    				String url = mCur.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
    				result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", "title: "+title+" url:"+url));
                    mCur.moveToNext();
                }
            }

            
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
