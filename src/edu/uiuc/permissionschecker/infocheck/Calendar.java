package edu.uiuc.permissionschecker.infocheck;

import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class Calendar extends Info {
	public static String NAME = "Calendar";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {

			/**
			 * http://jimblackler.net/blog/?p=151&cpage=2#comments
			 */
			
			
			ContentResolver contentResolver = context.getContentResolver();
			 
			final Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"),
			        (new String[] { "_id", "displayName", "selected" }), null, null, null);
			 
			int id=0;
			while (cursor.moveToNext()) {
			 
			    final String _id = cursor.getString(0);
			    final String displayName = cursor.getString(1);
			    final Boolean selected = !cursor.getString(2).equals("0");
			   id = Integer.parseInt(_id);
			    System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
			}

			
			
			
			
			Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
			long now = new Date().getTime();
			ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);
			ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);
			 
			Cursor eventCursor = context.getContentResolver().query(builder.build(),
			        new String[] { "title", "begin", "end", "allDay"}, "Calendars._id=" + id,
			        null, "startDay ASC, startMinute ASC");
			 
			while (eventCursor.moveToNext()) {
			    final String title = eventCursor.getString(0);
			    final Date begin = new Date(eventCursor.getLong(1));
			    final Date end = new Date(eventCursor.getLong(2));
			    final Boolean allDay = !eventCursor.getString(3).equals("0");
			   
			    System.out.println("Title: " + title + " Begin: " + begin + " End: " + end +
			            " All Day: " + allDay);
			    result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", "Title: " + title + " Begin: " + begin + " End: " + end + " All Day: " + allDay));
			}
			
			
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
