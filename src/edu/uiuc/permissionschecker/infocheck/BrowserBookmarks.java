package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.database.Cursor;
import android.provider.Browser;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class BrowserBookmarks extends Info {
	public static String NAME = "Browser Bookmarks";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			/**
			 * http://www.higherpass.com/Android/Tutorials/Accessing-Data-With-Android-Cursors/3/
			 */
			String[] projection = new String[] { Browser.BookmarkColumns.TITLE,
					Browser.BookmarkColumns.URL };
			Cursor mCur = context.getContentResolver().query(
					android.provider.Browser.BOOKMARKS_URI, projection, null,
					null, null);
			mCur.moveToFirst();
			int titleIdx = mCur.getColumnIndex(Browser.BookmarkColumns.TITLE);
			int urlIdx = mCur.getColumnIndex(Browser.BookmarkColumns.URL);
			while (mCur.isAfterLast() == false) {
				String title = mCur.getString(titleIdx);
				String url = mCur.getString(urlIdx);
				result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", "title: "+title+" url:"+url));
				mCur.moveToNext();
			}
	
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
