package com.main.evie;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import android.util.Log;

public class Availability {
	Context mcontext;
	public Availability(Context context) {
		this.mcontext = context.getApplicationContext();
	}
	public int numEvents() {
		final String[] INSTANCE_PROJECTION = new String[] {
		    Instances.EVENT_ID,      // 0
		    Instances.BEGIN,         // 1
		    Instances.TITLE
		  };
		
		// The indices for the projection array above.
		final int PROJECTION_ID_INDEX = 0;
		final int PROJECTION_BEGIN_INDEX = 1;
		final int PROJECTION_TITLE_INDEX = 2;
		
		// Specify the date range you want to search for recurring
		// event instances
		Calendar beginTime = Calendar.getInstance();
		long startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.add(Calendar.WEEK_OF_YEAR,2);
		long endMillis = endTime.getTimeInMillis();
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String starttime = format.format(beginTime.getTime());
		String endtime = format.format(endTime.getTime());
		  
		Cursor cur = null;
		ContentResolver cr = mcontext.getContentResolver();

		// Construct the query with the desired date range.
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		// Submit the query
		cur =  cr.query(builder.build(), 
		    INSTANCE_PROJECTION, 
		    null, 
		    null,
		    null);

		while (cur.moveToNext()) {
		    String title = null;
		    long beginVal = 0;    
		    
		    // Get the field values
		    beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
		    title = cur.getString(PROJECTION_TITLE_INDEX);
		    
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTimeInMillis(beginVal);  
		    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		    String date = formatter.format(calendar.getTime());
		    
		    // Do something with the values. 
		    Log.i("evie_debug", "Event:  " + title);    
		    Log.i("evie_debug", "Date: " + formatter.format(calendar.getTime()));
		    }
		
		return cur.getCount();
	}
}
